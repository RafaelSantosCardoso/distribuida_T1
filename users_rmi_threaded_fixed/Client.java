import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public class Client {
	public static void main(String[] args) {
		int result = 0;

		if (args.length != 3) {
			System.out.println("Usage: java Client <server ip> <client ip> <\"nickname\">");
			System.exit(1);
		}
	
		new ClientThread(args, "t1").start();
		new ClientThread(args, "t2").start();
	}
}
