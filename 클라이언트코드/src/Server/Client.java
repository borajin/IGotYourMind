package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
	Socket socket;
	
	public Client(Socket socket) {
		this.socket = socket;
		receive();	//클라이언트 접속시 receive 스레드 생성
	}
	
	//메세지 전달받는 메소드
	public void receive() {
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try  {
					while(true) {
						InputStream in = socket.getInputStream();
						byte[] buffer = new byte[1024];
						int length = in.read(buffer);
						while(length == -1) throw new IOException();
						//서버 메세지. 받은 메세지의 클라이언트 ip와 thread name 출력
						System.out.println("[메세지 수신 성공]" 
											+ socket.getRemoteSocketAddress()
											+ ": " + Thread.currentThread().getName());
						String message = new String(buffer, 0, length, "UTF-8");
						//받은 메세지 다른 클라이언트들한테도 보내줌
						for(Client client : Server.clients) {
							client.send(message);
						}
					}
				} catch(Exception e) {
					//이런 식의 이중 예외 처리를 많이 사용한다.
					try {
						System.out.println("[메세지 수신 오류]"
								+ socket.getRemoteSocketAddress()
								+ ": " + Thread.currentThread().getName());
					} catch(Exception e2) {
						
					}
				}
			}
		};
		Server.threadPool.submit(thread);	//스레드풀에 스레드 등록
	}
	
	//메세지 전송 메소드
	public void send(String message) {
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer = message.getBytes("UTF-8");
					out.write(buffer);
					out.flush();	//버퍼 비우기
				} catch (Exception e) {
					try {
						System.out.println("[메세지 송신 오류]" 
								+ socket.getRemoteSocketAddress()
								+ ": " + Thread.currentThread().getName());
						//오류가 생긴 클라이언트를 제거
						Server.clients.remove(Client.this);
						socket.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}

			}
			
		};
		Server.threadPool.submit(thread);
	}
}
