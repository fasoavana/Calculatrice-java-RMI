import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculatrice extends Remote {
    double calculer(double a, double b, String op) throws RemoteException;
}
