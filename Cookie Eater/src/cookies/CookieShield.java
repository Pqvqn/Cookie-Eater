package cookies;

import java.awt.Color;
import java.awt.Graphics;

import ce3.*;
//import levels.*;

public class CookieShield extends CookieStore{
	
	public CookieShield(Board frame, int startx, int starty) {
		super(frame,startx,starty);
		price = board.currFloor.getShieldCost();
		name = "Shield";
	}
	public boolean purchase() {
		if(board.cash>=price) {
			board.shields++;
			board.cash-=price;
			return true;}
		return false;
	}
	public void paint(Graphics g) {
		g.setColor(new Color(20,170,180,100));
		g.fillOval((int)(.5+x-radius*board.currFloor.getScale()), (int)(.5+y-radius*board.currFloor.getScale()), (int)(.5+radius*board.currFloor.getScale()*2), (int)(.5+radius*board.currFloor.getScale()*2));
	}
}