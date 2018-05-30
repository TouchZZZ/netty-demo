package com.zjw.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {
    public void connect(String host, int port) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap boot = new Bootstrap();
            boot.group(group).channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeClientHandler());
                }
                
            });
            
            ChannelFuture cf = boot.connect(host, port).sync();
            cf.channel().closeFuture().sync();
        }finally{
            group.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) {
        final String host = "127.0.0.1";
        final int port = 8180;
        try {
            new TimeClient().connect(host, port);
        } catch (Exception e) {
            System.out.println("客户端" + Thread.currentThread().getId() + " 请求出现异常");
        }
                
    }
}