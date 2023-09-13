package App;
import DTO.*;
import Engine.Engine;
import Engine.Status;
import TreeDetails.TreeDetailsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import TreeView.TreeViewController;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import Engine.myTask;
import javafx.util.Pair;

public class AppController implements Initializable {
    @FXML private TableColumn<QueueManagement, String> statusColumn;
    @FXML private TableColumn<QueueManagement, Integer> amountColumn;
    @FXML private TableView<QueueManagement> queueManagementTable;
    @FXML private TreeView<String> resultsTreeView;
    @FXML private Button resultsGraphButton;
    @FXML private Button histogramButton;
    @FXML private Label consistencyValueLabel;
    @FXML private Label averageValueLabel;
    @FXML private Button pauseButton;
    @FXML private GridPane resultsTab;
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
    @FXML private BorderPane treeDetailsComponent;
    @FXML private TreeDetailsController treeDetailsController;
    @FXML private TreeViewController treeViewController;
    @FXML private TreeView<DTOSimulationDetailsItem> detailsTreeView;
    @FXML private TextField loadedFilePathTextBox;
    @FXML private TextArea exceptionArea;
    private Stage primaryStage;
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

    private Alert alert = new Alert(Alert.AlertType.ERROR);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alert.setTitle("Error");
        alert.setWidth(1000);
        alert.setHeight(1000);
        if (treeViewController != null && treeDetailsController != null) {
            treeViewController.setMainController(this);
            treeDetailsController.setMainController(this);
            treeViewController.setAlert(alert);
        }
        engine = new Engine();
        resultsGraphButton.setDisable(true);
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

        executionListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue !=null){
                ticksValueLabel.setText("0");
                secondsValueLabel.setText("0");
                entitiesRunTablesData.clear();
                ExecutionListItem selectedListItem = newValue;
                Integer selectedValue = selectedListItem.getID();
                if(newTask != null){
                    engine.stopGettingDataUsingTask(newTask, lastSimulationNum);
                }
                lastSimulationNum = selectedValue;

                if (engine.getSimulationStatus(lastSimulationNum) != Status.FINISHED){
                    rerunButton.setDisable(true);
                }
                newTask = new myTask();
                //exceptionArea.promptTextProperty().bind(newTask.messageProperty());
                resultsGraphButton.setDisable(true);
                newTask.bindProperties(ticksValueLabel.textProperty(), secondsValueLabel.textProperty(), exceptionArea.promptTextProperty(), entitiesRunTablesData);
                engine.getDataUsingTask(newTask, selectedValue);
                if (engine.getSimulationStatus(selectedValue) == Status.FINISHED){
                    resultsGraphButton.setDisable(false);
                    displaySimulationResults(engine);
                }
            }
        }));
        resultsTreeView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue !=null && newValue.getChildren().isEmpty()){
                TreeItem<String > selectedTreeItem = newValue;
                String selectedValue = selectedTreeItem.getValue();
                getConsistencyValue(newValue.getParent().getValue(), newValue.getValue());
                getAverageValue(newValue.getParent().getValue(), newValue.getValue());
            }
        }));
    }

    private void getAverageValue(String entity, String property) {
        if (!engine.getPostRunData(lastSimulationNum).getAvPropertyValue().containsKey(entity+"_"+property)){
            return;
        }
        averageValueLabel.setText(engine.getPostRunData(lastSimulationNum).getAvPropertyValue().get(entity+"_"+property).getKey().toString());
    }

    private void displaySimulationResults(Engine engine) {

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
            treeViewController.displayFileDetails(engine, absolutePath);
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
        engine.pauseSimulation(lastSimulationNum);
    }

    public void stopSimulation(ActionEvent actionEvent) {
        engine.stopSimulation(lastSimulationNum);
    }

    public void resumeSimulation(ActionEvent actionEvent) {
        engine.resumeSimulation(lastSimulationNum);
    }

    public void showGraph(ActionEvent actionEvent) {

        lastSimulationGraph = createLastSimulationGraph();
        Stage stage = new Stage();
        stage.setTitle("Entities by Ticks Line Chart");
        Scene scene  = new Scene(lastSimulationGraph,800,600);
        stage.setScene(scene);
        stage.show();
    }

    public void showHistogram(ActionEvent actionEvent) {

    }
}
