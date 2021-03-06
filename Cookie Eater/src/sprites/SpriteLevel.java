package sprites;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import ce3.*;
import mechanisms.*;

public class SpriteLevel extends Sprite{

	private ArrayList<Wall> wallList;
	private Image wall;
	private Image floor;
	//private int wid,hei;
	private String lvl;
	private String prefix;
	
	public SpriteLevel(Board frame, ArrayList<Wall> w) throws IOException {
		super(frame);
		wallList = w;
		//wall = ImageIO.read(new File("Cookie Eater/src/resources/level/grad23.png"));
		//floor = ImageIO.read(new File("Cookie Eater/src/resources/level/grad23.png"));
		imgs.add(wall);
		lvl = "";
		prefix = "dep";
	}
	public void updateStuff(ArrayList<Wall> w) throws IOException {
		wallList = w;
		lvl = removeSpace(board.currFloor.getName());
		BufferedImage wallMask = new BufferedImage(board.x_resol,board.y_resol,BufferedImage.TYPE_INT_ARGB);
		Graphics2D newg = wallMask.createGraphics();
		
		//tile default image, dimensions, and offset from 0x0
		prefix = "dep";
		Image tile = ImageIO.read(new File("Cookie Eater/src/resources/level/"+prefix+"12AB"+".png"));
		int wid = (int)(.5+tile.getWidth(null)*board.currFloor.getScale()),hei = (int)(.5+tile.getHeight(null)*board.currFloor.getScale());
		int xOffset = (int)(.5+Math.random()*wid),yOffset = (int)(.5+Math.random()*hei);
		//list of names of all tiles on board
		String[][] tiles = new String[(int)(2+board.y_resol/hei)][(int)(2+board.x_resol/wid)];
		int pr = prefix.length();
		tiles[0][0] = chooseImage(null,null); //top-left corner
		for(int i=1; i<tiles.length; i++) { //left side
			tiles[i][0] = chooseImage(tiles[i-1][0].substring(1+pr,2+pr),null); //make sure it meshes
		}
		for(int i=1; i<tiles[0].length; i++) { //top side
			tiles[0][i] = chooseImage(null,tiles[0][i-1].substring(3+pr,4+pr));
		}
		for(int yi=1; yi<tiles.length; yi++) { //rest of squares
			for(int xi=1; xi<tiles[0].length; xi++) {
				tiles[yi][xi] = chooseImage(tiles[yi-1][xi].substring(1+pr,2+pr),tiles[yi][xi-1].substring(3+pr,4+pr));
			}
		}
		for(int yl=0;yl<tiles.length;yl++) { //add all tiles to the image
			for(int xl=0;xl<tiles[0].length;xl++) {
				File f = new File("Cookie Eater/src/resources/level/"+tiles[yl][xl]+".png");
				if (f.exists()) {
					tile = ImageIO.read(f);
				}else{
					tile = ImageIO.read(new File("Cookie Eater/src/resources/level/blank.png"));
				}
				
				newg.drawImage(tile, xl*wid-xOffset, yl*hei-yOffset, wid, hei, null);
			}
		}
		newg.dispose();
		//finish up wall graphics
		//wall = ImageIO.read(new File("Cookie Eater/src/resources/level/"+lvl+"WALL.png"));
		prefix = board.currFloor.getAbbrev();
		wall = ImageIO.read(new File("Cookie Eater/src/resources/level/"+prefix+"WallB.png"));
		BufferedImage wallF = ImageIO.read(new File("Cookie Eater/src/resources/level/"+prefix+"WallF.png"));
		newg = ((BufferedImage)wall).createGraphics();
		for(int i=0; i<board.x_resol;i++){ //masking wallF onto wall using wallMask based on blue channel
			for(int j=0; j<board.y_resol;j++){
				int rgb = wallF.getRGB(i, j);
				int mask = wallMask.getRGB(i,j);
				int color = rgb & 0x00ffffff;
			    int alpha = mask << 24;
			    rgb = color | alpha;
				wallF.setRGB(i, j, rgb);
			}
		}
		newg.drawImage(wallF,0,0,null);
		newg.dispose();
		//floor graphics
		floor = ImageIO.read(new File("Cookie Eater/src/resources/level/"+lvl+"FLOOR.png"));
	}
	public String removeSpace(String s) { //formats level names to match files
		String ret = "";
		for(int i=0; i<s.length(); i++) {
			if(!s.substring(i,i+1).equals(" "))
				ret+=s.substring(i,i+1);
		}
		return ret;
	}
	public void prePaint() throws IOException  {
	}
	public String chooseImage(String top, String left) {
		String ret = prefix;

		ArrayList<String> verts = new ArrayList<String>(); //build lists of available sides
		verts.add("1");verts.add("2");verts.add("3");
		ArrayList<String> horizs = new ArrayList<String>();
		horizs.add("A");horizs.add("B");horizs.add("C");
		
		if(top==null)top = verts.get((int)(Math.random()*verts.size())); //if top is free, choose random
		verts.remove(top); //remove from possibilities
		ret+=top; //add top side
		ret+=verts.get((int)(Math.random()*verts.size())); //choose bottom side
		if(left==null)left = horizs.get((int)(Math.random()*horizs.size()));
		horizs.remove(left);
		ret+=left;
		ret+=horizs.get((int)(Math.random()*horizs.size()));

		return ret;
	}
	public void paint(Graphics g) {
		super.paint(g);
		try {
			prePaint();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//images
		//floor
		g.drawImage(floor,0,0,board.x_resol,board.y_resol,null);
		//walls
		Area wallSpace = new Area(board.wallSpace);
		for(Mechanism m : board.mechanisms) {
			wallSpace.add(m.getArea());
		}
		if(wallList==null)return;
		/*for(Wall w : wallList) {
			x=w.getX();
			y=w.getY();
			wid=w.getW();
			hei=w.getH();
			wallSpace.add(new Area(new Rectangle(x,y,wid,hei)));
		}*/
		g.setClip(wallSpace);
		g.drawImage(wall,0,0,board.x_resol,board.y_resol,null);
		g.setClip(null);
	}
}
