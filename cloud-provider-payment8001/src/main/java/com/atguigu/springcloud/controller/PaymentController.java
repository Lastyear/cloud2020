package com.atguigu.springcloud.controller;


import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private DiscoveryClient discoveryClient;

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

    @GetMapping("/payment/discovery")
    public Object discovery(){
        List<String> regions = discoveryClient.getServices();
        for(String set: regions){
            log.info("xxxx"+set);
        }
        // 一个微服务下的全部实例
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getServiceId() + "\t" + instance.getHost() + "\t" + instance.getPort() + instance.getUri());
        }
        return this.discoveryClient;
    }
}
