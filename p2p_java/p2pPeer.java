import java.io.*;

public class p2pPeer {

	public static void main(String[] args) throws IOException {
		if (args.length < 2 || args.length > 5) {
			System.out.println("Criar Servidor: java p2pPeer server <server_ip>");
			System.out.println("Registrar Peer: java p2pPeer peer <client_name> <client_ip> <server_ip> port");
			return;
		} else {
			for(int i = 0; i < args.length; i++){
				System.out.println(args[i]);
			}
			switch(args[0]){
                case "server":
                    new p2pServer().run(args);
                    break;
                case "peer":
                    new p2pPeerThread(args).start();
			        new p2pPeerHeartbeat(args).start();
			        new p2pPeerClient(args).start();
                    break;
		    }
		}
	}
}
