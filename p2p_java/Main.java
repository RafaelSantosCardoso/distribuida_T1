import java.io.*;

public class Main {

	public static void main(String[] args) throws IOException {
		if (args.length < 3 || args.length > 5) {
			System.out.println("Criar Servidor: java Main server <server_ip> <server_port>");
			System.out.println("Registrar Peer: java Main peer <peer_name> <peert_ip> <server_ip> <peer_port>");
			return;
		} else {
			switch(args[0]){
                case "server":
                    new Server().run(args);
                    break;
                case "peer":
			        new Client(args).start();
                    break;
		    }
		}
	}
}
