package com.holiday.matcloud.protocol.command;

public class GroupMessageRequestPacket extends Packet{

	private String toGroupId;
	
	private String message;
	
	private String fileType;
	
	public GroupMessageRequestPacket() {
		
	}
	
	public GroupMessageRequestPacket(String toGroupId, String message) {
		super();
		this.toGroupId = toGroupId;
		this.message = message;
	}

	public String getToGroupId() {
		return toGroupId;
	}

	public void setToGroupId(String toGroupId) {
		this.toGroupId = toGroupId;
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
		return Command.GROUP_MESSAGE_REQUEST;
	}

}
