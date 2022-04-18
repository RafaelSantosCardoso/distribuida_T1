import java.io.*;
import java.net.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

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
					System.out.println("\nPara buscar o peer de um recurso: find <hash>");
					System.out.println("Example: find ABHA45687dasASDS\n\n");
					
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
