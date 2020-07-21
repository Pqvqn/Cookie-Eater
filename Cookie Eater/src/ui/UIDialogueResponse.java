package ui;

import java.awt.*;

import ce3.*;

public class UIDialogueResponse extends UIElement{

	//private UIText text;
	private UIRectangle backing;
	private boolean selected;
	private int width;
	
	public UIDialogueResponse(Board frame, String words, int x, int y) {
		super(frame,x,y);
		width = Math.max(words.length()*17,200);
		parts.add(new UIText(board,xPos+5,yPos+30,words,new Color(255,255,255,150),new Font("Arial",Font.BOLD,30))); //text
		parts.add(backing = new UIRectangle(board,xPos,yPos,width,40,new Color(0,0,0,50),true));
		selected = false;
	}
	
	public void select(boolean s) {
		selected = s;
		backing.setColor((selected)?new Color(255,255,255,50):new Color(0,0,0,50));
	}
	
	public int getWidth() {return width;}
	
	
}
