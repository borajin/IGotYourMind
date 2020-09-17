package Client;

import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

public class User {
	public String userName;
	public Socket socket;
	public int score = 0;
	
	public Socket tcpServerSocket;
	public MulticastSocket udpSocket;
	public InetAddress udpIp;
	public int udpPort;
	
	public User(String userName, Socket socket, MulticastSocket udpSocket, InetAddress udpIp, int udpPort) {
		this.userName = userName;
		this.socket = socket;
		
		this.udpSocket = udpSocket;
		this.udpIp = udpIp;
		this.udpPort = udpPort;
	}
}
