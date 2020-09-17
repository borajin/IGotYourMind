package Client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class Paint extends Canvas{
	public Color cr = Color.black;
	
	//처음에 까만색 점 안찍히게 하기 위해서 x,y -값 지정
	public int x = -50; 
	public int y = -50; 
	
	public boolean isAllerase = false;
	
	@Override
	 public void paint(Graphics g){
	  g.setColor(cr);
	  g.fillOval(x, y, 10, 10); // x, y 지점에 70,70 크기의 원 그리기
	 }
	
	@Override
	 public void update(Graphics g){
	  paint(g);
	 }
	
	
}
