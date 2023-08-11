package gameEngien;

import gameEngien.world.World;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.List;

public interface gameEngineInterface {

    public void lodSimuletion(String fileName) throws NoSuchFileException, UnsupportedFileTypeException, InvalidValue, allReadyExistsException , JAXBException, FileNotFoundException;
    public void activeSimultion()throws InvalidValue;
    public void saveSystemState();
    public void loadSystemState();
}
