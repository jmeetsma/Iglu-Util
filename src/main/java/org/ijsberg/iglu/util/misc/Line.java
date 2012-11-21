package org.ijsberg.iglu.util.misc;

public class Line {
	
	private String fileName;
	private int number;
	private String line;

	public Line(String fileName, int number, String line) {
		super();
		this.fileName = fileName;
		this.number = number;
		this.line = line;
	}

	public Line(int number, String line) {
		super();
		this.number = number;
		this.line = line;
	}
	
	public int getNumber() {
		return number;
	}

	public String getLine() {
		return line;
	}
	
	public String toString() {
		return (fileName != null ? fileName + " " : "") + number + ": " + line;
	}
	
	

}
