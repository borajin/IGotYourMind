package Client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GameManager {
	User user;
	GameFrame gameFrame;
	SendRecive sendRecive;
	int count;

	String[] answerList = new String[50];
	int round = 0;
	String answer = "";

	public boolean imCorrect = false;
	public boolean someWhoCorrect = false;

	GameManager(User user, GameFrame gameFrame, SendRecive sendRecive) {
		this.user = user;
		this.gameFrame = gameFrame;
		this.sendRecive = sendRecive;
	}

	public void myTurnSetting() {
		startRound();
		gameFrame.setChatting(false);
		gameFrame.setDrawing(true);
		gameFrame.setButtonEnabled(true);
		gameFrame.setAnswerText(answer);
	}
	
	void otherTurnSetting() {
		startRound();
		gameFrame.setChatting(true);
		gameFrame.setDrawing(false);
		gameFrame.setButtonEnabled(false);
		gameFrame.setAnswerText("What is answer?");
	}

	public void startRound() {
		answer = answerList[round];
		gameFrame.setDrawingColor("all");
		gameFrame.setDrawingColor("black");
		round++;
		gameFrame.setRound(round);
	}

	public void answerCorrect() {
		sendRecive.send("game@" + user.userName + "Á¤´ä!");
		user.score++;
	}

	public void endGame() {
		otherTurnSetting();
		gameFrame.setDrawingColor("all");
		gameFrame.setDrawingColor("black");
		gameFrame.setRound(0);
		sendRecive.send("result@" + user.userName + "@" + user.score);
		gameFrame.setAnswerText("You can't draw, not yet.");
		gameFrame.setStartButton(true);
	}

	public void startGame() {
		File file = new File("answer\\exam.txt");
		FileReader filereader;
		try {
			filereader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(filereader);
			String line = "";
			int i = 0;
			while((line = bufReader.readLine()) != null){
				answerList[i] = line;
				i++;
			}
			bufReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		user.score = 0;
		round = 0;
		gameFrame.setStartButton(false);
	}

}
