package com.example.template.pojo.internal;

import io.swagger.annotations.ApiModel;

@ApiModel("Hello")
public class HelloDto {
    private String response;
    private Content content1;
    private Content content2;

    public HelloDto(String response, Content content1, Content content2) {
        this.response = response;
        this.content1 = content1;
        this.content2 = content2;
    }
}
