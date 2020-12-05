package com.holiday.matcloud.protocol.command;

public class HeartBeatRequestPacket extends Packet{

	@Override
	public Byte getCommand() {
		// TODO Auto-generated method stub
		return Command.HEARTBEAT_REQUEST;
	}

}
