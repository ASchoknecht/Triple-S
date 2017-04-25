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

import java.util.HashMap;

/**
 * The class TripleSConfiguration is responsible for the configuration of a Triple-S matcher.
 */
public class TripleSConfiguration implements MatcherConfiguration {

	/** The weights for a TripleS matcher. */
	private HashMap<String, Float> weights;
	
	/** 
	 * The name of the matcher.
	 * The paths to PNML files for matching and output.
	 * */
	private String PNMLPath, outputPath, name;
	
	/**
	 * Instantiates a new TripleS configuration.
	 *
	 * @param weights the weights of a TripleS matcher
	 * @param path the path to the PNML files as input
	 * @param output the output path for the matching results
	 * @param name the name of the matcher
	 */
	public TripleSConfiguration(HashMap<String, Float> weights, String path, String output, String name) {
		this.weights = weights;
		this.PNMLPath = path;
		this.setOutputPath(output);
		this.name = name;
	}

	/* Getter and setter methods */
	/* ------------------------- */
	@Override
	public void setWeights(String key, Float value) {
		weights.put(key, value);
	}

	@Override
	public HashMap<String, Float> getWeights() {	
		return weights;
	}

	@Override
	public void setPath(String path) {	
		PNMLPath = path;	
	}

	@Override
	public String getPath() {
		return PNMLPath;
	}

	@Override
	public void setMatcherName(String name) {
		this.name = name;
	}

	@Override
	public String getMatcherName() {
		return name;
	}

	@Override
	public String getOutputPath() {
		return outputPath;
	}

	@Override
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	/* ------------------------- */
}
