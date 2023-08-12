import gameEngien.gameEngine;
import gameEngien.world.World;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        String fileName = "src/resources/ex1-cigarets.xml";
        gameEngine g = new gameEngine();
        try {
            if (true) {
                g.loadSimulation(fileName);
                g.activeSimulation();
                g.saveSystemState("serializedObject");
            } else {
                g.loadSystemState("serializedObject");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }


        //World w = new World();
        //w.loadFile();
        //w.setSimulation();
        //w.startSimolesan();
    }

}
