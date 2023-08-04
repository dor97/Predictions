package gameEngien.property;

import gameEngien.rule.action.increase.exprecnType;

public class StringProperty extends Property{

    private String m_name;
    private String m_property;

    public StringProperty(propertyDifenichan propertyDifenichan) {
        m_name = propertyDifenichan.getName();
        if (propertyDifenichan.getType() != exprecnType.STRING){
            //exception
        }
        m_property = propertyDifenichan.getInit().getString();
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
