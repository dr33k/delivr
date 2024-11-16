package com.seven.delivr.base;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloWorldController {
    @GetMapping(consumes="*/*")
    public HelloWorldObj helloWorld(){
        return new HelloWorldObj("Hello World");
    }
}

class HelloWorldObj{
    public String text;
    public HelloWorldObj(String t){this.text = t;}
}
