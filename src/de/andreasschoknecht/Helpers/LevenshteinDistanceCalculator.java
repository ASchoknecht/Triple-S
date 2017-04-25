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

package de.andreasschoknecht.Helpers;

/**
 * The class LevenshteinDistanceCalculator can be used to calculate the Levenshtein string edit distance between two words. It is used for
 * calculating the syntactic similarity value in the Triple-S algorithms.
 */
public class LevenshteinDistanceCalculator {
	
	/**
	 * Computes the Levenshtein distance between two words.
	 * 
	 * @param word1 String one to be compared.
	 * @param word2 String two to be compared.
	 * @return Returns the distance (between and including 0 and 1).
	 */
	public float computeLevenshteinDistance(CharSequence word1, CharSequence word2) {
		float maxStrLength;
		if (word1.length() >= word2.length())
			maxStrLength = word1.length();
		else
			maxStrLength = word2.length();

		int[][] distance = new int[word1.length() + 1][word2.length() + 1];

		for (int i = 0; i <= word1.length(); i++)
			distance[i][0] = i;
		for (int j = 1; j <= word2.length(); j++)
			distance[0][j] = j;

		for (int i = 1; i <= word1.length(); i++)
			for (int j = 1; j <= word2.length(); j++)
				distance[i][j] = minimum(
						distance[i - 1][j] + 1,
						distance[i][j - 1] + 1,
						distance[i - 1][j - 1]
								+ ((word1.charAt(i - 1) == word2.charAt(j - 1)) ? 0
										: 1));

		return ((float) distance[word1.length()][word2.length()])/maxStrLength;
	}
	
	/**
	 * Calculates the minimum of three values.
	 * 
	 * @param a Value one.
	 * @param b Value two.
	 * @param c Value three.
	 * @return Returns the minimum of three values.
	 */
	private int minimum(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}
	
	/**
	 * Computes the greedy minimum distance between words. Each row and each column represents a word of a transition label and each
	 * entry of the array represents the Levenshtein distance between two words. This function is used in the Triple-S algorithms to
	 * calculate the final syntactic similarity value between the labels of two transitions.
	 * 
	 * @param distances The Levenshtein distances between the words.
	 * @return Returns the minimum distance (between and including 0 and 1).
	 */
	public float calculateMinDistance(float[][] distances) {
		float totalDistance = 0, minimum = 1;
		int row = 0, column = 0;
		
		if (distances.length == 1) { // end case 1 of recursion
			/* If there is only one row left: Find minimum and add remaining number of columns
			 * (represents distances of 1 of these words compared to other transition label's words)
			 */
			for (int i = 0; i < distances.length; i++) {
				for (int j = 0; j < distances[i].length; j++) {
					if (distances[i][j] < minimum) {
						minimum = distances[i][j];
					}
				}
			}
			// Update total distance. -1 as the number of columns might be greater than the number of rows and only one column is eliminated
			totalDistance = minimum + distances[0].length - 1;
			
		} else if (distances[0].length == 1) { // end case 2 of recursion
			/* If there is only one column left: Find minimum and add remaining number of rows 
			 * (represents distances of 1 of these words compared to other transition label's words)
			 */
			for (int i = 0; i < distances.length; i++) {
				for (int j = 0; j < distances[i].length; j++) {
					if (distances[i][j] < minimum) {
						minimum = distances[i][j];
					}
				}
			}
			// Update total distance. -1 as the number of rows might be greater than the number of columns and only one column is eliminated
			totalDistance = minimum + distances.length - 1;
			
		}
		else if (distances.length != 1 && distances[0].length != 1) {
			// Find minimum in distances array.
			for (int i = 0; i < distances.length; i++) {
				for (int j = 0; j < distances[i].length; j++) {
					if (distances[i][j] < minimum) {
						minimum = distances[i][j];
						row = i;
						column = j;
					}
				}
			}
			// Adapt array
			float[][] adaptedArray = ArrayAdaptor.adaptArray(distances, row, column);
			
			// Update total distance
			totalDistance = minimum + calculateMinDistance(adaptedArray);
		}
		
		return totalDistance;
	}

}
