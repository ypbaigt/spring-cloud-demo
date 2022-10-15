package com.example.springcloud;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 使用dubbo Model一定要序列化
 */
@Data
public class Product implements Serializable {

    private String name;
    private BigDecimal price;

}
