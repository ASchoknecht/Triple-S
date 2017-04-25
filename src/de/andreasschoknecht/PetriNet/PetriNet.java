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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import de.andreasschoknecht.Dijkstra.DijkstraAlgorithm;
import semilar.data.Sentence;
import semilar.data.Word;
import semilar.tools.preprocessing.SentencePreprocessor;


/**
 * This class represents a labeled workflow net, which is a specific kind of Petri Net.
 */
public class PetriNet {
	
	/** 
	 * places contains a list of places of a labeled workflow net.
	 * transitions contains a list of transitions of a labeled workflow net.
	 * vertices contains all transition and place nodes of a labeled workflow net.
	 * arcs contains a list of arcs of a labeled workflow net.
	 * pnmlFile contains the full path to the PNML file representing the labeled workflow net.
	 * pnmlFileName contains just the file name for writing alignments
	 * startNode and endNode contain the IDs of the start and end nodes, i.e. places, of a labeled workflow net.
	 */
	private ArrayList<Place> places;
	private ArrayList<Transition> transitions;
	private ArrayList<Vertex> vertices;
	private ArrayList<Edge> arcs;
	private String pnmlFile, pnmlFileName, startNode, endNode;
	
	/**
	 * Initializes the path to the PNML file and sets the corresponding name of the labeled workflow net (i.e. process model).
	 * 
	 * @param pnmlFile Path to the PNML file.
	 */
	public PetriNet(String pnmlFile, String pnmlFileName) {
		this.pnmlFile = pnmlFile;
		this.pnmlFileName = pnmlFileName;
		this.places = new ArrayList<Place>();
		this.transitions = new ArrayList<Transition>();
		this.arcs = new ArrayList<Edge>();
		this.vertices = new ArrayList<Vertex>();
	}

	/**
	 * Preprocesses the transitions of a Petri Net. This currently includes label tokenization, conversion to lower case, word stemming and 
	 * stop word removal using the Semilar API (see http://deeptutor2.memphis.edu/Semilar-Web/index.jsp).
	 * 
	 * Besides, arc relations and relative position of transitions in a labeled workflow net are calculated.
	 */
	public void preprocessTransitions() {
			
		for (int i = 0, m = this.getTransitions().size(); i < m; i++) {
			String label = this.getTransitions().get(i).getLabel();
			if(label.length() > 0) {
				Sentence sentence;
		        SentencePreprocessor preprocessor = new SentencePreprocessor(SentencePreprocessor.TokenizerType.STANFORD, 
		        		SentencePreprocessor.TaggerType.STANFORD,
		        		SentencePreprocessor.StemmerType.PORTER,
		        		SentencePreprocessor.ParserType.STANFORD);
		        
		        sentence = preprocessor.preprocessSentence(label);
		        ArrayList<Word> words = sentence.getWords();
		        
		        this.getTransitions().get(i).setPreProcLabel( removeStopWords(words) );				
			} else
				this.getTransitions().get(i).setPreProcLabel( new ArrayList<Word>() );
			
			// Calculate arc relation
			calculateArcNumbers( this.getTransitions().get(i) );
		}
		
		// Calculate relative position
		calculateTransitionPositions();
	}
	
	/**
	 * Removes English stop words from a list of words.
	 * 
	 * @param words A list of words containing stop words.
	 * @return Returns a list of words without stop words.
	 */
	private ArrayList<Word> removeStopWords(ArrayList<Word> words) {
		ArrayList<Word> finalWords = new ArrayList<Word>();
		for (Word word: words) {
			if ( !word.isIsStopWord() == true && !(word.getRawForm().length() == 1) )
				finalWords.add( word );
		}
		return finalWords;
	}
	
	/**
	 * Calculates the amount of incoming and outgoing arcs for a transition.
	 *
	 * @param transition The transition for which the amount of incoming and outgoing arcs is determined.
	 */
	public void calculateArcNumbers(Transition transition) {
		int incoming = 0, outgoing = 0;
		String transitionID = transition.getId();
		
		for(int j = 0, n = this.getArcs().size(); j < n; j++){
			if(transitionID.equals(this.getArcs().get(j).getSource().getId())){
				outgoing++;
			}
			if(transitionID.equals(this.getArcs().get(j).getTarget().getId())){
				incoming++;
			}				
		}
			
		transition.setIncomingArcs(incoming);
		transition.setOutgoingArcs(outgoing);
	}
	
