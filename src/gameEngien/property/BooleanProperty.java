package gameEngien.property;

import gameEngien.generated.PRDEnvProperty;
import gameEngien.property.propertyInterface.propertyType;
import gameEngien.rule.action.increase.exprecnType;

import java.io.Serializable;
import java.util.Random;

public class BooleanProperty extends Property implements Serializable {


    private String m_name;
    private boolean m_property;

    public BooleanProperty(propertyDifenichan propertyDifenichan) {
        super(propertyType.BOOL);
        m_name = propertyDifenichan.getName();
        if (propertyDifenichan.getType() != exprecnType.BOOL){
            //exception
        }
        if(propertyDifenichan.isRandom()){
            Random random = new Random();
            m_property = random.nextBoolean();
        }
        else{
            m_property = propertyDifenichan.getInit().getBool();
        }
    }

    public BooleanProperty(EnvironmentDifenichan environmentDifenichan){
        super(propertyType.BOOL);
        m_name = environmentDifenichan.getName();
        if (environmentDifenichan.getType() != exprecnType.BOOL){
            //exception
        }
        if(environmentDifenichan.isRandom()){
            Random random = new Random();
            m_property = random.nextBoolean();
        }
        else{
            m_property = environmentDifenichan.getInit().getBool();
        }
    }

    public BooleanProperty(PRDEnvProperty envProperty){
        super(propertyType.BOOL);
        m_name = envProperty.getPRDName();
        m_property = true;    //TODO get from user
    }

    @Override
    public Object getValue() {
        return m_property;
    }
    @Override
    public String getName() {
        return m_name;
    }
    @Override
    public void setProperty(boolean i_property) {
        m_property = i_property;
    }
}
