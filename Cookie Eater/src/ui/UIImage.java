package ui;

import java.awt.*;

import ce3.*;

public class UIImage extends UIElement{
	
	private double ratio;
	private Image img;
	
	public UIImage(Game frame, int x, int y, double r, Image i) {
		super(frame,x,y);
		xPos = x;
		yPos = y;
		ratio = r;
		img = i;
	}
	
	public void setImage(Image i) {
		img = i;
	}

	public void paint(Graphics g) {
		if(img!=null)g.drawImage(img, xPos, yPos, (int)(.5+img.getWidth(null)*ratio), (int)(.5+img.getHeight(null)*ratio), null);
	}
	
	
}
