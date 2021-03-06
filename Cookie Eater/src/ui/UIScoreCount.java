package ui;

import java.awt.*;
import java.text.DecimalFormat;

import ce3.*;

public class UIScoreCount extends UIElement{

	private UIText cash, cooks;
	
	public UIScoreCount(Game frame, int x, int y) {
		super(frame,x,y);
		parts.add(cash = new UIText(game,x+20,y+40,"",new Color(120,120,120,200),new Font("Arial",Font.BOLD,30)));
		parts.add(cooks = new UIText(game,x,y,"",new Color(255,255,255,200),new Font("Arial",Font.BOLD,40)));
	}
	public void update(double ch, int ck, int nd) {
		DecimalFormat rounder = new DecimalFormat("#.#");
		cash.setText("$"+rounder.format(ch+.04));
		cooks.setText(ck+"/"+nd);
	}
}
