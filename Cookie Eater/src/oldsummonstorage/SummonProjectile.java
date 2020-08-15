package oldsummonstorage;

import java.awt.*;

import ce3.*;
import cookies.*;
import entities.Eater;
import entities.Entity;
import levels.*;

public class SummonProjectile extends Summon{
	
	private double speed, xSpeed, ySpeed;
	private double angle;
	private double angle_offset;
	private double radius;
	
	public SummonProjectile(Board frame, Entity summoner, double s, double offset) {
		super(frame, summoner);
		speed = s*board.currFloor.getScale();
		angle_offset = offset;
		mass = 100;
	}
	public void setSpeed(double s) {
		speed = s*board.currFloor.getScale();
	}
	public void prepare() {
		
		x=user.getX();
		y=user.getY();
		angle = userVelAngle()+angle_offset;
	}
	public void initialize() {
		mass = 100*(user.getTotalRadius()/(Eater.DEFAULT_RADIUS*board.currFloor.getScale()));
		radius = user.getTotalRadius()*.3;
		xSpeed = Math.cos(angle)*speed;
		ySpeed = Math.sin(angle)*speed;
		user.collideAt(this,x+xSpeed,y+ySpeed,xSpeed,ySpeed,mass);
	}
	public void execute() {
		if(isDed())return;
		mass = 100*(user.getTotalRadius()/(Eater.DEFAULT_RADIUS*board.currFloor.getScale()));
		radius = user.getTotalRadius()*.3;
		x+=xSpeed;
		y+=ySpeed;
		super.execute();
	}
	public void end(boolean interrupted) {
		
	}
	public void kill() {
		ded = true;
	}
	public void collisionCookie(Cookie c) {
		user.hitCookie(c);
	}
	public void collisionWall(Wall w, boolean ghost, boolean shield) {
		if(shield) {
			double[] point = rectHitPoint(w.getX(),w.getY(),w.getW(),w.getH());
			collisionEntity(w,point[0],point[1],999999999,0,0,ghost,shield);
			return;
		}
		if(ghost)return;
		kill();
	}
	//if circle intersects an edge
	public boolean hitsCircle(double cX, double cY, double cR) {
		return Level.lineLength(x, y, cX, cY) <= cR+radius;
	}
	//if rectangle intersects an edge
	public boolean hitsRect(double rX, double rY, double rW, double rH) {
		return Level.collidesCircleAndRect(x,y,radius,rX,rY,rW,rH);		
	}
	public void collisionEntity(Object b, double hx, double hy, double omass, double oxv, double oyv, boolean ghost, boolean shield) {
		if(shield) {
			double pvx = (hx-x), pvy = (hy-y);
			double oxm = oxv*omass, oym = oyv*omass;
			double txm = xSpeed*mass, tym = ySpeed*mass;
			double oProj = -(oxm*pvx+oym*pvy)/(pvx*pvx+pvy*pvy);
			double tProj = Math.abs((txm*pvx+tym*pvy)/(pvx*pvx+pvy*pvy));
			double projdx = (oProj+tProj)*pvx,projdy = (oProj+tProj)*pvy;
			
			double proejjjg = (xSpeed*pvy+ySpeed*-pvx)/(pvx*pvx+pvy*pvy);
			
			xSpeed=pvy*proejjjg-projdx/mass;
			ySpeed=-pvx*proejjjg-projdy/mass;
			super.collisionEntity(b, hx, hy, omass, oxv, oyv, ghost, shield);
			return;
		}
		if(ghost)return;
		super.collisionEntity(b, hx, hy, omass, oxv, oyv, ghost, shield);
		kill();
	}
	public double[] circHitPoint(double cx, double cy, double cr) {
		double[] ret = {x,y};
		double ratio = radius/Level.lineLength(cx, cy, x, y);
		ret[0] = (cx-x)*ratio+x;
		ret[1] = (cy-y)*ratio+y;
		return ret;
	}
	public double[] rectHitPoint(double rx, double ry, double rw, double rh) { 
		return Level.circAndRectHitPoint(x, y, radius, rx, ry, rw, rh);
	}
	public double getXVel() {return xSpeed;}
	public double getYVel() {return ySpeed;}
	public void paint(Graphics2D g2) {
		if(ded)return;
		g2.setColor(Color.WHITE);
		if(user.getGhosted())g2.setColor(new Color(255,255,255,100));
		if(user.getShielded())g2.setColor(new Color(50,200,210));
		g2.rotate(angle,x,y);
		g2.fillOval((int)(.5+x-radius),(int)(.5+y-radius),(int)(.5+radius*2),(int)(.5+radius*2));
		
		
	}
}