package gpg.gcode.application;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.airhacks.afterburner.injection.Injector;

import gpg.gcode.application.language.Messages;
import gpg.gcode.application.view.gcodecanvas.GCodeCanvasPresenter;
import gpg.gcode.application.view.gcodecanvas.GCodeCanvasView;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GCodeFX extends Application {

	private static final String FXML_RESOURCE_NAME = "/fxml/GCodeFX.fxml";
	private static final String CSS_RESOURCE_NAME = "/styles/styles.css";

	public static void main(String[] args) throws Exception {
		// Forced to set the language
		// Locale.setDefault(new Locale("en", "EN"));
		// Locale.setDefault(new Locale("be", "BY"));
		 //Locale.setDefault(new Locale("ru", "RU"));

		launch(args);

	}

	public void start(Stage stage) throws Exception {
		/*
		 * Properties of any type can be easily injected.
		 */
		String appName = "GCodeFX";
		Map<Object, Object> customProperties = new HashMap<>();
		customProperties.put("AppName", appName);
		/*
		 * any function which accepts an Object as key and returns and return an
		 * Object as result can be used as source.
		 */
		Injector.setConfigurationSource(customProperties::get);

		GCodeCanvasView canvasView = new GCodeCanvasView();
		((GCodeCanvasPresenter)canvasView.getPresenter() ).setStage(stage);

		Scene scene = new Scene(canvasView.getView());
		scene.getStylesheets().add(CSS_RESOURCE_NAME);

		stage.setTitle(Messages.GetBundle().getString("GCodeFX.title"));

		final ObservableList<Image> icons = stage.getIcons();
		icons.add(new Image("/icons/app-128x128x32.png"));


	//	final String uri = getClass().getResource("app.css").toExternalForm();
//		scene.getStylesheets().add(uri);
		stage.setScene(scene);
		stage.show();

		// FXMLLoader loader = new FXMLLoader();
		// loader.setResources(Messages.GetBundle());
		// Parent rootNode =
		// loader.load(getClass().getResourceAsStream(FXML_RESOURCE_NAME));
		//
		// Scene scene = new Scene(rootNode, 640, 480);
		// scene.getStylesheets().add(CSS_RESOURCE_NAME);
		//
		// stage.setTitle(Messages.GetBundle().getString("GCodeFX.title"));
		// stage.setMinHeight(480);
		// stage.setMinWidth(640);
		//
		// final ObservableList<Image> icons = stage.getIcons();
		// icons.add(new Image("/icons/app-128x128x32.png"));
		//
		// stage.setScene(scene);
		//
		// GCodeFXController controller = loader.getController();
		// controller.setStage(stage);
		// stage.show();
	}

	@Override
	public void stop() throws Exception {
		Injector.forgetAll();
	}
}
