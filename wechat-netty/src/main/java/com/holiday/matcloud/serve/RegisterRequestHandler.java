package com.holiday.matcloud.serve;

import com.holiday.matcloud.dto.User;
import com.holiday.matcloud.protocol.command.RegisterRequestPacket;
import com.holiday.matcloud.utils.SessionUtils;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
/**
 * 注册 绑定用户channel
 * @author holiday
 * date 2020-11-04
 */
@Sharable
public class RegisterRequestHandler extends SimpleChannelInboundHandler<RegisterRequestPacket>{

	public static RegisterRequestHandler INSTANCE = new RegisterRequestHandler();
	
	private RegisterRequestHandler() {
		
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RegisterRequestPacket registerRequestPacket) throws Exception {
		User loginUser = registerRequestPacket.getUser();
		SessionUtils.bindChannel(loginUser, ctx.channel());
		if (SessionUtils.hasLogin(ctx.channel())) {
			System.out.println("该用户已登录");
		}
//		
//     ByteBuf buffer = getByteBuf(ctx);      
//     ctx.channel().writeAndFlush(new TextWebSocketFrame(buffer));
	}
	
}
