package levels;

import java.util.*;

import ce3.*;
//import entities.*;

public abstract class Arena extends Level{
	
	public Arena(Game frame, Board gameboard, String id) {
		super(frame,gameboard,id);
	}
	public Arena(Game frame, Board gameboard, ArrayList<Level> nextFloor, SaveData sd) {
		super(frame, gameboard, nextFloor, sd);
	}
	public boolean installPickups() {return true;}
}
