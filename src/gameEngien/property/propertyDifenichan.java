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
        if(p.getPRDRange() != null) {
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

}
