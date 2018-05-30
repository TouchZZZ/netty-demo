package com.zjw.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    
    /**
     * 每个传入的消息都要调用
     * */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        ByteBuf buf = (ByteBuf)msg;
        try{
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            String body = new String(req, "UTF-8");
            System.out.println("服务器收到的请求：" + body);
            String time = body.indexOf("111")>0 ? "确认编号" : "认证失败";
            ByteBuf resp = Unpooled.copiedBuffer(time.getBytes());
            ctx.write(resp);
        }finally{
            buf.release();
        }
    }
    
    //通知ChannelInboundHandlerAdapter 最后一次对channelRead()的调用是当前批量读取中的最后一条消息。
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        /**
         * ctx.write(resp) 是将信息写到消息队列，而不是每次write就写入SocketChannel
         * ctx.flush() 是将消息发送队列中的信息写到SocketChannel中发送给对方，
         * ctx.writeAndFlush(resp) 直接写入SocketChannel
         * */
        System.out.println("读取完成");
        ctx.flush();
    }
    
    //处理过程中出现的异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("出现异常");
        cause.printStackTrace();
        ctx.close();
    }
    
}