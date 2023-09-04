package App;
import DTO.*;
import Engine.Engine;
import TreeDetails.TreeDetailsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import TreeView.TreeViewController;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    @FXML private BorderPane treeViewComponent;
    @FXML private BorderPane treeDetailsComponent;
    @FXML private TreeDetailsController treeDetailsController;
    @FXML private TreeViewController treeViewController;
    @FXML private TreeView<DTOSimulationDetailsItem> detailsTreeView;
    @FXML private TextField loadedFilePathTextBox;
    private Stage primaryStage;
    private Engine engine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (treeViewController != null && treeDetailsController != null) {
            treeViewController.setMainController(this);
            treeDetailsController.setMainController(this);
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
}
