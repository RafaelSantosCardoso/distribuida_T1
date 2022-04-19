import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface ServerInterface extends Remote {
	public String registerPeer(String name, String ip, String port) throws RemoteException;
	public int registerResorce(String resourceName, String peerIp) throws RemoteException;
	public int heartBeat(String id) throws RemoteException;
	public List<String> searchResource(String resourceName) throws RemoteException;
	public String findResource(String hash) throws RemoteException;
}
