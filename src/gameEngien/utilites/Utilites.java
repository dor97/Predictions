package gameEngien.utilites;

import gameEngien.entity.Entity;
import gameEngien.property.propertyInterface.PropertyInterface;

import java.util.*;

public class Utilites {
    //private static List<Entity> m_entities;
    private static Map<String, PropertyInterface> m_environments;


    public static void Init(Map<String, PropertyInterface> environments){
        m_environments = environments;
    }

    public static Object environment(String name) {
        return m_environments.get(name).getValue();
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
}
