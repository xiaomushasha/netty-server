package com.zj.nettyserver.server;

//import com.haileer.hleconnect.bean.DtuEntity;
//import com.haileer.hleconnect.controller.ServerController;
//import com.haileer.hleconnect.tools.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.ReadTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@Qualifier("serverHandler")
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);
    private int loss_connect_time = 0;
    //private static DeviceWarnService deviceWarnService;

    private static Map<SocketChannel, String> nettyMap = new ConcurrentHashMap();
   // netty读数据方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String s = (String) msg;
        log.info("我是message：" + s);
        String clientIdToLong = ctx.channel().id().asLongText();
        log.info("client long id:" + clientIdToLong);
        String clientIdToShort = ctx.channel().id().asShortText();
        log.info("client short id:" + clientIdToShort);
        String address = ctx.channel().remoteAddress().toString();
        log.info("client address:" + address);
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
    }
   //netty 检测活跃端口方法
    @Override
    public void channelActive(ChannelHandlerContext ctx1) throws Exception {
        nettyMap.put((SocketChannel) ctx1.channel(), ctx1.channel().id().asLongText());
        log.info("Welcome to " + ctx1.channel().remoteAddress() + " service!\n");
        super.channelActive(ctx1);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        try {
            closeOnFlush(ctx.channel());
            if (cause instanceof ReadTimeoutException) {
                ctx.close().sync();
            }
        } catch (Exception e) {
            log.info(e.getMessage() + "你好！");
        }
    }

    public void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("\n 不活跃：+Channel is disconnected");

        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture channelFuture = serverBootstrap.bind(insocket.getPort()).syncUninterruptibly();
        channelFuture.channel().closeFuture().syncUninterruptibly();
        ctx.channel().close();
        super.channelInactive(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {


        log.info("不活跃map中的key值" +  ctx.channel().remoteAddress());
        super.channelInactive(ctx);

    }


//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent) {
//            IdleStateEvent event = (IdleStateEvent) evt;
//            if (event.state() == IdleState.READER_IDLE) {
//                ChannelFuture future = ctx.writeAndFlush("我要把你清除掉了");
//                future.addListener(new ChannelFutureListener() {
//                    @Override
//                    public void operationComplete(ChannelFuture future) throws Exception {
////                        String key = new UpdateStatus().stopClient(ctx);
//                        if(key!=null && !"".equals(key)){
//                         updateDtuStatus updateDtu = new updateDtuStatus(key);
//                         String loges=updateDtu.returnMes();
//                         log.info("更改设备状态0"+loges);
//        }
//                        new CheckMsg().remove(ctx.channel().remoteAddress().toString());
//                        log.info("不活跃map中的key值" + key + ctx.channel().remoteAddress());
//                        future.channel().close();
//                    }
//                });
//                return;
//            }
//        }
//        super.userEventTriggered(ctx, evt);
//    }


    class updateDtuStatus {
        private int count = 0;
        private String mn;

        public updateDtuStatus(String mn) {
            this.mn = mn;
        }

        String returnMes() {
            String loges = "";
            try {
//                DtuEntity ue = new DtuEntity();
//                ue.setConnectStatus(0);
//                loges = serverController.updateDtuStatus(mn, ue);
            } catch (Exception e) {
                count++;
                if (count < 3) {
                    loges = returnMes();
                } else {
                    loges = "更改设备信息：" + count + " " + e.toString();
                }
            }
            return count + "-" + loges;
        }

    }


}
