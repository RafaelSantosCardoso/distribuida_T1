import java.io.*;

public class p2pPeer {

	public static void main(String[] args) throws IOException {
		if (args.length < 3 || args.length > 5) {
			System.out.println("Criar Servidor: java p2pPeer server <server_ip> <server_port>");
			System.out.println("Registrar Peer: java p2pPeer peer <peer_name> <peert_ip> <server_ip> <peer_port>");
			return;
		} else {
			switch(args[0]){
                case "server":
                    new p2pServer().run(args);
                    break;
                case "peer":
			        new p2pPeerClient(args).start();
                    break;
		    }
		}
	}
}
