import java.io.*;
import java.net.*;

public class p2pPeerHeartbeat extends Thread {
	protected DatagramSocket socket = null;
	protected DatagramPacket packet = null;
	protected InetAddress addr = null;
	protected byte[] data = new byte[1024];
	protected int porta;

	public p2pPeerHeartbeat(String[] args) throws IOException {
		String vars[] = args[1].split("\\s");
		data = ("heartbeat " + vars[1]).getBytes();
		addr = InetAddress.getByName(args[0]);
		porta = Integer.parseInt(args[2]) + 100;
		socket = new DatagramSocket(porta);
	}

	public void run() {
		while (true) {
			try {
				packet = new DatagramPacket(data, data.length, addr, 9000);
				socket.send(packet);
			} catch (IOException e) {
				socket.close();
			}
			
			try {
				Thread.sleep(10000);
			} catch(InterruptedException e) {
				System.out.println(e);
			}
		}
	}
}
