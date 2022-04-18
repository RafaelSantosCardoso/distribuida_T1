import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface ServerInterface extends Remote {
	public int registerPeer(String name, String ip, String port) throws RemoteException;
	public int registerResorce(String resourceName, String peerIp) throws RemoteException;
	public Set<Resource> searchResource(String resourceName) throws RemoteException;
	public Peer findResource(String hash) throws RemoteException;
}
