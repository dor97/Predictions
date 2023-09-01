package Controller;
import DTO.*;
import Engine.Engine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private TextArea detailsTextArea;
    @FXML private TreeView<DTOSimulationDetailsItem> detailsTreeView;
    @FXML private Button loadFileButton;
    @FXML private TextField loadedFilePathTextBox;
    @FXML private TableColumn<?, ?> queueManagementTable;
    private Stage primaryStage;
    private Engine engine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        engine = new Engine();
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
;
        System.out.println("gat world ");


        try {
            engine.loadSimulation(absolutePath);
            DTOSimulationDetails details = engine.getSimulationDetails();
            TreeItem<DTOSimulationDetailsItem> rootItem = new TreeItem<>(details);
            System.out.println("adding item: ");
            for(DTOEntityData entityData : details.getEntitysList()){
                TreeItem<DTOSimulationDetailsItem> entityItem = new TreeItem<>(entityData);
                rootItem.getChildren().add(entityItem);
                for(DTOPropertyData propertyData : entityData.getPropertList()){
                    entityItem.getChildren().add(new TreeItem<>(propertyData));
                }
                System.out.println("Item: ");
            }
            for(DTORuleData ruleData : details.getRulesList()){
                TreeItem<DTOSimulationDetailsItem> ruleItem = new TreeItem<>(ruleData);
                rootItem.getChildren().add(ruleItem);
                for(DTOActionData actionData : ruleData.getActions()){
                    ruleItem.getChildren().add(new TreeItem<>(actionData));
                }
            }

            detailsTreeView.setRoot(rootItem);
        }catch (Exception e){
            System.out.println(e.getMessage());

        }





    }

    public void selectItem(){
        TreeItem<DTOSimulationDetailsItem> item = detailsTreeView.getSelectionModel().getSelectedItem();

        if(item != null){
            item.getValue().getData().ifPresent(putData -> putData(putData));
        }
    }

    private void putData(Map<String, String> data){
        for(String d : data.values()){
            System.out.println(d);
        }
    }

    public void setStage (Stage i_primaryStage){
        this.primaryStage = i_primaryStage;
    }


}
