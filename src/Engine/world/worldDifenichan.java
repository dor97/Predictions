package Engine.world;

import Engine.InvalidValue;
import Engine.UnsupportedFileTypeException;
import Engine.allReadyExistsException;
import Engine.generated.PRDEntity;
import Engine.generated.PRDEnvProperty;
import Engine.generated.PRDRule;
import Engine.generated.PRDWorld;
import Engine.utilites.Utilites;
import Engine.world.entity.EntityDifenichan;
import Engine.world.entity.property.EnvironmentDifenichan;
import Engine.world.expression.expression;
import Engine.world.rule.Rule;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


public class worldDifenichan {

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "Engine.generated";


//    public worldDifenichan(String xmlFile)throws NoSuchFileException , UnsupportedFileTypeException, allReadyExistsException, InvalidValue, JAXBException, FileNotFoundException {
//        PRDWorld xmlWorld = new PRDWorld();
//
//        //load file
//        Path path = Paths.get(xmlFile);
//        if (Files.exists(path) && Files.isRegularFile(path)) {
//            // Check if the file extension is .xml
//            String fileName = path.getFileName().toString();
//            if (!fileName.endsWith(".xml")) {
//                throw new UnsupportedFileTypeException("Not an XML file: " + xmlFile);
//            }
//        } else {
//            throw new NoSuchFileException("File does not exist: " + xmlFile);
//        }
//
//        try {
//            InputStream inputStream = new FileInputStream(new File(xmlFile));
//            xmlWorld = deserializeFrom(inputStream);
//        } catch (JAXBException | FileNotFoundException e) {
//            throw e;
//        }
//
//        m_cols = xmlWorld.getPRDGrid().getColumns();
//        m_rows = xmlWorld.getPRDGrid().getRows();
//
//        //entitys
//        for(PRDEntity e : xmlWorld.getPRDEntities().getPRDEntity()){
//            m_entitiesDifenichan.put(e.getName(), new EntityDifenichan(e));
//        }
//        //environment
//        for(PRDEnvProperty p : xmlWorld.getPRDEnvironment().getPRDEnvProperty()){
//            if(m_environmentsDifenichen.containsKey(p.getPRDName())){
//                throw new allReadyExistsException("environment variables " + p.getPRDName() + " all ready exists.");
//            }
//            m_environmentsDifenichen.put(p.getPRDName(), new EnvironmentDifenichan(p));
//        }
//
//        //environment values
////        for(PRDEnvProperty envProperty : xmlWorld.getPRDEvironment().getPRDEnvProperty()){
////            if(m_environments.containsKey(envProperty.getPRDName())){
////                throw new allReadyExistsException("enviroments varuble " + envProperty.getPRDName() + " all ready exists");
////            }
////            if(envProperty.getType().equals("decimal")) {
////                m_environments.put(envProperty.getPRDName(), new DecimalProperty(envProperty));
////            } else if(envProperty.getType().equals("float")){
////                m_environments.put(envProperty.getPRDName(), new FloatProperty(envProperty));
////            } else if (envProperty.getType().equals("string")) {
////                m_environments.put(envProperty.getPRDName(), new StringProperty(envProperty));
////            } else if (envProperty.getType().equals("boolean")) {
////                m_environments.put(envProperty.getPRDName(), new BooleanProperty(envProperty));
////            }
////        }
//
//        util = new Utilites(m_environments, m_entitiesDifenichan, m_environmentsDifenichen, m_entities);
//        //util.Init(m_environments, m_entitiesDifenichan, m_environmentsDifenichen);
//
//        //rules
//        for(PRDRule rule : xmlWorld.getPRDRules().getPRDRule()){
//            m_rules.add(new Rule(rule, util));
//        }
//
//        m_ticks = new expression();
//        m_secondToWork = new expression();
//        Optional<List<Object>> secondOrTicks = Optional.ofNullable((List<Object>) xmlWorld.getPRDTermination().getPRDBySecondOrPRDByTicks());
//        secondOrTicks.ifPresent(t -> getTermination(t));
//
////        m_ticks = new expression();
////        Optional<Integer> time = Optional.ofNullable().ifPresent(t -> t.getCount()))));
////        time.ifPresent((t) -> m_ticks.setValue(t));
////
////        m_secondToWork = new expression();
////        Optional<Integer> second = Optional.ofNullable(((PRDBySecond)xmlWorld.getPRDTermination().getPRDBySecondOrPRDByTicks().get(1)).getCount());
////        second.ifPresent((s) -> m_secondToWork.setValue(s));
//
//        if(m_ticks.getType() == null && m_secondToWork.getType() == null && xmlWorld.getPRDTermination().getPRDByUser() == null){
//            throw new InvalidValue("No termination method was added");
//        }
//
//        return xmlWorld.getPRDThreadCount();
//    }
//
//    private static PRDWorld deserializeFrom(InputStream in) throws JAXBException {
//        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
//        Unmarshaller u = jc.createUnmarshaller();
//        return (PRDWorld) u.unmarshal(in);
//    }
}
