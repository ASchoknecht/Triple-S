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

import de.andreasschoknecht.MatchingManager.Match;

/**
 * The class StructuralMatcher contains a method for calculating the relative positional similarity of two transitions
 * belonging to two workflow nets.
 */
public class StructuralMatcher {
		
	/**
	 * Calculates the relative positional similarity between two transitions.
	 *
	 * @param match The match object for similarity calculation.
	 * @return Returns the structural similarity value.
	 */
	protected void match(Match match) {
					
		float RelPosT1 = match.getTransition1().getRelativePosition();
		float RelPosT2 = match.getTransition2().getRelativePosition();
		
		float sim = 0;
		
		if (RelPosT1 >= RelPosT2)
			sim = RelPosT2 / RelPosT1;
		else
			sim = RelPosT1 / RelPosT2;
		
		match.setStructuralPositionSimilarity( sim );
	}
}