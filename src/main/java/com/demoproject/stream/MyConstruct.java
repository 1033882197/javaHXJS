package com.demoproject.stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Component
@Getter
@Setter
public class MyConstruct implements InitializingBean {
    @PostConstruct
    public void init(){
        System.out.println("init");
    }
    public MyConstruct(){
        System.out.println("construct");
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("after");
    }

    public void ccc(){
        System.out.println("ccc");
    }

}
