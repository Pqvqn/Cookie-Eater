package levels;


import ce3.*;
import cookies.CookieItem;
import entities.*;

import java.awt.*;
import java.util.*;

public class Floor4 extends Level{
	
	private int[][] areas = {{0,board.X_RESOL/3,0,board.Y_RESOL/2}, //regions of board - one node per until all filled
			{0,board.X_RESOL/3,board.Y_RESOL/2,board.Y_RESOL},
			{board.X_RESOL/3,2*board.X_RESOL/3,0,board.Y_RESOL/2},
			{board.X_RESOL/3,2*board.X_RESOL/3,board.Y_RESOL/2,board.Y_RESOL},
			{2*board.X_RESOL/3,board.X_RESOL,0,board.Y_RESOL/2},
			{2*board.X_RESOL/3,board.X_RESOL,board.Y_RESOL/2,board.Y_RESOL}};
	
	public Floor4(Game frame) {
		this(frame,null);
	}
	public Floor4(Game frame, Level nextFloor) {
		super(frame, nextFloor);
		name = "Frozen Chambers";
		nameAbbrev = "ice";
		next = nextFloor;
		scale = .85;
		minDecay = 90;
		maxDecay = 3000;
		nodes = new ArrayList<int[]>();
		lines = new ArrayList<int[]>();
		bgColor = new Color(50,60,60);
		wallColor = new Color(200,210,210);
	}
	
	public void build() {
		super.build();
		genPaths(9, 100, 150, 80, 10, areas); //num nodes, min radius around nodes, max radius around nodes, radius around lines, nodes per line, board regions to fill
		//genWalls(50, 40, 600); //wall separation, wall min size, wall max size
		genWalls(20, 40, 300);
		nodes = new ArrayList<int[]>();
		lines = new ArrayList<int[]>();
	}
	
	
	
	public void placeCookies() {
		super.placeCookies(5,(int)(100*scale));
	}
	
	public void spawnEnemies() { 
		int cycle = game.getCycle();
		ArrayList<String> possible = new ArrayList<String>();
		possible.add("Field");
		possible.add("Boost");
		possible.add("Boost");
		possible.add("Ricochet");
		possible.add("Ricochet");
		possible.add("Teleport");
		possible.add("Shield");
		possible.add("Ghost");
		possible.add("Return");
		possible.add("Circle");
		possible.add("Circle");
		possible.add("Shrink");
		for(int i=0;i<Math.random()*3;i++) {
			Enemy e;
			spawnAtRandom(e = new EnemyBlob(game,cycle,0,0));
			if(Math.random()>.3)e.giveCookie(new CookieItem(game,0,0,Level.generateItem(game,possible.get((int)(Math.random()*possible.size()))),0));
		}
		
		for(int i=0;i<(int)(Math.random()*2);i++) {
			spawnAtRandom(new EnemyGlob(game,cycle,0,0));
		}
		for(int i=0;i<(int)(Math.random()*2);i++) {
			spawnAtRandom(new EnemySlob(game,cycle,0,0));
		}
		
	}

}
