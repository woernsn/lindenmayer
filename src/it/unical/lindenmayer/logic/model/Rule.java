package it.unical.lindenmayer.logic.model;

public class Rule {

	private int propability;
	private Character symbolFrom;
	private Node nodeTo;
	
	public Rule(Character symbolFrom, Node nodeTo) {
		this(symbolFrom, nodeTo, 100);
	}
	
	public Rule(Character symbolFrom, Node nodeTo, int propability) throws IllegalArgumentException {
		if(propability < 0 || propability > 100) {
			throw new IllegalArgumentException("Propability has to be between 0 and 100.");
		}
		this.symbolFrom = symbolFrom;
		this.nodeTo = nodeTo;
		this.propability = propability;
	}
	
	public int getPropability() {
		return this.propability;
	}
	
	public Character getSymbolFrom() {
		return this.symbolFrom;
	}
	
	public Node getNodeTo() {
		// return a copy!!
		return this.nodeTo.deepCopy();
	}
	
}
