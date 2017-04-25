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

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import de.andreasschoknecht.PetriNet.PNMLParser;
import de.andreasschoknecht.PetriNet.PetriNet;
import de.andreasschoknecht.TripleS.TripleS;
import de.andreasschoknecht.TripleS2.TripleS2;

/**
 * Example class for conducting a process model matching task using the Triple-S algorithms in the Triple-S code library.
 */
public class ExampleUsage {
	
	/** The weights for the Triple-S matching algorithms. */
	private static HashMap<String, Float> weights = new HashMap<String, Float>();
	
	/** The input path to a directory which contains PNML files. */
	private static String inputPath;
	
	/** The output path into which matching result files will be written. */
	private static String outputPath;
	
	/** The matching configuration used to configure a Triple-S matcher. */
	private static MatcherConfiguration configuration;
	
	/**
	 * The main method used as example for using the Triple-S code library.
	 *
	 * @param args the arguments
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.println("-----------------------------------------\r\n"
				+ "Welcome to the Triple-S family of process model matching approaches!\r\n\r\n"
				+ "Please select the matching approach by typing in the corresponding number.\r\n"
				+ "Press 8 or 9 for evaluating match results.\r\n"
				+ "Press 0 to exit.\r\n\r\n"
				+ "1) Triple-S\r\n"
				+ "2) Triple-S2\r\n"
				+ "0) Exit\r\n"
				+ "-----------------------------------------\r\n");
		int algorithm = in.nextInt();
		in.close();
		
		switch(algorithm) {		
		case 1:		
			System.out.println("-----------------------------------------\r\n"
					+ "You chose the Triple-S approach.");
			defineTripleSInput();
			break;
		case 2:
			System.out.println("-----------------------------------------\r\n"
					+ "You chose the Triple-S2 approach.");
			defineTripleS2Input();
			break;
		case 0:
			System.out.println("-----------------------------------------\r\n"
					+ "Matching finished.");
			System.exit(0);
		default:
			System.out.println("-----------------------------------------\r\n"
					+ "Wrong input.");
			System.exit(1);
		}
	}
	
	/**
	 * Define Triple-S input parameters.
	 * 
	 * @throws IOException 
	 */
	private static void defineTripleSInput() throws IOException {
		/* For testing purposes */
		/*----------------------------------------*/
		inputPath = "C:/Testmodels";
		outputPath = "C:/Testmodels";
		weights.put("Syntactic Weight", 0.28f);
		weights.put("Semantic Weight", 0.27f);
		weights.put("Structural Arc Weight", 0.05f);
		weights.put("Structural Position Weight", 0.4f);
		weights.put("Threshold", 0.77f);
		/*----------------------------------------*/	
		
		configuration = new TripleSConfiguration(weights, inputPath, outputPath, "Triple-S");
		startTripleSMatching();
	}

	/**
	 * Define Triple-S2 input parameters.
	 * 
	 * @throws IOException 
	 */
	private static void defineTripleS2Input() throws IOException {
		/* For testing purposes */
		/*----------------------------------------*/
		inputPath = "C:/Testmodels";
		outputPath = "C:/Testmodels";
		weights.put("Syntactic Weight", 0.55f);
		weights.put("Structural Weight Syn", 0.45f);
		weights.put("Semantic Weight", 0.6f);
		weights.put("Structural Weight Sem", 0.4f);
		weights.put("Threshold Syn", 0.75f);
		weights.put("Threshold Sem", 0.82f);
		/*----------------------------------------*/
		
		configuration = new TripleS2Configuration(weights, inputPath, outputPath, "Triple-S2");
		startTripleS2Matching();
	}
	
