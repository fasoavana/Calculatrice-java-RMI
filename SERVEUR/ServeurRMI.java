import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServeurRMI {
    public static void main(String[] args) {
        try {
            // Indique l'adresse réseau Docker
            System.setProperty("java.rmi.server.hostname", "srv-calcul");
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost:1099/CalculService", new CalculatriceImpl());
            System.out.println("✅ Serveur prêt !");
        } catch (Exception e) { e.printStackTrace(); }
    }
}
