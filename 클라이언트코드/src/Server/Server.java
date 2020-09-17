package Server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	
	//스레드풀 : 여러 클라이언트 스레드를 관리. 스레드 숫자 제한을 두므로 갑작스럽게 클라이언트 접속 폭주하더라도 제한때문에 서버 폭주 방지
	public static ExecutorService threadPool;
	public static Vector<Client> clients = new Vector<Client>();	//접속한 클라이언트 벡터
	
	ServerSocket serverSocket;
	
	//서버 구동시키고 클라이언트 연결 기다리는 메소드
	public void startServer(String IP, int port) {
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(IP, port));
			System.out.println("서버 가동중 . . . " +  IP + "/" + port);
		} catch(Exception e) { 
			e.printStackTrace();
			//서버 에러 났을때, 만약 서버가 안 닫혔다면 stopServer 하도록
			if(!serverSocket.isClosed()) {
				stopServer();
			}
			return;
		}
		
		//클라이언트가 접속할 때까지 대기
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				while(true) {
					try {
						Socket socket = serverSocket.accept();
						//클라이언트 접속시 클라이언트 벡터에 추가
						clients.add(new Client(socket));
						System.out.println("[클라이언트 접속] " 
								+ socket.getRemoteSocketAddress()
								+ ": " + Thread.currentThread().getName());
					} catch (Exception e) {
						if(!serverSocket.isClosed()) {
							stopServer();
						}
						break;
					}
				}
			}
		};
		//스레드풀 초기화하고, 첫번째 스레드로서 클라이언트 접속 기다리는 스레드 추가.
		threadPool = Executors.newCachedThreadPool();
		threadPool.submit(thread);
	}
	
	//서버의 작동 중지시키는 메소드
	public void stopServer() {
		try {
			//현재 작동중인 모든 클라이언트 소켓 닫기
			Iterator<Client> iterator = clients.iterator();
			while(iterator.hasNext()) {
				Client client = iterator.next();
				client.socket.close();
				iterator.remove();
			}
			//서버 소켓 객체 닫기
			if(serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			//스레드풀 종료하기
			if(threadPool != null && !threadPool.isShutdown()) {
				threadPool.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.startServer("127.0.0.1", 9999);
	}
}
