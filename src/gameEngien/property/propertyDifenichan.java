package gameEngien.property;

import DTO.DTOEntityData;
import DTO.DTOPropertyData;
import DTO.DTOPropertyType;
import gameEngien.generated.PRDProperty;
import gameEngien.rule.action.increase.exprecn;
import gameEngien.rule.action.increase.exprecnType;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.io.Serializable;

public class propertyDifenichan implements Serializable {
    private String m_name;
    private exprecnType m_type;
    private double m_lowRange, m_highRang;
    private boolean m_randomlyIneceat;
    private exprecn m_init;
    private boolean haveRange = false;

    public propertyDifenichan(PRDProperty p) throws InvalidValue{
        m_name = p.getPRDName();
        m_type = myType(p.getType());
        if(p.getPRDRange() != null) {
            haveRange = true;
            m_lowRange = p.getPRDRange().getFrom();
            m_highRang = p.getPRDRange().getTo();
            if(m_highRang < m_lowRange){
                //excepcen
            }
        }
        m_randomlyIneceat = p.getPRDValue().isRandomInitialize();
        if(m_randomlyIneceat == true){
            return;
        }
        m_init = new exprecn();
        m_init.convertValueInString(p.getPRDValue().getInit());
        if(m_type != m_init.getType()){
            throw new InvalidValue("In property " + m_name + " got a wrong type value");
        }

    }

    public exprecnType getType(){
        return m_type;
    }

    public double getLowRange(){
        return m_lowRange;
    }

    public double getHighRange(){
        return m_highRang;
    }

    public exprecn getInit(){
        return m_init;
    }

    public boolean isRandom(){
        return m_randomlyIneceat;
    }

    public boolean haveRange(){
        return haveRange;
    }

    public String getName(){
        return m_name;
    }

    private exprecnType myType(String t){
        if(t.equals("decimal")){
            return exprecnType.INT; 
        } else if (t.equals("float")) {
            return exprecnType.FLOAT;
        } else if (t.equals("string")) {
            return exprecnType.STRING;
        } else if (t.equals("boolean")) {
            return exprecnType.BOOL;
        }
        else{
            return  null;
            //exepcen
        }
    }

    public DTOPropertyData makeDtoProperty(){
        DTOPropertyType type;
        if(m_type == exprecnType.INT) {
            type = DTOPropertyType.INT;
        } else if (m_type ==exprecnType.FLOAT) {
            type = DTOPropertyType.FLOAT;
        } else if (m_type == exprecnType.STRING) {
            type = DTOPropertyType.STRING;
        } else {
            type = DTOPropertyType.BOOL;
        }
        DTOPropertyData DTO;
        if(haveRange){
            DTO = new DTOPropertyData(m_name, type, haveRange, m_randomlyIneceat, m_highRang, m_lowRange);
        }
        else{
            DTO = new DTOPropertyData(m_name, type, haveRange, m_randomlyIneceat);
        }
        return DTO;
    }

}
