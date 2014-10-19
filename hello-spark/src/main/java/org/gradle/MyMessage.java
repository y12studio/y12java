package org.gradle;

public class MyMessage {

	public MyMessage(String message, int fooInt) {
		super();
		this.message = message;
		this.fooInt = fooInt;
	}

	private String message;
	private int fooInt;
	
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getFooInt() {
		return fooInt;
	}

	public void setFooInt(int fooInt) {
		this.fooInt = fooInt;
	}

}
