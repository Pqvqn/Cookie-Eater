package ui;

import java.util.*;

import ce3.*;
import items.*;
import levels.*;
import menus.*;
import menus.MenuButton.*;

public class UISettings extends UIElement{
	
	private boolean visible;
	//menu selector button
	private MenuButton sel;
	private SubmenuHandler menuHandler;
	
	public UISettings(Board frame, int x, int y) {
		super(frame,x,y);
		makeButtons();	
	}
	
	public void makeButtons() {
		menuHandler = new SubmenuHandler("MAIN");
		OnClick oc;
		
		//toggles which menu is selected
		sel = new MenuButton(board, this, null, new String[] {"MAIN","DEBUG"}, false, 1300,700,400,200);
		oc = () -> {
			//select menu from list based on button state
			menuHandler.displayMenu(sel.getState());
		};
		sel.setClick(oc);
		menuHandler.addButton("MAIN",sel);
		menuHandler.addButton("DEBUG",sel);
				
		//volume control
		MenuButton vol = new MenuButton(board, this, null, new String[] {"mutevol.png", "highvol.png", "midvol.png", "lowvol.png"}, true, 600,500,400,200);
		oc = () -> {
			//select volume from list based on button state
			int[] vols = {0,10,20};
			int a = vol.currentState()-1;
			if(a>-1) {
				board.audio.setMute(false);
				board.audio.setVolumeReduction(vols[a]);
			}else {
				board.audio.setMute(true);
			}
		};
		vol.setClick(oc);
		menuHandler.addButton("MAIN",vol);
		
		//set controls
		int[] keyBinds = {Controls.UPKEY,Controls.DOWNKEY,Controls.LEFTKEY,Controls.RIGHTKEY};
		String[] keyNames = {"up","down","left","right"};
		int[][] keyPos = {{300,400},{300,520},{180,520},{420,520}};
		for(int i=0; i<4; i++) {
			MenuButton keyset = new MenuButton(board, this, null, 
					new String[] {java.awt.event.KeyEvent.getKeyText(board.controls.get(0).getKeyBind(0,keyBinds[i]))}, false, keyPos[i][0],keyPos[i][1],100,100);
			final String keyname = keyNames[i];
			final int keybind = keyBinds[i];
			oc = () -> {
				//ask board to await key press to reassign
				keyset.setCurrStateValue(keyname+" =");
				board.controls.get(0).awaitKeyBind(keyset,0,keybind);
			};
			keyset.setClick(oc);
			menuHandler.addButton("MAIN",keyset);
		}
		
	
		
		//gives the player a shield
		MenuButton givsh = new MenuButton(board, this, null, new String[] {"give 1 shield"}, false, 120,475,200,100);
		oc = () -> {
			board.player.addShields(1);
		};
		givsh.setClick(oc);
		menuHandler.addButton("DEBUG",givsh);
		
		//kills player to return to first floor
		MenuButton reset = new MenuButton(board, this, null, new String[] {"end run"}, false, 120,325,200,100);
		oc = () -> {
			board.player.kill();
		};
		reset.setClick(oc);
		menuHandler.addButton("DEBUG",reset);
		
		//kills player to return to first floor
		MenuButton title = new MenuButton(board, this, null, new String[] {"title screen"}, false, 120,25,200,100);
		oc = () -> {
			board.player.kill();
			board.ui_tis.show();
		};
		title.setClick(oc);
		menuHandler.addButton("DEBUG",title);
		
		//moves to next floor
		MenuButton advance = new MenuButton(board, this, null, new String[] {"advance floor"}, false, 120,175,200,100);
		oc = () -> {
			if(!board.inConvo())board.player.win();
		};
		advance.setClick(oc);
		menuHandler.addButton("DEBUG",advance);
		
		//gives player 10 cookies
		MenuButton givco = new MenuButton(board, this, null, new String[] {"give 10 cookies"}, false, 120,625,200,100);
		oc = () -> {
			board.player.pay(10);
		};
		givco.setClick(oc);
		menuHandler.addButton("DEBUG",givco);

		//gives player item in name
		String[] powerups = {"Boost","Circle","Chain","Field","Hold","Recycle","Shield","Slowmo","Ghost",
				"Return","Teleport","Repeat","Rebound","Clone","Ricochet","Shrink","Autopilot","Flow","Recharge",
				"Melee","Projectile"}; //list of all items to make buttons for
		for(int i=0; i<powerups.length; i++) {
			String pw = powerups[i];
			int rows = 6, xs=400, ys=100, gap=50, wid=200, hei=100; //values for placement of buttons
			
			MenuButton givit = new MenuButton(board, this, null, new String[] {"give "+pw}, false, xs+(i/rows*(wid+gap)),(ys+((hei+gap)*(i%rows))),wid,hei);
			oc = () -> {
				board.player.addItem(0,Level.generateItem(board,pw));
			};
			givit.setClick(oc);
			menuHandler.addButton("DEBUG",givit);
		}
		
		
	}
	
	
	
	public void show(boolean s) {
		menuHandler.showFull(s);
		sel.resetState();
		if(visible!=s) {
			if(s) {
				if(!board.draw.getUIList().contains(this))board.draw.addUI(this);
			}else {
				if(board.draw.getUIList().contains(this))board.draw.removeUI(this);
			}
		}
		visible = s;
	}
	
	public boolean isVisible() {
		return visible;
	}

}
