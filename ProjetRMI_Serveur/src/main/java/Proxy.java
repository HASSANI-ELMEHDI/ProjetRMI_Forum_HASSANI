import java.rmi.Remote;
import java.rmi.RemoteException;
public interface Proxy extends Remote {
    public void ecouter(String msg) throws RemoteException;
}
