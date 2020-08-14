package com.zj.nettyserver.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Qualifier("springProtocolInitializer")
public class StringProtocolInitalizer extends ChannelInitializer<SocketChannel> {
    private static final Logger log = LoggerFactory.getLogger(StringProtocolInitalizer.class);
    @Autowired
    StringDecoder stringDecoder;

    @Autowired
    StringEncoder stringEncoder;

    @Autowired
    ServerHandler serverHandler;
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //检测心跳必须在第一位
        pipeline.addLast(new IdleStateHandler(600,0,0,TimeUnit.SECONDS));
        pipeline.addLast("decoder", stringDecoder);
        pipeline.addLast("handler", serverHandler);
        pipeline.addLast("encoder", stringEncoder);
      //  pipeline.addLast( new LineBasedFrameDecoder(1024));
    }

    public StringDecoder getStringDecoder() {
        return stringDecoder;
    }

    public void setStringDecoder(StringDecoder stringDecoder) {
        this.stringDecoder = stringDecoder;
    }

    public StringEncoder getStringEncoder() {
        return stringEncoder;
    }

    public void setStringEncoder(StringEncoder stringEncoder) {
        this.stringEncoder = stringEncoder;
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

}
