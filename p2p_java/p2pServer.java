import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Period;
import java.rmi.registry.LocateRegistry;

public class p2pServer extends UnicastRemoteObject implements ServerInterface{
	private static volatile String ipServer; 
	private static volatile List<Peer> peers = new ArrayList<>();
	private static volatile List<Resource> resorces = new ArrayList<>();

	public p2pServer() throws RemoteException {

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
            Naming.rebind(server, new p2pServer());
            System.out.println("Server is ready.");
			while (true) {
					new Thread( new Runnable() {
						public void run(){
							for(int i = 0; i < peers.size(); i++){
								peers.get(i).setTimeout(peers.get(i).getTimeout() - 1);
								if(peers.get(i).getTimeout() == 0){
									peers.remove(i);
								}
								System.out.println(".");
							}
						}
					});
				
			}
        } catch (Exception e) {
            System.out.println("Serverfailed: " + e);
        }
	}

	@Override
	public synchronized int registerPeer(String name, String ip, String port) throws RemoteException{
		if(name != null && ip != null && port != null){
			try {
				String hash = createSHAHash(name+ip);
				peers.add(new Peer(hash, name, ip, Integer.parseInt(port)));
				return 1;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return -1;
	};

	@Override
	public synchronized int registerResorce(String resourceName, String peerIp) throws RemoteException {
		if(resourceName != null && peerIp != null){
			try {
			String hash = createSHAHash(resourceName);
			Peer peer = sourcePeer(peerIp);
			if(peer != null){
				resorces.add(new Resource(resourceName, hash, peer));
				System.out.println(hash);
				return 1;
			}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return 0;
		}
		return -1;
	}

	@Override
	public synchronized List<String> searchResource(String resourceName) throws RemoteException {
		List<String> list = new ArrayList<>();
		for(int i = 0; i < resorces.size(); i ++){
			if (resorces.get(i).getResourceName().contains(resourceName)){
				list.add(resorces.get(i).toString());
			}
		}

		return list;
	}

	@Override
	public synchronized String findResource(String hash) throws RemoteException {
		for(int i = 0; i < resorces.size(); i++){
			if(resorces.get(i).getHash().equals(hash)){
				System.out.println(resorces.get(i).getPeer().getName());
				return resorces.get(i).getPeer().toString();
			}
			
		}
		return null;
	}

	@Override
	public synchronized int heartBeat(String id) throws RemoteException {
		for(int i = 0; i < peers.size(); i++){
			if(peers.get(i).getId().equals(id)){
				peers.get(i).setTimeout(30);
				return peers.get(i).getTimeout();
			}
			
		}
		return 0;
	}

	private Peer sourcePeer(String ip){
		Peer peer = null;

		for(int i = 0; i < peers.size(); i++){
			if(peers.get(i).getAddrIp().equals(ip)){
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
