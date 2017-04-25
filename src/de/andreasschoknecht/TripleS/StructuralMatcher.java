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

import de.andreasschoknecht.MatchingManager.Match;

/**
 * The class StructuralMatcher calculates the structural similarity value of two transitions based on their
 * incoming and outgoing arcs as well as their relative position in the whole workflow net.
 */
public class StructuralMatcher {
		
	public void match(Match match){
		match.setStructuralArcSimilarity( calculateArcRelationSimilarity(match) );
		match.setStructuralPositionSimilarity( calculateRelativePositionSimilarity(match) );  
	}
	
	/**
	 * Calculates the relative position similarity of two transitions in a workflow net.
	 *
	 * @param match The match object containing the two transitions.
	 * @return Returns the similarity value of two transitions with respect to their relative position.
	 */
	private float calculateRelativePositionSimilarity(Match match) {
		float RelPosT1 = match.getTransition1().getRelativePosition();
		float RelPosT2 = match.getTransition2().getRelativePosition();
		
		float sim;
		
		if (RelPosT1 >= RelPosT2)
			sim = RelPosT2 / RelPosT1;
		else
			sim = RelPosT1 / RelPosT2;
		
		return sim;
	}

	/**
	 * Calculates the structural similarity related to the ratio of amount of incoming and outgoing arcs.
	 * 
	 * @return Returns the similarity value of two transitions with respect to their amount of incoming and outgoing arcs.
	 */
	public float calculateArcRelationSimilarity(Match match){
		//Setting a relationship for incoming and outgoing arcs between transition1 and transition2
		float outgoingRatio = ((float) 1)-((float)Math.abs(
				(float)match.getTransition1().getOutgoingArcs()-match.getTransition2().getOutgoingArcs())/Math.max(match.getTransition1().getOutgoingArcs(), match.getTransition2().getOutgoingArcs())); 
		float incomingRatio = ((float) 1)-((float)Math.abs(
				(float)match.getTransition1().getIncomingArcs()-match.getTransition2().getIncomingArcs())/Math.max(match.getTransition1().getIncomingArcs(), match.getTransition2().getIncomingArcs())); 
		
		return (float)(outgoingRatio+incomingRatio)/2;
	}

}
