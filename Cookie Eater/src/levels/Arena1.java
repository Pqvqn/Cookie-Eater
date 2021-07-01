package levels;

import java.util.*;

import ce3.*;

public class Arena1 extends Arena{

	public Arena1(Game frame, Board gameboard, String id) {
		super(frame,gameboard,id);
		name = "Forest Entrance";
		nameAbbrev = "for";
	}
	public Arena1(Game frame, Board gameboard, ArrayList<Level> prev, ArrayList<Level> next, SaveData sd) {
		super(frame, gameboard, prev, next, sd);
	}
}
