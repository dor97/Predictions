package UI.ConsoleUI;



import Engine.myTask;
import javafx.application.Application;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.stage.Stage;

public class Main extends Application {

    public static MapProperty<String, Integer> name = new SimpleMapProperty<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        myTask task = new myTask();

        name.set(new SimpleMapProperty<>());

        // Bind name property to the task's valueProperty
        name.bind(task.valueProperty());

        // Set up a listener to print updates to the console
//        task.messageProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("Message: " + newValue);
//        });

        task.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Message: " + newValue);
        });

//        task.tickProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("tick: " + newValue);
//        });

        task.secProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("sec: " + newValue);
        });

        // Start the task
        ConsoleUI c = new ConsoleUI(task);
        c.start();

        // Your application can continue to run and respond to user interactions
        // without the need for a busy-wait loop.
    }
}

//    public static void doSomething(myTask task){
//        try {
//            task.call();
//        }catch (Exception e){
//
//        }
//    }
//
//    public static void fun(ObservableValue<?> observableValue, String old, String newValue){
//        name.set(newValue);
//    }
//}
//















//public class Main {
//
//    public static void main(String[] args) {
//        ConsoleUI consoleUI = new ConsoleUI();
//        consoleUI.start();
//    }
//}

