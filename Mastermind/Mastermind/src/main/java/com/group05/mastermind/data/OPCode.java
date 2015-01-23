package com.group05.mastermind.data;

public enum OPCode {
	CLIENT_GAMESTART	((byte) 22),
	CLIENT_GUESS		((byte) 33),
	CLIENT_FORFEIT		((byte) 44),
	CLIENT_TESTING		((byte) 55),
	SERVER_GAMESTART	((byte) 66),
	SERVER_HINT			((byte) 77),
	SERVER_LOSE			((byte) 88),
	SERVER_WIN			((byte) 99),
	CLIENT_DISCONNECT	((byte) 127);
	
	private byte code;
	
	private OPCode(byte code) {
		this.code = code;
	}
	
	public byte getOPCode() {
		return code;
	}
}
