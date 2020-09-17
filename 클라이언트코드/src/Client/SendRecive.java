package Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SendRecive {
	User user;
	GameFrame gameFrame;
	GameManager gameManager;
	int count = 5;

	public SendRecive(User user, GameFrame gameFrame) {
		this.user = user;
		this.gameFrame = gameFrame;
		this.gameManager = new GameManager(user, gameFrame, this);
	}
	
	public void drawRecive() {
		byte[] buffer = new byte[512];
		while(true) {
			try {
				DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
				user.udpSocket.receive(datagramPacket);
				
				String message =  new String(datagramPacket.getData(), 0, datagramPacket.getLength());
				String[] split = message.split("@");
				
				if(split[0].equals("draw")) {
					gameFrame.setDrawingPoint(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void drawSend(String message) {
		Thread thread = new Thread() {
			public void run() {
				byte[] buffer = new byte[512];
				
				try {
					DatagramSocket dataSocket = new DatagramSocket();
					buffer = message.getBytes();
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length, user.udpIp, user.udpPort);
					user.udpSocket.send(packet);
				} catch (Exception e) {
					stopClient();
				}
			}
		};
		thread.start();
	}

	 public void receive() {
		try {
			// 맨 처음 접속했을 때 접속했습니다 메세지 보내기.
			send("game@" + "[ " + user.userName + " ] 님이 접속하셨습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			stopClient();
		}

		// 계속 수신 대기
		while (true) {
			try {
				byte[] buffer = new byte[1024];
				InputStream in = user.socket.getInputStream();
				int length = in.read(buffer);
				if (length == -1)
					throw new IOException();
				
				String message = new String(buffer, 0, length, "UTF-8");
				System.out.println(message);
				String[] split = message.split("@");
				
				if (split[0].equals("chat")) {
					message = split[2];
					gameFrame.appendChtting("[ " + split[1] + "] " + split[2]);

					if(message.equals(gameManager.answer)) {
						if(split[1].equals(user.userName)) {
							gameManager.answerCorrect();
							gameManager.myTurnSetting();
						} else {
							gameManager.otherTurnSetting();
						}
					}
					
					if(message.equals("end")) {
						gameManager.endGame();
					}
				} 
				else if(split[0].equals("color")) {
					gameFrame.setDrawingColor(split[1]);
				}
				else if (split[0].equals("game")) {
					if(split[1].equals("start")) {
						gameManager.startGame();
						if(split[2].equals(user.userName)) {
							gameManager.myTurnSetting();
						} else {
							gameManager.otherTurnSetting();
						}
					}
					else {
						gameFrame.appendChtting(split[1]);
					}
				} 
				else if(split[0].equals("result")) {
					gameFrame.appendChtting(split[1] + "의 점수: " + split[2]);
				} 
			} catch (Exception e) {
				e.printStackTrace();
				stopClient();
			}
		}
	}

	// 서버로 메세지 보내기
	public void send(String message) {
		Thread thread = new Thread() {
			public void run() {
				try {
					OutputStream out = user.socket.getOutputStream();
					byte[] buffer = message.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
				} catch (Exception e) {
					stopClient();
				}
			}
		};
		thread.start();
	}

	// 클라이언트 종료 메소드
	public void stopClient() {
		try {
			if (user.socket != null && !user.socket.isClosed()) {
				user.socket.close();
				user.udpSocket.close();
				gameFrame.dispose();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 클라이언트 프로그램 동작 메소드
	public void startClient() {
		// 클라이언트 측은 중구난방으로 스레드를 사용하지 않기 때문에 스레드풀 관리가 필요 없다.
		// 따라서 runnable 을 사용하지 않고 간단한 thread 객체를 구현한다.

		Thread thread = new Thread() {
			public void run() {
				try {
					receive();
				} catch (Exception e) {
					if (!user.socket.isClosed()) {
						stopClient();
						System.out.println("[서버 접속 실패]");
					}
				}
			}
		};
		thread.start();
		
		Thread thread2 = new Thread() {
			public void run() {
				try {
					drawRecive();
				} catch(Exception e) {
					if (!user.udpSocket.isClosed()) {
						stopClient();
						System.out.println("[서버 접속 실패]");
					}
				}
			}
		};
		thread2.start();
	}
	
}
