package it.unical.lindenmayer.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class DrawTest {

	public static void main(String[] args) {
		GraphicPanel gp = new GraphicPanel();
		double currentL = 0;
		
		
		for(int i = 0; i < 10; i++) {
			gp.addLine(i, new Line2D.Double(currentL, currentL, currentL+100, currentL+100));
			currentL+=100;
		}
		
		gp.startAnimation(1);
	}

}
