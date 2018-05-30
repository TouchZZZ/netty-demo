package com.zjw.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
    public void bind(int port) throws Exception{
        //NIO线程组,专门用于处理网络事件，本质是Reactor线程组。
        EventLoopGroup bosser = new NioEventLoopGroup();//用于服务端接受客户端的连接
        EventLoopGroup worker = new NioEventLoopGroup();//用于进行SocketChannel的网络读写
        
        try {
            ServerBootstrap boot = new ServerBootstrap();//启动NIO服务端的辅助启动类，降低开发复杂度。
            
            boot.group(bosser, worker)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 1024)//配置TCP参数
            .childHandler(new ChannelInitializer<SocketChannel>(){//用于处理网络IO事件（记录日志，对消息进行编解码）
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeServerHandler());
                }
            });
            
            ChannelFuture cf = boot.bind(port).sync();//异步地绑定服务器，调用sync()方法阻塞等待直到绑定完成。
            cf.channel().closeFuture().sync();//获取ChannelFuture，并且阻塞当前线程直到它完成。
        } finally{
            bosser.shutdownGracefully();//释放所有资源
            worker.shutdownGracefully();
        }
        
    }
    
    
    public static void main(String[] args) {
        int port = 8180;
        try {
            new TimeServer().bind(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}