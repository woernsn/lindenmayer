package it.unical.lindenmayer.logic.model;

public class Rule {

	private int probability;
	private Character symbolFrom;
	private Node nodeTo;
	
	public Rule(Character symbolFrom, Node nodeTo) {
		this(symbolFrom, nodeTo, 100);
	}
	
	public Rule(Character symbolFrom, Node nodeTo, int probability) throws IllegalArgumentException {
		if(probability < 0 || probability > 100) {
			throw new IllegalArgumentException("Propability has to be between 0 and 100.");
		}
		this.symbolFrom = symbolFrom;
		this.nodeTo = nodeTo;
		this.probability = probability;
	}
	
	public int getProbability() {
		return this.probability;
	}
	
	public Character getSymbolFrom() {
		return this.symbolFrom;
	}
	
	public Node getNodeTo() {
		// return a copy!!
		return this.nodeTo.deepCopy();
	}
	
}
