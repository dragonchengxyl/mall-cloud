package com.mall.order.feign;

import com.mall.order.entity.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mall-user", url = "http://localhost:8081")
public interface UserFeignClient {
    
    @GetMapping("/user/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}
