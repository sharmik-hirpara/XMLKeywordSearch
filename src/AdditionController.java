/**
 * AdditionController class is used to link model and view classes
 *
 * @author Sharmik Hirpara 101980352
 * @author Tzu-Jung Chi 101662320
 * @version 1.0
 * @since 27/10/2019
 */

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class AdditionController implements EventHandler<Event> {
	
	private static Stage primaryStage;
	private static File selectedFile;
	
	public AdditionController(AdditionView view, AdditionModel model, Stage primaryStage) {
		
		AdditionController.primaryStage = primaryStage;
		
	}
	
	/**
	 * browseFile function allows user to select file from local directory
	 *
	 * @param ActionEvent
	 * @return File
	 */
	
	//browse file btn pressed; tested
	public static File browseFile(ActionEvent browseEvent) {
		
		FileChooser file = new FileChooser();      
        file.setTitle("Open XML File");
        selectedFile = file.showOpenDialog(primaryStage);
		return selectedFile;
	}
	
	/**
	 * getSectionXML function returns XML data in string version
	 *
	 * @param ActionEvent, String
	 * @return String
	 */
	
	//get section of XML file according to the input keyword;
	public static String getSectionXML(ActionEvent xmlParse, String keyword) {
		
		Document xmlDocument = AdditionModel.convertXMLFileToXMLDocument(selectedFile);
		String result = AdditionModel.getItemDataFromXML(xmlDocument, keyword);
		return result;
		
	}
	
	/**
	 * parseXML function populates XML data in text area
	 *
	 * @param ActionEvent
	 * @return String
	 */
	
	//parse XML to text;tested
	public static String parseXML(ActionEvent xmlParse) {
		//show file browsing window;
		Document xmlDocument = AdditionModel.convertXMLFileToXMLDocument(selectedFile);
		String output= AdditionModel.writeXmlDocumentToXmlFile(xmlDocument);	
		return output;
	}

	/**
	 * getSortedListOfRelatedKeyword function sorts the content basesd on keywords
	 *
	 * @param ActionEvent
	 * @return String
	 */
	
	public static List getSortedListOfRelatedKeyword(ActionEvent chartEvent, String keyword) {
		Map oldMap = AdditionModel.catergorizeKeywordsToHashMap(selectedFile);
		Document xmlDoc = AdditionModel.convertXMLFileToXMLDocument(selectedFile); 
		Node nodeForkeyword = AdditionModel.getItemOfKeywordFromXML(xmlDoc, keyword);
		ArrayList<String> al = AdditionModel.getNodeContentOfKeyword(nodeForkeyword);
		LinkedHashMap newMap = AdditionModel.transferKeyValueToNewMap(al, oldMap);
		return AdditionModel.transferMapToList(newMap);		
	}
	
	@Override
	public void handle(Event event) {

	}

}
