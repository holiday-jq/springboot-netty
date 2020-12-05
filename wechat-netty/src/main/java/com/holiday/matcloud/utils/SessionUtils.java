package com.holiday.matcloud.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.holiday.matcloud.dto.User;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

public class SessionUtils {
	/**
	 * userID 映射 连接channel
	 */
	private static Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();
	
	/**
	 * groupId ---> channelgroup 群聊ID和群聊ChannelGroup映射
	 */
	private static Map<String, ChannelGroup> groupIdChannelGroupMap = new ConcurrentHashMap<>();
	
	public static void bindChannel(User user, Channel channel) {
		userIdChannelMap.put(user.getUserId(), channel);
		channel.attr(Attributes.SESSION).set(user);
	}
	
	public static void unbind(Channel channel) {
		if (hasLogin(channel)) {
			userIdChannelMap.remove(getUser(channel).getUserId());
			channel.attr(Attributes.SESSION).set(null);
		}
	}
	
	public static boolean hasLogin(Channel channel) {
		return channel.hasAttr(Attributes.SESSION);
	}
	
	public static User getUser(Channel channel) {
		return channel.attr(Attributes.SESSION).get();
	}
	
	public static Channel getChannel(String userId) {
		return userIdChannelMap.get(userId);
	}
	/**
	 * 绑定群聊Id  群聊channelGroup
	 * @param groupId
	 * @param channelGroup
	 */
	public static void bindChannelGroup(String groupId, ChannelGroup channelGroup) {
		groupIdChannelGroupMap.put(groupId, channelGroup);
	}

	public static ChannelGroup getChannelGroup(String groupId) {
		return groupIdChannelGroupMap.get(groupId);
	}
}
