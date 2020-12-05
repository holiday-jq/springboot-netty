package com.holiday.matcloud.protocol.command;

public interface Command {
     Byte MESSAGE_REQUEST = 1;
     Byte MESSAGE_RESPONSE = 2;
     Byte CREATE_GROUP_REQUEST = 3; //创建群
     Byte CREATE_GROUP_RESPONSE = 4; //创建群响应
     Byte LOGIN_REQUEST = 5; //登录请求
     Byte LOGIN_RESPONSE = 6; // 登录响应
     Byte REGISTER_REQUEST = 7;  //注册请求
     Byte REGISTER_RESPONSE = 8; //注册响应
     Byte GROUP_MESSAGE_REQUEST = 9;  //发送群消息
     Byte GROUP_MESSAGE_RESPONSE = 10; //接收群消息  
     Byte HEARTBEAT_REQUEST = 11; //心跳消息请求
     Byte HEARTBEAT_RESPONSE = 12; //心跳消息恢复
}
