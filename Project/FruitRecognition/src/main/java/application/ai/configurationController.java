package application.ai;

import application.ai.BackEnd.Fruit;
import application.ai.BackEnd.NeuralNetwork;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import org.apache.poi.EncryptedDocumentException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static application.ai.configurationHelper.*;

public class configurationController implements Initializable {
    public String parsedContent;
    public boolean isFromfile = false;
    public int epochsNumber;
    public float goal;
    public float learningRate;
    public int perceptronsNumber;
    public String activationFunction;
    public NeuralNetwork neuralNetwork =new NeuralNetwork();
    public String TrainingPath ="src/main/resources/Data/TrainingData.txt";
    public String TestingPath="src/main/resources/Data/TestingData.txt";
    int delayTime=250;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StatusLabel.setText("");
        AccuracyValueLabel.setText("");
        PerformanceValueLabel.setText("");
        AccuracyLabel.setVisible(false);
        PerformanceLabel.setVisible(false);
        StartTesting.setVisible(false);
        TestAgain.setVisible(false);
        TrainFromSkratch.setVisible(false);
        TrainAgain.setVisible(false);
        String Choices[] = {"relu", "tanh", "sigmoid"};
        ActivationFuncutionChoiceBox.getItems().addAll(Choices);
        DataTableView.setEditable(true);

        initializeColumn();
        DataTableView.setEditable(true);
        ObservableList<fruits> data = FXCollections.observableArrayList();

