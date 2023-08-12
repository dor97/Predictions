package gameEngien.entity;

import DTO.DTOEntityData;
import DTO.DTOEntityHistogram;
import DTO.DTOPropertyHistogram;
import DTO.DTOPropertyType;
import gameEngien.property.*;
import gameEngien.property.propertyInterface.PropertyInterface;
import gameEngien.property.propertyInterface.propertyType;
import gameEngien.rule.action.increase.exprecnType;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Entity implements Serializable {
    private String m_name;
    private Map<String, PropertyInterface> m_propertys;


    public Entity(EntityDifenichan entity) throws InvalidValue {
        m_name = entity.getName();
        m_propertys = new HashMap<>();
        try {
            for (propertyDifenichan propertyDifenichan : entity.getPropertys().values()) {
                if (propertyDifenichan.getType() == exprecnType.INT) {
                    m_propertys.put(propertyDifenichan.getName(), new DecimalProperty(propertyDifenichan));
                } else if (propertyDifenichan.getType() == exprecnType.FLOAT) {
                    m_propertys.put(propertyDifenichan.getName(), new FloatProperty(propertyDifenichan));
                } else if (propertyDifenichan.getType() == exprecnType.STRING) {
                    m_propertys.put(propertyDifenichan.getName(), new StringProperty(propertyDifenichan));
                } else if (propertyDifenichan.getType() == exprecnType.BOOL) {
                    m_propertys.put(propertyDifenichan.getName(), new BooleanProperty(propertyDifenichan));
                }
            }
        }
        catch (InvalidValue e){
            throw new InvalidValue(e.getMessage() + ". referred in entity " + m_name);
        }
    }

    public Entity(String name){ //delete
        m_name = name;
    }
    public PropertyInterface getProperty(String name){
        if(m_propertys.containsKey(name)){
            return m_propertys.get(name);
        }
        return null;
    }

    public String getName(){
        return m_name;
    }

    public boolean isPropertyExists(String name){
        return m_propertys.containsKey(name);
    }

    public void addProperty(PropertyInterface propertyToAdd){
        m_propertys.put(propertyToAdd.getName(), propertyToAdd);
    }

    public DTOEntityHistogram makeDtoEntity(){
        DTOEntityHistogram DTO = new DTOEntityHistogram(m_name);

        for(PropertyInterface property : m_propertys.values()){
            DTO.addProperty(new DTOPropertyHistogram(property.getName(), property.getValue(), getDTOPropertyType(property)));
        }

        return DTO;
    }

    private DTOPropertyType getDTOPropertyType(PropertyInterface propertyInterface){
        DTOPropertyType type;

        if(propertyInterface.getType() == propertyType.INT) {
            type = DTOPropertyType.INT;
        } else if (propertyInterface.getType() ==propertyType.FLOAT) {
            type = DTOPropertyType.FLOAT;
        } else if (propertyInterface.getType() == propertyType.STRING) {
            type = DTOPropertyType.STRING;
        } else {
            type = DTOPropertyType.BOOL;
        }

        return type;
    }

    //public void actionOnProperty(PropertyInterface p){
    //    p.addToProperty();
    //}
}
