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
        try {
            gameEngine g = new gameEngine();
            if (false) {
                g.lodSimuletion(fileName);
                g.activeSimultion();
                g.saveSystemState();
            } else {
                g.loadSystemState();
            }
        }
        catch (Exception e){

        }


        //World w = new World();
        //w.loadFile();
        //w.setSimulation();
        //w.startSimolesan();
    }

}