        // Add sample data to the TableView (replace this with your actual data)
        for (int i = 0; i < 20; i++) {  // You can change 20 to the number of empty rows you want
            data.add(new fruits("", null, "", "")); // Assuming the fruits constructor accepts null for numeric fields
        }
        DataTableView.setItems(data);
//        DataTableView.getItems().clear();
        // Make the TableView editable
        DataTableView.setEditable(true);
        StatusLabel.setText("Either upload .txt/.xlsx file or enter data manually to table");
    }
    @FXML
    void UploadFileHandllar(ActionEvent event) {
        if(StartTraining.isVisible()) {
            isFromfile = true;

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose a File");

            FileChooser.ExtensionFilter allFilesFilter = new FileChooser.ExtensionFilter("All Files", "*.*");
            fileChooser.getExtensionFilters().add(allFilesFilter);

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
            );

            File selectedFile = fileChooser.showOpenDialog(new Stage());

            if (selectedFile != null) {
                if (selectedFile.getName().endsWith(".txt")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                        parsedContent = reader.lines().collect(Collectors.joining("\n"));
                        System.out.println(parsedContent);

                    } catch (IOException e) {
                        StatusLabel.setText("Error in reading file");
                    }
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(TrainingPath))) {
                        writer.write(parsedContent);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fillTableFromFile(TrainingPath,DataTableView);
                }
                else if (selectedFile.getName().endsWith(".xlsx")) {
                    try {
                        parsedContent = readExcelFile(selectedFile);
                        System.out.println(parsedContent);
                    } catch (IOException | EncryptedDocumentException e) {
                        StatusLabel.setText("Error in reading file");
                    }
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(TrainingPath))) {
                        writer.write(parsedContent);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fillTableFromFile(TrainingPath,DataTableView);
                }
                else {
                    StatusLabel.setText("Unsupported file type. Only .txt and .xlsx files are supported.");
                }
            }
            else { StatusLabel.setText("No file selected"); }
        }
        else {
            isFromfile = true;

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose a File");

            FileChooser.ExtensionFilter allFilesFilter = new FileChooser.ExtensionFilter("All Files", "*.*");
            fileChooser.getExtensionFilters().add(allFilesFilter);

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
            );

            File selectedFile = fileChooser.showOpenDialog(new Stage());

            if (selectedFile != null) {
                if (selectedFile.getName().endsWith(".txt")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                        parsedContent = reader.lines().collect(Collectors.joining("\n"));
                        System.out.println(parsedContent);
                    } catch (IOException e) {
                        StatusLabel.setText("Error in reading file");
                    }
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(TestingPath))) {
                        writer.write(parsedContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (selectedFile.getName().endsWith(".xlsx")) {
                    try {
                        parsedContent = readExcelFile(selectedFile);
                        System.out.println(parsedContent);
                    } catch (IOException | EncryptedDocumentException e) {
                        StatusLabel.setText("Error in reading file");
                    }
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(TestingPath))) {
                        writer.write(parsedContent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    StatusLabel.setText("Unsupported file type. Only .txt and .xlsx files are supported.");
                }
            }
            else { StatusLabel.setText("No file selected"); }
        }
    }
    @FXML
    void StartTrainingHandllar(ActionEvent event) {
        activationFunction = ActivationFuncutionChoiceBox.getValue();
        epochsNumber = Integer.parseInt(EpochesNumberTextField.getText());
        goal = Float.parseFloat(GoalTextField.getText());
        learningRate = Float.parseFloat(LearningRateTextField.getText());
        perceptronsNumber = Integer.parseInt(PerceptronsNumberTextField.getText());

        if (validateInputs())
        {
            if (isFromfile)
            {
                isFromfile=false;
                if (parsedContent != null) {
                    StatusLabel.setText("Training with file data");
                }
                else {
                    StatusLabel.setText("No file content available for training!");
                }
            }
            else {
                StatusLabel.setText("Training with table data");
                //TODO: Validate input table
                validateTableInputs(DataTableView);
                writeTableDataToFile(TrainingPath, DataTableView);
            }

            Object eventSource = event.getSource();
            if (eventSource instanceof Node) {
                Node sourceNode = (Node) eventSource;
                maximizeStageWidth(sourceNode);
            } else {

                System.out.println("Event source is not a Node.");
            }
            int delayValue= ((perceptronsNumber * 2) + (perceptronsNumber * 3) + (5+perceptronsNumber) )* delayTime;
            drawNetwork(perceptronsNumber);
            PauseTransition delay1 = new PauseTransition(Duration.millis(delayValue));

            delay1.setOnFinished(event2 -> {
                neuralNetwork.trainData(TrainingPath,perceptronsNumber,(double) learningRate,activationFunction,epochsNumber,goal);
                int totalEpochs = neuralNetwork.returnedEpoch;
                int totalDuration=0;

                for (int j = 1; j <= totalEpochs; j++) {
                    int finalJ = j;


                    PauseTransition delay = new PauseTransition(Duration.millis(j*20));

                    delay.setOnFinished(event1 -> {
                        epochsLabel.setText("Epochs: " + (finalJ+1));
                    });

                    delay.play();
                }
                PauseTransition delay = new PauseTransition(Duration.millis(totalEpochs*20));

                delay.setOnFinished(event1 -> {
                    AccuracyValueLabel.setText(neuralNetwork.trainingAcc+"");
                    double crossEntropy=-neuralNetwork.crossEntropyTraining/(neuralNetwork.size*epochsNumber);
                    PerformanceValueLabel.setText(crossEntropy+"");
                });

                delay.play();

            });
            delay1.play();

            StartTraining.setVisible(false);
            StartTesting.setVisible(true);
            TrainAgain.setVisible(true);
            TrainFromSkratch.setVisible(true);

            AccuracyLabel.setVisible(true);
            PerformanceLabel.setVisible(true);
        }
        else {
            StatusLabel.setText("Invalid inputs. Please check your input values.");
        }
    }
    @FXML
    void StartTestingHandllar(ActionEvent event) {
        try {
            DataTableView.getItems().clear();
            ArrayList<Fruit> FruitList = neuralNetwork.readDataFromFile(TestingPath);
            ArrayList<fruits> fruitList = new ArrayList<>();

            if (FruitList.isEmpty()) {
                StatusLabel.setText("No data found in file.");
                return;
            }
            for (Fruit fruit : FruitList) {
                if (fruit.color == null || fruit.sweetness == null || fruit.name == null) {
                    System.out.println("Invalid fruit data encountered.");
                    continue; // Skip this iteration
                }
                neuralNetwork.testData(fruit.color, Integer.parseInt(fruit.sweetness), fruit.name, activationFunction);
                fruits fruitelemnt = new fruits(encoder(fruit.color,1), Integer.parseInt(fruit.sweetness),encoder(fruit.name,3) , neuralNetwork.output);
                fruitList.add(fruitelemnt);
            }
            double accu = (double) neuralNetwork.counter / FruitList.size();
            AccuracyValueLabel.setText((accu * 100) + " %");

            double crossEntropy;
            crossEntropy = -neuralNetwork.crossEntropyTest / (FruitList.size());
            PerformanceValueLabel.setText(crossEntropy+"");

            // Update the table with the new data
            if (!fruitList.isEmpty()) {
                ObservableList<fruits> observableList = FXCollections.observableArrayList(fruitList);
                DataTableView.setItems(observableList);
                DataTableView.refresh();
            } else {
                StatusLabel.setText("No valid fruits data to display.");
            }
        } catch (Exception e) {
            e.printStackTrace();
           StatusLabel.setText("An error occurred in StartTestingHandllar.");
        }
    }
    String encoder (String data ,int parametr){
        if (parametr==1)
        {
            if (data.equals("1"))
                return "red";
            if (data.equals("2"))
                return "orange";
            if (data.equals("3"))
                return "yellow";

        }
        else if (parametr==3) {
            if (data.equals("1 0 0"))
                return "apple";
            if (data.equals("0 1 0"))
                return "orange";
            if (data.equals("0 0 1"))
                return "banana";
        }

        return "error in encoder";
    }

    @FXML
    void TestAgainHandllar(ActionEvent event) {
        DataTableView.getItems().clear();
        StartTestingHandllar(new ActionEvent());
        neuralNetwork.counter=0;
    }

    @FXML
    void TrainAgainHandllar(ActionEvent event) {
        epochsLabel.setText("Epochs : ");
        PerformanceValueLabel.setText("");
        AccuracyValueLabel.setText("");
        neuralNetwork.counter=0;
       // DataTableView.getItems().clear();
        ObservableList<fruits> data = FXCollections.observableArrayList();

        // Add sample data to the TableView (replace this with your actual data)
//        for (int i = 0; i < 20; i++) {  // You can change 20 to the number of empty rows you want
//            data.add(new fruits("", null, "", "")); // Assuming the fruits constructor accepts null for numeric fields
//        }
//        DataTableView.setItems(data);
        StartTrainingHandllar(new ActionEvent());
    }
    @FXML
    void TrainFromSkratchHandllar(ActionEvent event) {
        neuralNetwork.counter=0;
        epochsLabel.setText("Epochs : ");
        DataTableView.getItems().clear();
        networkPane.getChildren().clear();
        neuralNetwork=new NeuralNetwork();
        StatusLabel.setText("");
        AccuracyValueLabel.setText("");
        PerformanceValueLabel.setText("");
        AccuracyLabel.setVisible(false);
        PerformanceLabel.setVisible(false);

        ActivationFuncutionChoiceBox.setValue(null);
        PerceptronsNumberTextField.setText("");
        EpochesNumberTextField.setText("");
        LearningRateTextField.setText("");
        GoalTextField.setText("");

        TrainFromSkratch.setVisible(false);
        TrainAgain.setVisible(false);
        TestAgain.setVisible(false);
        StartTesting.setVisible(false);
        StartTraining.setVisible(true);
        Object eventSource = event.getSource();
        if (eventSource instanceof Node) {
            Node sourceNode = (Node) event.getSource();
            minimizeStageWidth(sourceNode);
        } else {
            // Handle the case when the source is not a Node
            // For example, you might want to log an error or use a default Node
            System.out.println("Event source is not a Node.");
        }
        ObservableList<fruits> data = FXCollections.observableArrayList();

        // Add sample data to the TableView (replace this with your actual data)
        for (int i = 0; i < 20; i++) {  // You can change 20 to the number of empty rows you want
            data.add(new fruits("", null, "", "")); // Assuming the fruits constructor accepts null for numeric fields
        }
        DataTableView.setItems(data);
    }
    public static void fillTableFromFile(String filePath, TableView<fruits> DataTableView) {
        ObservableList<fruits> data = FXCollections.observableArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Reading line: " + line); // Print the line being read
                String[] parts = line.split(" ");
                System.out.println("Parts length: " + parts.length); // Check the length of the parts array

                if (parts.length == 3) {
                    String desiredName = parts[0];
                    Integer sweetnes = null;
                    try {
                        sweetnes = Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("NumberFormatException for sweetness: " + parts[1]);
                    }
                    String color = parts[2];

                    System.out.println("Parsed data: " + desiredName + " " + sweetnes + " " + color); // Print parsed data
                    data.add(new fruits(color, sweetnes, desiredName, ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataTableView.setItems(data);
        DataTableView.refresh(); // Refresh the TableView
    }
    public boolean validateInputs() {
        // Validation logic for each input field
        if (ActivationFuncutionChoiceBox.getValue() == null) {
            StatusLabel.setText("Please select an activation function.");
            return false;
        }

        if (!isInteger(EpochesNumberTextField.getText())) {
            StatusLabel.setText("Please enter a valid integer for Epochs Number.");
            return false;
        }

        if (!isNumber(GoalTextField.getText())) {
            StatusLabel.setText("Please enter a valid float for Goal.");
            return false;
        }

        if (!isNumber(LearningRateTextField.getText())) {
            StatusLabel.setText("Please enter a valid float for Learning Rate.");
            return false;
        }

        if (!isInteger(PerceptronsNumberTextField.getText())) {
            StatusLabel.setText("Please enter a valid integer for Perceptrons Number.");
            return false;
        }
        return true;
    }
    public void initializeColumn() {
//        colorColumn = new TableColumn<>("Color");
        colorColumn.setCellValueFactory(new PropertyValueFactory<fruits,String>("color"));
        colorColumn.setMinWidth(140);
        colorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        colorColumn.setOnEditCommit(event -> {
            fruits fruit = event.getRowValue();
            fruit.setColor(event.getNewValue());
        });

//        sweetnessColumn = new TableColumn<>("Sweetness");
        sweetnessColumn.setCellValueFactory(new PropertyValueFactory<fruits,Integer>("sweetnes"));
        sweetnessColumn.setMinWidth(140);
        sweetnessColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        sweetnessColumn.setOnEditCommit(event -> {
            try {
                int newValue = Integer.parseInt(String.valueOf(event.getNewValue()));
                fruits fruit = event.getRowValue();
                fruit.setSweetnes(newValue);
            } catch (NumberFormatException e) {
                StatusLabel.setText("Error: Please enter a valid integer for Sweetness.");
            }
        });

//        desiredNameColumn = new TableColumn<>("Desired Name");
        desiredNameColumn.setCellValueFactory(new PropertyValueFactory<fruits,String>("desiredName"));
        desiredNameColumn.setMinWidth(140);
        desiredNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        desiredNameColumn.setOnEditCommit(event -> {
            fruits fruit = event.getRowValue();
            fruit.setDesiredName(event.getNewValue());
        });

//        actualNameColumn = new TableColumn<>("Actual Name");
        actualNameColumn.setCellValueFactory(new PropertyValueFactory<fruits,String>("actualName"));
        actualNameColumn.setMinWidth(140);

        // Add columns to the TableView
//        DataTableView.getColumns().addAll(colorColumn, sweetnessColumn, desiredNameColumn, actualNameColumn);
    }
    public void drawNetwork(int hiddenLayerSize) {
        networkPane.getChildren().clear(); // Clear previous drawings
        SequentialTransition sequentialTransition = new SequentialTransition();

        List<Circle> inputNeurons = new ArrayList<>();
        List<Circle> hiddenNeurons = new ArrayList<>();
        List<Circle> outputNeurons = new ArrayList<>();

        Color inputColor = Color.web("#5D9AA2");
        Color hiddenColor = Color.web("#6D72A2");
        Color outputColor = Color.web("#852CA0");

        double vSpacing = 100;
        double radius = 15;
        double hSpacing = 100;
        int level = 1;
        double displacementInput = (hiddenLayerSize / 2.0 - 1);
        double displacementOutput = (hiddenLayerSize / 2.0 - 1.5) ;

        // Create neurons for each layer and add fade-in animations
        for (int i = 0; i < 2; i++) {
            Circle neuron = createNeuron(radius, inputColor, hSpacing * level, displacementInput * vSpacing + vSpacing * i);
            inputNeurons.add(neuron);
            addFadeInAnimation(sequentialTransition, neuron);
        }
        level++;
        for (int i = 0; i < hiddenLayerSize; i++) {
            Circle neuron = createNeuron(radius, hiddenColor, hSpacing * level, i * vSpacing);
            hiddenNeurons.add(neuron);
            addFadeInAnimation(sequentialTransition, neuron);
        }
        level++;
        for (int i = 0; i < 3; i++) {
            Circle neuron = createNeuron(radius, outputColor, hSpacing * level, displacementOutput * vSpacing + i * vSpacing);
            outputNeurons.add(neuron);
            addFadeInAnimation(sequentialTransition, neuron);
        }

        // Create and add animations for each connection
        for (Circle input : inputNeurons) {
            for (Circle hidden : hiddenNeurons) {
                Line connection = connectCircles(input, hidden);
                addLineDrawingAnimation(sequentialTransition, connection);
            }
        }
        for (Circle hidden : hiddenNeurons) {
            for (Circle output : outputNeurons) {
                Line connection = connectCircles(hidden, output);
                addLineDrawingAnimation(sequentialTransition, connection);
            }
        }

        sequentialTransition.play();
    }
    public Circle createNeuron(double radius, Color color, double x, double y) {
        Circle neuron = new Circle(radius, color);
        neuron.setCenterX(x);
        neuron.setCenterY(y);
        neuron.setOpacity(0); // Start fully transparent
        networkPane.getChildren().add(neuron);
        return neuron;
    }
    public void addFadeInAnimation(SequentialTransition sequentialTransition, Circle neuron) {
        FadeTransition ft = new FadeTransition(Duration.millis(delayTime), neuron);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        sequentialTransition.getChildren().add(ft);
    }
    public void addLineDrawingAnimation(SequentialTransition sequentialTransition, Line connection) {
        double lineLength = Math.sqrt(Math.pow(connection.getEndX() - connection.getStartX(), 2) + Math.pow(connection.getEndY() - connection.getStartY(), 2));
        connection.getStrokeDashArray().setAll(lineLength);
        connection.setStrokeDashOffset(lineLength);
        networkPane.getChildren().add(connection);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(connection.strokeDashOffsetProperty(), lineLength)),
                new KeyFrame(Duration.millis(delayTime), new KeyValue(connection.strokeDashOffsetProperty(), 0))
        );
        sequentialTransition.getChildren().add(timeline);
    }
    public  Line connectCircles(Circle c1, Circle c2) {
        Line connection = new Line(c1.getCenterX(), c1.getCenterY(), c2.getCenterX(), c2.getCenterY());
        connection.setStroke(Color.BLACK); // Set the color of your connection line
        connection.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                StatusLabel.setText("hi");
            }
        });
        return connection;
    }



    public void drawNetworkOld(int hiddenLayerSize) {
        networkPane.getChildren().clear(); // Clear previous drawings
        SequentialTransition sequentialTransition = new SequentialTransition();

        List<Circle> inputNeurons = new ArrayList<>();
        List<Circle> hiddenNeurons = new ArrayList<>();
        List<Circle> outputNeurons = new ArrayList<>();

        Color inputColor = Color.web("#5D9AA2");
        Color hiddenColor = Color.web("#6D72A2");
        Color outputColor = Color.web("#852CA0");
        Color connectionColor = Color.BLACK;

        double vSpacing = 100;
        double radius = 15;
        double hSpacing = 100;
        int level=1;
        double displacementInput=0;
        double displacementOutput=0;

        if (hiddenLayerSize==1)displacementInput=0;
        else if (hiddenLayerSize %2==0)displacementInput =((double) hiddenLayerSize/2) -1;
        else                           displacementInput =((double) hiddenLayerSize/2) -1;

        if (hiddenLayerSize==1)displacementOutput=0;
        else if (hiddenLayerSize %2==0)displacementOutput =((double) hiddenLayerSize/2) - 1.5;
        else displacementOutput =((double) hiddenLayerSize/2) - 1.5;

        for (int i = 0; i < 2; i++) {
            Circle inputNeuron = new Circle(radius,inputColor);
            inputNeuron.setCenterX(hSpacing);
            inputNeuron.setCenterY( displacementInput *vSpacing + vSpacing *i );
            System.out.println(displacementInput *vSpacing + vSpacing *i  );
            networkPane.getChildren().add(inputNeuron);
            inputNeurons.add(inputNeuron);

        }
        level++;
        for (int i = 0; i < hiddenLayerSize; i++) {
            Circle hiddenNeuron = new Circle(radius,hiddenColor);
            hiddenNeuron.setCenterX(hSpacing*level);
            hiddenNeuron.setCenterY( i * vSpacing);
            networkPane.getChildren().add(hiddenNeuron);
            hiddenNeurons.add(hiddenNeuron);
        }
        level++;
        for (int i = 0; i < 3; i++) {
            Circle outputNeuron = new Circle(radius,outputColor);
            outputNeuron.setCenterX(hSpacing*level);
            outputNeuron.setCenterY(displacementOutput * vSpacing + i * vSpacing);
            networkPane.getChildren().add(outputNeuron);
            outputNeurons.add(outputNeuron);
        }
        for(Circle outrCircle:inputNeurons){
            for(Circle inerCircle:hiddenNeurons){
                Line connection = connectCircles(outrCircle, inerCircle);
                networkPane.getChildren().add(connection);
            }
        }
        for(Circle outrCircle:hiddenNeurons){
            for(Circle inerCircle:outputNeurons){
                Line connection = connectCircles(outrCircle, inerCircle);
                networkPane.getChildren().add(connection);
            }
        }
        Line exampleConnection1 = new Line(50, 50, 150, 50);
        exampleConnection1.setStroke(connectionColor);
        Line exampleConnection2 = new Line(150, 50, 250, 50);
        exampleConnection2.setStroke(connectionColor);
    }
    public Line connectCirclesOld(Circle c1, Circle c2) {
        double x1 = c1.getCenterX();
        double y1 = c1.getCenterY();
        double x2 = c2.getCenterX();
        double y2 = c2.getCenterY();

        return new Line(x1, y1, x2, y2);
    }


    @FXML
    private Label epochsLabel;
    @FXML
    public Pane networkPane;
    @FXML
    public  TableView<fruits> DataTableView ;
    @FXML
    public   TableColumn<fruits, String> colorColumn;
    @FXML
    public  TableColumn<fruits, Integer> sweetnessColumn;
    @FXML
    public TableColumn<fruits, String> desiredNameColumn;
    @FXML
    public TableColumn<fruits, String> actualNameColumn;

    @FXML
    public  ChoiceBox<String> ActivationFuncutionChoiceBox;
    @FXML
    public  TextField EpochesNumberTextField;
    @FXML
    public  TextField GoalTextField;
    @FXML
    public  TextField LearningRateTextField;
    @FXML
    public  TextField PerceptronsNumberTextField;
    @FXML
    public  Label StatusLabel;
    @FXML
    public Label AccuracyLabel;

    @FXML
    public  Label AccuracyValueLabel;
    @FXML
    public Label PerformanceLabel;

    @FXML
    public  Label PerformanceValueLabel;
    @FXML
    public  Button StartTesting;
    @FXML
    public  Button StartTraining;

    @FXML
    public Button TestAgain;

    @FXML
    public Button TrainAgain;

    @FXML
    public Button TrainFromSkratch;
}