import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CalculatriceImpl extends UnicastRemoteObject implements Calculatrice {
    // Force le port 1099 pour éviter les ports aléatoires bloqués par Docker
    public CalculatriceImpl() throws RemoteException { super(1099); }

    public double calculer(double a, double b, String op) throws RemoteException {
        switch(op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": if(b==0) throw new RemoteException("Div par 0"); return a / b;
            default: throw new RemoteException("Opérateur inconnu");
        }
    }
}
