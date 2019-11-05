/**
 * AdditionView class is responsible to create GUI for this applicaiton 
 *
 * @author Sharmik Hirpara 101980352
 * @author Tzu-Jung Chi 101662320
 * @version 1.0
 * @since 27/10/2019
 */

import java.io.File;
import java.util.List;
import java.util.Map.Entry;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class AdditionView  {	
	
	//tab content for XML tab
	private static VBox vboxForXMLLoadTab = new VBox();
	private static HBox hboxForXML = new HBox();
	private static TextField pathToXML = new TextField();
	private static Button btnToChooseXML = new Button("Browse");
	private static Button btnToLoadXML = new Button("Load");
	private static Label labelForTextAreaXML = new Label("XML file content");
	private static TextArea txtAreaForXML = new TextArea();
	
	//tab content for keyword search;
	private static VBox vboxForKeywordSearch = new VBox();
	private static HBox hboxForKeywordSearch = new HBox();
	private static TextField inputForKeyword = new TextField();
	private static Button keywordSearchBtn = new Button("Search");
	private static Label labelForKeywordSearch = new Label("Result");
	private static TextArea outputForKeywordSearch = new TextArea();
    
    //tab content for statistic chart;
	private static VBox vboxForStatisticChart = new VBox();
	private static VBox vboxForToggleGroup = new VBox();
	private static ToggleGroup radioGroup = new ToggleGroup();
	private static RadioButton radioButton1 = new RadioButton("Top 3-correlated keywords");
	private static RadioButton radioButton2 = new RadioButton("Top 5-correlated keywords");
	private static RadioButton radioButton3 = new RadioButton("Top 8-correlated keywords");
	private static RadioButton radioButton4 = new RadioButton("Top 10-correlated keywords");
	private static Button barChartButton = new Button("Bar Chart");
	private static Button pieChartButton = new Button("Pie Chart");
	private static HBox hboxForChartButtons = new HBox();
	private static HBox hboxForCharts = new HBox();
	private static PieChart pieChart = new PieChart();
	private static CategoryAxis xaxis = new CategoryAxis();
	private static NumberAxis yaxis = new NumberAxis();
	private static BarChart<String,Integer> barChart = new BarChart(xaxis,yaxis);
	
    //main tab pane;
	private static TabPane tabPane = new TabPane();
	private static VBox vbox= new VBox(tabPane);
	private static Tab tabForXMLload = new Tab("XML File", vboxForXMLLoadTab);
	private static Tab tabForDataSearch = new Tab("Keyword Search"  , vboxForKeywordSearch);
	private static Tab tabForChart = new Tab("Statistics Chart" , vboxForStatisticChart);
    
	private static int correlatedSelection;
	private static Alert alert = new Alert(AlertType.ERROR);
	
	public AdditionView() {
		createAndConfigurePane();
		createAndLayoutControls();

		//browse btn pressed
		btnToChooseXML.setOnAction (e -> {
			File selectedFile = AdditionController.browseFile(e);
			pathToXML.setText(selectedFile.getPath());
		});
		
		//load btn pressed
		btnToLoadXML.setOnAction( e -> {
			String output = AdditionController.parseXML(e);
			txtAreaForXML.setText(output);	
		});
		
		//keyword search btn pressed
		keywordSearchBtn.setOnAction( e -> {
			String keyword = inputForKeyword.getText();
			String sectionXML = AdditionController.getSectionXML(e, keyword);
			outputForKeywordSearch.setText(sectionXML);
		});
		
		//radio btn selected
		//reference from :https://www.geeksforgeeks.org/javafx-radiobutton-with-examples/
		radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
        	public void changed(ObservableValue<? extends Toggle> ov,Toggle old_toggle, Toggle new_toggle) {

    	        if (radioGroup.getSelectedToggle() != null) {
    	        	RadioButton selectedRadioButton = (RadioButton) radioGroup.getSelectedToggle();
    	        	String toogleGroupValue = selectedRadioButton.getText();
    	        	correlatedSelection = setRangeValue(toogleGroupValue);
    	        } 
        	}
        });
		
		//bar chart btn pressed
		barChartButton.setOnAction( e -> {
			checkRadioGroup();
			String keyword = inputForKeyword.getText();
			List list = AdditionController.getSortedListOfRelatedKeyword(e,keyword);
			BarChart barChart = addBarChart(list);
			if(!hboxForCharts.getChildren().isEmpty())
				hboxForCharts.getChildren().clear();
			hboxForCharts.getChildren().add(barChart);
		});
		
		//pie chart btn pressed
		pieChartButton.setOnAction( e -> {
			checkRadioGroup();
			String keyword = inputForKeyword.getText();
			List list = AdditionController.getSortedListOfRelatedKeyword(e,keyword);
			PieChart pieChart = addPieChart(list);
			if(!hboxForCharts.getChildren().isEmpty())
				hboxForCharts.getChildren().clear();
			hboxForCharts.getChildren().add(pieChart);		
		});		
	}
	
	/**
	 * setRangeValue function returns selected radio button option
	 *
	 * @param String
	 * @return int
	 */
	
	//set keyword range from toggle value
	public static int setRangeValue(String toggleValue) {
	
		int rangeValue = 0;
		switch (toggleValue) {
			case "Top 3-correlated keywords":
				rangeValue = 3;
				break;
			case "Top 5-correlated keywords":
				rangeValue = 5;
				break;
			case "Top 8-correlated keywords":
				rangeValue = 8;
				break;	
			case "Top 10-correlated keywords":
				rangeValue = 10;
				break;
		}
		return rangeValue;
		
	}
	
	/**
	 * checkRadioGroup function checks if user has selected any 1 option or not
	 * and throw error message accordingly
	 *
	 * @return void
	 */
	
	public static void checkRadioGroup() {
		
		if (radioGroup.getSelectedToggle() == null) { 
			alert.setContentText("please choose a correlated range.");
			alert.show();
		} 
		
	}
	
	/**
	 * addPieChart function populates the pie chart
	 *
	 * @param List
	 * @return PieChart
	 */
	
	public static PieChart addPieChart(List<Entry> list) {
		
		pieChart.setData(drawPieChartData(list, correlatedSelection));
        return pieChart;
        
	}
	
	/**
	 * drawPieChartData function pulls the necessary data to draw pie chart
	 *
	 * @param List, int
	 * @return ObservableList
	 */
	
	public static ObservableList<Data> drawPieChartData(List<Entry> list, int correlatedSelection){
	
		ObservableList<Data> listData = FXCollections.observableArrayList();
		for (int i = 0; i< correlatedSelection; i++) {
			listData.addAll(new PieChart.Data(list.get(i).getKey().toString(),(int)list.get(i).getValue()));
		}
		return listData;
		
	}
	
	/**
	 * addBarChart function populates the bar chart
	 *
	 * @param List
	 * @return BarChart
	 */
	
	public static BarChart addBarChart(List list) {
		
		barChart.getData().clear();
		barChart.getData().add(drawBarChartData(list, correlatedSelection));
		return barChart;
	
	}
	
	/**
	 * drawBarChartData function pulls the necessary data to draw pie chart
	 *
	 * @param List, int
	 * @return Series
	 */
	
	public static Series drawBarChartData(List<Entry> list, int correlatedSelection){
		XYChart.Series seriesData = new XYChart.Series();
		for (int i = 0; i< correlatedSelection; i++) {
			seriesData.getData().add(new XYChart.Data(list.get(i).getKey().toString(), (int)list.get(i).getValue()));
		}
		return seriesData;
	}

	/**
	 * asParent function returns the root node which need to be added in Scene
	 *
	 * @return Parent
	 */
	
	public static Parent asParent() {	
		return vbox;
	}
	
	/**
	 * createAndConfigurePane function handles all styling properties of JavaFX components
	 *
	 * @return void
	 */

	private static void createAndConfigurePane() {

		//configuration for XML tab;
		hboxForXML.setMinHeight(40);
		hboxForXML.setSpacing(10);
		hboxForXML.setAlignment(Pos.CENTER_LEFT);

		pathToXML.setMinWidth(780);
		pathToXML.setEditable(false);
		
		labelForTextAreaXML.setFont(Font.font(30));
		labelForTextAreaXML.setAlignment(Pos.CENTER);
		labelForTextAreaXML.setMaxSize(1080, 50);

		txtAreaForXML.setMinSize(1060, 635);
		txtAreaForXML.setWrapText(true);
		txtAreaForXML.setEditable(false);
		
		vboxForXMLLoadTab.setSpacing(10);
		vboxForXMLLoadTab.setPadding(new Insets(10,10,10,10));
		
		//configuration for keyword search tab;
		hboxForKeywordSearch.setMinHeight(40);
		hboxForKeywordSearch.setSpacing(10);
		hboxForKeywordSearch.setAlignment(Pos.CENTER_LEFT);
		
		labelForKeywordSearch.setFont(Font.font(30));
		labelForKeywordSearch.setAlignment(Pos.CENTER);
		labelForKeywordSearch.setMaxSize(1080, 50);
		
		outputForKeywordSearch.setMinSize(1060, 635);
		outputForKeywordSearch.setWrapText(true);
		outputForKeywordSearch.setEditable(false);
		
		vboxForKeywordSearch.setSpacing(10);
		vboxForKeywordSearch.setPadding(new Insets(10,10,10,10));
		
		//configuration for statistic chart tab;
		vboxForToggleGroup.setSpacing(10);
		hboxForChartButtons.setSpacing(10);
		
		vboxForStatisticChart.setPadding(new Insets(10,10,10,10));
		vboxForStatisticChart.setSpacing(10);
		
		pieChart.setLegendSide(Side.LEFT);
		pieChart.setTitle("Movie Keywords Rankings");
		pieChart.setMinSize(1060, 575);
		
		xaxis.setLabel("Related keyword");
		yaxis.setLabel("Frequency");
		barChart.setTitle("Movie Keywords Rankings");
		barChart.setMinSize(1060, 575);
		
		hboxForCharts.setAlignment(Pos.CENTER);
		hboxForCharts.setMinSize(1060, 575);
		hboxForCharts.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
	}
	
	/**
	 * createAndLayoutControls function add all components into root node
	 *
	 * @return void
	 */
	
	private static void createAndLayoutControls() {
		//tab content for XML tab;	
		hboxForXML.getChildren().addAll(new Label("Choose XML file: "), pathToXML, btnToChooseXML, btnToLoadXML);
		vboxForXMLLoadTab.getChildren().addAll(hboxForXML, labelForTextAreaXML, txtAreaForXML);
		
        //tab content for keyword search;
        hboxForKeywordSearch.getChildren().addAll(new Label("Keyword: "), inputForKeyword, keywordSearchBtn);
        vboxForKeywordSearch.getChildren().addAll(hboxForKeywordSearch, labelForKeywordSearch, outputForKeywordSearch);
        
        //tab content for statistic chart;
        radioButton1.setToggleGroup(radioGroup);
        radioButton2.setToggleGroup(radioGroup);
        radioButton3.setToggleGroup(radioGroup);
        radioButton4.setToggleGroup(radioGroup);
        
        vboxForToggleGroup.getChildren().addAll(radioButton1,radioButton2,radioButton3,radioButton4);
        hboxForChartButtons.getChildren().addAll(pieChartButton, barChartButton);
        vboxForStatisticChart.getChildren().addAll(vboxForToggleGroup, hboxForChartButtons, hboxForCharts);
        
        //add tabs to main tab pane
        tabPane.getTabs().addAll(tabForXMLload, tabForDataSearch, tabForChart);
        
	}
	
}

