package application.ai;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloController {
    @FXML
    private RadioButton DarkButton;
    @FXML
    public Stage stage;
    public Scene scene;
    public Parent root=null;

    @FXML
    public void onStratApplicationButtonClick(ActionEvent event) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("configuration.fxml")));
        } catch (IOException ex) {
            System.out.println("error in  root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(\"configuration.fxml\")));\n");
            //ex.printStackTrace();
            return;
        }

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setWidth(805);
        stage.setHeight(721);
        scene = new Scene(root);

        if (DarkButton.isSelected()) {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/tableView.css")).toExternalForm());
        }
        else{
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/bright.css")).toExternalForm());

        }
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/Buttons.css")).toExternalForm());

        stage.setTitle("Fruit Recognition");

        stage.setX(0);
        stage.setY(Screen.getPrimary().getBounds().getMinY() + 50);
        stage.setScene(scene);
        stage.show();
    }
}
