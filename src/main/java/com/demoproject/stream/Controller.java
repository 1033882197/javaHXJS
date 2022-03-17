package com.demoproject.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/aaa")
public class Controller {
    @Autowired
    private MyConstruct myConstruct;

    @GetMapping(value = "/bbb")
    public void getMethod(){
        myConstruct.ccc();

    }

}
