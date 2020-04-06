package ce3;

import java.awt.Color;
import java.awt.Graphics;

public class Wall {
	
	Board board;
	int x,y;
	int w,h;
	
	public Wall(Board frame, int xPos, int yPos, int width, int height) {
		board = frame;
		x = xPos;
		y = yPos; 
		w = width;
		h = height;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.RED.darker());
		g.fillRect(x,y,w,h);
	}
	public int getX() {return x;}
	public int getY() {return y;}
	public int getW() {return w;}
	public int getH() {return h;}

}
