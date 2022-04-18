import java.io.*;
import java.net.*;
import java.util.*;

public class p2pPeer {

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.out.println("Uso: java p2pPeer server <serverip>");
			System.out.println("<message> is:");
			System.out.println("create nickname");
			System.out.println("list nickname");
			System.out.println("wait");
			return;
		} else {
			switch(args[0]){
                case "server":
                    new p2pServer(args).start();
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
