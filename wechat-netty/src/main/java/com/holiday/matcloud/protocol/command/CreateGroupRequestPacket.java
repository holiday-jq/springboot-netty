package com.holiday.matcloud.protocol.command;

import java.util.List;

public class CreateGroupRequestPacket extends Packet{
    /**
     * 创建群里需要把群里的Id发过来
     */
	private List<String> userIdList;
		
	public List<String> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<String> userIdList) {
		this.userIdList = userIdList;
	}

	@Override
	public Byte getCommand() {
		// TODO Auto-generated method stub
		return Command.CREATE_GROUP_REQUEST;
	}

}
