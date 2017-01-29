package it.unical.lindenmayer.logic.model;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import it.unical.lindenmayer.graphics.Line2D;

public class Node {

	private LindenmayerModel lModel;
	private char symbol;
	private Map<Direction, Node> childNodes;
	private Node parent;

	private Direction direction;
	private Direction lastDirection; // needed for calculation Center/Center
	private Point2D startCoordinates, endCoordinates;
	private double angle;

	private Function<Object[], Double> branchSizeFunction;

	protected Node(LindenmayerModel lModel, char s) {
		this.lModel = lModel;
		this.symbol = s;
		this.childNodes = new HashMap<>();
		this.direction = Direction.CENTER; // default
		this.lastDirection = Direction.CENTER; // default

		this.branchSizeFunction = new Function<Object[], Double>() {

			@Override
			public Double apply(Object[] t) {
				// t[0] = double stepSize
				// t[1] = double myDepth
				// t[2] = Node node

				double stepSize = (double) t[0];
				double myDepth = (double) t[1];
				return stepSize / myDepth;
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
		return new Line2D(this.startCoordinates, this.endCoordinates);
	}

	public Function<Object[], Double> getBranchSizeFunction() {
		return branchSizeFunction;
	}

	public void setBranchSizeFunction(Function<Object[], Double> branchSizeFunction) {
		this.branchSizeFunction = branchSizeFunction;
	}

	public Node getParent() {
		return this.parent;
	}

	private int getMyDepth(int depth) {
		if (this.parent == null) {
			return 1;
		}
		return this.parent.getMyDepth(depth) + 1;
	}

	public void replaceAndDrawChilds(int step) {
		Node leftChild = this.childNodes.get(Direction.LEFT);
		Node centerChild = this.childNodes.get(Direction.CENTER);
		Node rightChild = this.childNodes.get(Direction.RIGHT);

		if (leftChild != null) {
			Node nodeToDraw = null;
			if (leftChild.isApex()) {
				this.setChildNode(Direction.LEFT, this.lModel.rules.getNewNode(leftChild.getSymbol()));
				this.childNodes.get(Direction.LEFT).setCoordinates(this.endCoordinates, this.angle, this.direction,
						this.lastDirection, Direction.LEFT);
				nodeToDraw = this.childNodes.get(Direction.LEFT);
			} else {
				nodeToDraw = new Node(this.lModel, leftChild.getSymbol());
				nodeToDraw.setCoordinates(this.endCoordinates, this.angle, this.direction, this.lastDirection,
						Direction.LEFT);
			}
			this.lModel.gp.addLine(step, nodeToDraw.getLine());
			if (leftChild.isInternode()) {
				leftChild.replaceAndDrawChilds(step);
			}
		}
		if (centerChild != null) {
			Node nodeToDraw = null;
			if (centerChild.isApex()) {
				this.setChildNode(Direction.CENTER, this.lModel.rules.getNewNode(centerChild.getSymbol()));
				this.childNodes.get(Direction.CENTER).setCoordinates(this.endCoordinates, this.angle, this.direction,
						this.lastDirection, Direction.CENTER);
				nodeToDraw = this.childNodes.get(Direction.CENTER);
			} else {
				nodeToDraw = new Node(this.lModel, centerChild.getSymbol());
				nodeToDraw.setCoordinates(this.endCoordinates, this.angle, this.direction, this.lastDirection,
						Direction.CENTER);
			}
			this.lModel.gp.addLine(step, nodeToDraw.getLine());
			if (centerChild.isInternode()) {
				centerChild.replaceAndDrawChilds(step);
			}
		}
		if (rightChild != null) {
			Node nodeToDraw = null;
			if (rightChild.isApex()) {
				this.setChildNode(Direction.RIGHT, this.lModel.rules.getNewNode(rightChild.getSymbol()));
				this.childNodes.get(Direction.RIGHT).setCoordinates(this.endCoordinates, this.angle, this.direction,
						this.lastDirection, Direction.RIGHT);
				nodeToDraw = this.childNodes.get(Direction.RIGHT);
			} else {
				nodeToDraw = new Node(this.lModel, rightChild.getSymbol());
				nodeToDraw.setCoordinates(this.endCoordinates, this.angle, this.direction, this.lastDirection,
						Direction.RIGHT);
			}
			this.lModel.gp.addLine(step, nodeToDraw.getLine());
			if (rightChild.isInternode()) {
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
				.apply(new Object[] { this.lModel.getStepSize(), new Double(this.getMyDepth(0)), this });

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
