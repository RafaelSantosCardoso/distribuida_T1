import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public class p2pServer extends Thread, UnicastRemoteObject implements ServerInterface {
	private static volatile String ipServer; 

	public p2pServer(String[] args) throws IOException, RemoteException {
		ipServer = args[1];
	}

	public void run() {
		try {
            System.setProperty("java.rmi.server.hostname", ipServer);
            LocateRegistry.createRegistry(52369);
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            System.out.println("java RMI registry already exists.");
        }

        try {
            String server = "rmi://" + ipServer + ":52369/server";
            Naming.rebind(server, new p2pServer());
            System.out.println("Server is ready.");
        } catch (Exception e) {
            System.out.println("Serverfailed: " + e);
        }
	}

	public synchronized int register(int i) {
		return 0;
	}

	public synchronized int end(int i) {
		return 0;
	}
}
