package com.trade.service;

import com.trade.entity.Thing;
import com.trade.vo.ThingVO;
import java.util.List;

public interface ThingService {
    List<Thing> list(long categoryId);
    List<ThingVO> listWithFlavor(Thing thing);
}
