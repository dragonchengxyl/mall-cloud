package com.mall.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.user.entity.User;
import com.mall.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }
    
    public User getUserById(Long id) {
        String key = "user:" + id;
        
        // 1. 先查Redis
        String cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            try {
                System.out.println("🔥 从Redis获取用户：" + id);
                return objectMapper.readValue(cached, User.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // 2. Redis没有，查数据库
        System.out.println("💾 从MySQL获取用户：" + id);
        User user = userMapper.findById(id);
        
        // 3. 存入Redis，过期时间5分钟
        if (user != null) {
            try {
                redisTemplate.opsForValue().set(
                    key, 
                    objectMapper.writeValueAsString(user),
                    5, 
                    TimeUnit.MINUTES
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return user;
    }
}
