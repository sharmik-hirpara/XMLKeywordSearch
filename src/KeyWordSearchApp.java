/**
 * KeyWordSearchApp class is main class which initiates MVC classes and starts the application
 *
 * @author Sharmik Hirpara 101980352
 * @author Tzu-Jung Chi 101662320
 * @version 1.0
 * @since 27/10/2019
 */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class KeyWordSearchApp extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		AdditionModel model = new AdditionModel();
		AdditionView view = new AdditionView();
		new AdditionController(view, model,primaryStage);
		
		Scene scene = new Scene(view.asParent(),1200,800);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Keyword Search Ranking");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
