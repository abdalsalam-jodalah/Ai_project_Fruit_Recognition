package application.ai;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class configurationHelper {
//    static class fruits {
//        String color;
//        int sweetnes;
//
//        String desiredName;
//        String actualName;
//
//        public fruits() {
//        }
//
//        public fruits(String color, int sweetnes, String desiredName, String actualName) {
//            this.color = color;
//            this.sweetnes = sweetnes;
//            this.desiredName = desiredName;
//            this.actualName = actualName;
//        }
//
//        public void setColor(String color) {
//            this.color = color;
//        }
//
//        public void setSweetnes(int sweetnes) {
//            this.sweetnes = sweetnes;
//        }
//
//        public void setDesiredName(String desiredName) {
//            this.desiredName = desiredName;
//        }
//
//        public void setActualName(String actualName) {
//            this.actualName = actualName;
//        }
//
//        public String getColor() {
//            return color;
//        }
//
//        public int getSweetnes() {
//            return sweetnes;
//        }
//
//        public String getDesiredName() {
//            return desiredName;
//        }
//
//        public String getActualName() {
//            return actualName;
//        }
//    }

    public static void writeTableDataToFile(String filePath, TableView<fruits> DataTableView) {
        ObservableList<fruits> tableData = DataTableView.getItems();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) { // false to overwrite
            // Append each line of data to the file
            for (fruits fruit : tableData) {
                if (fruit.getSweetnes() != null) {
                    writer.write(fruit.getDesiredName() + " " +
                            fruit.getSweetnes() + " " +
                            fruit.getColor());
                    writer.newLine(); // Add a newline after each line
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Content has been written to " + filePath);
    }

    public static boolean validateTableInputs(TableView<fruits> DataTableView){
        ObservableList<fruits> tableData = DataTableView.getItems();
        for (fruits fruits : tableData) {
            System.out.println("Color: " + fruits.getColor() +
                    ", Sweetness: " + fruits.getSweetnes() +
                    ", Desired Name: " + fruits.getDesiredName() +
                    ", Actual Name: " + fruits.getActualName());
        }
        boolean flag =true;
        for (fruits fruits : tableData) {
            System.out.println("Color: " + fruits.getColor() +
                    ", Sweetness: " + fruits.getSweetnes() +
                    ", Desired Name: " + fruits.getDesiredName() +
                    ", Actual Name: " + fruits.getActualName());
        }
        return true;
    }

    @FXML
    public static void maximizeStageWidth(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        final double targetWidth = screenBounds.getWidth();
        final double targetHeight = screenBounds.getHeight();
        final double deltaWidth = (targetWidth - stage.getWidth()) / 10;  // Number of steps
        final double deltaHeight = (targetHeight - stage.getHeight()) / 10;

        Timeline timeline = new Timeline();
        for (int i = 0; i < 10; i++) {  // 10 steps
            double newWidth = stage.getWidth() + deltaWidth * i;
            double newHeight = stage.getHeight() + deltaHeight * i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(50 * i), e -> {
                stage.setWidth(newWidth);
                stage.setHeight(newHeight);
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();
    }
    public static void minimizeStageWidth(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        final double targetWidth = 790;  // Target width to minimize to
        final double deltaWidth = (stage.getWidth() - targetWidth) / 10;  // Number of steps

        Timeline timeline = new Timeline();
        for (int i = 0; i < 10; i++) {  // 10 steps
            double newWidth = stage.getWidth() - deltaWidth * i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(50 * i), e -> {
                stage.setWidth(newWidth);
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();
    }
    public static String readExcelFile(File excelFile) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(excelFile)) {
            StringBuilder contentBuilder = new StringBuilder();

            for (Sheet sheet : workbook) {
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        contentBuilder.append(cell.toString()).append("\t");
                    }
                    contentBuilder.append("\n");
                }
            }

            return contentBuilder.toString();
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isNumber(String input) {
        try {
            Integer.parseInt(input);
            return true;  // If it's a valid integer, return true
        } catch (NumberFormatException eInt) {
            try {
                Float.parseFloat(input);
                return true;  // If it's a valid float, return true
            } catch (NumberFormatException eFloat) {
                return false;  // If neither parsing succeeds, return false
            }
        }
    }
    public static boolean isFloat(String input) {
        try {
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
