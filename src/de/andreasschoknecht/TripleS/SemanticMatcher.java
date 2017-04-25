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

import de.andreasschoknecht.Helpers.ArrayAdaptor;
import de.andreasschoknecht.MatchingManager.Match;
import semilar.wordmetrics.WNWordMetric;

/**
 * The class SemanticMatcher calculates the semantic similarity value of two transition labels based on the semantic similarity 
 * of words defined by Wu and Palmer.
 */
public class SemanticMatcher {

	/**
	 * Calculates the semantic similarity between two transition labels.
	 * 
	 * @param match The match object for similarity calculation.
	 */
	public void match(Match match, WNWordMetric wnMetricWup) {
		// Array for the semantic similarity results.
		float[][] similarities = new float[match.getTransition1().getPreProcLabel().size()][match.getTransition2().getPreProcLabel().size()];
		for (int i = 0, n = match.getTransition1().getPreProcLabel().size(); i < n; i++) {
			for (int j = 0, y = match.getTransition2().getPreProcLabel().size(); j < y; j++) {
				similarities[i][j] = (float) wnMetricWup.computeWordSimilarityNoPos(
						match.getTransition1().getPreProcLabel().get(i).getRawForm(), 
						match.getTransition2().getPreProcLabel().get(j).getRawForm());
			}
		}
		
		int maxNumberWords = Math.max(similarities.length, similarities[0].length);
		float totalSimilarity = calculateMaxSimilarity(similarities);
		float semScore = totalSimilarity / maxNumberWords;
		
		match.setSemanticSimilarity( semScore );
	}
	
	/**
	 * Computes the greedy maximum similarity between two transition labels.
	 * 
	 * @param similarities The Wu & Palmer similarities between the words in transition labels.
	 * @return Returns the maximum similarity (between and including 0 and 1).
	 */
	private float calculateMaxSimilarity(float[][] similarities) {
		float totalSimilarity = 0, maximum = 0;
		int row = 0, column = 0;
		
		if (similarities.length == 1) { // end case 1 of recursion
			// if there is only one row left: find minimum and add remaining number of columns (represents distances of 1 of these words compared to other transition name tokens)
			for (int i = 0; i < similarities.length; i++) {
				for (int j = 0; j < similarities[i].length; j++) {
					if (similarities[i][j] > maximum) {
						maximum = similarities[i][j];
					}
				}
			}
			// update total distance. -1 as the number of columns might be greater than the number of rows and only one column is eliminated.
			totalSimilarity = maximum;
			
		} else if (similarities[0].length == 1) { // end case 2 of recursion
			// if there is only one column left: find minimum and add remaining number of rows (represents distances of 1 of these words compared to other transition name tokens)
			for (int i = 0; i < similarities.length; i++) {
				for (int j = 0; j < similarities[i].length; j++) {
					if (similarities[i][j] > maximum) {
						maximum = similarities[i][j];
					}
				}
			}
			// update total distance. -1 as the number of rows might be greater than the number of columns and only one column is eliminated.
			totalSimilarity = maximum;
			
		}
		else if (similarities.length != 1 && similarities[0].length != 1) {
			// find minimum in distances array.
			for (int i = 0; i < similarities.length; i++) {
				for (int j = 0; j < similarities[i].length; j++) {
					if (similarities[i][j] > maximum) {
						maximum = similarities[i][j];
						row = i;
						column = j;
					}
				}
			}
			// adapt array.
			float[][] adaptedArray = ArrayAdaptor.adaptArray(similarities, row, column);
			
			// update total distance.
			totalSimilarity = maximum + calculateMaxSimilarity(adaptedArray);
		}
		
		return totalSimilarity;
	}

}