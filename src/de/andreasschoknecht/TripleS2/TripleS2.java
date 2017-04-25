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

package de.andreasschoknecht.TripleS2;

import java.util.ArrayList;

import org.deeplearning4j.models.word2vec.Word2Vec;

import de.andreasschoknecht.MatchingManager.Match;
import de.andreasschoknecht.PetriNet.PetriNet;


/**
 * The class TripleS2 contains methods for running the Triple-S2 matching approach. Additionally, the list of calculated matches can be retrieved.
 */
public class TripleS2 {
	
	/** The weights and thresholds for parameterizing the Triple-S2 algorithm. */
	private float syntacticWeight, semanticWeight, structuralWeightsyn, structuralWeightsem, thresholdsyn, thresholdsem;
	
	/** The matches calculated by the Triple-S2 algorithm. */
	private ArrayList<Match> matches;
		
	/** The labeled workflow nets to be matched. */
	private PetriNet net1, net2;
	
	/** The Word2Vec object for calculating semantic similarity of words according to the Word2Vec idea. */
	private Word2Vec vec;

	/**
	 * Instantiates a new TripleS2 object.
	 *
	 * @param vec the Word2Vec object for calculating semantic similarity between words
	 */
	public TripleS2(Word2Vec vec) {
		matches = new ArrayList<Match>();
		this.vec = vec;
	}
	
	/**
	 * Starts the matching process of two Petri Nets and calculates the final matches.
	 */
	public void startMatching(){
		SyntacticMatcher synMatcher = new SyntacticMatcher();
		SemanticMatcher semMatcher = new SemanticMatcher();
		StructuralMatcher strucMatcher = new StructuralMatcher();
		
		for (int i = 0, n = net1.getTransitions().size(); i < n; i++){
			for (int j = 0, m = net2.getTransitions().size(); j < m; j++){
				// Do not match transitions if one of their pre-processed labels is empty.
				if ( !net1.getTransitions().get(i).getPreProcLabel().isEmpty() && !net2.getTransitions().get(j).getPreProcLabel().isEmpty() ) {
					// Create new match object.
					Match match = new Match(net1.getTransitions().get(i), net2.getTransitions().get(j));
					
					/* Perform syntactic + structural match evaluation */
					/*----------------------------------------*/
					if ( !match.getTransition1().getLabel().equalsIgnoreCase(match.getTransition2().getLabel()) ) {
						// Perform syntactic matching
						synMatcher.match(match);
					}
					else {
						match.setSyntacticSimilarity(1.0f);
					}
					// Perform structural matching
					strucMatcher.match(match);
					
					// Calculate syntactic similarity value
					float synStrucSim = (match.getSyntacticSimilarity() * syntacticWeight +  match.getStructuralPositionSimilarity() * structuralWeightsyn);
					/*----------------------------------------*/
					
					// Perform semantic match evaluation only if similarity value is not above or equal to syntactic theshold
					if ( synStrucSim >= thresholdsyn) {
						match.setSimilarityValue(synStrucSim);
						matches.add(match);
					} else {
						
						/* Perform semantic + structural match evaluation */
						/*----------------------------------------*/
						// Perform semantic matching
						semMatcher.match(match, vec);
						
						// Calculate semantic similarity value
						float semStrucSim =	match.getSemanticSimilarity() * semanticWeight + match.getStructuralPositionSimilarity() * structuralWeightsem;
						
						if ( semStrucSim >= thresholdsem) {
							match.setSimilarityValue(semStrucSim);
							matches.add(match);
						} else 
							match.setSimilarityValue(0);
						/*----------------------------------------*/
					}
				}
			}
		}
	}

	/* Getter and setter methods */
	/* ------------------------- */
	public ArrayList<Match> getMatches() {
		return matches;
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

	public float getStructuralWeightsyn() {
		return structuralWeightsyn;
	}

	public void setStructuralWeightsyn(float structuralWeightsyn) {
		this.structuralWeightsyn = structuralWeightsyn;
	}

	public float getStructuralWeightsem() {
		return structuralWeightsem;
	}

	public void setStructuralWeightsem(float structuralWeightsem) {
		this.structuralWeightsem = structuralWeightsem;
	}

	public float getThresholdsyn() {
		return thresholdsyn;
	}

	public void setThresholdsyn(float thresholdsyn) {
		this.thresholdsyn = thresholdsyn;
	}

	public float getThresholdsem() {
		return thresholdsem;
	}

	public void setThresholdsem(float thresholdsem) {
		this.thresholdsem = thresholdsem;
	}
	/* ------------------------- */
}