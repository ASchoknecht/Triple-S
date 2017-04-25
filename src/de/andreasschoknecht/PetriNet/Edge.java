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
 * The interface Edge is the abstraction layer for arcs to enable the calculation of shortest paths with the Dijkstra
 * algorithm class.
 */
public interface Edge {

	/* Getter and setter methods */
	/* ------------------------- */
	public String getId();
	
	public Vertex getSource();
	
	public Vertex getTarget();
	
	public int getWeight();
	/* ------------------------- */

}
