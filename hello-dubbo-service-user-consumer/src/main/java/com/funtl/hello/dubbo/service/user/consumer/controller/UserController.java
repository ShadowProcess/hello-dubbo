package com.funtl.hello.dubbo.service.user.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.funtl.hello.dubbo.service.user.api.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Reference(version = "${user.service.version}")  //阿里巴巴的注解
    private UserService userService;


    @HystrixCommand(fallbackMethod = "hiError")
    @RequestMapping(value = "hi",method = RequestMethod.GET)
    public String sayHi(){
        return userService.sayHi();
    }


    public String hiError(){
        return "触发熔断";
    }


    /**
     * 获取请求地址
     * @param path
     * @return
     */
    private String getRequest(String path){
        //本端是否为消费端,这个会返回true
        boolean consumerSide = RpcContext.getContext().isConsumerSide();
        if (consumerSide) {
            //获取最后一次调用的提供方IP地址
            String remoteHost = RpcContext.getContext().getRemoteHost();
            return String.format("redirect:http://%s:%s%s","127.0.0.1",8080,path);
        }
        return null;
    }
}
