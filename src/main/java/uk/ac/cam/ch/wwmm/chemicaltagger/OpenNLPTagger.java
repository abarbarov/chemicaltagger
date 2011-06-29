/**
 * Copyright 2011 Lezan Hawizy, David M. Jessop, Daniel Lowe and Peter Murray-Rust
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.cam.ch.wwmm.chemicaltagger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;

/*****************************************************
 * Runs the OpenNLP tagger .
 * 
 * @author lh359, dmj30,jat45
 *****************************************************/
public class OpenNLPTagger {
	/**************************************
	 * Private Singleton holder.
	 ***************************************/
	private static class INSTANCE_HOLDER {
		private static OpenNLPTagger myInstance = new OpenNLPTagger();
	}
	POSTagger posTagger;
//	private static Logger LOG = Logger.getLogger(OpenNLPTagger.class);

	/**************************************
	 * Private Constructor Class.
	 ***************************************/
	private OpenNLPTagger() {

		
		try {
			setUpPosTagger();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**************************************
	 * Returns an OpenNLPTagger singleton.
	 * @return OpenNLPTaggerInstance.
	 ***************************************/
	public static OpenNLPTagger getInstance() {
		return INSTANCE_HOLDER.myInstance;
	}

	/**************************************
	 * Getter method for posTagger.
	 * 
	 * @return posTagger(PosTagger).
	 ***************************************/
	public POSTagger getTagger() {
		return posTagger;
	}



    
    /**************************************
     * Loads the Penn Treebank tagset to POSTagger.
	 * @throws IOException
	 * ***************************************/
	private void setUpPosTagger() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(
        "uk/ac/cam/ch/wwmm/chemicaltagger/openNLPTagger/en-pos-maxent.bin");
        POSModel model = new POSModel(is);
		posTagger = new POSTaggerME(model);
	}

	/*****************************************************
	 * Runs the OpenNLP POS tagger against the text and stores the tags in
	 * POSContainer.
	 * @param posContainer (POSContainer)
	 * @return posContainer (POSContainer) 
	 *****************************************************/
	public POSContainer runTagger(POSContainer posContainer) {
		List<String> tokenList = posContainer.getWordTokenList();
		String[] tokens = tokenList.toArray(new String[tokenList.size()]);
		String[] tags = posTagger.tag(tokens);
		posContainer.createPosTagListFromStringArray(tags);
		return posContainer;
	}

}
