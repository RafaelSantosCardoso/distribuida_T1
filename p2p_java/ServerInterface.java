import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface ServerInterface extends Remote {
    public int register(int port) throws RemoteException;
    public int end(int id) throws RemoteException;
}