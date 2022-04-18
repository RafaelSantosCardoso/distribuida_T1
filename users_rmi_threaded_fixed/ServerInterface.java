import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface ServerInterface extends Remote {
	public int register(String username) throws RemoteException;
	public List<Integer> list_query(int user) throws RemoteException;
	public String id_query(int user, int id) throws RemoteException;
}
