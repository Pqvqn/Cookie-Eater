package levels;

import java.util.*;

import ce3.*;

public class ArenaRound extends Arena{

	public ArenaRound(Game frame, Board gameboard, String id) {
		super(frame,gameboard,id);
		name = "Hostile Tunnels";
		nameAbbrev = "enm";
	}
	public ArenaRound(Game frame, Board gameboard, ArrayList<Level> prev, ArrayList<Level> next, SaveData sd) {
		super(frame, gameboard, prev, next, sd);
	}
}
