package com.holiday.matcloud.protocol.command;

import com.holiday.matcloud.dto.User;

public class RegisterRequestPacket extends Packet{
    	
	private User user;
			
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public Byte getCommand() {
		// TODO Auto-generated method stub
		return Command.REGISTER_REQUEST;
	}

}
