package it.unical.lindenmayer.logic.test;

import it.unical.lindenmayer.logic.model.Direction;
import it.unical.lindenmayer.logic.model.LindenmayerModel;
import it.unical.lindenmayer.logic.model.Node;
import it.unical.lindenmayer.logic.model.Rule;

public class LindenmayerTest {

	public static void main(String[] args) {
		runTest1();
		runTest2();
		runTest3();
	}

	@SuppressWarnings("unused")
	private static void runTest1() {
		LindenmayerModel lm = new LindenmayerModel();
		lm.setAngle(5);
		lm.setStepSize(200);
		lm.setSteps(10);
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
	
	@SuppressWarnings("unused")
	private static void runTest3() {
		LindenmayerModel lm = new LindenmayerModel();
		lm.setAngle(20);
		lm.setStepSize(75);
		lm.setSteps(7);
		lm.setTimeout(0.5);
		
		Direction l = Direction.LEFT;
		Direction c = Direction.CENTER;
		Direction r = Direction.RIGHT;

		Node rootG = lm.createNode('G');
		Node childGF = lm.createNode('F');
		
		childGF.setChildNode(l, lm.createNode('G'));
		childGF.setChildNode(r, lm.createNode('G'));
		
		Node childG = lm.createNode('F');
		childG.setChildNode(l,  lm.createNode('G'));
		childG.setChildNode(c, childGF);
		
		Node rootF = lm.createNode('F');
		Node childF = lm.createNode('F');
		childF.setChildNode(c, lm.createNode('F'));

		Rule r1 = new Rule('G', childG);
		Rule r2 = new Rule('F', childF);

		lm.addRule(r1);
		lm.addRule(r2);

		lm.start('G');
	}
}
