package DTO;

import java.util.Map;
import java.util.Optional;

public class DTOSimulationDetailsItem implements DTOSimulationDetailsItemInterface{
    @Override
    public Optional<Map<String, String>> getData() {
        return Optional.empty();
    }
}
