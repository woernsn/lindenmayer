package it.unical.lindenmayer.logic.model;

import it.unical.lindenmayer.graphics.GraphicPanel;

public class LindenmayerModel {

	private final static double DEFAULT_ANGLE = 45;
	private final static double DEFAULT_STEPSIZE = 100;
	private final static int DEFAULT_TIMEOUT = 1;
	
	private double angle;
	private double stepSize;
	private int timeout;
	protected RuleMap rules;
	private int steps;
	
	protected GraphicPanel gp;
	
	public LindenmayerModel() {
		this(DEFAULT_ANGLE);
	}
	
	public LindenmayerModel(double angle) {
		this(angle, DEFAULT_STEPSIZE);
	}
	
	public LindenmayerModel(double angle, double stepSize) {
		this.angle = angle;
		this.stepSize = stepSize;
		this.rules = new RuleMap();
		this.gp = new GraphicPanel();
		this.timeout = DEFAULT_TIMEOUT;
	}

	public void addRule(Rule r) {
		this.rules.add(r);
	}
	
	public Node createNode(char symbol) {
		return new Node(this, symbol);
	}
	
	public void start(char rootSymbol) {
		// build tree
		Node rootNode = new Node(this, rootSymbol);
		rootNode.setCoordinates();
		this.gp.addLine(0, rootNode.getLine());
		rootNode = rules.getNewNode(rootNode); //first step
		rootNode.setCoordinates();
		for (int i = 1; i < this.steps; i++) {
			rootNode.replaceAndDrawChilds(i);
		}
		
		this.gp.startAnimation(this.timeout);
	}
	
	
	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = 90-angle;
	}

	public double getStepSize() {
		return stepSize;
	}

	public void setStepSize(double stepSize) {
		this.stepSize = stepSize;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public GraphicPanel getGp() {
		return gp;
	}

	public void setGp(GraphicPanel gp) {
		this.gp = gp;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
}
