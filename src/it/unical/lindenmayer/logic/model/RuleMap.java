package it.unical.lindenmayer.logic.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RuleMap {

	private int propabilities;
	private Map<Character, List<Rule>> ruleMap;

	public RuleMap() {
		this.propabilities = 0;
		this.ruleMap = new HashMap<>();
	}
	
	public Node getNewNode(Node nodeFrom) {
		return getNewNode(nodeFrom.getSymbol());
	}

	public Node getNewNode(Character symbolFrom) {
		Random rand = new Random();
		int randomNumber = rand.nextInt(100);
		int sum = 0;
		for (Rule r : this.ruleMap.get(symbolFrom)) {
			sum += r.getPropability();
			if (randomNumber < sum) {
				return r.getNodeTo();
			}
		}

		return null;
	}

	private void checkPropabilityOverflow(Rule element) {
		this.propabilities += element.getPropability();
		if (this.propabilities > 100) {
			throw new IllegalArgumentException("Propability of the RuleSet is not allowed to be over 100!");
		}
	}

	public void add(Rule r) {
		checkPropabilityOverflow(r);
		if (this.ruleMap.get(r.getSymbolFrom()) == null) {
			this.ruleMap.put(r.getSymbolFrom(), new ArrayList<Rule>());
		}
		this.ruleMap.get(r.getSymbolFrom()).add(r);
	}

	public void addAll(Collection<? extends Rule> c) {
		for (Rule r : c) {
			this.add(r);
		}
	}

}
