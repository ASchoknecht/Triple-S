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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class can be used to parse a Petri Net stored as a PNML file.
 */
public class PNMLParser {

	/**
	 * Parses a PNML file and stores its places, transitions and arcs in the PetriNet object.
	 * 
	 * @param net The Petri Net to store its elements.
	 */
	public void startParsing(PetriNet net){

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = builderFactory.newDocumentBuilder();

			// Document represents PNML file as DOM tree
			Document document = builder.parse( net.getPnmlFile() );

			// Get root element
			Element rootElement = document.getDocumentElement();

			/* Process place elements */
			/*----------------------------------------*/
			NodeList nodes = rootElement.getElementsByTagName("place");

			for(int i = 0, n = nodes.getLength(); i < n; i++){
				Node node = nodes.item(i);
				Element child = (Element) node;

				Place place = new Place();

				place.setId(child.getAttribute("id"));

				NodeList placeNodes = child.getElementsByTagName("text");
				Node placeNode = placeNodes.item( placeNodes.getLength()-1 );
				Element placeElement = (Element) placeNode;
				place.setLabel(placeElement.getTextContent());

				net.addPlace(place);
				net.addVertex(place);
			}
			/*----------------------------------------*/

			/* Process transition elements */
			/*----------------------------------------*/
			nodes = rootElement.getElementsByTagName("transition");

			for(int i = 0, n = nodes.getLength(); i < n; i++){
				Node node = nodes.item(i);
				Element child = (Element) node;

				Transition transition = new Transition();

				transition.setId(child.getAttribute("id"));

				NodeList transitionNodes = child.getElementsByTagName("text");
				Node transitionNode = transitionNodes.item(transitionNodes.getLength()-1);
				Element transitionElement = (Element) transitionNode;
				transition.setLabel(transitionElement.getTextContent().toLowerCase());

				net.addTransition(transition);
				net.addVertex(transition);
			}
			/*----------------------------------------*/

			/* Process arc elements */
			/*----------------------------------------*/
			nodes = rootElement.getElementsByTagName("arc");

			for(int i = 0, n = nodes.getLength(); i < n; i++){
				Node node = nodes.item(i);
				Element child = (Element) node;

				Arc arc = new Arc();

				arc.setId(child.getAttribute("id"));
				arc.setSource( findVertex(net.getVertices(), child.getAttribute("source")) );
				arc.setTarget( findVertex(net.getVertices(), child.getAttribute("target")) );

				net.addArc(arc);
			}
			/*----------------------------------------*/
		} catch (ParserConfigurationException e) {
			System.out.println("Something went wrong with the ParserConfiguration!");  
		} catch (FileNotFoundException e) {
			System.out.println("PNML file could not be found!");
		} catch (SAXException e) {
			System.out.println("Sax Exception!");
		} catch (IOException e) {
			System.out.println("Something went wrong with the I/O operations!");
		}		
	}

	/**
	 * Finds a vertex based on its ID attribute value.
	 *
	 * @param vertices The vertices to search in.
	 * @param id The ID of the vertex.
	 * @return Returns the index of the searched vertex in the vertices list.
	 */
	private Vertex findVertex(List<Vertex> vertices, String id) {
		boolean vertexNotFound = true;
		Vertex vertex = null;
		while (vertexNotFound) {
			for (int i = 0, n = vertices.size(); i < n; i++) {
				Vertex currentVertex = vertices.get(i);
				if (currentVertex.getId().equals(id)) {
					vertex =  currentVertex;
					vertexNotFound = false;
				}	
			}
		}
		return vertex;
	}

}
