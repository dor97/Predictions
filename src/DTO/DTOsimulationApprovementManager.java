package DTO;



public class DTOsimulationApprovementManager {
    private approvementStatus status;
    private String simulationName;
    private String userName;
    private Integer amountToRun;
    private Integer ticks;
    private Integer sec;
    private Integer id;
    private Integer amountRun;
    private Integer currentRun;
    private Integer done;

    public DTOsimulationApprovementManager(String simulationName, String userName, approvementStatus status, Integer amountToRun, Integer  amountRun, Integer ticks, Integer sec, Integer id, Integer currentRun, Integer done){
        this.simulationName = simulationName;
        this.userName = userName;
        this.amountToRun = amountToRun;
        this.amountRun = amountRun;
        this.ticks = ticks;
        this.sec = sec;
        this.id = id;
        this.status = status;
        this.currentRun = currentRun;
        this.done = done;
    }

    public Integer getCurrentRun(){
        return currentRun;
    }

    public Integer getDone(){
        return done;
    }

    public Integer getId(){
        return id;
    }


    public Integer getTicks(){
        return ticks;
    }

    public Integer getSec(){
        return sec;
    }

    public approvementStatus getStatus(){
        return status;
    }

    public String getSimulationName(){
        return simulationName;
    }

    public String getUserName(){
        return userName;
    }

    public Integer getAmountToRun(){
        return amountToRun;
    }
    public Integer getRemainingRun(){
        return amountToRun - amountRun;
    }


}