	/**
	 * Calculates relative transition positions in a labeled workflow net for all transitions contained in the workflow net.
	 * 
	 * Therefore Dijkstra's shortest path algorithm is used, calculating distances between the source place of a workflow net and
	 * a transition as well as between a transition and the sink place.
	 */
	public void calculateTransitionPositions() {
	    /* Determine start and end node of the labeled workflow net */
	    /* -------------------------------------------------- */
	    HashSet<String> targetArcIDs = new HashSet<String>();
	    for (int j = 0, m = this.getArcs().size(); j < m; j++) {
	    	targetArcIDs.add(this.getArcs().get(j).getTarget().getId());
		}
	    HashSet<String> sourceArcIDs = new HashSet<String>();
	    for (int j = 0, m = this.getArcs().size(); j < m; j++) {
	    	sourceArcIDs.add(this.getArcs().get(j).getSource().getId());
		}
	    this.determineStartNode(targetArcIDs);
	    this.determineEndNode(sourceArcIDs);
	    /* -------------------------------------------------- */
	    
	    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(arcs);
	    
	    for (int i = 0, n = this.getTransitions().size(); i < n; i++) {    
	    	Transition currentTransition = this.getTransitions().get(i);
	    	int currentIndexInVertices = findVertex(vertices, currentTransition.getId());
	    	
		    dijkstra.execute(vertices.get(findVertex(vertices, startNode)));
		    LinkedList<Vertex> pathToStart = dijkstra.getPath(vertices.get(currentIndexInVertices));
		    
		    dijkstra.execute(vertices.get(currentIndexInVertices));
		    LinkedList<Vertex> pathToEnd = dijkstra.getPath(vertices.get(findVertex(vertices, endNode)));
		    
		    currentTransition.setDistanceStart(pathToStart.size() - 1);
		    currentTransition.setDistanceEnd(pathToEnd.size() - 1);
		    currentTransition.calculateRelativePosition();
	    }
	}
	
	/**
	 * Finds a vertex based on its ID attribute value.
	 *
	 * @param vertices The vertices to search in.
	 * @param id The ID of the vertex.
	 * @return Returns the index of the searched vertex in the vertices list.
	 */
	private int findVertex(List<Vertex> vertices, String id) {
	//private int findVertex(List<Vertex> vertices, String id) {
		boolean vertexNotFound = true;
		int index = 0;
		while (vertexNotFound) {
			for (int i = 0, n = vertices.size(); i < n; i++) {
				if (vertices.get(i).getId().equals(id)) {
					index =  i;
					vertexNotFound = false;
				}	
			}
		}
		return index;
	}
	
	/**
	 * Determines the start node (source place) of a labeled workflow net.
	 *
	 * @param targetArcIDs The target IDs in arcs.
	 */	
	private void determineStartNode(HashSet<String> targetArcIDs) {
		String id = null;
		for (int i = 0, n = this.getPlaces().size(); i < n; i++) {
			if ( targetArcIDs.contains(this.getPlaces().get(i).getId()) ) {
				id = null;
			}
			else {
				// start node ID found
				id = this.getPlaces().get(i).getId();
				// exit loop
				i = n;
			}
		}
		startNode = id;
	}
	
	/**
	 * Determines the end node (sink place) of a labeled workflow net.
	 *
	 * @param sourceArcIDs The source IDs in arcs.
	 */	
	private void determineEndNode(HashSet<String> sourceArcIDs) {
		String id = null;
		for (int i = 0, n = this.getPlaces().size(); i < n; i++) {
			if ( sourceArcIDs.contains(this.getPlaces().get(i).getId()) ) {
				id = null;
			}
			else {
				// end node ID found
				id = this.getPlaces().get(i).getId();
				// exit loop
				i = n;
			}
		}
		endNode = id;
	}
	
	/**
	 * Adds a transition to transitions.
	 * 
	 * @param transition The transition to add to transitions.
	 */
	public void addTransition(Transition transition) {
		this.transitions.add(transition);
	}
	
	/**
	 * Adds an arc to arcs.
	 * 
	 * @param arc The arc to add to arcs.
	 */
	public void addArc(Arc arc) {
		this.arcs.add(arc);
	}
	
	/**
	 * Adds a place to places.
	 * 
	 * @param place The place to add to places.
	 */
	public void addPlace(Place place) {
		this.places.add(place);
	}

	/**
	 * Adds a vertex node of the workflow net to vertices.
	 * 
	 * @param vertex The vertex to add to vertices.
	 */
	public void addVertex(Vertex vertex) {
		this.vertices.add(vertex);
	}

	/* Getter and setter methods */
	/* ------------------------- */
	public ArrayList<Vertex> getVertices() {
		return vertices;
	}
	
	public ArrayList<Place> getPlaces() {
		return places;
	}
	
	public void setPlaces(ArrayList<Place> places) {
		this.places = places;
	}
	
	public ArrayList<Transition> getTransitions() {
		return transitions;
	}
	
	public void setTransitions(ArrayList<Transition> transitions) {
		this.transitions = transitions;
	}

	public ArrayList<Edge> getArcs() {
		return arcs;
	}

	public void setArcs(ArrayList<Edge> arcs) {
		this.arcs = arcs;
	}

	public String getPnmlFile() {
		return pnmlFile;
	}

	public void setPnmlFile(String pnmlFile) {
		this.pnmlFile = pnmlFile;
	}
	
	public String getPnmlFileName() {
		return pnmlFileName;
	}

	public void setPnmlFileName(String pnmlFileName) {
		this.pnmlFileName = pnmlFileName;
	}
	
	public int getAmountOfNodes() {
		return places.size() + transitions.size();
	}
	
	public int getAmountOfTransitions() {
		return transitions.size();
	}
	
	public int getAmountOfPlaces() {
		return places.size();
	}
	
	public int getAmountOfArcs() {
		return arcs.size();
	}
	/* ------------------------- */
}
