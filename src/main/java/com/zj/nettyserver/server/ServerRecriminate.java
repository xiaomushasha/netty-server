//package com.zj.nettyserver.server;
//
//
//import io.netty.channel.ChannelHandlerContext;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//
//@Component
//@Qualifier("ServerRecriminate")
//public class ServerRecriminate {
//    private static final Logger logs = LoggerFactory.getLogger(ServerRecriminate.class);
//   /* @Autowired
//         ServerController serverController;*/
//    public   HJ212Bean   ceshi(ChannelHandlerContext ctx,HJ212Bean  handler,String msg) {
//      //  logs.info("handler.getCN():" + handler.getCN());
//            CpDate cpDate=new CpDate();
//        if (handler.getCN().equals("1013")) {
//            handler.setCN("9013");
//            handler.setFlag("4");
//            ctx.channel().writeAndFlush(new Zifu().strings(handler));
//        }
//        else if (handler.getCN().equals("2021")) {
//            handler.setCN("9014");
//            handler.setCP(cpDate);
//            handler.setFlag("4");
//            ctx.channel().writeAndFlush(new Zifu().strings(handler));
//        }
//         else {
//            logs.info("设备平台应答失败");
//        }
//        return handler;
//    }
//
//}
//
//
//
