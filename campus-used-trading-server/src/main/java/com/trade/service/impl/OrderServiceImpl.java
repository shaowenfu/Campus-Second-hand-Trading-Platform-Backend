package com.trade.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.trade.constant.MessageConstant;
import com.trade.context.BaseContext;
import com.trade.dto.OrdersCancelDTO;
import com.trade.dto.OrdersPageQueryDTO;
import com.trade.dto.OrdersPaymentDTO;
import com.trade.dto.OrdersSubmitDTO;
import com.trade.entity.*;
import com.trade.exception.AddressBookBusinessException;
import com.trade.exception.OrderBusinessException;
import com.trade.exception.ShoppingCartBusinessException;
import com.trade.exception.ShoppingCartException;
import com.trade.mapper.*;
import com.trade.result.PageResult;
import com.trade.service.OrderService;
import com.trade.vo.OrderPaymentVO;
import com.trade.vo.OrderStatisticsVO;
import com.trade.vo.OrderSubmitVO;
import com.trade.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ThingMapper thingMapper;
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据id找order
     * @param ordersCancelDTO
     * @return
     */
    public void cancel(OrdersCancelDTO ordersCancelDTO){
        Orders ordersDB = orderMapper.getById(ordersCancelDTO.getId());

        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == 1) {
//            //用户已支付，需要退款
//            String refund = weChatPayUtil.refund(
//                    ordersDB.getNumber(),
//                    ordersDB.getNumber(),
//                    new BigDecimal(0.01),
//                    new BigDecimal(0.01));
            log.info("申请退款：{}");
        }

        //更新原因、时间、账单状态
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 查看order状态的数量
     */
    public OrderStatisticsVO statistics(Long id){
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED, id);
        Integer confirmed = orderMapper.countStatus(Orders.TRADING, id);

        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        return orderStatisticsVO;
    }

    /**
     * 根据id查村订单
     * @param id
     * @return
     */
    public OrderVO detials(Long id){
        Orders orders = orderMapper.getById(id);
        List<OrderDetail> orderDetailList = orderDetailMapper.getById(id);

        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    /**
     * 订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> orderVOList = new ArrayList<>();
        List<Orders> orders = page.getResult();
        for (Orders order : orders) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            //找到对应order对应的orderDetail
            List<OrderDetail> orderDetailList = orderDetailMapper.getById(order.getId());

            List<String> orderThingsList = orderDetailList.stream().map(x->
            {
                String orderThing = x.getName() + "*" + x.getAmount() + ";";
                return orderThing;
            }).collect(Collectors.toList());

            orderVO.setOrderThings(String.join("", orderThingsList));
            orderVOList.add(orderVO);
        }
        return new PageResult(page.getTotal(),orderVOList);
    }

    /**
     * 完成订单
     * @param id
     */
    public void complete(Long id){
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在，并且状态为3(即为交易中)
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TRADING)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 更新订单状态,状态转为完成
        orders.setStatus(Orders.COMPLETED);
        orderMapper.update(orders);
    }

    /**
     * 接单
     * @param id
     */
    public void confirm(Long id){
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在，并且状态为3(即为交易中)
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.TRADING);
        orderMapper.update(orders);
    }

    public List<OrderSubmitVO> submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //处理各种业务异常（地址簿为空，购物车数据为空）
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        //购物车为空
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .build());
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        //向订单表插入多条数据条数据（有几个商户就生成几个订单）
        //得到订单的marketerId的列表
        List<Long> marketerList = shoppingCartMapper.getOrders(BaseContext.getCurrentId());
        List<OrderSubmitVO> orderSubmitVOList = new ArrayList<>();
        //给每个marketerId生成一个order
        for (Long marketerId : marketerList) {

            Orders orders = new Orders();
            //将原本特点复制到orders里
            BeanUtils.copyProperties(ordersSubmitDTO, orders);
            //给每个marketerId 计算金额总数
            BigDecimal amount = shoppingCartMapper.getAmountByuserIdAndMarketerId(BaseContext.getCurrentId(), marketerId);

            orders.setOrderTime(LocalDateTime.now());
            orders.setAmount(amount);
            orders.setMarketerId(marketerId);
            orders.setPayStatus(Orders.UN_PAID);
            orders.setStatus(Orders.PENDING_PAYMENT);
            //设置订单号
            orders.setNumber(String.valueOf(System.currentTimeMillis()));
            orders.setPhone(addressBook.getPhone());
            orders.setConsignee(addressBook.getConsignee());
            orders.setUserId(BaseContext.getCurrentId());

            orderMapper.insert(orders);

            //这个marketerId对应的orderList
            List<OrderDetail> orderDetailList = new ArrayList<>();

            //找到对应的购物车的内容
            shoppingCartList = shoppingCartMapper.list(ShoppingCart.builder()
                    .userId(BaseContext.getCurrentId())
                    .marketerId(marketerId)
                    .build());

            for (ShoppingCart cart : shoppingCartList) {
                OrderDetail orderDetail = new OrderDetail();//订单明细
                BeanUtils.copyProperties(cart, orderDetail);
                orderDetail.setOrderId(orders.getId());
                orderDetail.setAmount(cart.getAmount());
                orderDetail.setPrice(cart.getPrice());
                orderDetailList.add(orderDetail);

            }
            orderDetailMapper.insertBatch(orderDetailList);

            //封装VO返回数据
            OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                    .id(orders.getId())
                    .orderTime(orders.getOrderTime())
                    .orderNumber(orders.getNumber())
                    .orderAmount(orders.getAmount())
                    .build();

            orderSubmitVOList.add(orderSubmitVO);
        }
        //清空当前用户的购物车数据
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());

        return orderSubmitVOList;
    }

    public List<OrderPaymentVO> payment(List<OrdersPaymentDTO> ordersPaymentDTOList) throws Exception {
//        // 当前登录用户id
//        Long userId = BaseContext.getCurrentId();
//        User user = userMapper.getById(userId);
//
//        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );
//
//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException("该订单已支付");
//        }
//
//        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
//        vo.setPackageStr(jsonObject.getString("package"));
//
//        return vo;
        //直接强行把他整成paysuccess结果
        List<OrderPaymentVO> voList = new ArrayList<>();
        for (OrdersPaymentDTO ordersPaymentDTO : ordersPaymentDTOList ) {
            paySuccess(ordersPaymentDTO.getOrderNumber());

            String orderNumber = ordersPaymentDTO.getOrderNumber(); //订单号
            Long orderId = orderMapper.getorderId(orderNumber);//根据订单号查主键
            Integer OrderPaidStatus = Orders.PAID; //支付状态，已支付
            Integer OrderStatus = Orders.TO_BE_CONFIRMED; //订单状态，待接单
            //发现没有将支付时间 check_out属性赋值，所以在这里更新
            LocalDateTime check_out_time = LocalDateTime.now();
            orderMapper.updateStatus(OrderStatus, OrderPaidStatus, check_out_time, orderId);

            OrderPaymentVO vo = OrderPaymentVO.builder()
                    .timeStamp(String.valueOf(System.currentTimeMillis() / 1000))
                    .build();

            voList.add(vo);

            //还需要更新thing的数量问题

            Orders orders = orderMapper.getByNumber(orderNumber);
            List<OrderDetail> orderDetailList = orderDetailMapper.getById(orders.getId());
            for (OrderDetail orderDetail : orderDetailList) {
                Thing thing = thingMapper.getByThingId(orderDetail.getThingId());
                if(thing.getAmount() > orderDetail.getAmount()) {
                    //所有购物车数据清空,抛出异常
                    shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
                    throw new ShoppingCartException(MessageConstant.AMOUNT_IS_ERROR);
                }
                else if(thing.getAmount().equals(orderDetail.getAmount())) {
                    //设置停售
                    thing.setStatus(0);
                }
                //需要将Redis缓存更新
                Set keys = redisTemplate.keys("thing_" + thing.getCategoryId());
                redisTemplate.delete(keys);
                thing.setAmount(thing.getAmount() - orderDetail.getAmount());
                thingMapper.update(thing);
            }

        }
        return voList;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    /**
     * 查看订单
     * @param id
     * @return
     */
    public OrderVO details(Long id){
        Orders order = orderMapper.getById(id);
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(id);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderDetailList(orderDetails);
        return orderVO;
    }

    /**
     * 分页查找
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    public PageResult pageQuery4User(int pageNum, int pageSize, Integer status){
        // 设置分页, Mybatis实现分页操作
        PageHelper.startPage(pageNum, pageSize);

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        // 分页条件查询
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> list = new ArrayList();

        // 查询出订单明细，并封装入OrderVO进行响应
        if (page != null && page.getTotal() > 0) {
            for (Orders orders : page) {
                Long orderId = orders.getId();// 订单id

                // 查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                //先将order中的属性复制到orderVO中
                BeanUtils.copyProperties(orders, orderVO);
                //再将oderDetail导入进来
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(), list);
    }

    /**
     * 取消订单
     * @param id
     */
    public void userCancelById(Long id){
        Orders ordersDB = orderMapper.getById(id);
        if(ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        if (ordersDB.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());

        // 订单处于待接单状态下取消，需要进行退款
        if (ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            //调用微信支付退款接口(由于没有微信支付，故注释取消掉)
//            weChatPayUtil.refund(
//                    ordersDB.getNumber(), //商户订单号
//                    ordersDB.getNumber(), //商户退款单号
//                    new BigDecimal(0.01),//退款金额，单位 元
//                    new BigDecimal(0.01));//原订单金额

            //支付状态修改为 退款
            orders.setPayStatus(Orders.REFUND);
        }

        // 更新订单状态、取消原因、取消时间
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }
}
