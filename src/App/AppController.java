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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import TreeView.TreeViewController;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import Engine.myTask;

public class AppController implements Initializable {
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
    @FXML private TableColumn<String, Integer> queueManagementTable;
    @FXML private TextArea exceptionArea;
    private Stage primaryStage;
    private Engine engine;
    private ObservableList<EnvironmentVariableTable> environmentVariableTableData = FXCollections.observableArrayList();
    private ObservableList<EntitiesTable> entitiesTableData = FXCollections.observableArrayList();
    private ObservableList<EntitiesRunTable> entitiesRunTablesData = FXCollections.observableArrayList();
    private ObservableList<ExecutionListItem> executionListViewData = FXCollections.observableArrayList();
    private int simulationID;
    private myTask newTask = null;
    private Integer lastSimulationNum = 0;
    private Boolean isFirstSimulationForFile = true;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (treeViewController != null && treeDetailsController != null) {
            treeViewController.setMainController(this);
            treeDetailsController.setMainController(this);
        }
        engine = new Engine();

        environmentVarColumn.setCellValueFactory(new PropertyValueFactory<>("envVarNameColumn"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        entityColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        populationColumn.setCellValueFactory(new PropertyValueFactory<>("population"));
        entityRunColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        populationRunColumn.setCellValueFactory(new PropertyValueFactory<>("population"));

        environmentVarTable.setItems(environmentVariableTableData);
        entitiesTable.setItems(entitiesTableData);
        entitiesRunTable.setItems(entitiesRunTablesData);
        executionListView.setItems(executionListViewData);

        executionListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue !=null){
                ExecutionListItem selectedListItem = newValue;
                Integer selectedValue = selectedListItem.getID();
                if(newTask != null){
                    engine.stopGettingDataUsingTask(newTask, lastSimulationNum);
                }
                lastSimulationNum = selectedValue;
                newTask = new myTask();
                exceptionArea.promptTextProperty().bind(newTask.messageProperty());
                newTask.bindProperties(ticksValueLabel.textProperty(), secondsValueLabel.textProperty(), entitiesRunTablesData);
                engine.getDataUsingTask(newTask, selectedValue);
                if (engine.getSimulationStatus(selectedValue) == Status.FINISHED){
                    displaySimulationResults(engine);
                }

            }
        }));
    }

    private void displaySimulationResults(Engine engine) {
        
    }

    public void clearSimulation(){
        environmentVariableTableData.clear();
        entitiesTableData.clear();
        entitiesRunTablesData.clear();
        executionListViewData.clear();
        //ticksValueLabel.setText("0");
        //secondsValueLabel.setText("0");
        newTask = null;
        lastSimulationNum = 0;
        treeDetailsController.clearData();



    }

    public void setTreeViewComponentController(TreeViewController treeViewController) {
        this.treeViewController = treeViewController;
        treeViewController.setMainController(this);
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

        treeViewController.displayFileDetails(engine, absolutePath);
        fillEnvironmentVariableTable(engine);
        fillEntitiesTable(engine);
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
                    System.out.println(e.getMessage());
                }
            }
        }

        for (EntitiesTable entity : entitiesTableData){
            if (!entity.getPopulation().getText().equals("0")){
                try{
                    engine.addPopulationToEntity(entity.getEntityName(), Integer.parseInt(entity.getPopulation().getText()));
                }catch (Exception e){
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
}
