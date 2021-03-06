package levels;

import java.util.*;

import ce3.*;

public class Store2 extends Store{
	
	public Store2(Game frame, Board gameboard, String id) {
		super(frame,gameboard,id);
		name = "Dungeon Foyer";
		nameAbbrev = "dun";
		scale = .95;
		int[][][] vs = {{{board.x_resol/2-200,board.y_resol-110} , {board.x_resol/2+200,board.y_resol-110} , {board.x_resol/2,board.y_resol-110} , {board.x_resol/2-200,board.y_resol-280} , {board.x_resol/2+200,board.y_resol-280} , {board.x_resol/2,board.y_resol-280}} ,
				{{board.x_resol/2-200,110} , {board.x_resol/2+200,110} , {board.x_resol/2,110} , {board.x_resol/2-200,280} , {board.x_resol/2+200,280} , {board.x_resol/2,280}} };
		vendorSpaces = vs;
		int[][] ps = {{board.x_resol/2-300,400}};
		passerbySpaces = ps;
		int[][] ms = {{board.x_resol-200,board.y_resol/2+180},{board.x_resol-390,315},{board.x_resol-390,board.y_resol-315},{board.x_resol-185,board.y_resol/2}};
		mechanicSpaces = ms;
	}
	public Store2(Game frame, Board gameboard, ArrayList<Level> prev, ArrayList<Level> next, SaveData sd) {
		super(frame, gameboard, prev, next, sd);
	}
	
}
