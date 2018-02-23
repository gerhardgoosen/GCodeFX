package gpg.gcode.application.view.gcodecanvas;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.inject.Inject;

import gpg.gcode.application.language.Messages;
import gpg.gcode.application.service.JPAService;
import gpg.gcode.application.view.gcodesettings.GCodeSettingsPresenter;
import gpg.gcode.application.view.gcodesettings.GCodeSettingsView;
import gpg.gcode.db.utils.DBUtils;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GCodeCanvasPresenter implements Initializable {

	private static final Logger logger = Logger.getLogger(GCodeCanvasPresenter.class.getName());

	@Inject
	private JPAService jpaService;

	@FXML
	private Button gotoSettingButton;

	@FXML
	private BorderPane borderPane;

	private DBUtils dbUtils;

	private Stage stage;
	private Scene scene;

	private Canvas gCodeCanvas;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		dbUtils = new DBUtils(jpaService);

		print(Messages.GetBundle().getString("helloText"));
		initilzeDatabase();

		gCodeCanvas = new  Canvas( );

		DoubleBinding heightBinding = borderPane.heightProperty().subtract(borderPane.topProperty().getValue().getBoundsInParent().getHeight() + 20 );
		gCodeCanvas.widthProperty().bind(borderPane.widthProperty());
		gCodeCanvas.heightProperty().bind(heightBinding);

		borderPane.setCenter(gCodeCanvas);
		initializeCanvas();
	}

	private void initializeCanvas() {
		// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		final GraphicsContext graphicsContext = gCodeCanvas.getGraphicsContext2D();
		initDraw(graphicsContext);

		gCodeCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				graphicsContext.beginPath();
				graphicsContext.moveTo(event.getX(), event.getY());
				graphicsContext.stroke();
			}
		});

		gCodeCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				graphicsContext.lineTo(event.getX(), event.getY());
				graphicsContext.stroke();
			}
		});

		gCodeCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

			}
		});



	}

	private void initDraw(GraphicsContext gc) {
		double canvasWidth = gc.getCanvas().getWidth();
		double canvasHeight = gc.getCanvas().getHeight();

		//gc.setFill(Color.WHITE);
		//gc.setStroke(Color.BLACK);
		//gc.setLineWidth(5);
		//gc.fill();

		gc.strokeRect(0, // x of the upper left corner
				0, // y of the upper left corner
				canvasWidth, // width of the rectangle
				canvasHeight); // height of the rectangle

		gc.setFill(Color.RED);
		gc.setStroke(Color.BLUE);
		gc.setLineWidth(1);

	}

	private void initilzeDatabase() {

		if (!dbUtils.checkSeedData()) {
			dbUtils.populateSeedData();
		}

	}

	private void print(String text) {
		logger.info(text);
	}

	public void setStage(Stage stage) {

		this.stage = stage;
		this.scene = stage.getScene();

		// Hot key Ctrl + H for clickMe button
		this.stage.addEventFilter(KeyEvent.KEY_PRESSED, (event -> {
			if (new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN).match(event)) {
				gotoSettings();
				event.consume();
			}
		}));

		// Stage is closing
		this.stage.setOnCloseRequest(e -> {
			try {
				print(Messages.GetBundle().getString("goodbyeText"));
				// dbUtils.dropSchema(dbUtils.getConnection());
				jpaService.closeConnection();

			} catch (Throwable t) {
				t.printStackTrace();
				System.exit(-1);
			}
		});

		this.scene.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
					Number newSceneWidth) {
				System.out.println("Width: " + newSceneWidth);
				//initDraw(gCodeCanvas.getGraphicsContext2D());

			}
		});
		this.scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight,
					Number newSceneHeight) {
				System.out.println("Height: " + newSceneHeight);
				//initDraw(gCodeCanvas.getGraphicsContext2D());
			}
		});



	}

	public void gotoSettings() {

		GCodeSettingsView settingsView = new GCodeSettingsView();
		((GCodeSettingsPresenter) settingsView.getPresenter()).setStage(stage);

		Scene scene = new Scene(settingsView.getView());

		this.stage.setScene(scene);
		this.stage.show();

		// Optional<ButtonType> result = showAlert(Alert.AlertType.INFORMATION,
		// Messages.GetBundle().getString("GCodeFXController.dialog.information.contentText"));
		// result.ifPresent(buttonType -> print(buttonType.getText()));

	}

}
