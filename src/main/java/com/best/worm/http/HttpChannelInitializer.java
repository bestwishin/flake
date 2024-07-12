package com.best.worm.http;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.channel.*;

public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {
    private HttpFlakeHandler httpFlakeHandler;

    public HttpChannelInitializer() throws Exception {
        this.httpFlakeHandler = new HttpFlakeHandler();
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(4 * 1024));
        pipeline.addLast(new IdleStateHandler(0, 0, 10));
        pipeline.addLast(httpFlakeHandler);
    }

}
