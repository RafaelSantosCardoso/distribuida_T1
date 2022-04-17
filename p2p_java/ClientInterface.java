import java.rmi.Remote;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface ClientInterface extends Remote {
    public void inicialize(int id) throws RemoteException;
    public void filize() throws RemoteException;
    public void heatbeat() throws RemoteException;
}
