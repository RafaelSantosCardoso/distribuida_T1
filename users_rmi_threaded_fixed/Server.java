import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public class Server extends UnicastRemoteObject implements ServerInterface {
	private static volatile int result;
	private static volatile String remoteHostName;
	private static Random rnd = new Random();
	private static volatile List<String> userNameList = new ArrayList<>();
	private static volatile List<Integer> userIdList = new ArrayList<>();

	public Server() throws RemoteException {
	}
	
	public static void main(String[] args) throws RemoteException {
		if (args.length != 1) {
			System.out.println("Usage: java Server <server ip>");
			System.exit(1);
		}

		try {
			System.setProperty("java.rmi.server.hostname", args[0]);
			LocateRegistry.createRegistry(52369);
			System.out.println("java RMI registry created.");
		} catch (RemoteException e) {
			System.out.println("java RMI registry already exists.");
		}

		try {
			String server = "rmi://" + args[0] + ":52369/server_if";
			Naming.rebind(server, new Server());
			System.out.println("Server is ready.");
		} catch (Exception e) {
			System.out.println("Serverfailed: " + e);
		}
	}
	
	public synchronized int register(String username) {
		int val = rnd.nextInt();
		
		userNameList.add(username);
		
		try {
			Thread.sleep(100);
		} catch (Exception e) {
		}
		
		userIdList.add(val);
		
		return val;
	}
	
	public synchronized List<Integer> list_query(int user) {
		for (int i = 0; i < userIdList.size(); i++) {
			if (user == userIdList.get(i))
				return userIdList;
		}
		
		return new ArrayList<>();
	}
	
	public synchronized String id_query(int user, int id) {
		int i = 0;
		
		for (i = 0; i < userIdList.size(); i++)
			if (user == userIdList.get(i)) break;
		
		if (i < userIdList.size()) {
			for (i = 0; i < userIdList.size(); i++) {
				if (id == userIdList.get(i))
					return userNameList.get(i);
			}
		}
		
		return "";
	}
}
