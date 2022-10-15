package com.example.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("gateway")
@Slf4j
public class GatewayController {

    public static final Map<Long, Product> items = new ConcurrentHashMap<>();

    @RequestMapping(value = "details", method = RequestMethod.GET)
    public Product get(@RequestParam("pid") Long pid) {
        if (!items.containsKey(pid)) {
            Product product = Product.builder()
                    .productId(pid)
                    .description("好吃不贵")
                    .stock(100L)
                    .build();
            items.putIfAbsent(pid, product);
        }
        return items.get(pid);
    }

    @RequestMapping(value = "placeOrder", method = RequestMethod.POST)
    public String buy(@RequestParam("pid") Long pid) {
        Product product = items.get(pid);
        if (product == null) {
            return "Product not found";
        } else if (product.getStock() < 1) {
            return "Sold out";
        }
        synchronized (product) {
            if (product.getStock() < 1) {
                return "Sold out";
            }
            product.setStock(product.getStock() - 1);
        }
        return "Order Placed";
    }

}
