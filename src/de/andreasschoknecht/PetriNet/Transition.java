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

import java.util.ArrayList;

import semilar.data.Word;


/**
 * This class represents a transition of a labeled workflow net. Besides getter and setter methods for the class attributes different
 * characteristics relevant for the Triple-S algorithms can be stored respectively calculated.
 */
public class Transition implements Vertex{
	
	/**
	 * id is the internal identifier of a transition in a PNML file
	 * label is the textual label of a transition in a PNML file
	 */
	private String label, id;
	
	/**
	 * preProcName is the preprocessed textual label of a transition in a PNML file.
	 * This includes tokenization and stop word removal.
	 */
	private ArrayList<Word> preProcLabel;
	
	/** The distances from a transition to the source place and the sink place of a of a labeled workflow net. */
	private int distanceStart, distanceEnd;
	
	/** The relative position of a transition between the source and sink place of a labeled workflow net. */
	private float relativePosition;
	
	/** The number of incoming and outgoing arcs of a transition. */
	private int incomingArcs, outgoingArcs;

	/**
	 * Calculates the relative position of this transition in a labeled workflow net.
	 */
	public void calculateRelativePosition() {
		this.relativePosition = (float) ( this.getDistanceStart() / (float) (this.getDistanceStart() + this.getDistanceEnd()) );;
	}
	
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

	public int getDistanceEnd() {
		return distanceEnd;
	}

	public void setDistanceEnd(int distanceEnd) {
		this.distanceEnd = distanceEnd;
	}

	public int getDistanceStart() {
		return distanceStart;
	}

	public void setDistanceStart(int distanceStart) {
		this.distanceStart = distanceStart;
	}

	public float getRelativePosition() {
		return relativePosition;
	}

	public int getOutgoingArcs() {
		return outgoingArcs;
	}

	public void setOutgoingArcs(int outgoingArcs) {
		this.outgoingArcs = outgoingArcs;
	}

	public int getIncomingArcs() {
		return incomingArcs;
	}

	public void setIncomingArcs(int incomingArcs) {
		this.incomingArcs = incomingArcs;
	}

	public ArrayList<Word> getPreProcLabel() {
		return preProcLabel;
	}

	public void setPreProcLabel(ArrayList<Word> preProcLabel) {
		this.preProcLabel = preProcLabel;
	}
	/* ------------------------- */
}
