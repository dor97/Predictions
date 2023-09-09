package App;
import DTO.*;
import Engine.Engine;
import SimulationDetailsTable.SimulationDetailsTableController;
import TreeDetails.TreeDetailsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import TreeView.TreeViewController;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    public HBox newExeHbox;
    public Button runSimulationButton;
    @FXML private BorderPane treeViewComponent;
    @FXML private BorderPane treeDetailsComponent;
    @FXML private SimulationDetailsTableController simulationDetailsTableController;
    @FXML private TableView<?> environmentVariablesTable;
    @FXML private TableView<?> entitiesTable;
    @FXML private TreeDetailsController treeDetailsController;
    @FXML private TreeViewController treeViewController;
    @FXML private TreeView<DTOSimulationDetailsItem> detailsTreeView;
    @FXML private TextField loadedFilePathTextBox;
    private Stage primaryStage;
    private Engine engine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (treeViewController != null && treeDetailsController != null && simulationDetailsTableController!=null) {
            treeViewController.setMainController(this);
            treeDetailsController.setMainController(this);
            simulationDetailsTableController.setMainController(this);
        }
        engine = new Engine();
    }

    public void setTreeViewComponentController(TreeViewController treeViewController) {
        this.treeViewController = treeViewController;
        treeViewController.setMainController(this);
    }

    public void setTreeDetailsComponentController(TreeDetailsController treeDetailsController) {
        this.treeDetailsController = treeDetailsController;
        treeDetailsController.setMainController(this);
    }

    public void setSimulationDetailsTableController(SimulationDetailsTableController simulationDetailsTableController) {
        this.simulationDetailsTableController = simulationDetailsTableController;
        simulationDetailsTableController.setMainController(this);
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
        simulationDetailsTableController.updateTablesWithDetails(engine);
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

    public void runSimulationListener(ActionEvent actionEvent) {
        
    }
}
