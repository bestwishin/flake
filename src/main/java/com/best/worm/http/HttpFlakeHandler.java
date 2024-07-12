package com.best.worm.http;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.best.worm.process.Event;
import com.best.worm.process.EventProcessor;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.timeout.IdleStateEvent;

@Sharable
public class HttpFlakeHandler extends SimpleChannelInboundHandler<HttpObject> {
    private EventProcessor processor;

    public HttpFlakeHandler() throws Exception {
        this.processor = EventProcessor.getInstance();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        FullHttpRequest req = (FullHttpRequest) msg;
        processor.publish(new Event(req.uri().substring(1), ctx));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            System.out.println(event.state());
            ctx.close().addListener(future -> {
                System.out.println("close channel:" + future.isSuccess());

            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}