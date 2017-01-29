package it.unical.lindenmayer.graphics;

import java.awt.geom.Point2D;

public class Line2D extends java.awt.geom.Line2D.Double {

	private static final long serialVersionUID = 1L;

	public Line2D(Point2D startCoordinates, Point2D endCoordinates) {
		super(startCoordinates, endCoordinates);
	}

	@Override
	public boolean equals(Object obj) {
		Line2D otherLine = (Line2D) obj;
		
		if (otherLine.getX1() == this.getX1())
			if (otherLine.getX2() == this.getX2())
				if (otherLine.getY1() == this.getY1())
					if (otherLine.getY2() == this.getY2())
						return true;
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("P1: [%f/%f], P2: [%f/%f]", this.getX1(), this.getY1(), this.getX2(), this.getY2());
	}
}
