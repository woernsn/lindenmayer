package it.unical.lindenmayer.logic.test;

import it.unical.lindenmayer.logic.model.Direction;
import it.unical.lindenmayer.logic.model.LindenmayerModel;
import it.unical.lindenmayer.logic.model.Node;
import it.unical.lindenmayer.logic.model.Rule;

public class LindenmayerTest {
	
	public static void main(String[] args) {
		LindenmayerModel lm = new LindenmayerModel();
		lm.setAngle(22);
		lm.setStepSize(100);
		lm.setSteps(5);
		
		Node root = lm.createNode('F');
		Node leftChild = lm.createNode('F');
		Node rightChild = lm.createNode('F');
		
		root.setChildNode(Direction.LEFT, leftChild);
		root.setChildNode(Direction.RIGHT, rightChild);
		
		Rule r1 = new Rule('F', root);
		
		lm.addRule(r1);
		
		lm.start('F');
	}
}
