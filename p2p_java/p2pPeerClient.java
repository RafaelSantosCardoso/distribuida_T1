import java.io.*;
import java.net.*;
import java.rmi.Naming;

public class p2pPeerClient extends Thread {
	protected DatagramSocket socket = null;
	protected DatagramPacket packet = null;
	protected InetAddress addr = null;
	protected byte[] resource = new byte[1024];
	protected byte[] response = new byte[1024];
	protected int port, peer_port;

	protected String[] args;

	public p2pPeerClient(String[] args) throws IOException {
		this.args = args;
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

			int result = serverIf.registerPeer(this.args[1], this.args[2], this.args[4]);
			if(result > 0 ){
				System.out.println("registerPeer() successful");

				while (true) {

					System.out.println("\nPara registrar um recurso: resource <resource_name> <client_ip>");
					System.out.println("Example: resource index.html 127.0.0.1");
					System.out.println("\nPara buscar um recurso: search <conteudo a ser buscado>");
					System.out.println("Example: search index.html");
					System.out.println("\nPara buscar descobrir o peer de um recurso: find <hash>");
					System.out.println("Example: find ABHA45687dasASDS");
					
					try {
						str = obj.readLine();
						String vars[] = str.split("\\s");
						switch(vars[0]){
							case "resource":
								int resource = serverIf.registerResorce(vars[1], vars[2]);
								if(resource > 0){
									System.out.println("Recurso criado com sucesso.");
								} else {
									System.out.println("A criação do recurso falhou. Tente novamente");
								}
							case "search":
							case "find":
			
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					try {
						packet = new DatagramPacket(resource, resource.length, addr, peer_port);
						socket.send(packet);
						
						while (true) {
							try {
								// obtem a resposta
								packet = new DatagramPacket(response, response.length);
								socket.setSoTimeout(500);
								socket.receive(packet);
								
								// mostra a resposta
								String resposta = new String(packet.getData(), 0, packet.getLength());
								System.out.println("recebido: " + resposta);
							} catch (IOException e) {
								break;
							}
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
}
