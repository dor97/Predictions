package App;
import DTO.*;
import Engine.Engine;
import Engine.Status;

import TreeDetails.TreeDetailsController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import TreeView.TreeViewController;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import Engine.myTask;
import javafx.util.Pair;

public class AppController implements Initializable {

    @FXML private Button graphicDisplayButton;
    @FXML private Button stopSimulationButton;
    @FXML private Button resumeSimulationButton;
    @FXML private Label consistencyLabel;
    @FXML private Label averageLabel;
    @FXML private Button loadFileButton;
    @FXML private HBox mainHbox;
    @FXML private Tab resultsTab;
    @FXML private Tab newExecutionTab;
    @FXML private Button hotStyleButton;
    @FXML private Button coldStyleButton;
    @FXML private Button nextButton;
    @FXML private TabPane tabPane;
    @FXML private Tab DetailsTab;
    @FXML private BorderPane detailsBorderPane;
    @FXML private TableColumn<QueueManagement, String> statusColumn;
    @FXML private TableColumn<QueueManagement, Integer> amountColumn;
    @FXML private TableView<QueueManagement> queueManagementTable;
    @FXML private TreeView<String> resultsTreeView;
    @FXML private Button resultsGraphButton;
    @FXML private Button histogramButton;
    @FXML private Label consistencyValueLabel;
    @FXML private Label averageValueLabel;
    @FXML private Button pauseButton;
    @FXML private ListView<ExecutionListItem> executionListView;
    @FXML private TableView entitiesRunTable;
    @FXML private TableColumn entityRunColumn;
    @FXML private TableColumn populationRunColumn;
    @FXML private Button rerunButton;
    @FXML private Label ticksLabel;
    @FXML private Label ticksValueLabel;
    @FXML private Label secondsLabel;
    @FXML private Label secondsValueLabel;
    @FXML private HBox tablesHbox;
    @FXML private Button startSimulationButton;
    @FXML private Button clearSimulationButton;
    @FXML private TableColumn<EntitiesTable, TextField> populationColumn;
    @FXML private TableColumn<EntitiesTable, String> entityColumn;
    @FXML private TableView<EntitiesTable> entitiesTable;
    @FXML private TableColumn<EnvironmentVariableTable, TextField> valueColumn;
    @FXML private TableColumn<EnvironmentVariableTable, String > environmentVarColumn;
    @FXML private TableView<EnvironmentVariableTable> environmentVarTable;
    @FXML private BorderPane treeViewComponent;
    @FXML private TreeDetailsController treeDetailsController;
    @FXML private TreeViewController treeViewController;
    @FXML private TreeView<DTOSimulationDetailsItem> detailsTreeView;
    @FXML private TextField loadedFilePathTextBox;
    @FXML private TextArea exceptionArea;
    private Stage primaryStage;
    private Stage graphicDisplayStage;
    private Engine engine;
    private ObservableList<EnvironmentVariableTable> environmentVariableTableData = FXCollections.observableArrayList();
    private ObservableList<EntitiesTable> entitiesTableData = FXCollections.observableArrayList();
    private ObservableList<EntitiesRunTable> entitiesRunTablesData = FXCollections.observableArrayList();
    private ObservableList<ExecutionListItem> executionListViewData = FXCollections.observableArrayList();
    private ObservableList<QueueManagement> queueManagementData = FXCollections.observableArrayList();
    private int simulationID;
    private myTask newTask = null;
    private Integer lastSimulationNum = 0;
    private Boolean isFirstSimulationForFile = true;
    private LineChart lastSimulationGraph;
    private BarChart lastSimulationHistogram;
    private ScatterChart simulationSpace;
    private String lastChosenPropertyForHistogram;
    private String lastChosenEntityForHistogram;
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    private BooleanProperty isSimulationEnded = new SimpleBooleanProperty(false);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alert.setTitle("Error");
        alert.setWidth(1000);
        alert.setHeight(1000);
        rerunButton.setDisable(true);
        if (treeViewController != null && treeDetailsController != null) {
            treeViewController.setMainController(this);
            treeDetailsController.setMainController(this);
            treeViewController.setAlert(alert);
        }
        engine = new Engine();
        resultsGraphButton.setDisable(true);
        histogramButton.setDisable(true);
        environmentVarColumn.setCellValueFactory(new PropertyValueFactory<>("envVarNameColumn"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        entityColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        populationColumn.setCellValueFactory(new PropertyValueFactory<>("population"));
        entityRunColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        populationRunColumn.setCellValueFactory(new PropertyValueFactory<>("population"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        environmentVarTable.setItems(environmentVariableTableData);
        entitiesTable.setItems(entitiesTableData);
        entitiesRunTable.setItems(entitiesRunTablesData);
        queueManagementTable.setItems(queueManagementData);
        executionListView.setItems(executionListViewData);

        nextButton.setDisable(true);
        graphicDisplayButton.setDisable(true);
        pauseButton.setDisable(true);
        resumeSimulationButton.setDisable(true);
        stopSimulationButton.setDisable(true);

        isSimulationEnded.addListener((observable, oldValue, newValue) -> {
            displaySimulationResults();
            resultsGraphButton.setDisable(false);});


        executionListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(newTask != null && engine.getSimulationStatus(lastSimulationNum) == Status.RUNNING){
                engine.stopGettingDataUsingTask(newTask, lastSimulationNum);
            }

            if (newValue !=null){
                ticksValueLabel.setText("0");
                secondsValueLabel.setText("0");
                entitiesRunTablesData.clear();
                resultsTreeView.setRoot(null);
                consistencyValueLabel.setText("");
                averageValueLabel.setText("");
                rerunButton.setDisable(true);
                histogramButton.setDisable(true);
                resultsGraphButton.setDisable(true);
                exceptionArea.setText("");

                ExecutionListItem selectedListItem = newValue;
                Integer selectedValue = selectedListItem.getID();

                lastSimulationNum = selectedValue;

                if (engine.getSimulationStatus(lastSimulationNum) == Status.RUNNING){
                    pauseButton.setDisable(false);
                    resumeSimulationButton.setDisable(false);
                    stopSimulationButton.setDisable(false);
                }
                else {
                    pauseButton.setDisable(true);
                    resumeSimulationButton.setDisable(true);
                    stopSimulationButton.setDisable(true);
                    nextButton.setDisable(true);
                    graphicDisplayButton.setDisable(true);
                }

                if (engine.getSimulationStatus(lastSimulationNum) != Status.FINISHED){
                    rerunButton.setDisable(true);
                }else{
                    rerunButton.setDisable(false);
                }
                if(engine.getSimulationStatus(selectedValue) == Status.WAITINGTORUN){
                    newTask = null;
                    return;
                }
                newTask = new myTask();
                //exceptionArea.promptTextProperty().bind(newTask.messageProperty());
                resultsGraphButton.setDisable(true);
                histogramButton.setDisable(true);
                newTask.bindProperties(ticksValueLabel.textProperty(), secondsValueLabel.textProperty(), exceptionArea.promptTextProperty(), entitiesRunTablesData, isSimulationEnded);
                engine.getDataUsingTask(newTask, selectedValue);
                if (engine.getSimulationStatus(selectedValue) == Status.FINISHED){
                    resultsGraphButton.setDisable(false);
                    displaySimulationResults();
                }
            }
        }));
        //loadFileButton.setStyle();
        resultsTreeView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue !=null && newValue.getChildren().isEmpty()){
                histogramButton.setDisable(false);
                TreeItem<String > selectedTreeItem = newValue;
                String selectedValue = selectedTreeItem.getValue();
                lastChosenPropertyForHistogram = newValue.getValue();
                lastChosenEntityForHistogram = newValue.getParent().getValue();
                getConsistencyValue(newValue.getParent().getValue(), newValue.getValue());
                getAverageValue(newValue.getParent().getValue(), newValue.getValue());
            }
            else if (newValue !=null && !newValue.getChildren().isEmpty()){
                histogramButton.setDisable(true);
            }
        }));
    }

    private void getAverageValue(String entity, String property) {
        if (!engine.getPostRunData(lastSimulationNum).getAvPropertyValue().containsKey(entity+"_"+property)){
            return;
        }
        averageValueLabel.setText(engine.getPostRunData(lastSimulationNum).getAvPropertyValue().get(entity+"_"+property).getKey().toString());
    }

    private void displaySimulationResults() {

        fillResultsTreeView();
    }

    private void getConsistencyValue(String entity, String property) {

        if (!engine.getPostRunData(lastSimulationNum).getPropertyChangeByTick().containsKey(entity+"_"+property)){
            return;
        }
        Float sum = 0f;
        List<Float> consistencies = engine.getPostRunData(lastSimulationNum).getPropertyChangeByTick().get(entity+"_"+property);
        Float size = (float)consistencies.size();
        for (Float consistency : consistencies){
            sum+=consistency;
        }
        Float result = sum/size;
        consistencyValueLabel.setText(result.toString());
    }

    private void fillResultsTreeView() {

        DTOSimulationDetailsPostRun results = engine.getPostRunData(lastSimulationNum);
        TreeItem<String> rootItem = new TreeItem<>(new String("Entities"));

        for (DTOEntitysProperties entity: results.getEntitysProperties().values()){
            TreeItem<String> entityItem = new TreeItem<>(entity.getName());
            for (DTOProperty property :entity.getProperties()){
                entityItem.getChildren().add(new TreeItem<>(property.getName()));
            }
            rootItem.getChildren().add(entityItem);
        }
        resultsTreeView.setRoot(rootItem);
    }

    private LineChart createLastSimulationGraph() {

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Ticks");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("No.of entities");
        LineChart linechart = new LineChart(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        series.setName("Entities Per Tick");

        for (Pair <Integer, Integer> pair : engine.getPostRunData(lastSimulationNum).getNumOfEntitiesPerTick()){
            int tick = pair.getKey();
            int entityAmount = pair.getValue();
            series.getData().add(new XYChart.Data(tick, entityAmount));
        }

        linechart.getData().add(series);
        return linechart;
    }

    private BarChart createLastSimulationHistogram() {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> barChart = new BarChart<String,Number>(xAxis,yAxis);
        xAxis.setLabel("Property Value");
        yAxis.setLabel("Amount");

        XYChart.Series series1 = new XYChart.Series();
        for (Map.Entry<Object, Integer> property: engine.getPostRunData(lastSimulationNum).getHistogram(lastChosenEntityForHistogram, lastChosenPropertyForHistogram).entrySet()){
            series1.getData().add(new XYChart.Data(property.getKey().toString(),property.getValue()));
        }
        barChart.getData().addAll(series1);
        return barChart;
    }

    public void shutDownSystem(){
        engine.disposeOfThreadPool();
    }

    public void clearSimulation(){
        environmentVariableTableData.clear();
        entitiesTableData.clear();
        entitiesRunTablesData.clear();
        executionListViewData.clear();
        ticksValueLabel.setText("0");
        secondsValueLabel.setText("0");
        newTask = null;
        lastSimulationNum = 0;
        treeDetailsController.clearData();



    }

    public void setTreeViewComponentController(TreeViewController treeViewController) {
        this.treeViewController = treeViewController;
        treeViewController.setMainController(this);
        treeViewController.setAlert(alert);
    }

    public void setTreeDetailsComponentController(TreeDetailsController treeDetailsController) {
        this.treeDetailsController = treeDetailsController;
        treeDetailsController.setMainController(this);
    }
    @FXML
    void loadFile(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        loadedFilePathTextBox.setText(absolutePath);
        loadedFilePathTextBox.setDisable(true);
        System.out.println("get world ");
        try {
            treeViewController.displayFileDetails(engine, absolutePath, queueManagementData);
            fillEnvironmentVariableTable(engine);
            fillEntitiesTable(engine);
        }catch (Exception e){
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    private void fillEntitiesTable(Engine engine) {

        DTOSimulationDetails details = engine.getSimulationDetails();
        entitiesTableData.clear();
        for (DTOEntityData entity : details.getEntitysList()){
            entitiesTableData.add(new EntitiesTable(entity.getName(), "0"));
        }
    }

    public void fillEnvironmentVariableTable(Engine engine) {

        DTOSimulationDetails details = engine.getSimulationDetails();
        environmentVariableTableData.clear();
        for (DTOEnvironmentVariables environmentVariable : details.getEnvironmentVariables()){
            environmentVariableTableData.add(new EnvironmentVariableTable(environmentVariable.getVariableName() + "("+ environmentVariable.getVariableType()+")", "", environmentVariable.getVariableName()));
        }
    }

    public BorderPane getTreeViewComponent(){
        return treeViewComponent;
    }

    public void setStage (Stage i_primaryStage){
        this.primaryStage = i_primaryStage;
    }

    public void displayTreeItemsDetails(DTOSimulationDetailsItem selectedValue) {
        treeDetailsController.displayTreeItemsDetails(selectedValue);
    }

    public void startSimulation(ActionEvent actionEvent) {
        for (EnvironmentVariableTable environmentVariable : environmentVariableTableData){
            if (!environmentVariable.getValue().getText().isEmpty()){
                try{
                    engine.addEnvironmentVariableValue(environmentVariable.getEnvVarNameNoType(), environmentVariable.getValue().getText());
                }catch (Exception e){
                    alert.setContentText(e.getMessage());
                    alert.show();
                    System.out.println(e.getMessage());
                }
            }
        }

        for (EntitiesTable entity : entitiesTableData){
            if (!entity.getPopulation().getText().equals("0")){
                try{
                    engine.addPopulationToEntity(entity.getEntityName(), Integer.parseInt(entity.getPopulation().getText()));
                }catch (Exception e){
                    alert.setContentText(e.getMessage());
                    alert.show();
                    System.out.println(e.getMessage());
                }
            }
        }

        try{
            engine.setSimulation();
            simulationID = engine.activeSimulation(new myTask());
            executionListViewData.add(new ExecutionListItem(simulationID));
            if(isFirstSimulationForFile){
                isFirstSimulationForFile = false;
                engine.updateNewlyFinishedSimulationInLoop(executionListViewData);
            }

        }catch (Exception e){
            alert.setContentText(e.getMessage());
            alert.show();
            System.out.println(e.getMessage());
        }
    }

    public void clearSimulation(ActionEvent actionEvent) {

        for (EnvironmentVariableTable environmentVariable : environmentVariableTableData){
            environmentVariable.getValue().setText("");
        }

        for (EntitiesTable entity : entitiesTableData){
            entity.getPopulation().setText("0");
        }
    }

    public void rerun(ActionEvent actionEvent) {

        if (engine.getSimulationStatus(lastSimulationNum) != Status.FINISHED){
            return;
        }

        if(lastSimulationNum != 0) {
            entitiesTableData.clear();
            DTODataForReRun dataForReRun = engine.getDataForRerun(lastSimulationNum);
            if(dataForReRun == null){
                return;
            }
            for (String entity : dataForReRun.getEntitiesPopulation().keySet()) {
                entitiesTableData.add(new EntitiesTable(entity, dataForReRun.getEntitiesPopulation().get(entity).toString()));
                //entitiesTableData.add(new EntitiesTable(entity.getName(), "0"));
            }

            for(EnvironmentVariableTable environmentVariableTable : environmentVariableTableData){
                if(dataForReRun.getEnvironmentsValues().containsKey(environmentVariableTable.getEnvVarNameNoType())){
                    environmentVariableTable.setValueInText(dataForReRun.getEnvironmentsValues().get(environmentVariableTable.getEnvVarNameNoType()).toString());
                }else{
                    environmentVariableTable.setValueInText("");
                }
            }
        }
    }

    public void pauseSimulation(ActionEvent actionEvent) {
        nextButton.setDisable(false);
        pauseButton.setDisable(true);
        resumeSimulationButton.setDisable(false);
        graphicDisplayButton.setDisable(false);
        resultsGraphButton.setDisable(false);
        engine.pauseSimulation(lastSimulationNum);
        fillResultsTreeView();
    }

    public void stopSimulation(ActionEvent actionEvent) {
        nextButton.setDisable(true);
        engine.stopSimulation(lastSimulationNum);
    }

    public void resumeSimulation(ActionEvent actionEvent) {
        nextButton.setDisable(true);
        resumeSimulationButton.setDisable(true);
        pauseButton.setDisable(false);
        graphicDisplayButton.setDisable(true);
        resultsGraphButton.setDisable(true);
        engine.resumeSimulation(lastSimulationNum);
        resultsTreeView.setRoot(null);
        averageValueLabel.setText("");
        consistencyValueLabel.setText("");
        histogramButton.setDisable(true);
        graphicDisplayButton.setDisable(true);
        resultsGraphButton.setDisable(true);
    }

    public void showGraph(ActionEvent actionEvent) {

        lastSimulationGraph = createLastSimulationGraph();

        ScrollPane scrollPane = new ScrollPane(lastSimulationGraph);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        Stage stage = new Stage();
        stage.setTitle("Entities by Ticks Line Chart");
        Scene scene  = new Scene(scrollPane,650,450);
        stage.setScene(scene);
        stage.show();
    }

    public void showHistogram(ActionEvent actionEvent) {

        lastSimulationHistogram = createLastSimulationHistogram();
        ScrollPane scrollPane = new ScrollPane(lastSimulationHistogram);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        Stage stage = new Stage();
        Scene scene  = new Scene(scrollPane,650,450);
        stage.setScene(scene);
        stage.show();
    }

    public void nextSimulationStep(ActionEvent actionEvent) {
        engine.moveOneStep(lastSimulationNum);
        if (simulationSpace != null){
            createSimulationSpace(engine.getMap(lastSimulationNum));
        }
        consistencyValueLabel.setText("");
        averageValueLabel.setText("");
        fillResultsTreeView();
    }
    public void changeStyleHot(ActionEvent actionEvent) {
        DetailsTab.setStyle("-fx-background-color: yellow;");
        resultsTab.setStyle("-fx-background-color: red;");
        newExecutionTab.setStyle("-fx-background-color: orange;");
        mainHbox.setStyle("-fx-background-color: #f1ce47;");
        loadFileButton.setStyle("-fx-background-color: #ee7d0c;");
        detailsBorderPane.setStyle("-fx-background-color: #efba71;");
        statusColumn.setStyle("-fx-background-color: #efba71;");
        amountColumn.setStyle("-fx-background-color: #efba71;");
        environmentVarColumn.setStyle("-fx-background-color: #fc6b3c;");
        valueColumn.setStyle("-fx-background-color: #fc6b3c;");
        entityColumn.setStyle("-fx-background-color: #fc6b3c;");
        populationColumn.setStyle("-fx-background-color: #fc6b3c;");
        startSimulationButton.setStyle("-fx-background-color: #eac81d;");
        executionListView.setStyle("-fx-background-color: #fdc076;");
        entityRunColumn.setStyle("-fx-background-color: #FD7676FF;");
        populationRunColumn.setStyle("-fx-background-color: #FD7676FF;");
        resultsTreeView.setStyle("-fx-background-color: #B67878FF;");
        ticksLabel.setStyle("-fx-background-color: #ffde00;");
        secondsLabel.setStyle("-fx-background-color: #FF0000FF;");
        ticksValueLabel.setStyle("-fx-background-color: #ffde00;");
        secondsValueLabel.setStyle("-fx-background-color: #FF0000FF;");
        consistencyLabel.setStyle("-fx-background-color: #FF7300FF;");
        averageLabel.setStyle("-fx-background-color: #d97575;");
        consistencyValueLabel.setStyle("-fx-background-color: #FF7300FF;");
        averageValueLabel.setStyle("-fx-background-color: #d97575;");
        rerunButton.setStyle("-fx-background-color: #eac81d;");
        resumeSimulationButton.setStyle("-fx-background-color: #eac81d;");
        nextButton.setStyle("-fx-background-color: #fdc076;");
        histogramButton.setStyle("-fx-background-color: #ee7d0c;");
        resultsGraphButton.setStyle("-fx-background-color: #ee7d0c;");
        pauseButton.setStyle("-fx-background-color: #FF0000FF;");
        stopSimulationButton.setStyle("-fx-background-color: #FF0000FF;");
        clearSimulationButton.setStyle("-fx-background-color: #FF0000FF;");
        graphicDisplayButton.setStyle("-fx-background-color: #efba71;");
        queueManagementTable.refresh();
        environmentVarTable.refresh();
        entitiesTable.refresh();
        entitiesRunTable.refresh();
    }
    public void changeStyleCold(ActionEvent actionEvent) {
        DetailsTab.setStyle("-fx-background-color: #0BEAD0FF;");
        resultsTab.setStyle("-fx-background-color: #0B9CEAFF;");
        newExecutionTab.setStyle("-fx-background-color: #765EE1FF;");
        mainHbox.setStyle("-fx-background-color: #5EE197FF;");
        loadFileButton.setStyle("-fx-background-color: #094ee3;");
        detailsBorderPane.setStyle("-fx-background-color: #74EFA9FF;");
        statusColumn.setStyle("-fx-background-color: #0AD6F1FF;");
        amountColumn.setStyle("-fx-background-color: #0AD6F1FF;");
        environmentVarColumn.setStyle("-fx-background-color: #A777E5FF;");
        valueColumn.setStyle("-fx-background-color: #A777E5FF;");
        entityColumn.setStyle("-fx-background-color: #A777E5FF;");
        populationColumn.setStyle("-fx-background-color: #A777E5FF;");
        startSimulationButton.setStyle("-fx-background-color: #6193B2FF;");
        clearSimulationButton.setStyle("-fx-background-color: #8d77d9;");
        executionListView.setStyle("-fx-background-color: #1dea76;");
        entityRunColumn.setStyle("-fx-background-color: #4BA7E0FF;");
        populationRunColumn.setStyle("-fx-background-color: #4BA7E0FF;");
        resultsTreeView.setStyle("-fx-background-color: #7897b6;");
        ticksLabel.setStyle("-fx-background-color: #00d0ff;");
        secondsLabel.setStyle("-fx-background-color: #96b2bd;");
        ticksValueLabel.setStyle("-fx-background-color: #00d0ff;");
        secondsValueLabel.setStyle("-fx-background-color: #96b2bd;");
        consistencyLabel.setStyle("-fx-background-color: #9887b0;");
        averageLabel.setStyle("-fx-background-color: #c0afe7;");
        consistencyValueLabel.setStyle("-fx-background-color: #9887b0;");
        averageValueLabel.setStyle("-fx-background-color: #c0afe7;");
        rerunButton.setStyle("-fx-background-color: #1dea76;");
        resumeSimulationButton.setStyle("-fx-background-color: #1dea76;");
        nextButton.setStyle("-fx-background-color: #9c76fd;");
        histogramButton.setStyle("-fx-background-color: #00d0ff;");
        resultsGraphButton.setStyle("-fx-background-color: #00d0ff;");
        pauseButton.setStyle("-fx-background-color: #994de3;");
        stopSimulationButton.setStyle("-fx-background-color: #994de3;");
        graphicDisplayButton.setStyle("-fx-background-color: #96b2bd;");
        queueManagementTable.refresh();
        environmentVarTable.refresh();
        entitiesTable.refresh();
        entitiesRunTable.refresh();
    }

    public void showGraphicDisplay(ActionEvent actionEvent) {

        DTOMap graphicDisplay = engine.getMap(lastSimulationNum);
        graphicDisplayStage = new Stage();

        graphicDisplayStage.setTitle("Simulation Space");
        final NumberAxis xAxis = new NumberAxis(0, graphicDisplay.getCols(), 5);
        final NumberAxis yAxis = new NumberAxis(0, graphicDisplay.getRows(), 5);
        simulationSpace = new ScatterChart(xAxis,yAxis);

        createSimulationSpace(graphicDisplay);

        Scene scene  = new Scene(simulationSpace, 600, 500);
        graphicDisplayStage.setScene(scene);
        graphicDisplayStage.show();
    }

    private void createSimulationSpace(DTOMap graphicDisplay) {

        simulationSpace.getData().clear();
        double column =0;
        double row =0;
        List<DTOEntityData> entities = treeViewController.getEntities();
        for (DTOEntityData entity : entities) {
            XYChart.Series series = new XYChart.Series<>();
            series.setName(entity.getName());
            row = 0;
            column = 0;
            for (DTOMapSpace[] rows : graphicDisplay.getMap()) {
                for(DTOMapSpace columns : rows){
                    if (columns.getEntityName().equals(entity.getName())){
                        series.getData().add(new XYChart.Data<>(column, row));
                    }
                    column= column+1;
                }
                column=0;
                row= row+1;
            }
            simulationSpace.getData().addAll(series);
        }
    }
}
