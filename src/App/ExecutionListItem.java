package App;

public class ExecutionListItem {
    private Integer id;

    public ExecutionListItem(Integer id){
        this.id = id;
    }

    public Integer getID(){
        return id;
    }

    @Override
    public String toString(){
        return "Simulation Number: "+ id.toString();
    }

}
