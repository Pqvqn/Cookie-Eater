package entities;

import java.awt.*;
import java.awt.geom.*;

import ce3.*;

public class SegmentCircle extends Segment{

	private double radius;
	
	public SegmentCircle(Board frame, Entity host, double x, double y, double rad, double a, String id) {
		super(frame,host,x,y,a,id);
		radius = rad;
		size = rad;
	}
	public SegmentCircle(Board frame, Entity host, SaveData sd) {
		super(frame, host, sd);
		radius = sd.getDouble("radius",0);
	}
	public SaveData getSaveData() {
		SaveData data = super.getSaveData();
		data.addData("type","circ");
		data.addData("radius",radius);
		return data;
	}
	/*public boolean collidesWithRect(boolean extra, double x, double y, double w, double h, double a) {
		return board.currFloor.collidesCircleAndRect(xPos, yPos, (extra)?getTotalRadius():getRadius(), x, y, w, h, a);
	}
	public boolean collidesWithCircle(boolean extra, double x, double y, double r) {
		return Level.lineLength(x, y, xPos, yPos) <= r+((extra)?getTotalRadius():getRadius());
	}
	/*public boolean collidesWithSummon(boolean extra, Summon2 s) {
		return s.hitsCircle(xPos,yPos,(extra)?getTotalRadius():getRadius());
	}
	public double[] rectHitPoint(boolean extra, double rx, double ry, double rw, double rh, double ra) {
		return board.currFloor.circAndRectHitPoint(xPos,yPos,(extra)?getTotalRadius():getRadius(),rx,ry,rw,rh,ra);
	}
	public double[] circHitPoint(boolean extra, double cx, double cy, double cr) {
		return board.currFloor.circAndCircHitPoint(xPos,yPos,(extra)?getTotalRadius():getRadius(),cx,cy,cr);
	}
	/*public double[] summonHitPoint(boolean extra, Summon2 s) {
		return s.circHitPoint(xPos, yPos, (extra)?getTotalRadius():getRadius());
	}*/
	
	public double getRadius() {return radius*scale;}
	public void setSize(double s) {
		super.setSize(s);
		radius=s;}
	public double getTotalRadius() {return getRadius()+extra_size*scale;}
	public Area getArea(boolean extra) {
		double r = (extra)?getTotalRadius():getRadius();
		Ellipse2D.Double c = new Ellipse2D.Double(xPos-r,yPos-r,r*2,r*2);
		return new Area(c);
	}
	public Rectangle getBounding(boolean extra) {
		double radd = (extra)?getTotalRadius():getRadius();
		return new Rectangle((int)(.5+xPos-radd),(int)(.5+yPos-radd),(int)(.5+radd*2),(int)(.5+radd*2));
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.fillOval((int)(.5+xPos-radius*scale), (int)(.5+yPos-radius*scale), (int)(.5+radius*scale*2), (int)(.5+radius*scale*2));
	}
	
}
