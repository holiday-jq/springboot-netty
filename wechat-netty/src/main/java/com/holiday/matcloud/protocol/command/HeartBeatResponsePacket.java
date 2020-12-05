package com.holiday.matcloud.protocol.command;

public class HeartBeatResponsePacket extends Packet{

	@Override
	public Byte getCommand() {
		// TODO Auto-generated method stub
		return Command.HEARTBEAT_RESPONSE;
	}

}
