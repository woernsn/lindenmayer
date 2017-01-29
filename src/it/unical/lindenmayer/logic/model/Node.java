package it.unical.lindenmayer.logic.model;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Node {

	private LindenmayerModel lModel;
	private char symbol;
	private Map<Direction, Node> childNodes;
	private Node parent;

	private Direction direction;
	private Direction lastDirection; // needed for calculation Center/Center
	private Point2D startCoordinates, endCoordinates;
	private double angle;

	private Function<Double[], Double> branchSizeFunction;

	protected Node(LindenmayerModel lModel, char s) {
		this.lModel = lModel;
		this.symbol = s;
		this.childNodes = new HashMap<>();
		this.direction = Direction.CENTER; // default
		this.lastDirection = Direction.CENTER; // default

		this.branchSizeFunction = new Function<Double[], Double>() {

			@Override
			public Double apply(Double[] t) {
				// t[0] = double stepSize
				// t[1] = int myDepth
				double k1 = 50, k2 = 2, k3 = 3, k4 = 4, k5 = 0.2;
				double o = t[1]+1;
				double m = Node.this.getInternodeNumber();
				double a = k1 + k2*o + k3*m + k4*m + k5*m*m;
				double b = 11.5*Math.pow(10, 6);
				double c = 0.58 + 0.0144*a - 0.0244*m;
				double x = a/(1 + b * Math.pow(Math.E, -c*(t[1]/100)));
				System.out.println(o+ " "+ m + " "+ x*Math.pow(10, 7));
				return x*Math.pow(10, 7);

				/*double stepSize = t[0];
				double myDepth = t[1];
				return stepSize / myDepth;*/


				/*
				 * put the function according to the paper
				 * inserire nelle slides figure della pianta con i nomi inglesi per ogni parte
				 */
			}
		};
	}

	public Node getChildNode(Direction d) {
		return this.childNodes.get(d);
	}

	public Node setChildNode(Direction d, Node n) {
		n.direction = d;
		n.parent = this;
		this.childNodes.put(d, n);
		return this.childNodes.get(d);
	}

	public boolean isApex() {
		return childNodes.size() == 0;
	}

	public boolean isInternode() {
		return !isApex();
	}

	public char getSymbol() {
		return this.symbol;
	}

	public Line2D getLine() {
		return new Line2D.Double(this.startCoordinates, this.endCoordinates);
	}
	
	public Function<Double[], Double> getBranchSizeFunction() {
		return branchSizeFunction;
	}

	public void setBranchSizeFunction(Function<Double[], Double> branchSizeFunction) {
		this.branchSizeFunction = branchSizeFunction;
	}

	private int getMyDepth(int depth) {
		if (this.parent == null) {
			return 1;
		}
		return this.parent.getMyDepth(depth) + 1;
	}
	
	private int getInternodeNumber() {
		if (this.parent == null) {
			return 0;
		}
		if (this.parent.symbol == 'I')
			return this.parent.getInternodeNumber()+1;
		return this.parent.getInternodeNumber();
	}

	public void replaceAndDrawChilds(int step) {
		Node leftChild = this.childNodes.get(Direction.LEFT);
		Node centerChild = this.childNodes.get(Direction.CENTER);
		Node rightChild = this.childNodes.get(Direction.RIGHT);

		if (leftChild != null) {
			if (leftChild.isApex()) {
				this.setChildNode(Direction.LEFT, this.lModel.rules.getNewNode(leftChild.getSymbol()));
				this.childNodes.get(Direction.LEFT).setCoordinates(this.endCoordinates, this.angle, this.direction,
						this.lastDirection, Direction.LEFT);
				this.lModel.gp.addLine(step, this.childNodes.get(Direction.LEFT).getLine());
			} else {
				leftChild.replaceAndDrawChilds(step);
			}
		}
		if (centerChild != null) {
			if (centerChild.isApex()) {
				this.setChildNode(Direction.CENTER, this.lModel.rules.getNewNode(centerChild.getSymbol()));
				this.childNodes.get(Direction.CENTER).setCoordinates(this.endCoordinates, this.angle, this.direction,
						this.lastDirection, Direction.CENTER);
				this.lModel.gp.addLine(step, this.childNodes.get(Direction.CENTER).getLine());
			} else {
				centerChild.replaceAndDrawChilds(step);
			}
		}
		if (rightChild != null) {
			if (rightChild.isApex()) {
				this.setChildNode(Direction.RIGHT, this.lModel.rules.getNewNode(rightChild.getSymbol()));
				this.childNodes.get(Direction.RIGHT).setCoordinates(this.endCoordinates, this.angle, this.direction,
						this.lastDirection, Direction.RIGHT);
				this.lModel.gp.addLine(step, this.childNodes.get(Direction.RIGHT).getLine());
			} else {
				rightChild.replaceAndDrawChilds(step);
			}
		}
	}

	public void setCoordinates() {
		this.setCoordinates(null, 0, Direction.CENTER, Direction.CENTER, Direction.CENTER);
	}

	public void setCoordinates(Point2D endPointParent, double parentAngle, Direction parentDirection,
			Direction lastDirection, Direction myDirection) {
		// i am groot!
		if (endPointParent == null) {
			this.startCoordinates = new Point2D.Double(375, 725);
		} else {
			this.startCoordinates = endPointParent;
		}

		double stepSize = this.branchSizeFunction
				.apply(new Double[] { this.lModel.getStepSize(), new Double(this.getMyDepth(0)) });

		if (this.lastDirection == Direction.CENTER)
			this.lastDirection = lastDirection;

		switch (myDirection) {
		case LEFT:
			this.lastDirection = Direction.LEFT;
			switch (parentDirection) {
			case LEFT:
				this.angle = parentAngle + this.lModel.getAngle() * 2;
				break;
			case RIGHT:
				this.angle = parentAngle;
				break;
			case CENTER:
				this.angle = parentAngle + this.lModel.getAngle();
				break;
			}
			this.endCoordinates = new Point2D.Double(
					this.startCoordinates.getX() - stepSize * Math.sin(Math.toRadians(this.angle)),
					this.startCoordinates.getY() - stepSize * Math.cos(Math.toRadians(this.angle)));
			break;
		case CENTER:
			this.angle = parentAngle;
			switch (parentDirection) {
			case LEFT:
				this.endCoordinates = new Point2D.Double(
						this.startCoordinates.getX() - stepSize * Math.sin(Math.toRadians(this.angle)),
						this.startCoordinates.getY() - stepSize * Math.cos(Math.toRadians(this.angle)));
				break;
			case CENTER:
				switch (lastDirection) {
				case LEFT:
					this.angle = parentAngle + this.lModel.getAngle();
					this.endCoordinates = new Point2D.Double(
							this.startCoordinates.getX() - stepSize * Math.sin(Math.toRadians(this.angle)),
							this.startCoordinates.getY() - stepSize * Math.cos(Math.toRadians(this.angle)));
					break;
				case CENTER:
					this.endCoordinates = new Point2D.Double(this.startCoordinates.getX(),
							this.startCoordinates.getY() - stepSize);
					break;
				case RIGHT:
					this.angle = parentAngle + this.lModel.getAngle();
					this.endCoordinates = new Point2D.Double(
							this.startCoordinates.getX() + stepSize * Math.sin(Math.toRadians(this.angle)),
							this.startCoordinates.getY() - stepSize * Math.cos(Math.toRadians(this.angle)));
					break;
				}
				break;
			case RIGHT:
				this.endCoordinates = new Point2D.Double(
						this.startCoordinates.getX() + stepSize * Math.sin(Math.toRadians(this.angle)),
						this.startCoordinates.getY() - stepSize * Math.cos(Math.toRadians(this.angle)));
				break;
			}

			break;
		case RIGHT:
			this.lastDirection = Direction.RIGHT;
			switch (parentDirection) {
			case LEFT:
				this.angle = parentAngle;
				break;
			case CENTER:
				this.angle = parentAngle + this.lModel.getAngle();
				break;
			case RIGHT:
				this.angle = parentAngle + this.lModel.getAngle() * 2;
				break;
			}
			this.endCoordinates = new Point2D.Double(
					this.startCoordinates.getX() + stepSize * Math.sin(Math.toRadians(this.angle)),
					this.startCoordinates.getY() - stepSize * Math.cos(Math.toRadians(this.angle)));
		default:
			break;
		}

		if (this.isInternode()) {
			Node leftChild = this.childNodes.get(Direction.LEFT);
			Node centerChild = this.childNodes.get(Direction.CENTER);
			Node rightChild = this.childNodes.get(Direction.RIGHT);
			if (leftChild != null) {
				leftChild.setCoordinates(this.endCoordinates, this.angle, myDirection, this.lastDirection,
						Direction.LEFT);
			}
			if (centerChild != null) {
				centerChild.setCoordinates(this.endCoordinates, this.angle, myDirection, this.lastDirection,
						Direction.CENTER);
			}
			if (rightChild != null) {
				rightChild.setCoordinates(this.endCoordinates, this.angle, myDirection, this.lastDirection,
						Direction.RIGHT);
			}
		}
	}

	public Node deepCopy() {
		Node newNode = new Node(this.lModel, this.symbol);
		Node leftChild = this.childNodes.get(Direction.LEFT);
		Node centerChild = this.childNodes.get(Direction.CENTER);
		Node rightChild = this.childNodes.get(Direction.RIGHT);

		if (leftChild != null) {
			newNode.setChildNode(Direction.LEFT, leftChild.deepCopy());
		}
		if (centerChild != null) {
			newNode.setChildNode(Direction.CENTER, centerChild.deepCopy());
		}
		if (rightChild != null) {
			newNode.setChildNode(Direction.RIGHT, rightChild.deepCopy());
		}

		return newNode;
	}
}
