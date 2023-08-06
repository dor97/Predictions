package gameEngien;

import gameEngien.world.World;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

public class gameEngine {
    List<World> worldsList = new ArrayList<>();
    World cuurentSimuletion;
    int simulationNum = 0;

    public void lodSimuletion() throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException {
        cuurentSimuletion = new World();
        cuurentSimuletion.loadFile();
        cuurentSimuletion.setSimulation();
        worldsList.add(cuurentSimuletion);
    }

    public void activeSimultion(){
        cuurentSimuletion.startSimolesan();
    }

    public void saveSystemState(){
        try (FileOutputStream fileOut = new FileOutputStream("serializedObject.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(worldsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSystemState(){
        try (FileInputStream fileIn = new FileInputStream("serializedObject.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            worldsList = (List<World>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
