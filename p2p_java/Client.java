import java.io.*;
import java.net.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Client extends Thread {
	protected DatagramSocket socket = null;
	int countFile = 0;

	protected String[] args;

	private List<ClientResource> clientResource = new ArrayList<>();

	public Client(String[] args) throws IOException {
		this.args = args;
		this.socket = new DatagramSocket(Integer.parseInt(args[4]));
	}

	public void run() {
		BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
		String str;
		String remoteHostName = this.args[3];
		String connectLocation = "rmi://" + remoteHostName + ":52369/server";

			try {
				System.out.println("** Conectando ao server: " + connectLocation);
				ServerInterface serverIf = (ServerInterface) Naming.lookup(connectLocation);

				String userId = serverIf.registerPeer(this.args[1], this.args[2], this.args[4]);
				String peerName = this.args[1];

				if(userId != null){
					System.out.println("** Peer registrado com sucesso **");
					while (true) {
						// Timer timer = new Timer();
						// timer.schedule(new TimerTask() {
						// 	public void run() {
						// 		try {
						// 			serverIf.heartBeat(userId);
						// 		} catch (Exception e) {
						// 			System.out.print("HeartBeat falhou ao ser enviado");
						// 			e.printStackTrace();
						// 		}
						// 	}
						// }, 0, 10000);

						//Socket received
						// new Thread( new Runnable() {
						// 	@Override
						// 	public void run(){
						// 		while (true) {
						// 			try {
						// 				serverIf.heartBeat(userId);
						// 				Thread.sleep(10000);
						// 			} catch (IOException | InterruptedException e) {
						// 				socket.close();
						// 			}
						// 		}
						// 	}
						// }).start();

						sendHeartBeat(serverIf, userId);

						//Socket received
						new Thread( new Runnable() {
							@Override
							public void run(){
								while (true) {
									try {
										byte[] downloadRequested = new byte[1024];
										byte[] lengthSend = new byte[8];
		
										DatagramPacket packetReceived = new DatagramPacket(downloadRequested, downloadRequested.length);
										socket.receive(packetReceived);
										downloadRequested = packetReceived.getData();
										String fileName = findResouceByHash(new String(downloadRequested).trim());
										
										String pathBase = "./" + peerName + "/" + fileName;
										File file = new File(pathBase);
										String msgLenghtToDownload = String.valueOf(file.length());

										InetAddress ipClient = packetReceived.getAddress();
										int portClient = packetReceived.getPort();

										String hashNotFound = "0";
										lengthSend = fileName == null ? hashNotFound.getBytes() : msgLenghtToDownload.getBytes();

										if(fileName == null){
											System.out.println("\n**Arquivo não encontrado");
											System.out.println("\n**Enviando tamanho 0");
										} else{
											System.out.println("\n**Enviando o tamanho do arquivo");
										}

										DatagramPacket packetSendLength = new DatagramPacket(lengthSend, lengthSend.length, ipClient, portClient);
										socket.send(packetSendLength);

										if(fileName != null){
											byte[] fileSend = new byte[(int)file.length()];
											FileManager fileManager = new FileManager();
											fileSend = fileManager.toFileFromArray(pathBase);
											System.out.println("\n**Enviando o arquivo " + fileName);
											DatagramPacket packetSend = new DatagramPacket(fileSend, fileSend.length, ipClient, portClient);
											socket.send(packetSend);
										}
									} catch (IOException e) {
										socket.close();
									} catch (Exception e) {
										socket.close();
										e.printStackTrace();
									}
								}
							}
						}).start();

						//Menu Peer
						try {
							System.out.println("\n\n##Para registrar um recurso: resource <resource_name> <client_ip>");
							System.out.println("##Para buscar um recurso: search <conteudo a ser buscado>");
							System.out.println("##Para buscar o peer de um recurso: find <hash>");
							System.out.println("##Para fazer download de um recurso: download <hash_recurso> <peer_server_ip> <peer_server_port>\n\n");

							str = obj.readLine();
							String vars[] = str.split("\\s");
							switch(vars[0]){
								case "resource":
									String resourceHash = serverIf.registerResorce(vars[1], userId);
									if(resourceHash != null){
										clientResource.add(new ClientResource(vars[1], resourceHash));
										System.out.println("\n**Recurso criado com sucesso.");
									} else {
										System.out.println("\n**A criação do recurso falhou. Tente novamente");
									}
									break;

								case "search":
									try{
										Set<String> resourcesList = serverIf.searchResource(vars[1], userId);

										if(resourcesList.size() > 0){
											for(Iterator<String> iter = resourcesList.iterator(); iter.hasNext();){
												System.out.println(iter.next());
											}
										} else{
											System.out.println("\n**Não há recursos registrados com esse nome");
										}
									}catch(RemoteException e){
										e.printStackTrace();
									}
									break;

								case "find":
									try {
										List<String> resourcesList = serverIf.findResource(vars[1]);
										if(resourcesList.size() > 0){
											for(int i = 0; i < resourcesList.size(); i ++){
												System.out.println(resourcesList.get(i));
											}
										} else{
											System.out.println("\n**Não há peers para esse hash");
										}
									} catch (RemoteException e){
										e.getStackTrace();
									}
									break;

								case "download":
									resourceDownload(vars[1], vars[2], vars[3], peerName);
									break;
								default:
									System.out.println("\n**Comando não encontrado");
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					System.out.println("**Registro do peer falhou");
				}
			} catch (Exception e) {
				System.out.println ("Client failed: ");
				e.printStackTrace();
			}
	}

	private void resourceDownload(String hash, String ipDestination, String port, String peerName) throws IOException{
		try {
			DatagramSocket socketDownload = new DatagramSocket();
			byte[] hashDownload = new byte[1024];

			hashDownload = hash.getBytes();
			InetAddress ipDest = InetAddress.getByName(ipDestination);
			Integer portDestination = Integer.parseInt(port);
	
			System.out.println("\n**Solicitando download de arquivo");
			DatagramPacket send = new DatagramPacket(hashDownload, hashDownload.length, ipDest, portDestination);
			socketDownload.send(send);

			byte[] fileLenghtBytes = new byte[8];
			DatagramPacket lengthFile = new DatagramPacket(fileLenghtBytes, fileLenghtBytes.length);
			socketDownload.receive(lengthFile);
			String messageLenghReceived = new String (lengthFile.getData());
			int receivedLengh = Integer.parseInt(messageLenghReceived.trim());
			System.out.println("**Recebido o tamanho do arquivo:" + receivedLengh);

			if(receivedLengh > 0){
				System.out.println("**Recebido o tamanho do arquivo:" + receivedLengh);
				byte[] getFileDownload = new byte[receivedLengh];
				System.out.println("**Fazendo o download do arquivo");
				DatagramPacket received = new DatagramPacket(getFileDownload, getFileDownload.length);
				socketDownload.receive(received);
				
				String pathFile = "./"+ peerName + "/file_received"+ countFile++ +".txt";
				FileManager fileManager = new FileManager();
				fileManager.toArrayFromFile(received.getData(), pathFile);
				System.out.println("**Download concluído!!");
			} else {
				System.out.println("**Arquivo não encontrado");
			}

			socketDownload.close();
		}catch(IOException e){
		}
		
	}

	private String findResouceByHash(String resourceHash){
		for(int i = 0; i < clientResource.size(); i++){
			if(clientResource.get(i).getHash().equals(resourceHash.trim())){
				return clientResource.get(i).getResourceName();
			}
		}
		return null;
	}

	private void sendHeartBeat(ServerInterface server, String userId) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					server.heartBeat(userId);
				} catch (Exception e) {
					System.out.print("HeartBeat falhou ao ser enviado");
					e.printStackTrace();
				}
			}
		}, 0, 10000);
	}
}
