package gameEngien.property;

import gameEngien.generated.PRDEnvProperty;
import gameEngien.property.propertyInterface.propertyType;
import gameEngien.rule.action.increase.exprecnType;

import java.io.Serializable;

public class StringProperty extends Property implements Serializable {

    private String m_name;
    private String m_property;

    public StringProperty(propertyDifenichan propertyDifenichan) {
        super(propertyType.STRING);
        m_name = propertyDifenichan.getName();
        if (propertyDifenichan.getType() != exprecnType.STRING){
            //exception
        }
        m_property = propertyDifenichan.getInit().getString();
    }

    public StringProperty(PRDEnvProperty envProperty){
        super(propertyType.FLOAT);
        m_name = envProperty.getPRDName();
        m_property = "";    //TODO get from user
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
    public void setProperty(String i_property) {
        m_property = i_property;
    }
}
