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

package de.andreasschoknecht.PetriNet;

/**
 * This class represents a place of a labeled workflow net. This class basically contains only getters and setters for the attributes.
 */
public class Place implements Vertex{
	
	/** 
	 * id is the internal identifier of a place in a PNML file.
	 * label is the textual label of a place in a PNML file.
	 */
	private String label, id;
	
	/* Getter and setter methods */
	/* ------------------------- */
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	/* ------------------------- */
}