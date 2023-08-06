package gameEngien.utilites;

import gameEngien.entity.Entity;
import gameEngien.entity.EntityDifenichan;
import gameEngien.property.propertyInterface.PropertyInterface;

import java.util.*;

public class Utilites {
    //private static List<Entity> m_entities;
    private static Map<String, PropertyInterface> m_environments;
    private static Map<String, EntityDifenichan> m_entityDifenichan;


    public static void Init(Map<String, PropertyInterface> environments, Map<String, EntityDifenichan> entityDifenichan){
        m_environments = environments;
        m_entityDifenichan = entityDifenichan;
    }

    public static Object environment(String name) {
        return m_environments.get(name).getValue();
    }

    public static boolean isEnvironmentExist(String name) {
        return m_environments.get(name).getValue() != null ? true : false;
    }

    public static int random(int num) {
        Random random = new Random();
        return random.nextInt(num);
    }
    public static int random(String numString) {
        Random random = new Random();
        int num = 1;
        try {
            num = Integer.parseInt(numString);
        } catch (NumberFormatException e) {
            // Not an int
        }
        return random.nextInt(num);
    }

    public static EntityDifenichan getEntityDifenichan(String name){
        return m_entityDifenichan.get(name);
    }

    public static boolean isEntityDifenichanExists(String name){
        return m_entityDifenichan.containsKey(name);
    }

}
