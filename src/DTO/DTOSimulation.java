package DTO;

import java.util.Map;

public class DTOSimulation {
    Map<Integer, String> m_simulations;

    public DTOSimulation(Map<Integer, String> simulation){
        m_simulations = simulation;
    }

    public Map<Integer, String> getSimulations(){
        return m_simulations;
    }
}
