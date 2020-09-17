package Client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GameFrame extends JFrame implements ActionListener {
	private User user;
	private SendRecive sendRecive;
	private String color;

	private JPanel gamePanel;
	
	private JPanel answerPanel;
	private JLabel answerLabel;
	
	private JPanel startPanel;
	private JButton startButton;
	
	private JPanel PaintPanel;
	private Canvas can;
	private JPanel paintToolPanel;
	private JButton blackColorButton;
	private JButton redColorButton;
	private JButton blueColorButton;
	private JButton greenColorButton;
	private JButton eraseButton;
	private JButton AlleraseButton;
	
	private JLabel roundLabel;
	
	private JPanel ChattingPanel;
	private JTextArea chattingTextArea;
	private JTextField chattingTextField;
	
	Color nam = new Color(23, 58, 77);

	public GameFrame(User user) throws UnknownHostException, IOException {
		this.user = user;
		this.sendRecive = new SendRecive(user, this);

		// 프레임 기본 설정----------------------------------------------------------------------------
		setTitle("I Got A Your Mind");
		setBounds(100, 100, 660, 660);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		getContentPane().setBackground(nam);

		// 게임 패널----------------------------------------------------------------------------
		gamePanel = new JPanel();
		gamePanel.setBackground(nam);
		getContentPane().add(gamePanel);
		gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));

		// 정답글자----------------------------------------------------------------------------
		answerPanel = new JPanel();
		answerPanel.setBackground(nam);
		answerLabel = new JLabel("You can't draw, not yet.");
		answerLabel.setFont(new Font("굴림", Font.BOLD, 30));
		answerLabel.setForeground(new Color(255, 192, 0));
		gamePanel.add(answerPanel);
		answerPanel.add(answerLabel);

		// 그림 그리는거---------------------------------------------------------------------------
		PaintPanel = new JPanel();
		PaintPanel .setBackground(nam);
		gamePanel.add(PaintPanel);
		PaintPanel.setLayout(new BoxLayout(PaintPanel, BoxLayout.Y_AXIS));

		can = new Paint();
		can.setSize(500, 400);
		can.setBackground(Color.WHITE);
		can.setEnabled(false);
		PaintPanel.add(can);

		can.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				String message = "draw@" + e.getX() + "@" + e.getY();
				sendRecive.drawSend(message);
			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}
		});

		can.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {

			}

			public void mouseDragged(MouseEvent e) {
				String message = "draw@" + e.getX() + "@" + e.getY();
				sendRecive.drawSend(message);
			}
		});
		
	
		startPanel = new JPanel();
		startPanel.setBackground(nam);
		
		// 라운드 ----------------------------------------------------------------------------
		JPanel roundPanel = new JPanel();
		roundPanel.setBackground(nam);
		roundLabel = new JLabel("라운드: ");
		roundLabel.setFont(new Font("굴림", Font.BOLD, 15));
		roundLabel.setForeground(new Color(255, 192, 0));
		roundPanel.add(roundLabel);
		startPanel.add(roundPanel);

		
		// 시작 버튼----------------------------------------------------------------------------
		startButton = new JButton("START");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendRecive.send("game@" + "start@" + user.userName);
			} 
		});
		startPanel.add(startButton);
		gamePanel.add(startPanel);
		
		
		// 그림도구버튼 ----------------------------------------------------------------------------
		paintToolPanel = new JPanel();
		paintToolPanel.setLayout(new BoxLayout(paintToolPanel, BoxLayout.X_AXIS));
		paintToolPanel.setEnabled(false);

		blackColorButton = new JButton("black");
		blackColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendRecive.send("color@black");
			}
		});
		paintToolPanel.add(blackColorButton);

		redColorButton = new JButton("red");
		redColorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendRecive.send("color@red");
			}
		});
		paintToolPanel.add(redColorButton);

		blueColorButton = new JButton("blue");
		blueColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendRecive.send("color@blue");
			}
		});
		paintToolPanel.add(blueColorButton);

		greenColorButton = new JButton("green");
		greenColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendRecive.send("color@green");
			}
		});
		paintToolPanel.add(greenColorButton);

		eraseButton = new JButton("erase");
		eraseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendRecive.send("color@erase");
			}
		});
		paintToolPanel.add(eraseButton);

		AlleraseButton = new JButton("All erase");
		AlleraseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendRecive.send("color@all");
			}
		});
		paintToolPanel.add(AlleraseButton);

		gamePanel.add(paintToolPanel);
		paintToolPanel.setLayout(new BoxLayout(paintToolPanel, BoxLayout.X_AXIS));

		// 채팅----------------------------------------------------------------------------
		ChattingPanel = new JPanel();

		ChattingPanel.setLayout(new BoxLayout(ChattingPanel, BoxLayout.Y_AXIS));

		JScrollPane ChattingScrollPane = new JScrollPane();
		ChattingPanel.add(ChattingScrollPane);

		chattingTextArea = new JTextArea();
		chattingTextArea.setRows(4);
		chattingTextArea.setEnabled(false);
		ChattingScrollPane.setViewportView(chattingTextArea);
		JPanel ChattingInputPanel = new JPanel();
		ChattingPanel.add(ChattingInputPanel);
		ChattingInputPanel.setLayout(new GridLayout(1, 0, 0, 0));

		chattingTextField = new JTextField();
		chattingTextField.setColumns(10);

		ChattingInputPanel.add(chattingTextField);

		chattingTextField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String message = chattingTextField.getText();
				chattingTextField.setText(null);

				byte[] buffer = new byte[1024];

				if (message == null || message == "" || message == "\n" || message.length() == 0)
					return;

				String msg = "chat@" + user.userName + "@" + message;
				sendRecive.send(msg);
			}
		});
		gamePanel.add(ChattingPanel);

		// 시작설정 ---------------------------------------------------------------------------
		this.setVisible(true);
		sendRecive.startClient();
	}
	
	
	// gui 컨트롤 메소드들---------------------------------------------------------------------------
	public void setStartButton(boolean isStart) {
		startButton.setEnabled(isStart);
	}

	public void setButtonEnabled(boolean isEnabled) {
		paintToolPanel.setEnabled(isEnabled);
		AlleraseButton.setEnabled(isEnabled);
		blackColorButton.setEnabled(isEnabled);
		blueColorButton.setEnabled(isEnabled);
		eraseButton.setEnabled(isEnabled);
		greenColorButton.setEnabled(isEnabled);
		redColorButton.setEnabled(isEnabled);
	}

	public void setAnswerText(String text) {
		answerLabel.setText(text);
	}

	public void setDrawing(boolean canYouDrawing) {
		can.setEnabled(canYouDrawing);
	}

	public void setChatting(boolean canYouChatting) {
		chattingTextField.setEditable(canYouChatting);
	}

	public void setDrawingPoint(int x, int y) {
		((Paint) can).x = x;
		((Paint) can).y = y;
		can.repaint();
	}

	public void setDrawingColor(String color) {
		if (color.equals("black")) ((Paint) can).cr = Color.black;
		else if (color.equals("blue")) ((Paint) can).cr = Color.blue;
		else if (color.equals("red")) ((Paint) can).cr = Color.red;
		else if (color.equals("green")) ((Paint) can).cr = Color.green;
		else if (color.equals("erase")) ((Paint) can).cr = Color.white;
		else if (color.equals("all")) can.getGraphics().clearRect(0, 0, can.getWidth(), can.getHeight());
	}

	public void appendChtting(String message) {
		chattingTextArea.append(message + "\n");
		chattingTextArea.setCaretPosition(chattingTextArea.getDocument().getLength()); // 스크롤 자동으로 내리기
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}


	public void setRound(int round) {
		roundLabel.setText("라운드: " + round);
	}
}
