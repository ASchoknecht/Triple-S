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

package de.andreasschoknecht.MatchingManager;

import java.util.ArrayList;

import de.andreasschoknecht.PetriNet.Transition;
import semilar.data.Word;

/**
 * The class Match represents a match between two transitions of two labeled workflow nets.
 */
public class Match {
	
	/**
	 * The two transitions represent the matching transitions.
	 * Their different similarity values a stored in the corresponding variables.
	 */
	private Transition transition1;
	private Transition transition2;
	private float syntacticSimilarity;
	private float semanticSimilarity;
	private float structuralArcSimilarity;
	private float structuralPositionSimilarity;
	private float similarityValue;
	
	
	public Match(Transition transition1, Transition transition2) {
		this.transition1 = transition1;
		this.transition2 = transition2;
		this.syntacticSimilarity = 0;
		this.semanticSimilarity = 0;
		this.structuralArcSimilarity = 0;
		this.structuralPositionSimilarity = 0;
		this.similarityValue = 0;
	}
	
	/* Getter and setter methods */
	/* ------------------------- */
	public Transition getTransition1() {
		return transition1;
	}
	
	public void setTransition1(Transition transition1) {
		this.transition1 = transition1;
	}
	
	public Transition getTransition2() {
		return transition2;
	}
	
	public void setTransition2(Transition transition2) {
		this.transition2 = transition2;
	}
	
	public float getSyntacticSimilarity() {
		return syntacticSimilarity;
	}
	
	public void setSyntacticSimilarity(float syntacticSimilarity) {
		this.syntacticSimilarity = syntacticSimilarity;
	}
	
	public float getSemanticSimilarity() {
		return semanticSimilarity;
	}

	public void setSemanticSimilarity(float semanticSimilarity) {
		this.semanticSimilarity = semanticSimilarity;
	}

	public float getStructuralArcSimilarity() {
		return structuralArcSimilarity;
	}

	public void setStructuralArcSimilarity(float structuralArcSimilarity) {
		this.structuralArcSimilarity = structuralArcSimilarity;
	}
	
	public float getStructuralPositionSimilarity() {
		return structuralPositionSimilarity;
	}

	public void setStructuralPositionSimilarity(float structuralPositionSimilarity) {
		this.structuralPositionSimilarity = structuralPositionSimilarity;
	}

	public float getSimilarityValue() {
		return similarityValue;
	}

	public void setSimilarityValue(float similarityValue) {
		this.similarityValue = similarityValue;
	}
	/* ------------------------- */
	
	
	// TODO: Possibly delete in final published version on Github.
	/**
	 * For testing purposes print raw words
	 */
	public String printTokens() {			
		ArrayList<String> tokens1 = new ArrayList<String>();
		for (Word token: transition1.getPreProcLabel()) {
			tokens1.add(token.getRawForm());
		}
		ArrayList<String> tokens2 = new ArrayList<String>();
		for (Word token: transition2.getPreProcLabel()) {
			tokens2.add(token.getRawForm());
		}
		return "Transition 1: "+tokens1.toString()+"\r\n"+"Transition 2: "+tokens2.toString();
	}
	
	/**
	 * For testing purposes print stemmed words
	 */
	public String printStemmedTokens() {			
		ArrayList<String> tokens1 = new ArrayList<String>();
		for (Word token: transition1.getPreProcLabel()) {
			tokens1.add(token.getBaseForm());
		}
		ArrayList<String> tokens2 = new ArrayList<String>();
		for (Word token: transition2.getPreProcLabel()) {
			tokens2.add(token.getBaseForm());
		}
		return "Transition 1: "+tokens1.toString()+"\r\n"+"Transition 2: "+tokens2.toString();
	}

}
