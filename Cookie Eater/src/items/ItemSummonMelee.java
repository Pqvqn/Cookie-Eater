package items;

import java.util.*;

import ce3.*;
import entities.*;

public class ItemSummonMelee extends Item{
	
	private Summon summon;
	private int hitpoints;
	
	public ItemSummonMelee(Game frame, Board gameboard) {
		super(frame,gameboard);
		hitpoints = 0;
		name = "Melee";
		desc="Items affect a separate summoned entity that is anchored to the player. `Amplify- Summon gains health";
	}
	public void prepare() {
		//user's items given to summon
		user.addSummon(summon = new Summon(game,board,user,game.getCycle(),true,hitpoints));
		summon.eatItems();
		summon.activateSpecials();
		summon.special(user.getCurrentSpecial());
	}
	public void initialize() {
		//summon.prepareItems();
	}
	public void execute() {
		if(checkCanceled())return;
		if(summon==null)prepare(); //sometimes prepare is skipped?
		if(!summon.isDed()) {
			//summon's item progress given to user
			ArrayList<Double> kep = new ArrayList<Double>();
			ArrayList<Double> las = summon.getSpecialFrames();
			for(int i=0; i<las.size(); i++)kep.add(las.get(i));
			user.setSpecialFrames(kep); //keep player special use same as summon's
		}else {
			//dead summons don't set player counter
		}
		
	}
	public void end(boolean interrupted) {
		//undo user summon thing
		summon.regurgitateItems();
		summon.regurgitateCookies();
		user.removeSummon(summon);
	}
	public void amplify() {
		super.amplify();
		hitpoints++;
	}
	public void deamplify() {
		super.deamplify();
		hitpoints--;
	}
}
