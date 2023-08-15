import Engine.Engine;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        String fileName = "src/resources/ex1-cigarets.xml";
        Engine g = new Engine();
        try {
            if (true) {
                g.loadSimulation(fileName);
                g.activeSimulation();
                g.saveSystemState("serializedObject");
            } else {
                g.loadSystemState("serializedObject");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }


        //World w = new World();
        //w.loadFile();
        //w.setSimulation();
        //w.startSimolesan();
    }

}
