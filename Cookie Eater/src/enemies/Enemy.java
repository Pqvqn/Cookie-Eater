package enemies;

import java.awt.*;
import java.util.*;

import ce3.*;
import cookies.*;
import levels.*;

public abstract class Enemy {

	protected ArrayList<Segment> parts;
	protected double xPos,yPos;
	protected Board board;
	protected Eater player;
	protected double mass;
	protected double x_vel, y_vel;
	protected double fric;
	protected double constfric;
	protected int shields;
	protected int shield_duration;
	protected int shield_frames;
	protected boolean steals;
	protected ArrayList<Cookie> stash;
	
	public Enemy(Board frame, double x, double y) {
		board = frame;
		xPos = x;
		yPos = y;
		player = board.player;
		parts = new ArrayList<Segment>();
		x_vel=0;
		y_vel=0;
		mass = 100;
		shield_duration = 60*board.getAdjustedCycle();
		shield_frames = 0;
		stash = new ArrayList<Cookie>();
		buildBody();
	}
	//create parts for the enemy
	protected void buildBody() {
		
	}
	//runs each cycle
	public void runUpdate() {
		if(offStage())kill();
		fric = constfric*board.currFloor.getScale();
		xPos+=x_vel;
		yPos+=y_vel;
		if(Math.abs(x_vel)>fric) {
			x_vel-=Math.signum(x_vel)*fric;
		}else {
			x_vel=0;
		}
		if(Math.abs(y_vel)>fric) {
			y_vel-=Math.signum(y_vel)*fric;
		}else {
			y_vel=0;
		}
		if(shield_frames>0)shield_frames++;
		if(shield_frames>=shield_duration)
			shield_frames=0;
		testCollisions();
	}
	//given point, adjusts velocity for bouncing off from that point
	public void collideAt(double x, double y, double oxv, double oyv, double om) {
		double pvx = (x-xPos), pvy = (y-yPos);
		double oxm = oxv*om, oym = oyv*om;
		double txm = x_vel*mass, tym = y_vel*mass;
		double oProj = Math.abs((oxm*pvx+oym*pvy)/(pvx*pvx+pvy*pvy));
		double tProj = Math.abs((txm*pvx+tym*pvy)/(pvx*pvx+pvy*pvy));
		double projdx = (oProj+tProj)*pvx,projdy = (oProj+tProj)*pvy;
		
		double proejjjg = (x_vel*pvy+y_vel*-pvx)/(pvx*pvx+pvy*pvy);
		
		x_vel=pvy*proejjjg-projdx/mass;
		y_vel=-pvx*proejjjg-projdy/mass;
	}
	//when hit wall
	public void collideWall() {
		shield_duration = 60*board.getAdjustedCycle();
		if(shield_frames==0) {
			if(shields--<=0)kill();
			shield_frames++;
		}
	
	}
	//if offstage
	public boolean offStage() {
		return xPos<0||xPos>board.X_RESOL||yPos<0||yPos>board.Y_RESOL;
	}
	//tests all collisions
	public void testCollisions() {
		for(int j=0; j<parts.size(); j++) {
			for(int i=0; i<board.walls.size(); i++) { //for every wall, test if any parts impact
				Wall w = board.walls.get(i);
				if(parts.get(j).collidesWithRect(w.getX(),w.getY(),w.getW(),w.getH())){
					collideAt(parts.get(j).rectHitPoint(w.getX(),w.getY(),w.getW(),w.getH())[0],
							parts.get(j).rectHitPoint(w.getX(),w.getY(),w.getW(),w.getH())[1],
							0,0,999999999);
					collideWall();
				}
			}
			if(parts.get(j).collidesWithCircle(player.getX(),player.getY(),player.getTotalRadius())) { //test if hits player
				collideAt(parts.get(j).circHitPoint(player.getX(),player.getY(),player.getTotalRadius())[0],
						parts.get(j).circHitPoint(player.getX(),player.getY(),player.getTotalRadius())[1],
						player.getXVel(),player.getYVel(),player.getMass());
				player.collideAt(parts.get(j).circHitPoint(player.getX(),player.getY(),player.getTotalRadius())[0],
						parts.get(j).circHitPoint(player.getX(),player.getY(),player.getTotalRadius())[1], x_vel, y_vel, mass);
			}
			for(int i=0; i<board.cookies.size(); i++) { //for every cookie, test if any parts impact
				Cookie c = board.cookies.get(i);
				if(parts.get(j).collidesWithCircle(c.getX(),c.getY(),c.getRadius())) {
					stash.add(c);
					board.cookies.remove(c);
				}
			}
		}
	}
	//deletes this enemy
	public void kill() {
		while(!stash.isEmpty()) {
			double ang = Math.random()*Math.PI*2;
			double r=80*board.currFloor.getScale();
			double addx = r*Math.cos(ang), addy = r*Math.sin(ang);
			boolean hit = false;
			for(Wall w:board.walls) {
				if(Level.collidesCircleAndRect((int)(.5+xPos+addx),(int)(.5+yPos+addy),stash.get(0).getRadius(),w.getX(),w.getY(),w.getW(),w.getH())) {
					hit = true;
				}
			}
			if((int)(.5+xPos+addx)<0||(int)(.5+xPos+addx)>board.X_RESOL||(int)(.5+yPos+addy)<0||(int)(.5+yPos+addy)>board.Y_RESOL)hit=true;
			if(!hit) {
				stash.get(0).setPos((int)(.5+xPos+addx),(int)(.5+yPos+addy));
				board.cookies.add(stash.remove(0));
			}
		}
		board.enemies.remove(this);
	}
	//is this in shield stun
	public boolean isShielded() {
		return shield_frames>0;
	}
	public double getX() {return xPos;}
	public double getY() {return yPos;}
	//draws
	public void paint(Graphics g) {
		for(int i=0; i<parts.size(); i++) {
			parts.get(i).paint(g);
		}
	}
	public double totalVel() {
		return Math.sqrt(x_vel*x_vel+y_vel*y_vel);
	}
}