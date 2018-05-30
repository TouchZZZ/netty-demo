package com.zjw.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    int a=101;

    //在到服务器的连接已经建立之后将被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("给服务器发送消息");
        a++;
        byte[] req = ("我是客户端1号,编号"+String.valueOf(a)).getBytes();
        ByteBuf firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
        ctx.writeAndFlush(firstMessage);
    }
    
    //当从服务器接受到一条消息时被调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        ByteBuf buf = (ByteBuf)msg;
        byte[] resp = new byte[buf.readableBytes()];
        buf.readBytes(resp);
        String str = new String(resp, "UTF-8");
        System.out.println("服务器时间 : "+ str);
        System.out.println("服务器反馈");
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws InterruptedException {
        Thread.sleep(1000);

        System.out.println("读取完成，再次请求");
        channelActive(ctx);

        // ctx.flush();
    }
 
    //处理过程中出现的异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
      
}