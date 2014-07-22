import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HelloRMI {

    public static void main(String[] args) {
       try {
           Registry registry = LocateRegistry.getRegistry("helloRMI");
           System.out.println("Hello RMI!");
       }
       catch (Exception e) {
           System.err.println("RMI exception:");
           e.printStackTrace();
       }
    }

}

