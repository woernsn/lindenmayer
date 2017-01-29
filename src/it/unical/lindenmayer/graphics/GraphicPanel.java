package it.unical.lindenmayer.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphicPanel extends JFrame {

	private static final long serialVersionUID = 1L;

	private LinePanel panel;

	public GraphicPanel() {
		super("Lindenmayer");
		this.setSize(750, 750);
		this.setVisible(false);
		this.setBackground(Color.WHITE);

		this.panel = new LinePanel();

		this.add(panel);
		this.panel.setVisible(true);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
	}

	public void addLine(int interval, Line2D line) {
		this.panel.addLine(interval, line);
	}

	public void startAnimation(double timeout) {
		int maximalStep = panel.getMaximalStep();
		for (int i = 0; i < maximalStep; i++) {
			try {
				Thread.sleep((int) Math.round(timeout * 1000));
				this.panel.incrementCurrentInterval();
				this.repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private class LinePanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private Map<Integer, List<Line2D>> lines;
		private int maximalStep;
		private int currentInterval;

		private BasicStroke stroke;
		private int colorWidth;

		public LinePanel() {
			super();
			this.lines = new HashMap<>();
			this.currentInterval = 0;
			this.maximalStep = 0;
			this.stroke = new BasicStroke(2);
		}

		public int incrementCurrentInterval() {
			return ++this.currentInterval;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			for (int i = 0; i <= this.currentInterval; i++) {
				try {
					for (Line2D line : lines.get(i)) {
						g2.setStroke(this.stroke);
						g2.setColor(new Color(i * colorWidth, Math.min(100+(i*colorWidth), 255), i * colorWidth));
						g2.draw(line);
					}
				} catch (NullPointerException e) {
					//
				}
			}
		}

		public void addLine(int interval, Line2D line) {
			if (interval > this.maximalStep) {
				this.maximalStep = interval;
				colorWidth = 180 / this.maximalStep;
			}

			if (this.lines.get(interval) == null) {
				this.lines.put(interval, new ArrayList<Line2D>());
			}
			this.lines.get(interval).add(line);
		}

		public int getMaximalStep() {
			return this.maximalStep;
		}
	}
}
