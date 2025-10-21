package com.mall.order.mapper;

import com.mall.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {
    
    @Select("SELECT * FROM orders WHERE user_id = #{userId}")
    List<Order> findByUserId(Long userId);
}
