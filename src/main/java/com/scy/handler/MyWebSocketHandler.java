package com.scy.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @desc:
 * @author: Suocaiyuan
 * @date: 2019-12-28 16:26
 **/
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup channelGroup;

    //每当有客户端连接的时候，就添加到channelGroup中
    static {
        channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    //客户端与服务器建立连接时触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户建立连接，通道开启");
        //添加到group通道组
        channelGroup.add(ctx.channel());
    }

    //客户端与服务器关闭连接时触发
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端断开连接，通道关闭");
        channelGroup.remove(ctx.channel());
    }

    //服务器接收客户端的数据信息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器收到的数据： " + msg.text());
        //sendMessage(msg);
       sendAllMessage();
    }

    //给固定的人发消息
    private void sendMessage(ChannelHandlerContext ctx) {
        String message = "你好," + ctx.channel().localAddress() + "给固定的人发消息";
        ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
    }

    //发送群消息，此时其他客户端也能接收到消息
    private void sendAllMessage() {
        String msg = "我是服务器,发送群消息";
        channelGroup.writeAndFlush(new TextWebSocketFrame(msg));
    }
}
