package com.holiday.matcloud.serve;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.holiday.matcloud.dto.User;
import com.holiday.matcloud.protocol.command.CreateGroupRequestPacket;
import com.holiday.matcloud.protocol.command.GroupMessageRequestPacket;
import com.holiday.matcloud.protocol.command.HeartBeatRequestPacket;
import com.holiday.matcloud.protocol.command.MessageRequestPacket;
import com.holiday.matcloud.protocol.command.Packet;
import com.holiday.matcloud.protocol.command.RegisterRequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

@Sharable
public class HttpRequestHandler extends SimpleChannelInboundHandler<Object> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public static HttpRequestHandler INSTANCE = new HttpRequestHandler();
	
	private WebSocketServerHandshaker handshaker;

	private HttpRequestHandler() {

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest)msg);
			logger.info("http 握手成功");
		} else if (msg instanceof WebSocketFrame) {
			//fireChannelRead 传递给下一个handler
			try  {
				handWebsocketFrame(ctx, (WebSocketFrame)msg);								
			}catch (Exception ex) {
				throw new Exception();
			}
		}
	}
	
	private void handWebsocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		//判断是否是关闭websocket的指令
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame)frame.retain());
			return;
		}
		//判断是否是ping消息
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		
		TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) frame;
		ByteBuf bytebuf = textWebSocketFrame.content();
		String content = bytebuf.toString(Charset.forName("utf-8"));
		JSONObject jsonObject = JSONObject.parseObject(content);
		System.err.println("请求参数："+jsonObject);
		Byte type = jsonObject.getByte("type");
		JSONObject parmas = jsonObject.getJSONObject("params");
		Packet packet = null;
		switch (type) {
		   // 注册user-->channel 映射
		   case 7:
			  RegisterRequestPacket registerRequestPacket = new RegisterRequestPacket();
			  User user =  JSON.parseObject(parmas.toJSONString(), User.class);
			  registerRequestPacket.setUser(user);
			  packet = registerRequestPacket;
			  break;
		   // 单聊
		   case 1:
			  MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
			  messageRequestPacket.setMessage(parmas.getString("message"));
			  messageRequestPacket.setToUserId(parmas.getString("toMessageId"));
			  messageRequestPacket.setFileType(parmas.getString("fileType"));
			  packet = messageRequestPacket;
			  break;
		   // 创建群聊
		   case 3:
			  CreateGroupRequestPacket createGroupRequestPacket = new CreateGroupRequestPacket();
			  String userListStr = parmas.getString("userIdList");
			  List<String> userIdList = Arrays.asList(userListStr.split(","));
			  createGroupRequestPacket.setUserIdList(userIdList);
			  packet = createGroupRequestPacket;
			  break;
		  // 群聊消息
		   case 9:
			  GroupMessageRequestPacket groupMessageRequestPacket = new GroupMessageRequestPacket(); 
			  groupMessageRequestPacket.setMessage(parmas.getString("message"));
			  groupMessageRequestPacket.setToGroupId(parmas.getString("toMessageId"));
			  groupMessageRequestPacket.setFileType(parmas.getString("fileType"));
			  packet = groupMessageRequestPacket;
			  break;
		  //心跳检测	  
		   case 11:
			  HeartBeatRequestPacket heartBeatRequestPacket = new HeartBeatRequestPacket();
			  packet = heartBeatRequestPacket;
			  break;
			  default: 
			  break;
		}
		ctx.fireChannelRead(packet);
	}
	
	  /**
     * 描述：处理Http请求，主要是完成HTTP协议到Websocket协议的升级
     * @param ctx
     * @param req
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req,
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws:/" + ctx.channel() + "/websocket", null, false);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
        this.handshaker = handshaker;
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }
    
    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        boolean keepAlive = HttpUtil.isKeepAlive(req);
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!keepAlive) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 描述：异常处理，关闭channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
