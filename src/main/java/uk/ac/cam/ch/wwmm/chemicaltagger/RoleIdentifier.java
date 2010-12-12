package uk.ac.cam.ch.wwmm.chemicaltagger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;

import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

public class RoleIdentifier {

	private String text;
	private ChemistryPOSTagger chemPos;
	private static Logger LOG = Logger.getLogger(RoleIdentifier.class);

	
	public RoleIdentifier() {
		chemPos = ChemistryPOSTagger.getInstance();
	}

	public void setText(String text) {
		this.text = text;
	}

	public HashMap<String, List<String>> getRoles() {
		HashMap<String, List<String>> roleMap = new HashMap<String, List<String>>();
		
		Document parsedDoc = runChemicalTagger();
		LOG.info(parsedDoc.toXML());
		Nodes nodes = parsedDoc.query("//MOLECULE");
		for (int i = 0; i < nodes.size(); i++) {
			List<String> roles = new ArrayList<String>();
			Element nodeElement = (Element) nodes.get(i);
			String role = "None";
			String chemicalName = "";
			if (nodeElement.getAttribute("role") != null)
				role = nodeElement.getAttribute("role").getValue();
			Nodes chemicalNodes = nodeElement.query(".//OSCAR-CM");
			for (int j = 0; j < chemicalNodes.size(); j++) {
				Element chemicalElement = (Element) chemicalNodes.get(j);
				chemicalName = chemicalName + " " + chemicalElement.getValue();
			}

			if (roleMap.containsKey(chemicalName))
				roles = roleMap.get(chemicalName);

			roles.add(role);
			roleMap.put(chemicalName, roles);

		}

		return roleMap;
	}

	private Document runChemicalTagger() {
		
		LOG.info("Text is "+text);
		POSContainer posContainer = chemPos.runTaggers(text);
		
		String tagged = posContainer.getTokenTagTupleAsString();
		LOG.info("Tagged text is "+tagged);
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(tagged.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ChemistrySentenceParser chemistrySentenceParser = new ChemistrySentenceParser(
				in);

		Tree t = chemistrySentenceParser.parseTags();
		LOG.info(t.toStringTree());
		ASTtoXML ast2XML = new ASTtoXML();
		Document doc = ast2XML.convert(t, true);
		return doc;
	}

	public static void main(String[] args) {
		RoleIdentifier roleIdent = new RoleIdentifier();
		roleIdent.setText("Potassium was washed with acetone. Salt in water");
		HashMap<String, List<String>> identifiedRoles = roleIdent.getRoles();
		LOG.info(identifiedRoles);
	}
}
