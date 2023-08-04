package gameEngien.property;

import gameEngien.rule.action.increase.exprecnType;

public class BooleanProperty extends Property{


    private String m_name;
    private boolean m_property;

    public BooleanProperty(propertyDifenichan propertyDifenichan) {
        m_name = propertyDifenichan.getName();
        if (propertyDifenichan.getType() != exprecnType.BOOL){
            //exception
        }
        m_property = propertyDifenichan.getInit().getBool();
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
