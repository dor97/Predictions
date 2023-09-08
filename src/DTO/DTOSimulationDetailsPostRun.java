package DTO;

import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class DTOSimulationDetailsPostRun {
    private List<DTOEntityPostRun> m_entitiesPostRuns = new ArrayList<>();
    private Map<String, List<DTOEntityHistogram>> m_entitiesHistogram = new HashMap<>();

    private Map<String, DTOEntitysProperties> m_entitysProperties = new HashMap<>();

    private Map<String, List<Float>> propertyChangeByTick = new HashMap<>();
    private Map<String, Pair<Float, Integer>> avPropertyValue = new HashMap<>();
    private List<Pair<Integer, Integer>> numOfEntitiesPerTick = new ArrayList<>();

    public void setPropertyChangeByTick(Map<String, List<Float>> propertyChangeByTick){
        this.propertyChangeByTick = propertyChangeByTick;
    }

    public void setAvPropertyValue(Map<String, Pair<Float, Integer>> avPropertyValue){
        this.avPropertyValue = avPropertyValue;
    }

    public void setNumOfEntitiesPerTick(List<Pair<Integer, Integer>> numOfEntitiesPerTick){
        this.numOfEntitiesPerTick = numOfEntitiesPerTick;
    }

    public Map<String, List<Float>> getPropertyChangeByTick(){
        return propertyChangeByTick;
    }

    public Map<String, Pair<Float, Integer>> getAvPropertyValue(){
        return avPropertyValue;
    }

    public List<Pair<Integer, Integer>> getNumOfEntitiesPerTick(){
        return numOfEntitiesPerTick;
    }

    public void addEntityPostRun(DTOEntityPostRun entityPostRun){
        m_entitiesPostRuns.add(entityPostRun);
    }

    public void addEntitiesHistigram(DTOEntityHistogram entityHistogram){
        if(!m_entitiesHistogram.containsKey(entityHistogram.getName())){
            List<DTOEntityHistogram> temp = new ArrayList<>();
            temp.add(entityHistogram);
            m_entitiesHistogram.put(entityHistogram.getName(), temp);
        }
        else{
            List<DTOEntityHistogram> temp = m_entitiesHistogram.get(entityHistogram.getName());
            temp.add(entityHistogram);
            //entitiesHistogram.put(entityHistogram.getName(), temp);    // ???
        }
    }

    public void setEntitiesPostRuns(List<DTOEntityPostRun> entitiesPostRuns){
        m_entitiesPostRuns = entitiesPostRuns;
    }

    public void setEntitysProperties(Map<String, DTOEntitysProperties> entitysProperties){
        m_entitysProperties = entitysProperties;
    }

    public void setEntitiesHistogram(Map<String, List<DTOEntityHistogram>> entitiesHistogram){
        m_entitiesHistogram = entitiesHistogram;
    }

    public Map<Object, Integer> getHistogram(String entity, String property){
//        Map<Object, Integer> histogram = new HashMap<>();
//
//        for(DTOEntityHistogram entityHistogram : entitiesHistogram.get(entity)){
//            if(!histogram.containsKey(entityHistogram.getProperty(property).getValue())){
//                histogram.put(entityHistogram.getProperty(property).getValue(), 1);
//            }
//            else{
//                histogram.put(entityHistogram.getProperty(property).getValue(), histogram.get(entityHistogram.getProperty(property).getValue()) + 1);
//            }
//        }
//        return histogram;
        return m_entitiesHistogram.get(entity).stream().collect(Collectors.groupingBy(entityHistogram -> entityHistogram.getProperty(property).getValue(), Collectors.summingInt(e -> 1)));   //Collectors.counting()

    }

    public List<DTOEntityPostRun> getEntitiesPostRun(){
        return m_entitiesPostRuns;
    }

    public Map<String, DTOEntitysProperties> getEntitysProperties(){
        return m_entitysProperties;
    }
}
