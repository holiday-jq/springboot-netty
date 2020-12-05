package com.holiday.matcloud.serve;

import com.alibaba.fastjson.JSONObject;
import com.holiday.matcloud.protocol.command.HeartBeatRequestPacket;
import com.holiday.matcloud.protocol.command.HeartBeatResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket>{

	public static HeartBeatRequestHandler INSTANCE = new HeartBeatRequestHandler();
	
	private HeartBeatRequestHandler () {
		
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket heartBeatRequestPacket) throws Exception {
		// TODO Auto-generated method stub
		ByteBuf byteBuf = getBuf(ctx);
		ctx.channel().writeAndFlush(new TextWebSocketFrame(byteBuf));
	}
	
	public ByteBuf getBuf(ChannelHandlerContext ctx) {
		 ByteBuf byteBuf = ctx.alloc().buffer();
		 JSONObject data = new JSONObject();
		 data.put("type", new HeartBeatResponsePacket().getCommand());
		 data.put("status", 200);
		 byte bytes[] = data.toJSONString().getBytes();
		 byteBuf.writeBytes(bytes);
		 return byteBuf;
	}

}
