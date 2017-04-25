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

import java.util.Map;

/**
 * The interface MatcherConfiguration represents an abstraction level for different configurations used in different Triple-S algorithms.
 */
public interface MatcherConfiguration {
	
	public void setWeights(String key, Float value);
	
	public Map<String, Float> getWeights();
	
	public void setPath(String path);
	
	public String getPath();
	
	public String getOutputPath();

	public void setOutputPath(String outputPath);
	
	public void setMatcherName(String name);
	
	public String getMatcherName();

}
