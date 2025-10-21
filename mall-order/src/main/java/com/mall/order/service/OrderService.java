package com.mall.order.service;

import com.mall.order.entity.Order;
import com.mall.order.entity.UserDTO;
import com.mall.order.feign.UserFeignClient;
import com.mall.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private UserFeignClient userFeignClient;
    
    public Map<String, Object> getOrdersByUserId(Long userId) {
        // 调用用户服务
        UserDTO user = userFeignClient.getUserById(userId);
        
        // 查询订单
        List<Order> orders = orderMapper.findByUserId(userId);
        
        // 组装结果
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("orders", orders);
        return result;
    }
}
