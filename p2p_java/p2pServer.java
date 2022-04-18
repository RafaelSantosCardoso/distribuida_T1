import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public class p2pServer extends UnicastRemoteObject implements ServerInterface{
	private static volatile String ipServer; 

	public p2pServer() throws RemoteException {

	}

	public static void run(String[] args) throws RemoteException{
		ipServer = args[1];
		try {
            System.setProperty("java.rmi.server.hostname", ipServer);
            LocateRegistry.createRegistry(52369);
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            System.out.println("java RMI registry already exists.");
        }

        try {
            String server = "rmi://" + args[1] + ":52369/server";
            Naming.rebind(server, new p2pServer());
            System.out.println("Server is ready.");
        } catch (Exception e) {
            System.out.println("Serverfailed: " + e);
        }
	}

	@Override
	public synchronized int register(String username) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public synchronized List<Integer> list_query(int user) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public synchronized String id_query(int user, int id) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
}
