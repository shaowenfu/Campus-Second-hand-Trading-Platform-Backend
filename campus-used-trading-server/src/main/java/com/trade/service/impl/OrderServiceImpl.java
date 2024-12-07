package com.trade.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.trade.constant.MessageConstant;
import com.trade.context.BaseContext;
import com.trade.dto.OrdersCancelDTO;
import com.trade.dto.OrdersPageQueryDTO;
import com.trade.dto.OrdersPaymentDTO;
import com.trade.dto.OrdersSubmitDTO;
import com.trade.entity.AddressBook;
import com.trade.entity.OrderDetail;
import com.trade.entity.Orders;
import com.trade.entity.ShoppingCart;
import com.trade.exception.AddressBookBusinessException;
import com.trade.exception.OrderBusinessException;
import com.trade.exception.ShoppingCartBusinessException;
import com.trade.mapper.AddressBookMapper;
import com.trade.mapper.OrderDetailMapper;
import com.trade.mapper.OrderMapper;
import com.trade.mapper.ShoppingCartMapper;
import com.trade.result.PageResult;
import com.trade.service.OrderService;
import com.trade.vo.OrderPaymentVO;
import com.trade.vo.OrderStatisticsVO;
import com.trade.vo.OrderSubmitVO;
import com.trade.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
                String orderThing = x.getName() + "*" + x.getNumber() + ";";
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

    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //处理各种业务异常（地址簿为空，购物车数据为空）
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        ShoppingCart shoppingCart = new ShoppingCart();
        //查询当前用户的购物车数据
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        shoppingCartMapper.list(shoppingCart);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        //向订单表插入1条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(userId);
        orderMapper.insert(orders);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        //向订单明细表插入n条数据
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();//订单明细
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);

        //清空当前用户的购物车数据
        shoppingCartMapper.deleteByUserId(userId);

        //封装VO返回数据
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();
        return orderSubmitVO;
    }

    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
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
        paySuccess(ordersPaymentDTO.getOrderNumber());



        String orderNumber = ordersPaymentDTO.getOrderNumber(); //订单号
        Long orderId = orderMapper.getorderId(orderNumber);//根据订单号查主键

//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("code", "ORDERPAID");
//
//        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
//
//        vo.setPackageStr(jsonObject.getString("package"));

        //为替代微信支付成功后的数据库订单状态更新，多定义一个方法进行修改
        Integer OrderPaidStatus = Orders.PAID; //支付状态，已支付
        Integer OrderStatus = Orders.TO_BE_CONFIRMED; //订单状态，待接单
        //发现没有将支付时间 check_out属性赋值，所以在这里更新
        LocalDateTime check_out_time = LocalDateTime.now();
        orderMapper.updateStatus(OrderStatus, OrderPaidStatus, check_out_time, orderId);

        OrderPaymentVO vo = OrderPaymentVO.builder()
                .timeStamp(String.valueOf(System.currentTimeMillis() / 1000))
                .build();

        return vo;  //  修改支付方法中的代码
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
}
