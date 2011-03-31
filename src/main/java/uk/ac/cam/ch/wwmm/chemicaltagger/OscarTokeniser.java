package uk.ac.cam.ch.wwmm.chemicaltagger;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.document.IToken;
import uk.ac.cam.ch.wwmm.oscar.document.ITokenSequence;

public class OscarTokeniser {

	Oscar oscar;

	public OscarTokeniser(Oscar oscar) {
		this.oscar = oscar;
	}

	/*****************************************************
	 * Tokenises an inputText using OSCAR tokeniser. 
	 * Sets the tokens to POSContainer's wordTokenList and
	 * tokenSequenceList.
	 * @param posContainer
	 *            (POSContainer)
	 * @return posContainer (POSContainer)
	 *****************************************************/
	public POSContainer tokenise(POSContainer posContainer) {
		List<ITokenSequence> tokSequenceList = new ArrayList<ITokenSequence>();
		List<String> wordTokenList = new ArrayList<String>();
		String sentence = posContainer.getInputText();
		// Oscar doesn't do normalisation just yet
		// sentence = oscar.normalise(sentence);

		tokSequenceList = oscar.tokenise(sentence);
		for (ITokenSequence tokenSequence : tokSequenceList) {
			for (IToken tok : tokenSequence.getTokens()) {

				for (String subWord : tok.getSurface().trim().split(" ")) {
					if (StringUtils.isNotEmpty(subWord))
						wordTokenList.add(subWord);
				}

			}
		}
		posContainer.setTokenSequenceList(tokSequenceList);
		posContainer.setWordTokenList(wordTokenList);
		return posContainer;
	}

}