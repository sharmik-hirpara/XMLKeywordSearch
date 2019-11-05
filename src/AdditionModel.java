/**
 * AdditionModel class is responsible to handle all operations
 *
 * @author Sharmik Hirpara 101980352
 * @author Tzu-Jung Chi 101662320
 * @version 1.0
 * @since 27/10/2019
 */

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import java.io.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class AdditionModel {
	
	private static Map<String, Integer> mapForAllKeyValues = new HashMap<String, Integer>();
	private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	private static DocumentBuilder docBuilder = null;
	
	/**
	 * transferMapToList function converts map to list
	 *
	 * @param Map
	 * @return List
	 */
	
	public static List<Entry> transferMapToList(Map sortedMap) {
		
		List<Entry> entryList = new ArrayList<Entry>(sortedMap.entrySet());
		return entryList;
		
	}
	
	/**
	 * TransferKeyValueToNewMap function adds key value and sorts the value for correlated keyword list
	 * based on entered keyword
	 *
	 * @param ArrayList, Map
	 * @return List
	 */
	
	public static LinkedHashMap transferKeyValueToNewMap(ArrayList<String> keywords, Map mapForAllKeywords) {
		
		boolean DESC = false;
		Map<String, Integer> result = new HashMap<String, Integer>();
		for (int i = 0; i < keywords.size(); i++) {
			String keyword = keywords.get(i);
			if (mapForAllKeywords.containsKey(keyword)) {
				result.put(keyword, (Integer) mapForAllKeywords.get(keyword));
			}
		}
		return sortByValue(result, DESC);
		
	}
	
	/**
	 * sortByValue function sorts the content of Map in descending order
	 *
	 * @param Map, boolean
	 * @return HashMap
	 */
	
	//reference from https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values; tested
	private static LinkedHashMap<String, Integer> sortByValue(Map<String, Integer> unsortMap, final boolean order) {
		
        List<Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());
        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));
        
    }
	
	/**
	 * catergorizeKeywordsToHashMap function stores all keywords from the imdb database into hashmap
	 *
	 * @param File
	 * @return Map
	 */
   
	//take file input and read all node content into HashMap; tested
	public static Map catergorizeKeywordsToHashMap(File selectedFile) {
		
		try {
			docBuilder = factory.newDocumentBuilder();
			Document doc = docBuilder.parse(selectedFile);
			getNodeListFromXML(doc.getDocumentElement(), mapForAllKeyValues);
	
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {	
			e.printStackTrace();
		}
		return mapForAllKeyValues;
		
	}
	
	/**
	 * getNodeListFromXML function retrieve list of value for each node and update hash table key value pair
	 *
	 * @param Node, Map
	 * @return void
	 */

	//reference from https://stackoverflow.com/questions/5386991/java-most-efficient-method-to-iterate-over-all-elements-in-a-org-w3c-dom-docume
	public static void getNodeListFromXML(Node node, Map hMap) {
		
		NodeList nodeList = node.getChildNodes();
	    for (int i = 0; i < nodeList.getLength(); i++) {
	        Node currentNode = nodeList.item(i);
	        int type = currentNode.getNodeType();
	        if (type == Node.ELEMENT_NODE) {
	            //calls this method for all the children which is Element
	        	getNodeListFromXML(currentNode, hMap);
	        } else if (type == Node.TEXT_NODE){
	        	String value = currentNode.getTextContent();
	        	if (value != null) {
	        		updateValueOfMap(value, hMap);
	        	}		
	        }
	    }
	}

	/**
	 * updateValueOfMap function updates value pair in all keyword HashMap and return updated HashMap
	 *
	 * @param String, Map
	 * @return void
	 */
	
	public static void updateValueOfMap(String key, Map hMap ) {
		
		if(hMap.containsKey(key)) {
			int value = (int) hMap.get(key)+1;
			hMap.put(key,value);
		} else
			hMap.put(key,1);
		
	}
	
	/**
	 * getNodeContentOfKeyword function retruns the parent nodes based on entered keyword 
	 *
	 * @param Node
	 * @return ArrayList
	 */
	
	//get arraylist of nodeList content of certain keyword;
	public static ArrayList<String> getNodeContentOfKeyword(Node keywordNode) {
		
		ArrayList<String> aListforKeywordSet = new ArrayList<String>();
		NodeList nList = keywordNode.getChildNodes();
		for(int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			if (nNode.hasChildNodes()) {
				ArrayList<String> sublist = getNodeContentOfKeyword(nNode);
				for (int j = 0; j < sublist.size(); j++) {
					String keyword = sublist.get(j);
					if (!aListforKeywordSet.contains(keyword))
						aListforKeywordSet.add(keyword);
				}
			} else {
				String correlatedKeyword = nNode.getTextContent().trim();
				if (!correlatedKeyword.isEmpty() && !aListforKeywordSet.contains(correlatedKeyword)
						&&(!correlatedKeyword.contentEquals("(screenplay)"))
						&&(!correlatedKeyword.contentEquals("(characters)"))
						&&(!correlatedKeyword.contentEquals("(novel)"))
						&&(!correlatedKeyword.contentEquals("(story)")))
					aListforKeywordSet.add(correlatedKeyword);
			}
		}
		return aListforKeywordSet;	
		
	}
	
	/**
	 * getItemOfKeywordFromXML function returns single node based on entered keyword 
	 *
	 * @param Document, String
	 * @return Node
	 */
	
	//get the node of title and return its parent node as the tag "movie" 
	public static Node getItemOfKeywordFromXML(Document xmlDoc, String keyword) {
		
		xmlDoc.getDocumentElement().normalize();
		NodeList nList = xmlDoc.getElementsByTagName("title");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if(nNode.getTextContent().contains(keyword)) {
				return nNode.getParentNode();
			}
		}
		return null;
		
	}
	
	/**
	 * getItemDataFromXML function to retrieve data in tree format
	 *
	 * @param Document, String
	 * @return String
	 */
	
	public static String getItemDataFromXML(Document xmlDoc, String keyword) {
		
		String result = "";
		xmlDoc.getDocumentElement().normalize();
		NodeList nList = xmlDoc.getElementsByTagName("title");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getTextContent().contains(keyword)) {
				NodeList nListForkeyword = nNode.getParentNode().getChildNodes();
				result += transferNodeToString(nNode.getParentNode());
			}
		}
		if(result == "")
			return "Not Found";
		else
			return result;
		
	}
	
	/**
	 * transferNodeToString function retrieve data as per node
	 *
	 * @param Node
	 * @return String
	 */
	
	public static String transferNodeToString(Node node) {
		
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource();
			StringWriter writer = new StringWriter();
			source.setNode(node);
			transformer.transform(source, new StreamResult(writer));
			return writer.toString();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return "";
		
	}
	
	/**
	 * writeXmlDocumentToXmlFile function converts xml document into xml string
	 *
	 * @param Document
	 * @return String
	 */
	
	//reference from https://howtodoinjava.com/xml/xml-to-string-write-xml-file/;tested
	public static String writeXmlDocumentToXmlFile(Document xmlDocument) {
		
		String xmlString = "";
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer;
	    try {
	        transformer = tf.newTransformer();
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");//Uncomment if you do not require XML declaration
	        //A character stream that collects its output in a string buffer, which can then be used to construct a string.
	        StringWriter writer = new StringWriter();
	        transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));//transform document to string
	        xmlString = writer.getBuffer().toString();   
	    } catch (TransformerException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return  xmlString;
		
	}
	
	/**
	 * convertXMLFileToXMLDocument function convert xml file to xml document
	 *
	 * @param File
	 * @return Document
	 */
	
	//reference from https://howtodoinjava.com/xml/xml-to-string-write-xml-file/;tested
	public static Document convertXMLFileToXMLDocument(File selectedFile) {
		
	    try {
	    	docBuilder = factory.newDocumentBuilder();//Create DocumentBuilder with default configuration    
	        Document xmlDocument = docBuilder.parse(selectedFile);//Parse the content to Document object 
	        return xmlDocument;
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	    
	}
}
