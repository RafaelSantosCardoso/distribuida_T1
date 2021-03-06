import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.rmi.registry.LocateRegistry;

public class Server extends UnicastRemoteObject implements ServerInterface{
	private static volatile String ipServer; 
	private static volatile List<Peer> peers = new ArrayList<>();
	private static volatile List<Resource> resorces = new ArrayList<>();

	public Server() throws RemoteException {

	}

	public void run(String[] args) throws RemoteException{
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
			Naming.rebind(server, new Server());
			System.out.println("Server is ready.");
			
		} catch (Exception e) {
			System.out.println("Serverfailed: " + e);
		}

		new Thread( new Runnable() {
			@Override
			public void run(){
					while (true) {
						for(int i = 0; i < peers.size(); i++){
							peers.get(i).setTimeout(peers.get(i).getTimeout() - 1);
							if(peers.get(i).getTimeout() == 0){
								for(int j = 0; j < resorces.size(); i++){
									if(resorces.get(j).getPeer().getId().equals(peers.get(i).getId())){
										resorces.remove(j);
									}
								}
								peers.remove(i);

								System.out.print("Peer "+ peers.get(i).getName() + " morreu...");
							}
						}
						try {
							Thread.sleep(1000);
						} catch(InterruptedException e) {
							System.out.println(e);
						}
						System.out.print(".");
					}
				}
			}).start();
	}

	@Override
	public synchronized String registerPeer(String name, String ip, String port) throws RemoteException{
		if(name != null && ip != null && port != null){
			try {
				String hash = createSHAHash(name+ip+port);
				peers.add(new Peer(hash, name, ip, Integer.parseInt(port)));
				return hash;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return null;
	};

	@Override
	public synchronized String registerResorce(String resourceName, String peerId) throws RemoteException {
		if(resourceName != null && peerId != null){
			try {
			String hash = createSHAHash(resourceName);
			Peer peer = sourcePeer(peerId);
			if(peer != null){
				resorces.add(new Resource(resourceName, hash, peer));
				return hash;
			}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public synchronized Set<String> searchResource(String resourceName, String id) throws RemoteException {
		Set<String> list = new HashSet<String>();
		for(int i = 0; i < resorces.size(); i ++){
			if (resorces.get(i).getResourceName().contains(resourceName) && !(resorces.get(i).getPeer().getId().equals(id))){
				list.add(resorces.get(i).toString());
			}
		}

		return list;
	}

	@Override
	public synchronized List<String> findResource(String hash, String id) throws RemoteException {
		List<String> list = new ArrayList<>(); 
		for(int i = 0; i < resorces.size(); i++){
			if(resorces.get(i).getHash().equals(hash) && !(resorces.get(i).getPeer().getId().equals(id))){
				list.add(resorces.get(i).getPeer().toString());
			}
			
		}
		return list;
	}

	@Override
	public synchronized int heartBeat(String name) throws RemoteException {
		for(int i = 0; i < peers.size(); i++){
			if(peers.get(i).getName().equals(name)){
				System.out.println(name);
				peers.get(i).setTimeout(10);
				return peers.get(i).getTimeout();
			}
			
		}
		return 0;
	}

	private Peer sourcePeer(String id){
		Peer peer = null;

		for(int i = 0; i < peers.size(); i++){
			if(peers.get(i).getId().equals(id)){
				peer = peers.get(i);
			}
			
		}

		return peer;
	}

	private String createSHAHash(String input)throws NoSuchAlgorithmException {
      String hashtext = null;
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));

      hashtext = convertToHex(messageDigest);

      return hashtext;
   }

   private String convertToHex(final byte[] messageDigest) {
      BigInteger bigint = new BigInteger(1, messageDigest);
      String hexText = bigint.toString(16);

      while (hexText.length() < 32) {
         hexText = "0".concat(hexText);
      }

      return hexText;
   }
}
