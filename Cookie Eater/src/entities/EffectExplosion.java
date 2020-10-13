package entities;

import java.awt.*;

import ce3.*;
import cookies.Cookie;
import cookies.CookieItem;
import cookies.CookieShield;
import cookies.CookieStat;

public class EffectExplosion extends Effect{

	private SegmentCircle boom; //segment body
	private double inc; //amount to increase radius by per tick
	private double maxRad;
	
	public EffectExplosion(Board frame, int cycletime, int xp, int yp, double rad, int time, Entity initiator) {
		super(frame,cycletime,xp,yp,time,initiator);
		inc = (double)cycletime/time * rad;
		maxRad = rad;
		mass = 100;
		setRadius(0);
		buildBody();
		orientParts();
	}
	
	public void runUpdate() {
		if(ded)return;
		super.runUpdate();
		setRadius(getRadius()+inc); //increase radius
		mass = (int)(.5+((double)getRadius()/maxRad)*100);
		if(getRadius()>maxRad)kill(); //kill if too large
		orientParts();
	}
	protected void buildBody() {
		parts.add(boom = new SegmentCircle(board,this,x,y,getRadius(),0));
	}
	//remove explosion
	public void kill() {
		super.kill();
		setRadius(0);
	}
	
	//explosions cannot trigger shields, overriding
	public void triggerShield() {
	}
	
	public double getXVel() {return inc;}
	public double getYVel() {return inc;}
	
	public void orientParts() {
		boom.setLocation(x,y);
		boom.setSize(getRadius());
		super.orientParts();
	}
	public void paint(Graphics g) {
		super.paint(g);
		if(boom!=null)boom.paint(g);
		int opac = 255-(int)(.5+((double)getRadius()/maxRad)*255);
		g.setColor(new Color(255,255,255,opac));
		g.fillOval((int)(.5+boom.getCenterX()-boom.getRadius()),(int)(.5+boom.getCenterY()-boom.getRadius()),(int)(.5+boom.getRadius()*2),(int)(.5+boom.getRadius()*2));
	}
	
}