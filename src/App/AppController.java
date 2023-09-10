package App;
import DTO.*;
import Engine.Engine;
import TreeDetails.TreeDetailsController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
    private Stage primaryStage;
    private Engine engine;
    private ObservableList<EnvironmentVariableTable> environmentVariableTableData = FXCollections.observableArrayList();
    private ObservableList<EntitiesTable> entitiesTableData = FXCollections.observableArrayList();
    private int simulationID;


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

        environmentVarTable.setItems(environmentVariableTableData);
        entitiesTable.setItems(entitiesTableData);
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
        for (DTOEntityData entity : details.getEntitysList()){
            entitiesTable.getItems().add(new EntitiesTable(entity.getName(), "0"));
        }

        entitiesTable.refresh();
    }

    public void fillEnvironmentVariableTable(Engine engine) {

        DTOSimulationDetails details = engine.getSimulationDetails();

        for (DTOEnvironmentVariables environmentVariable : details.getEnvironmentVariables()){
            environmentVarTable.getItems().add(new EnvironmentVariableTable(environmentVariable.getVariableName()+"("+ environmentVariable.getVariableType()+")", ""));
        }
        environmentVarTable.refresh();
    }

    public BorderPane getTreeViewComponent(){
        return treeViewComponent;
    }

    public BorderPane getTreeDetailsComponent(){return treeDetailsComponent;}

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
                    engine.addEnvironmentVariableValue(environmentVariable.getEnvVarName(), environmentVariable.getValue().getText());
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
}
