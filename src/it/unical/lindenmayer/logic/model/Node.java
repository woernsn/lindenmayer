package it.unical.lindenmayer.logic.model;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

public class Node {

	private LindenmayerModel lModel;
	private char symbol;
	private Map<Direction, Node> childNodes;

	private Point2D startCoordinates, endCoordinates;

	protected Node(LindenmayerModel lModel, char s) {
		this.lModel = lModel;
		this.symbol = s;
		this.childNodes = new HashMap<>();
	}

	public Node getChildNode(Direction d) {
		return this.childNodes.get(d);
	}

	public void setChildNode(Direction d, Node n) {
		this.childNodes.put(d, n);
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

	public void replaceAndDrawChilds(int step) {
		Node leftChild = this.childNodes.get(Direction.LEFT);
		Node centerChild = this.childNodes.get(Direction.CENTER);
		Node rightChild = this.childNodes.get(Direction.RIGHT);

		if (leftChild != null) {
			if (leftChild.isApex()) {
				this.childNodes.put(Direction.LEFT, this.lModel.rules.getNewNode(leftChild.getSymbol()));
				this.childNodes.get(Direction.LEFT).setCoordinates(this.endCoordinates, Direction.LEFT);
				this.lModel.gp.addLine(step, this.childNodes.get(Direction.LEFT).getLine());
			} else {
				leftChild.replaceAndDrawChilds(step);
			}
		}
		if (centerChild != null) {
			if (centerChild.isApex()) {
				this.childNodes.put(Direction.CENTER, this.lModel.rules.getNewNode(centerChild.getSymbol()));
				this.childNodes.get(Direction.CENTER).setCoordinates(this.endCoordinates, Direction.CENTER);
				this.lModel.gp.addLine(step, this.childNodes.get(Direction.CENTER).getLine());
			} else {
				centerChild.replaceAndDrawChilds(step);
			}
		}
		if (rightChild != null) {
			if (rightChild.isApex()) {
				this.childNodes.put(Direction.RIGHT, this.lModel.rules.getNewNode(rightChild.getSymbol()));
				this.childNodes.get(Direction.RIGHT).setCoordinates(this.endCoordinates, Direction.RIGHT);
				this.lModel.gp.addLine(step, this.childNodes.get(Direction.RIGHT).getLine());
			} else {
				rightChild.replaceAndDrawChilds(step);
			}
		}
	}

	public void setCoordinates() {
		this.setCoordinates(null, Direction.CENTER);
	}

	public void setCoordinates(Point2D endPointParent, Direction myDirection) {
		// i am groot!
		if (endPointParent == null) {
			this.startCoordinates = new Point2D.Double(400, 800);
		} else {
			this.startCoordinates = endPointParent;
		}

		switch (myDirection) {
		case LEFT:
			this.endCoordinates = new Point2D.Double(
					this.startCoordinates.getX()
							- this.lModel.getStepSize() * Math.cos(Math.toRadians(this.lModel.getAngle())),
					this.startCoordinates.getY()
							- this.lModel.getStepSize() * Math.sin(Math.toRadians(this.lModel.getAngle())));
			break;
		case CENTER:
			this.endCoordinates = new Point2D.Double(this.startCoordinates.getX(),
					this.startCoordinates.getY() - this.lModel.getStepSize());
			break;
		case RIGHT:
			this.endCoordinates = new Point2D.Double(
					this.startCoordinates.getX()
							+ this.lModel.getStepSize() * Math.cos(Math.toRadians(this.lModel.getAngle())),
					this.startCoordinates.getY()
							- this.lModel.getStepSize() * Math.sin(Math.toRadians(this.lModel.getAngle())));
			break;

		default:
			break;
		}

		if (this.isInternode()) {
			Node leftChild = this.childNodes.get(Direction.LEFT);
			Node centerChild = this.childNodes.get(Direction.CENTER);
			Node rightChild = this.childNodes.get(Direction.RIGHT);
			if (leftChild != null) {
				leftChild.setCoordinates(this.endCoordinates, Direction.LEFT);
			}
			if (centerChild != null) {
				centerChild.setCoordinates(this.endCoordinates, Direction.CENTER);
			}
			if (rightChild != null) {
				rightChild.setCoordinates(this.endCoordinates, Direction.RIGHT);
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
