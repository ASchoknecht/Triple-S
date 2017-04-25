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
 * The class ArrayAdaptor can be used to remove a row or column from a matrix. This class is used when calculating the syntactic and semantic
 * similarity of transition labels in the Triple-S algorithms.
 */
public class ArrayAdaptor {
	
	/**
	 * Deletes a row and a column from a matrix.
	 * @param array The matrix to be reduced.
	 * @param row The row to remove.
	 * @param column The column to remove.
	 * @return Returns the adapted matrix.
	 */
	public static float[][] adaptArray(float[][] array, int row, int column) {
		
		float[][] newArray = new float[array.length-1][array[0].length-1];
		int counterRow = 0, counterColumn = 0;
		
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				if (j != column && i != row) {
					newArray[counterRow][counterColumn] = array[i][j];
					counterColumn = counterColumn + 1;
				}
			}
			counterColumn = 0;
			if (i != row) {
				counterRow = counterRow + 1;
			}
		}
		
		return newArray;
	}

}
