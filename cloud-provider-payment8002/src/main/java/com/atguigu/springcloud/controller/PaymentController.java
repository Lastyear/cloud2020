package com.atguigu.springcloud.controller;


import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import com.netflix.appinfo.InstanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;


    @PostMapping("/payment/create")
    public CommonResult  create(@RequestBody Payment payment){
        System.out.println(payment);
        int result=paymentService.create(payment);
        log.info("----插入结果："+"serverPort:"+serverPort);
        if(result>0){
        return new CommonResult(200,"成功",result);
        }else {
            return new CommonResult(500,"失败",null);
        }
    }

    @GetMapping("/payment/getPaymentById/{id}")
    public CommonResult  getPaymentById(@PathVariable("id") Long id){
        Payment payment=paymentService.getPaymentById(id);
        log.info("----查询结果："+payment+"serverPort:"+serverPort);
        log.info("测试热部署----");
        if(payment !=null){
            return new CommonResult(200,"成功",payment);
        }else {
            return new CommonResult(500,"失败",null);
        }
    }


    @GetMapping(value = "/payment/lb")
    public String getPaymentLB() {
        return serverPort;
    }
}
