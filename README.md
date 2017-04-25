# Triple-S
Repository for the Triple-S techniques for Process Model Matching

### Description
The Triple-S Java API can be used for Process Model Matching. It is developed as part of my doctoral thesis at the [Institute AIFB](http://www.aifb.kit.edu/web/Hauptseite/en) of the [Karlsruhe Institute of Technology](http://www.kit.edu/english/index.php) (KIT).

### Code and Used Libraries
An example on how to use the LS3 library can be found in the ExammpleUsage.java class.

The code is stored as a maven project with pom.xml included for the external library dependencies. Yet, the following external library and files have to be included manually:

* [Semilar API](http://deeptutor2.memphis.edu/Semilar-Web/): The Semilar API is used to calculate the semantic similarity of words using the implementation of Wu & Palmer's approach.
* [Deeplearning4J](https://deeplearning4j.org/index.html): The Triple-S2 implementation uses the Deeplearning4J implementation of the Word2Vec approach. Therefore, trained word vectors have been used, which can be obtained [here](https://s3.amazonaws.com/dl4j-distribution/GoogleNews-vectors-negative300.bin.gz).

Further information on using the Triple-S code can be found in the [Wiki](https://github.com/ASchoknecht/Triple-S/wiki/Setup).

### Licensing
Licensed under the EPL License v1.

Copyright 2017 by Andreas Schoknecht <andreas_schoknecht@web.de>

This source code is made available under the terms of the Eclipse Public License v1.0  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html.
