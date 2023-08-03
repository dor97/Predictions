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
    //public static void main(String[] args) {
     //   World w = new World();
     //   w.start();
     //   w.startSimolesan();
      //  }
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "examples.jaxb.schema.generated";

    public static void main(String[] args) {
        try {
            InputStream inputStream = new FileInputStream(new File("src/resources/predictions-v1.xsd"));
            deserializeFrom(inputStream);
            //System.out.println("name of first country is: " + countries.getCountry().get(0).getName());
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static void deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        //return (Countries) u.unmarshal(in);
    }
    }
