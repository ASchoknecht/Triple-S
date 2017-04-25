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
 * This class represents an arc of a Petri Net.
 */
public class Arc implements Edge{
	
	/** 
	 * id is the internal identifier of an arc in a PNML file.
	 * source refers to the vertex representing the start node of the arc.
	 * target refers to the vertex representing the end node of the arc.
	 */
	private String id;
	private Vertex source, target;
	
	/** The weight of this arc. Is always set to 1, but can be changed if weights in Petri Nets should be used. */
	private int weight;
	
	public Arc() {
		this.setWeight(1);
	}

	/* Getter and setter methods */
	/* ------------------------- */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Vertex getSource() {
		return source;
	}

	public void setSource(Vertex source) {
		this.source = source;
	}

	public Vertex getTarget() {
		return target;
	}

	public void setTarget(Vertex target) {
		this.target = target;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	/* ------------------------- */
}
