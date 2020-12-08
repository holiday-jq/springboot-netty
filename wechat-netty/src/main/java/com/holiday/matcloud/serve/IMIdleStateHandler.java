package com.holiday.matcloud.serve;

import java.util.concurrent.TimeUnit;
import com.holiday.matcloud.utils.SessionUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
/**
 * 空闲连接检测   如果规定时间没有数据传输则关闭连接    不能实现为单例模式  每个连接都有各自的状态
 */
public class IMIdleStateHandler extends IdleStateHandler{
			
	private static final int READER_IDLE_TIME = 30;
	
	/**
	 * 第一个参数表示读空闲时间
	 * 第二个参数表示写空闲时间
	 * 第三个参数表示读写空闲时间
	 * 第四个参数表示时间单位
	 */
	public IMIdleStateHandler() {
	     super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
	}
	
	@Override
	protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
		// TODO Auto-generated method stub
        System.err.println(READER_IDLE_TIME + "秒内未读到数据，关闭连接");
        SessionUtils.unbind(ctx.channel());
		ctx.channel().close();
	}
}
