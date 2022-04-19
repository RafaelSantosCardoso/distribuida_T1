import java.io.*;
import java.net.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class p2pPeerClient extends Thread {
	protected DatagramSocket socket = null;

	protected String[] args;

	public p2pPeerClient(String[] args) throws IOException {
		this.args = args;
		this.socket = new DatagramSocket(Integer.parseInt(args[4]));
	}

	public void run() {
		BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
		String str;
		String remoteHostName = this.args[3];
		String connectLocation = "rmi://" + remoteHostName + ":52369/server";

		ServerInterface serverIf = null;
			try {
				System.out.println("Connecting to server at : " + connectLocation);
				serverIf = (ServerInterface) Naming.lookup(connectLocation);

				String result = serverIf.registerPeer(this.args[1], this.args[2], this.args[4]);

				if(result != null){
					
					System.out.println("registerPeer() successful");
					while (true) {
						new Thread( new Runnable() {
							@Override
							public void run(){
								while (true) {
									try {
									byte[] download = new byte[1024];
									byte[] messageSend = new byte[1024];
	
									DatagramPacket packet = new DatagramPacket(download, download.length);
									socket.receive(packet);
									System.out.print("Recebi!");
	
									String message = "Reenviando a resposta!";
									messageSend = message.getBytes();
									InetAddress ipClient = packet.getAddress();
									int portClient = packet.getPort();

									DatagramPacket packetSend = new DatagramPacket(messageSend, messageSend.length, ipClient, portClient);
									socket.send(packetSend);
									} catch (IOException e) {
										socket.close();
									}
								}
							}
						}).start();
						try {
							System.out.println("\n\nPara registrar um recurso: resource <resource_name> <client_ip>");
							System.out.println("\nPara buscar um recurso: search <conteudo a ser buscado>");
							System.out.println("\nPara buscar o peer de um recurso: find <hash>");
							System.out.println("\nPara fazer download de um recurso: download <hash_recurso> <peer_server_ip> <peer_server_port>\n\n");

							str = obj.readLine();
							String vars[] = str.split("\\s");
							switch(vars[0]){
								case "resource":
									int resource = serverIf.registerResorce(vars[1], result);
									if(resource > 0){
										System.out.println("Recurso criado com sucesso.");
									} else {
										System.out.println("A criação do recurso falhou. Tente novamente");
									}
									break;

								case "search":
									try{
										List<String> resourcesList = serverIf.searchResource(vars[1]);

										if(resourcesList.size() > 0){
											for(int i = 0; i < resourcesList.size(); i ++){
												System.out.println(resourcesList.get(i));
											}
										} else{
											System.out.println("Não há recursos registrados com esse nome");
										}
									}catch(RemoteException e){
										e.printStackTrace();
									}
									break;

								case "find":
									try {
										System.out.println(serverIf.findResource(vars[1]));
									} catch (RemoteException e){
										e.getStackTrace();
									}
									break;

								case "download":
									resourceDownload(vars[1], vars[2], vars[3]);
									break;
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					System.out.println("**registerPeer() Failure!!");
				}
			} catch (Exception e) {
				System.out.println ("Client failed: ");
				e.printStackTrace();
			}
	}

	private void resourceDownload(String hash, String ipDestination, String port) throws IOException{
		try {
			DatagramSocket socketDownload = new DatagramSocket();
			byte[] hashDownload = new byte[1024];
			byte[] getFileDownload = new byte[1024];

			hashDownload = hash.getBytes();
			InetAddress ipDest = InetAddress.getByName(ipDestination);
			Integer portDestination = Integer.parseInt(port);
	
			System.out.println("Enviando mensagem");
			System.out.println("hash: " + hash + " ip:" + ipDest + "port: " + portDestination);
			DatagramPacket send = new DatagramPacket(hashDownload, hashDownload.length, ipDest, portDestination);
			socketDownload.send(send);

			DatagramPacket received = new DatagramPacket(getFileDownload, getFileDownload.length);
			socketDownload.receive(received);
			System.out.println("Retornou a mensagem");
			System.out.println(received.getData());


			socketDownload.close();
		}catch(IOException e){
		}
		
	}

	private void sendFile(){

	}
}
