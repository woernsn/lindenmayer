package it.unical.lindenmayer.logic.test;

import it.unical.lindenmayer.logic.model.Direction;
import it.unical.lindenmayer.logic.model.LindenmayerModel;
import it.unical.lindenmayer.logic.model.Node;
import it.unical.lindenmayer.logic.model.Rule;

public class LindenmayerTest {
	
	public static void main(String[] args) {
		LindenmayerModel lm = new LindenmayerModel();
		lm.setAngle(5);
		lm.setStepSize(75);
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
		
//		lm.start('F');
		
		
		
		// Test 2
		LindenmayerModel lm2 = new LindenmayerModel();
		lm2.setAngle(5);
		lm2.setStepSize(75);
		lm2.setSteps(10);
		lm2.setTimeout(0.5);
		
		root = lm2.createNode('F');
		leftChild = lm2.createNode('X');
		centerChild = lm2.createNode('F');
		rightChild = lm2.createNode('F');
		
		Node leftHanging = lm2.createNode('F');
		Node leftHangingCenter = lm2.createNode('F');
		
		root.setChildNode(Direction.CENTER, centerChild);
		root.setChildNode(Direction.RIGHT, rightChild);

		leftHanging.setChildNode(Direction.CENTER, leftHangingCenter);
		leftHanging.setChildNode(Direction.LEFT, leftChild);
		
		r1 = new Rule('F', root, 90);
		Rule r2 = new Rule('F', leftHanging, 10);
		
		lm2.addRule(r1);
		lm2.addRule(r2);
		
		lm2.start('F');
	}
}
