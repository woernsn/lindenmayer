package it.unical.lindenmayer.logic.test;

import it.unical.lindenmayer.logic.model.Direction;
import it.unical.lindenmayer.logic.model.LindenmayerModel;
import it.unical.lindenmayer.logic.model.Node;
import it.unical.lindenmayer.logic.model.Rule;

public class LindenmayerTest {

	public static void main(String[] args) {
//		runTest1();
//		runTest2();
		fraxinusPens();
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
	private static void fraxinusPens() {
		LindenmayerModel lm = new LindenmayerModel();
		lm.setAngle(45);
		lm.setStepSize(180);
		lm.setSteps(6);
		lm.setTimeout(0.5);

		Direction l = Direction.LEFT;
		Direction c = Direction.CENTER;
		Direction r = Direction.RIGHT;

		Node root = lm.createNode('I');

		Node child = lm.createNode('I');
		Node centerChild = lm.createNode('I');
		Node rightChild = lm.createNode('L');
		Node leftChild = lm.createNode('L');

		child.setChildNode(l, leftChild);
		child.setChildNode(c, centerChild);
		child.setChildNode(r, rightChild);

		Node last = lm.createNode('T');

		Rule r1 = new Rule('I', child, 75);
		Rule r2 = new Rule('I', last, 25);
		
		Node leaf = lm.createNode('L');
		Node centerLeaf = lm.createNode('L');
		Node rightLeaf = lm.createNode('E');
		Node leftLeaf = lm.createNode('E');
		
		leaf.setChildNode(l, leftLeaf);
		leaf.setChildNode(c, centerLeaf);
		leaf.setChildNode(r, rightLeaf);
		
		Node leafTerm = lm.createNode('K');
		
		Rule r3 = new Rule('L', leaf, 70);
		Rule r4 = new Rule('L', leafTerm, 30);

		lm.addRule(r1);
		lm.addRule(r2);
		lm.addRule(r3);
		lm.addRule(r4);

		lm.start('I');
	}
}
