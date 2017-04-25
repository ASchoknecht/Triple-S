/**
 * Part of the Triple-S Process Model Matching package.
 * 
 * Copyright 2017 by Andreas Schoknecht <andreas_schoknecht@web.de>
 *
 * This source code is made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * @author Andreas Schoknecht
 */

package de.andreasschoknecht.TripleS;

import java.util.ArrayList;

import de.andreasschoknecht.MatchingManager.Match;
import de.andreasschoknecht.PetriNet.PetriNet;
import semilar.tools.semantic.WordNetSimilarity;
import semilar.wordmetrics.WNWordMetric;


/**
 * The class TripleS can be used to calculate the similarity between the transitions of two labeled workflow nets accoring
 * to the Triple-S algorithm.
 */
public class TripleS {
	
	/** The weights and thresholds for parameterizing the Triple-S algorithm. */
	private float syntacticWeight, semanticWeight, structuralArcWeight, structuralPositionWeight, threshold;
	
	/** The matches calculated by the Triple-S algorithm. */
	private ArrayList<Match> matches;
	
	/** The labeled workflow nets to be matched. */
	private PetriNet net1, net2;
	
	/** A WNWordMetric object for calculating the Wu & Palmer similarity of words. */
	WNWordMetric wnMetricWup;
	private final boolean wnFirstSenseOnly = false;

	public TripleS() {
		matches = new ArrayList<Match>();	
		wnMetricWup = new WNWordMetric(WordNetSimilarity.WNSimMeasure.WUP, wnFirstSenseOnly);	
	}
	
	/**
	 * Starts the matching process of two labeled workflow net and calculates the final matches.
	 * 
	 * @param net1 Represents the first labeled workflow net to match.
	 * @param net2 Represents the second labeled workflow net to match.
	 */
	public void startMatching() {
		SyntacticMatcher synMatcher = new SyntacticMatcher();
		SemanticMatcher semMatcher = new SemanticMatcher();
		StructuralMatcher strucMatcher = new StructuralMatcher();
		
		for (int i = 0, n = net1.getTransitions().size(); i < n; i++){
			for (int j = 0, m = net2.getTransitions().size(); j < m; j++){
				// Do not match transitions if one of their preprocessed labels does not contain any words.
				if ( !net1.getTransitions().get(i).getPreProcLabel().isEmpty() && !net2.getTransitions().get(j).getPreProcLabel().isEmpty() ) {
					// Create new match object.
					Match match = new Match(net1.getTransitions().get(i), net2.getTransitions().get(j));
										
					if ( !match.getTransition1().getLabel().equalsIgnoreCase(match.getTransition2().getLabel()) ) {
						// Perform syntactic matching
						synMatcher.match(match);
					}
					else {
						match.setSyntacticSimilarity(1.0f);
					}
										
					if (match.getSyntacticSimilarity() != 1.0f) {
						// Perform semantic matching
						semMatcher.match(match, wnMetricWup);
					}
					else {
						match.setSemanticSimilarity(1.0f);
					}
					
					//Perform structural matching
					strucMatcher.match(match);
					
					// Add match to the list of matches if above or equal to threshold
					match.setSimilarityValue(match.getSyntacticSimilarity() * syntacticWeight + 
							match.getSemanticSimilarity() * semanticWeight + 
							match.getStructuralArcSimilarity() * structuralArcWeight +
							match.getStructuralPositionSimilarity() * structuralPositionWeight);
					
					if ( match.getSimilarityValue() >= threshold)
						matches.add(match);
				}
			}
		}
	}

	/* Getter and setter methods */
	/* ------------------------- */
	public ArrayList<Match> getMatches() {
		return matches;
	}

	public float getSyntacticWeight() {
		return syntacticWeight;
	}

	public void setSyntacticWeight(float syntacticWeight) {
		this.syntacticWeight = syntacticWeight;
	}

	public float getSemanticWeight() {
		return semanticWeight;
	}

	public void setSemanticWeight(float semanticWeight) {
		this.semanticWeight = semanticWeight;
	}

	public float getStructuralArcWeight() {
		return structuralArcWeight;
	}

	public void setStructuralArcWeight(float structuralArcWeight) {
		this.structuralArcWeight = structuralArcWeight;
	}

	public float getStructuralPositionWeight() {
		return structuralPositionWeight;
	}

	public void setStructuralPositionWeight(float structuralPositionWeight) {
		this.structuralPositionWeight = structuralPositionWeight;
	}

	public float getThreshold() {
		return threshold;
	}

	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}

	public PetriNet getNet1() {
		return net1;
	}

	public void setNet1(PetriNet net1) {
		this.net1 = net1;
	}

	public PetriNet getNet2() {
		return net2;
	}

	public void setNet2(PetriNet net2) {
		this.net2 = net2;
	}
	/* ------------------------- */
}