	/**
	 * Starts a Triple-S matching task.
	 *
	 * @throws IOException signals that an I/O exception has occurred when accessing the PNML files or writing the output files.
	 */
	private static void startTripleSMatching() throws IOException {

		System.out.println("Triple-S Matching:");
		System.out.println("----------------------------------");
		System.out.println("Name = " +configuration.getMatcherName());
		System.out.println("Syntactic Weight = " +configuration.getWeights().get("Syntactic Weight"));
		System.out.println("Semantic Weight = " +configuration.getWeights().get("Semantic Weight"));
		System.out.println("Structural Arc Weight = " +configuration.getWeights().get("Structural Arc Weight"));
		System.out.println("Structural Position Weight = " +configuration.getWeights().get("Structural Position Weight"));
		System.out.println("Threshold = " +configuration.getWeights().get("Threshold"));
		System.out.println("Path to PNML files = " +configuration.getPath());
		System.out.println("Path to output directory = " +configuration.getOutputPath());
		System.out.println("----------------------------------");
		System.out.println("");
	
		String path = configuration.getPath();

		File dir = new File(path);
		String absolutePath = dir.getAbsolutePath();
		String[] fileList = getPNMLFiles(dir);
		
		int k = fileList.length;
		
		// Start parsing of nets.
		ArrayList<PetriNet> nets1 = new ArrayList<PetriNet>();
		ArrayList<PetriNet> nets2 = new ArrayList<PetriNet>();
		PNMLParser pnmlParser = new PNMLParser();
		for (int i = 0; i < k; i++) {
			PetriNet net = new PetriNet(absolutePath+File.separatorChar+fileList[i], fileList[i]);		
			pnmlParser.startParsing(net);
			net.preprocessTransitions();
			nets1.add(net);
			nets2.add(net);
		}

		ArrayList<Match> matches = new ArrayList<Match>();

		long startTime = System.currentTimeMillis();

		// Create a Triple-S matcher and start the matching process.
		TripleS tripleSMatcher = new TripleS();
		tripleSMatcher.setSyntacticWeight( configuration.getWeights().get("Syntactic Weight") );
		tripleSMatcher.setSemanticWeight( configuration.getWeights().get("Semantic Weight") );
		tripleSMatcher.setStructuralArcWeight( configuration.getWeights().get("Structural Arc Weight") );
		tripleSMatcher.setStructuralPositionWeight( configuration.getWeights().get("Structural Position Weight") );
		tripleSMatcher.setThreshold( configuration.getWeights().get("Threshold") );
		
		for (int i = 0; i < k; i++) {						
			for (int j = i + 1; j < k; j++) {
				// Create a TripleS matcher and start the matching process.
				tripleSMatcher.setNet1(nets1.get(i));
				tripleSMatcher.setNet2(nets2.get(j));
				tripleSMatcher.startMatching();
				matches = tripleSMatcher.getMatches();
				
				// Code for writing matches to a file or handle them in some other way can come here.
				System.out.println("Number of matches found for the current pair of process models = "+matches.size());
				
				matches.clear();
			}
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("Time passed for matching: "+(endTime-startTime));	
	}
	
	private static void startTripleS2Matching() throws IOException {

		System.out.println("Triple-S 2 Matching:");
		System.out.println("----------------------------------");
		System.out.println("Name = " +configuration.getMatcherName());
		System.out.println("Syntactic Weight = " +configuration.getWeights().get("Syntactic Weight"));
		System.out.println("Semantic Weight = " +configuration.getWeights().get("Semantic Weight"));
		System.out.println("Structural Weight = " +configuration.getWeights().get("Structural Weight"));
		System.out.println("Threshold = " +configuration.getWeights().get("Threshold"));
		System.out.println("Path to PNML files = " +configuration.getPath());
		System.out.println("Path to output directory = " +configuration.getOutputPath());
		System.out.println("----------------------------------");
		System.out.println("");
	
		String path = configuration.getPath();

		File dir = new File(path);
		String absolutePath = dir.getAbsolutePath();
		String[] fileList = getPNMLFiles(dir);
		
		int k = fileList.length;
		
		// Start parsing of nets.
		ArrayList<PetriNet> nets1 = new ArrayList<PetriNet>();
		ArrayList<PetriNet> nets2 = new ArrayList<PetriNet>();
		PNMLParser pnmlParser = new PNMLParser();
		for (int i = 0; i < k; i++) {
			PetriNet net = new PetriNet(absolutePath+File.separatorChar+fileList[i], fileList[i]);		
			pnmlParser.startParsing(net);
			net.preprocessTransitions();
			nets1.add(net);
			nets2.add(net);
		}

		ArrayList<Match> matches = new ArrayList<Match>();

		long startTime = System.currentTimeMillis();
		
		// Initialize Word2Vec with a path to your Word2Vec vectors
		File gModel = new File("Path to your Word2Vec vectors");
		Word2Vec vec = WordVectorSerializer.readWord2VecModel(gModel);

		// Create a Triple-S2 matcher and start the matching process.
		TripleS2 tripleS2Matcher = new TripleS2(vec);
		tripleS2Matcher.setSyntacticWeight( configuration.getWeights().get("Syntactic Weight") );
		tripleS2Matcher.setSemanticWeight( configuration.getWeights().get("Semantic Weight") );
		tripleS2Matcher.setStructuralWeightsyn( configuration.getWeights().get("Structural Weight Syn") );
		tripleS2Matcher.setStructuralWeightsem( configuration.getWeights().get("Structural Weight Sem") );
		tripleS2Matcher.setThresholdsyn( configuration.getWeights().get("Threshold Syn") );
		tripleS2Matcher.setThresholdsem( configuration.getWeights().get("Threshold Sem") );
		
		for (int i = 0; i < k; i++) {						
			for (int j = i + 1; j < k; j++) {
				tripleS2Matcher.setNet1(nets1.get(i));
				tripleS2Matcher.setNet2(nets2.get(j));
				tripleS2Matcher.startMatching();
				matches = tripleS2Matcher.getMatches();

				// Code for writing matches to a file or handle them in some other way can come here.
				System.out.println("Number of matches found for the current pair of process models = "+matches.size());
				
				matches.clear();
			}
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("Time passed for matching: "+(endTime-startTime));
	}
	
	/**
	 * Gets the PNML files from a directory while filtering other files having not the ending .pnml.
	 *
	 * @param dir the path to the directory with the PNML files.
	 * @return the PNML files contained in a String array.
	 */
	private static String[] getPNMLFiles(File dir) {
		// check if directory can be accessed
		if (!dir.isDirectory()) {
			System.out.println("Error: could not open directory "+dir.getAbsolutePath());
			System.exit(-2);
		}
		// Filter PNML files
		String[] fileList = dir.list(new FilenameFilter() {
			public boolean accept(File d, String name) {
				return name.endsWith(".pnml");
			}
		});		
		return fileList;
	}
	
}
