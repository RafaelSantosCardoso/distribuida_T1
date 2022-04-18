import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public class ClientThread extends Thread {
	protected String[] thread_args;
	protected String thread_name;
	
	public ClientThread(String[] args, String name) {
		thread_args = args;
		thread_name = name;
	}

	public void run() {
		int result = 0;

		String remoteHostName = thread_args[0];
		String connectLocation = "rmi://" + remoteHostName + ":52369/server_if";

		ServerInterface server_if = null;
		try {
			System.out.println("Connecting to server at : " + connectLocation);
			server_if = (ServerInterface) Naming.lookup(connectLocation);
		} catch (Exception e) {
			System.out.println ("Client failed: ");
			e.printStackTrace();
		}

		for (int i = 0; i < 3; i++) {
			try {
				result = server_if.register(thread_name + thread_args[2] + Integer.toString(i));
				System.out.println(thread_name + " register() successful, id: " + result);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		List<Integer> ids = new ArrayList<>();
		try {
			ids = server_if.list_query(result);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
			
		for (int i = 0; i < ids.size(); i++) {
			String nick = "";
			try {
				nick = server_if.id_query(result, ids.get(i));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			System.out.println(thread_name + " user id: " + ids.get(i) + " name: " + nick);

		}
	}
}
