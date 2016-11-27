package it.unical.lindenmayer.logic.model;

public enum Direction {
	CENTER(""), LEFT("+"), RIGHT("-");
	
	private String directionSymbol;
	
	Direction(String symbol) {
		this.directionSymbol = symbol;
	}
	
	public String toString() {
		return this.directionSymbol;
	}
}
