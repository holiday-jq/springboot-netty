package com.holiday.matcloud.protocol.command;

public class MessageRequestPacket extends Packet{

	private String toUserId;
	
	private String message;
	
	private String fileType;
			
	public MessageRequestPacket() {
		super();
	}

	public MessageRequestPacket(String toUserId, String message) {
		super();
		this.toUserId = toUserId;
		this.message = message;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	@Override
	public Byte getCommand() {
		// TODO Auto-generated method stub
		return Command.MESSAGE_REQUEST;
	}

}
