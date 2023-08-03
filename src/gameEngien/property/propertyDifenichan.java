package gameEngien.property;

import gameEngien.generated.PRDProperty;
import gameEngien.rule.action.increase.exprecn;
import gameEngien.rule.action.increase.exprecnType;

public class propertyDifenichan {
    private String m_name;
    private exprecnType m_type;
    private double m_lowRange, m_highRang;
    private boolean m_randomlyIneceat;
    private exprecn m_init;

    public propertyDifenichan(PRDProperty p){
        m_name = p.getPRDName();
        m_type = myType(p.getType());
        m_lowRange = p.getPRDRange().getFrom();
        m_highRang = p.getPRDRange().getTo();
        m_randomlyIneceat = p.getPRDValue().isRandomInitialize();
        m_init.convertValueInString(p.getPRDValue().getInit());
        if(m_type != m_init.getType()){
            //exepcen
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

    public String getName(){
        return m_name;
    }

    private exprecnType myType(String t){
        if(t == "decimal"){
            return exprecnType.INT; 
        } else if (t == "float") {
            return exprecnType.FLOAT;
        } else if (t == "string") {
            return exprecnType.STRING;
        } else if (t == "boolean") {
            return exprecnType.BOOL;
        }
        else{
            return  null;
            //exepcen
        }
    }

}
