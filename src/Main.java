import java.io.IOException;

public class Main {

    private static final int RUNS = 5;

    public static void main(String[] args) {
        PropertiesManager propertiesManager;
        try {
            propertiesManager = new PropertiesManager();
        } catch (IOException e) {
            System.out.println("Unable to read properties file!");
            return;
        }
        for (int i = 0; i < RUNS; ++i) {
            new ThreadManager(propertiesManager).execute();
        }
    }

}
