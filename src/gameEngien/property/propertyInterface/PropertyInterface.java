package gameEngien.property.propertyInterface;

public interface PropertyInterface {

    public propertyType getType();
    public void addToProperty(int add);
    public void addToProperty(float add);
    public String getName();
    public Object getValue();
    public void setProperty(int v);
    public void setProperty(float v);
    public void setProperty(boolean v);
    public void setProperty(String v);
}
