package org.raygoza.bx.model;

public class GbkLocation {

	private long start;
	private long end;
	boolean extends_left=false;
	boolean extends_right=false;
	
	
	
	public GbkLocation() {
		// TODO Auto-generated constructor stub
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}
	
	public boolean isExtends_left() {
		return extends_left;
	}

	public void setExtends_left(boolean extends_left) {
		this.extends_left = extends_left;
	}

	public boolean isExtends_right() {
		return extends_right;
	}

	public void setExtends_right(boolean extends_right) {
		this.extends_right = extends_right;
	}
	
	
}
