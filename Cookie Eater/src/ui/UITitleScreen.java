package ui;

import java.awt.*;
import java.util.*;

import ce3.*;
import menus.*;
import menus.MenuButton.*;

public class UITitleScreen extends UIElement{

	//private UIRectangle backing;
	//private UIText title;
	private boolean visible;
	private SubmenuHandler menuHandler;
	
	public UITitleScreen(Game frame, int x, int y) {
		super(frame, x, y);
		parts.add(new UIRectangle(game,0,0,Board.DEF_X_RESOL,Board.DEF_Y_RESOL,Color.GRAY,true)); //backing
		parts.add(new UIText(game,200,350,"Cookie Eater 3",Color.WHITE,new Font("Arial",Font.BOLD,160))); //title
		makeButtons();
	}
	
	public void makeButtons() {
		//Board board = game.board;
		OnClick oc;
		if(menuHandler!=null)menuHandler.delete();
		menuHandler = new SubmenuHandler("MAIN");
		
		MenuButton quit = new MenuButton(game,this,null,new String[]{"X Quit"},false,100,700,120,120);
		oc = () -> {
			//close program
			System.exit(0);
		};
		quit.setClick(oc);
		menuHandler.addButton("MAIN",quit);
		
		MenuButton back = new MenuButton(game,this,null,new String[]{"< Back"},false,100,700,120,120);
		oc = () -> {
			//move to main title screen page options
			menuHandler.displayMenu("MAIN");
		};
		back.setClick(oc);
		menuHandler.addButton("NEWGAME",back);
		menuHandler.addButton("LOADGAME",back);
		
		MenuButton gnew = new MenuButton(game,this,null,new String[]{"New Game"},false,700,700,300,200);
		oc = () -> {
			//move to new game options
			menuHandler.displayMenu("NEWGAME");
		};
		gnew.setClick(oc);
		menuHandler.addButton("MAIN",gnew);
		
		MenuButton gload = new MenuButton(game,this,null,new String[]{"Load Game"},false,300,700,300,200);
		oc = () -> {
			//move to loading game options
			menuHandler.displayMenu("LOADGAME");
		};
		gload.setClick(oc);
		menuHandler.addButton("MAIN",gload);
		
		MenuButton mode = new MenuButton(game, this, null, new String[] {"Mode: Levels","Mode: PvP"}, false, 300,700,200,200);
		MenuButton dungeon = new MenuButton(game, this, null, new String[] {"Dungeon: Vaults","Dungeon: Inners","Dungeon: Training"}, false, 800,700,200,200);
		MenuButton pcount = new MenuButton(game, this, null, new String[] {"1 P","2 P","3 P","4 P"}, false, 550,700,200,200);
		oc = () -> {
			//switch selected dungeon generation
		};
		dungeon.setClick(oc);
		menuHandler.addButton("NEWGAME",dungeon);
		oc = () -> {
			//switch selected game mode
		};
		mode.setClick(oc);
		menuHandler.addButton("NEWGAME",mode);
		oc = () -> {
			//switch number of players
		};
		pcount.setClick(oc);
		menuHandler.addButton("NEWGAME",pcount);
		
		//Scanner sc = new Scanner(System.in);
		MenuButton start = new MenuButton(game, this, null, new String[] {"START"}, false, 1300,700,400,200);
		oc = () -> {
			int[] modes = {Board.LEVELS,Board.PVP};
			

			//System.out.println("savename: "); //temp
			String sn = null;
			UIInputType textInput = new UIInputType(game, 500, 500);
			textInput.startText("Savename: ");
			parts.add(textInput);
			game.addKeyListener(textInput);
			while(textInput.getSubmission()==null) {
				game.freeze(1);
				textInput.update();
				game.draw.repaint();
			}
			game.removeKeyListener(textInput);
			parts.remove(textInput);
			sn = textInput.getSubmission();
			//if(sc.hasNextLine())sn = sc.nextLine();
			game.createDungeon(sn,modes[mode.currentState()],dungeon.currentState(),pcount.currentState()+1);
			game.ui_set.show(false);
			//start game, load board from other buttons
			this.hide();
		};
		start.setClick(oc);
		menuHandler.addButton("NEWGAME",start);
		
		Iterator<String> it = game.boards.keySet().iterator();
		int xp = 300;
		while(it.hasNext()) {
			String saveName = it.next();
			MenuButton loadsave = new MenuButton(game, this, null, new String[] {saveName}, false,xp, 700, 200,200);
			oc = () -> {
				//load saved board
				game.loadDungeon(saveName);
				game.ui_set.show(false);
				this.hide();
			};
			loadsave.setClick(oc);
			menuHandler.addButton("LOADGAME",loadsave);
			xp += 250;
		}
	}
	
	//display title screen and lock board
	public void show() {
		if(visible)return;
		makeButtons();
		visible = true;
		game.draw.addUI(this);
		menuHandler.showFull(true);
	}
	//remove title screen and free board
	public void hide() {
		if(!visible)return;
		visible = false;
		game.draw.removeUI(this);
		menuHandler.showFull(false);
	}
	
	public boolean isVisible() {
		return visible;
	}

}
