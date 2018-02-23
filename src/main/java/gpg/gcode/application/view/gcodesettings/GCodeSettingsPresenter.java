package gpg.gcode.application.view.gcodesettings;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.inject.Inject;

import gpg.gcode.application.language.Messages;
import gpg.gcode.application.model.Student;
import gpg.gcode.application.service.JPAService;
import gpg.gcode.application.view.gcodecanvas.GCodeCanvasPresenter;
import gpg.gcode.application.view.gcodecanvas.GCodeCanvasView;
import gpg.gcode.db.utils.DBUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class GCodeSettingsPresenter implements Initializable {

	private static final Logger logger = Logger.getLogger(GCodeSettingsPresenter.class.getName());

	@Inject
	private JPAService jpaService;

	@FXML
	public Button gotoSettingCanvas;


	@FXML
	public ListView settingList;

	private DBUtils dbUtils;

	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		print(Messages.GetBundle().getString("helloText"));
		 settingList.setItems(fetchStudents());
	}


	private void print(String text) {
		System.out.println(text);
	}

	private Optional<ButtonType> showAlert(final Alert.AlertType alertType, final String message) {
		Alert alert = new Alert(alertType);
		alert.setHeaderText(Messages.GetBundle().getString("Dialog.information.header"));
		alert.setContentText(message);
		return alert.showAndWait();
	}

	public void setStage(Stage stage) {
		this.stage=stage;
		// Hot key Ctrl + H for clickMe button
		stage.addEventFilter(KeyEvent.KEY_PRESSED, (event -> {
			if (new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN).match(event)) {
				gotoCanvas();
				event.consume();
			}
		}));

		// Stage is closing
		stage.setOnCloseRequest(e -> {
			try {
				print(Messages.GetBundle().getString("goodbyeText"));
				// dbUtils.dropSchema(dbUtils.getConnection());
				jpaService.closeConnection();

			} catch (Throwable t) {
				t.printStackTrace();
				System.exit(-1);
			}
		});

	}

	public void gotoCanvas() {

	    GCodeCanvasView canvasView = new GCodeCanvasView();

			Scene scene = new Scene(canvasView.getView());

			this.stage.setScene(scene);
			((GCodeCanvasPresenter)canvasView.getPresenter() ).setStage(this.stage);
			this.stage.show();


//		Optional<ButtonType> result = showAlert(Alert.AlertType.INFORMATION,
//				Messages.GetBundle().getString("GCodeFXController.dialog.information.contentText"));
//		result.ifPresent(buttonType -> print(buttonType.getText()));



	}

	private ObservableList<String> fetchStudents()   {
		logger.info("Fetching Students from database");
		ObservableList<String> students = FXCollections.observableArrayList();

		List<Student> sList = jpaService.getEntityManager().createNamedQuery("Student.All").getResultList();
		Long studentCount = (Long) jpaService.getEntityManager().createNamedQuery("Student.Count").getSingleResult();
		for (Student s : sList) {
			students.add(s.getName());
		}

		logger.info("Found " + students.size() + " names");

		logger.info("studentCount : " + studentCount);

		return students;
	}
}
