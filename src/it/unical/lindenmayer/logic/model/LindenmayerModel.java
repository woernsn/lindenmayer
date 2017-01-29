package it.unical.lindenmayer.logic.model;

import java.util.function.Function;

import it.unical.lindenmayer.graphics.GraphicPanel;

public class LindenmayerModel {

	private final static double DEFAULT_ANGLE = 45;
	private final static double DEFAULT_STEPSIZE = 100;
	private final static double DEFAULT_TIMEOUT = 1;

	private double angle;
	private double stepSize;
	private double timeout;
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
		this.gp = new GraphicPanel();
		this.timeout = DEFAULT_TIMEOUT;

		Node finalNode = new Node(this, 'X');
		this.rules = new RuleMap(finalNode);
	}

	public void addRule(Rule r) {
		this.rules.add(r);
	}

	public Node createNode(char symbol) {
		return new Node(this, symbol);
	}

	public Node createNode(char symbol, Function<Object[], Double> branchSizeFunction) {
		Node newNode = new Node(this, symbol);
		newNode.setBranchSizeFunction(branchSizeFunction);
		return newNode;
	}

	public void start(Node rootNode) {
		// build tree
		rootNode.setCoordinates();
		this.gp.addLine(0, rootNode.getLine());
		rootNode = rules.getNewNode(rootNode); // first step
		rootNode.setCoordinates();
		for (int i = 1; i < this.steps; i++) {
			rootNode.replaceAndDrawChilds(i);
		}

		this.gp.setVisible(true);
		this.gp.startAnimation(this.timeout);
	}

//	public void start(char rootSymbol) {
//		// build tree
//		Node rootNode = new Node(this, rootSymbol);
//		rootNode.setCoordinates();
//		this.gp.addLine(0, rootNode.getLine());
//		rootNode = rules.getNewNode(rootNode); // first step
//		rootNode.setCoordinates();
//		for (int i = 1; i < this.steps; i++) {
//			rootNode.replaceAndDrawChilds(i);
//		}
//
//		this.gp.setVisible(true);
//		this.gp.startAnimation(this.timeout);
//	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
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

	public double getTimeout() {
		return timeout;
	}

	public void setTimeout(double timeout) {
		this.timeout = timeout;
	}

}
