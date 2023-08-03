package gameEngien.utilites;

import gameEngien.entity.Entity;
import gameEngien.property.propertyInterface.PropertyInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Utilites {
    //private static List<Entity> m_entities;
    private static Map<String, PropertyInterface> m_environments;


    public static void Init(Map<String, PropertyInterface> environments){
        m_environments = environments;
    }

    public static Object environment(String name) {
        return m_environments.get(name);
    }
}
