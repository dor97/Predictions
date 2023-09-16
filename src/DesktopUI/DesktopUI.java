package DesktopUI;

import TreeDetails.TreeDetailsController;
import TreeView.TreeViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import App.AppController;

import java.net.URL;

public class DesktopUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/TreeView/TreeView.fxml");
        fxmlLoader.setLocation(url);
        BorderPane treeViewComponent = fxmlLoader.load(url.openStream());
        TreeViewController treeViewController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/TreeDetails/TreeDetails.fxml");
        fxmlLoader.setLocation(url);
        BorderPane treeDetailsComponent = fxmlLoader.load(url.openStream());
        TreeDetailsController treeDetailsController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/resources/javaFXproject.fxml");
        fxmlLoader.setLocation(mainFXML);
        Parent root = fxmlLoader.load();
        AppController appController = fxmlLoader.getController();

        appController.setTreeViewComponentController(treeViewController);
        appController.setTreeDetailsComponentController(treeDetailsController);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        Scene scene = new Scene(scrollPane, 605 , 425);
        primaryStage.setScene(scene);
        appController.setStage(primaryStage);
        primaryStage.setTitle("Predictions");
        primaryStage.show();
        appController.shutDownSystem();
    }

    public static void main(String[] args){
        launch(args);
    }
}
