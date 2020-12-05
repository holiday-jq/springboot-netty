package com.holiday.matcloud.protocol.command;

public abstract class Packet {
    
	private Byte version = 1;
	
	public Byte getVersion() {
		return version;
	}

	public void setVersion(Byte version) {
		this.version = version;
	}

	public abstract Byte getCommand();
}