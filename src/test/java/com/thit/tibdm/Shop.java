package com.thit.tibdm;


import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Shop {
    private String name;
    private Random random = new Random();

    public Shop(String name) {
        this.name = name;
    }

    //直接获取价格
    public double getPrice(String product){
        return calculatePrice(product);
    }
    //模拟延迟
    public static void delay(){
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hello asny");
    }
    //模拟获取价格的服务
    private double calculatePrice(String product){
        delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    //异步获取价格
    public Future<Double> getPriceAsync(String product){
        CompletableFuture<Double> future = new CompletableFuture<>();
        new Thread(() -> {
            double price = calculatePrice(product);
            future.complete(price);
        }).start();
        return future;
    }

    @org.junit.Test
    public void testByte(){
        int i = 0xFF;
        byte b= (byte) i;
        System.out.println(b);
    }
}