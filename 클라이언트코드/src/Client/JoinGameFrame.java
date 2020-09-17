package Client;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JoinGameFrame extends JFrame {
	private JTextField nameTextField;
	Color nam = new Color(23, 58, 77);

	public JoinGameFrame() {
		//frame 설정
		setTitle("login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//x버튼눌렀을 때 프로세스까지 확실하게 끄게 하는 코드
		setBounds(100, 100, 660, 660);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		//frame 안의 요소들 설정
		JPanel titlePanel = new JPanel();
		getContentPane().add(titlePanel);
			titlePanel.setBackground(nam);
		 	ImageIcon titleImage = new ImageIcon("image\\title.png");
		 	JLabel titleLabel = new JLabel(titleImage);
			titlePanel.add(titleLabel);
			//titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			//titleLabel.setFont(new Font("바탕", Font.PLAIN, 30));

		JPanel accessPanel = new JPanel();
		getContentPane().add(accessPanel);
			accessPanel.setBackground(nam);
			accessPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			
			ImageIcon nameImage = new ImageIcon("image\\name.png");
			JLabel nameLabel = new JLabel(nameImage);
			accessPanel.add(nameLabel);
			
			nameTextField = new JTextField();
			accessPanel.add(nameTextField);
			nameTextField.setColumns(10);
			
		JPanel joinPanel = new JPanel();
		getContentPane().add(joinPanel);
		joinPanel.setBackground(nam);
			ImageIcon accessBtnImage = new ImageIcon("image\\accessbtn.png");
			JButton accessButton = new JButton(accessBtnImage);
			accessButton.setBorderPainted(false);
			accessButton.setFocusPainted(false);
			accessButton.setContentAreaFilled(false);
			joinPanel.add(accessButton);
		
		accessButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String userName = nameTextField.getText().toString().replaceAll(" ", "");	//이름에 공백 제거

					boolean accessOk = this.accessOk(userName);
					
					if (accessOk) {
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									InetAddress serverIp = InetAddress.getByName("203.232.210.195");
									int serverPort = 9999;
									Socket socket = new Socket(serverIp, serverPort);
									
									int udpPort = 8888;
									InetAddress udpIp = InetAddress.getByName("224.0.1.100");	//브로드캐스트 주소
									MulticastSocket udpSocket = new MulticastSocket(udpPort);
									
									User user = new User(userName, socket, udpSocket, udpIp, udpPort);
									user.udpSocket.joinGroup(udpIp);
									new GameFrame(user);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
						dispose();	//joingameframe 종료
					} else {
						JOptionPane.showMessageDialog(null, "접속실패");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
			//이름이 공백이라면 접속 불가.
			public boolean accessOk(String userName) {
				if(userName == null || userName.equals("")) {
					return false;
				} else {
					return true;
				}
			}
		});	
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
					JoinGameFrame Joinframe = new JoinGameFrame();
					Joinframe.setVisible(true);
			}
		});
	}
}