import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface ServerInterface extends Remote {
	public String registerPeer(String name, String ip, String port) throws RemoteException;
	public String registerResorce(String resourceName, String peerIp) throws RemoteException;
	public int heartBeat(String id) throws RemoteException;
	public Set<String> searchResource(String resourceName, String id) throws RemoteException;
	public List<String> findResource(String hash, String id) throws RemoteException;
}
