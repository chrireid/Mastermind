package com.group05.mastermind.data;

public enum CCode {

	RED			((byte)0),
	MAGENTA		((byte)1), 
	PURPLE		((byte)2), 
	BLUE		((byte)3), 
	CYAN		((byte)4), 
	GREEN		((byte)5), 
	YELLOW		((byte)6), 
	ORANGE		((byte)7);
	
	private byte code;
	
	private CCode (byte code) {
        this.code = code;
    }

    public byte getCCode() {
        return code;
    }
	
}