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

import de.andreasschoknecht.Helpers.LevenshteinDistanceCalculator;
import de.andreasschoknecht.MatchingManager.Match;

/**
 * The class SyntacticMatcher calculates the syntactic score of two transitions based on their labels.
 */
public class SyntacticMatcher {
		
	/**
	 * Calculates the syntactic distance between two transition labels.
	 * 
	 * @param match The match object for similarity calculation.
	 */
	public void match(Match match) {
		// Array for the Levenshtein distance results.
		float[][] distances = new float[match.getTransition1().getPreProcLabel().size()][match.getTransition2().getPreProcLabel().size()];
		
		LevenshteinDistanceCalculator levCalculator = new LevenshteinDistanceCalculator();
		
		for (int i = 0, n = match.getTransition1().getPreProcLabel().size(); i < n; i++) {
			for (int j = 0, m = match.getTransition2().getPreProcLabel().size(); j < m; j++) {

				distances[i][j] = levCalculator.computeLevenshteinDistance( match.getTransition1().getPreProcLabel().get(i).getRawForm().toLowerCase(), 
						match.getTransition2().getPreProcLabel().get(j).getRawForm().toLowerCase() );
				
			}
		}
		
		int maxNumberWords = Math.max(distances.length, distances[0].length);
		float totalDistance = levCalculator.calculateMinDistance(distances);
		float synSimilarity = totalDistance / maxNumberWords;
		match.setSyntacticSimilarity( 1 - synSimilarity );		
	}
	
}
