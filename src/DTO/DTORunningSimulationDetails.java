package DTO;

import javafx.beans.property.MapProperty;
import javafx.collections.ObservableMap;

import java.util.Map;

public class DTORunningSimulationDetails {
    private MapProperty<String, Integer> entities;
    private Integer tick;
    private Long time;

    public void setEntities(MapProperty<String, Integer> entities){
        this.entities = entities;
    }

    public void setTick(Integer tick){
        this.tick = tick;
    }

    public void setTime(Long time){
        this.time = time;
    }

    public MapProperty<String, Integer> getEntities(){
        return entities;
    }

    public Integer getTick(){
        return tick;
    }

    public Long getTime(){
        return time;
    }

}
