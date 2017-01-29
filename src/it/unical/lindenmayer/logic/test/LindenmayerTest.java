package it.unical.lindenmayer.logic.test;

import java.util.function.Function;

import it.unical.lindenmayer.logic.model.Direction;
import it.unical.lindenmayer.logic.model.LindenmayerModel;
import it.unical.lindenmayer.logic.model.Node;
import it.unical.lindenmayer.logic.model.Rule;

public class LindenmayerTest {

	public static void main(String[] args) {
		 runTest1();
//		 runTest2();
//		runTest3();
//		 fraxinusPens();
	}

	@SuppressWarnings("unused")
	private static void runTest1() {
		LindenmayerModel lm = new LindenmayerModel();
		lm.setAngle(5);
		lm.setStepSize(200);
		lm.setSteps(8);
		lm.setTimeout(0.5);

		Node root = lm.createNode('F');
		Node leftChild = lm.createNode('F');
		Node centerChild = lm.createNode('F');
		Node rightChild = lm.createNode('F');

		root.setChildNode(Direction.LEFT, leftChild);
		root.setChildNode(Direction.CENTER, centerChild);
		root.setChildNode(Direction.RIGHT, rightChild);

		Rule r1 = new Rule('F', root);

		lm.addRule(r1);

		lm.start('F');
	}

	@SuppressWarnings("unused")
	private static void runTest2() {
		LindenmayerModel lm = new LindenmayerModel();
		lm.setAngle(5);
		lm.setStepSize(200);
		lm.setSteps(10);
		lm.setTimeout(0.5);

		Node root = lm.createNode('F');
		Node leftChild = lm.createNode('X');
		Node centerChild = lm.createNode('F');
		Node rightChild = lm.createNode('F');

		Node leftHanging = lm.createNode('F');
		Node leftHangingCenter = lm.createNode('F');

		root.setChildNode(Direction.CENTER, centerChild);
		root.setChildNode(Direction.RIGHT, rightChild);

		leftHanging.setChildNode(Direction.CENTER, leftHangingCenter);
		leftHanging.setChildNode(Direction.LEFT, leftChild);

		Rule r1 = new Rule('F', root, 90);
		Rule r2 = new Rule('F', leftHanging, 10);

		lm.addRule(r1);
		lm.addRule(r2);

		lm.start('F');
	}

	private static void runTest3() {
		LindenmayerModel lm = new LindenmayerModel();
		lm.setAngle(20);
		lm.setStepSize(150);
		lm.setSteps(6);
		lm.setTimeout(0.5);

		Direction l = Direction.LEFT;
		Direction c = Direction.CENTER;
		Direction r = Direction.RIGHT;

		Node childGF = lm.createNode('F');

		childGF.setChildNode(l, lm.createNode('G'));
		childGF.setChildNode(r, lm.createNode('G'));

		Node childG = lm.createNode('G');
		childG.setChildNode(l, lm.createNode('G'));
		childG.setChildNode(c, childGF);

		Node childF = lm.createNode('F');
		childF.setChildNode(c, lm.createNode('F'));

		Rule r1 = new Rule('G', childG);
		Rule r2 = new Rule('F', childF);

		lm.addRule(r1);
		lm.addRule(r2);

		lm.start('G');
	}

	@SuppressWarnings("unused")
	private static void fraxinusPens() {
		Function<Object[], Double> branchSizeFunction = new Function<Object[], Double>() {

			@Override
			public Double apply(Object[] t) {
				double stepSize = (double) t[0];
				Node node = (Node) t[1];

				double k1 = 50, k2 = 2, k3 = 3, k4 = 4, k5 = 0.2;
				double o = (double) t[1] + 1;
				double m = getInternodeNumber((Node) t[2]);
				double a = k1 + k2 * o + k3 * m + k4 * m + k5 * m * m;
				double b = 11.5 * Math.pow(10, 6);
				double c = 0.58 + 0.0144 * a - 0.0244 * m;
				double x = a / (1 + b * Math.pow(Math.E, -c * ((double) t[1] / 100)));
				System.out.println(o + " " + m + " " + x * Math.pow(10, 7));
				return x * Math.pow(10, 7);

			}
		};

		LindenmayerModel lm = new LindenmayerModel();
		lm.setAngle(45);
		lm.setStepSize(180);
		lm.setSteps(6);
		lm.setTimeout(0.5);

		Direction l = Direction.LEFT;
		Direction c = Direction.CENTER;
		Direction r = Direction.RIGHT;

		Node root = lm.createNode('I', branchSizeFunction);

		Node child = lm.createNode('I', branchSizeFunction);
		Node centerChild = lm.createNode('I', branchSizeFunction);
		Node rightChild = lm.createNode('L', branchSizeFunction);
		Node leftChild = lm.createNode('L', branchSizeFunction);

		child.setChildNode(l, leftChild);
		child.setChildNode(c, centerChild);
		child.setChildNode(r, rightChild);

		Node last = lm.createNode('T', branchSizeFunction);

		Rule r1 = new Rule('I', child, 75);
		Rule r2 = new Rule('I', last, 25);

		Node leaf = lm.createNode('L', branchSizeFunction);
		Node centerLeaf = lm.createNode('L', branchSizeFunction);
		Node rightLeaf = lm.createNode('E', branchSizeFunction);
		Node leftLeaf = lm.createNode('E', branchSizeFunction);

		leaf.setChildNode(l, leftLeaf);
		leaf.setChildNode(c, centerLeaf);
		leaf.setChildNode(r, rightLeaf);

		Node leafTerm = lm.createNode('K', branchSizeFunction);

		Rule r3 = new Rule('L', leaf, 70);
		Rule r4 = new Rule('L', leafTerm, 30);

		lm.addRule(r1);
		lm.addRule(r2);
		lm.addRule(r3);
		lm.addRule(r4);

		lm.start('I');
	}

	private static int getInternodeNumber(Node n) {
		if (n.getParent() == null) {
			return 0;
		}
		if (n.getParent().getSymbol() == 'I')
			return getInternodeNumber(n.getParent()) + 1;
		return getInternodeNumber(n.getParent());
	}
}
