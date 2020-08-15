package menus;

import java.io.*;
import java.util.*;
import java.awt.event.*;

import ce3.*;
import entities.*;

public class Dialogue {

	private Board board;
	private Entity speaker;
	private Conversation convo;
	private String text;
	private String prefix;
	/*private ArrayList<String> sText; //possible texts based on conditions
	private ArrayList<ArrayList<String>> conditionText; //text for conditions for each option
	private ArrayList<ArrayList<String>> conditionOptionText; //text for conditions for each option
	private ArrayList<String> variableText; //text for states
	private ArrayList<String> replaceText; //text for replacement*/
	private ArrayList<Dialogue> followups; //next dialogue options
	private ArrayList<String> optionText; //text for every option
	private Selection chooser;
	//private String jumpLocation; //location for jump if needed
	
	public Dialogue(Board frame, Entity s,  Conversation c, String line, String pref, BufferedReader reader) {
		board = frame;
		prefix = pref;
		speaker = s;
		convo = c;
		/*conditionText = new ArrayList<ArrayList<String>>();
		optionText = new ArrayList<String>();
		conditionOptionText = new ArrayList<ArrayList<String>>();
		variableText = new ArrayList<String>();
		replaceText = new ArrayList<String>();*/
		text = line.substring(prefix.length());
		/*text = (line.contains("{")) ? line.substring(prefix.length(),line.indexOf("{")) : line.substring(prefix.length()); //extract just text
		while(line.contains("{") && line.contains("}")) { //pull out all options, which are surrounded by brackets
			optionText.add(line.substring(line.indexOf("{")+1,line.indexOf("}")));
			line = line.replaceFirst("\\{"," ");
			line = line.replaceFirst("\\}"," ");
		}*/
		try {
			createFollowups(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch blocky
			e.printStackTrace();
		}
	}
	
	//removes all sequences of front + anything + back with marker within fulltext, returns fulltext, deposits extracts in extracts
	public String extractFromText(String fulltext, String front, String back, String marker, ArrayList<String> extracts) {
		while(fulltext.contains(front) && fulltext.contains(back)) { //pull out Strings inside of front and back characters
			//sequence to remove
			String opt = fulltext.substring(fulltext.indexOf(front)+1);
			opt = opt.substring(0,opt.indexOf(back));
			
			//count how many duplicates exist
			int reps = 0;
			String t = fulltext;
			String r = front+opt+back;
			while(t.contains(r)) {
				t = t.substring(t.indexOf(r)+r.length());
				reps++;
			}
		
			//remove all of sequence
			fulltext = fulltext.replace(front+opt+back,marker);
			
			//add to list according to number of duplicates
			for(int i=0; i<reps; i++) {
				extracts.add(opt);
			}

		}
		return fulltext;
	}
	
	public void lineFunctionality() { //operates on line for any functionality before display
		
		//## -> replace with variable
		ArrayList<String> replace = new ArrayList<String>();
		text = extractFromText(text,"#","#","@S@",replace);
		for(int i=0; i<replace.size(); i++) {
			setText(getText().replaceFirst("@S@", speaker.getState(replace.get(i))));
		}
		
		ArrayList<String> payments = new ArrayList<String>();
		text = extractFromText(text,"$","$","",payments);
		for(int i=0; i<payments.size(); i++) {
			String[] parts = payments.get(i).split(";");
			double dollars = Double.parseDouble(parts[1]);
			switch(parts[0]) {
			case("Give"):
				speaker.payCookies(board.player,dollars);
				break;
			case("Take"):
				board.player.payCookies(speaker,dollars);
				break;
			default:
				break;
			
			}
		}
		
		//[] -> options
		optionText = new ArrayList<String>();
		text = extractFromText(text,"[","]","",optionText);
		for(int i=0; i<optionText.size(); i++) { //remove options that don't meet their conditions
			//[%%] -> options appear if conditions met / ; separates variable from state
			ArrayList<String> conditions = new ArrayList<String>();
			optionText.set(i,extractFromText(optionText.get(i),"%","%","",conditions));
			boolean passes = true;
			for(int j=0; j<conditions.size(); j++) {
				String stateTest = conditions.get(j);
				String[] parts = stateTest.split(";");
				if(!speaker.getState(parts[0]).equals(parts[1])) {
					passes=false;
				}
			}
			if(!passes) {
				optionText.remove(i);
				i--;
			}
		}
		
		//%% -> conditions for dialogue to show / ; separates variable from state / | separates dialogue options in order of test priority
		String[] possible = text.split("\\|");
		boolean conditionsMet = false;
		for(int i=0; i<possible.length && !conditionsMet; i++) {
			ArrayList<String> conditions = new ArrayList<String>();
			possible[i] = extractFromText(possible[i],"%","%","",conditions);
			boolean passes = true;
			for(int j=0; j<conditions.size(); j++) {
				String stateTest = conditions.get(j);
				String[] parts = stateTest.split(";");
				if(!speaker.getState(parts[0]).equals(parts[1])) {
					passes=false;
				}
			}
			if(passes) {
				conditionsMet = true;
				text = possible[i];
			}
		}
		//> -> jump
		if(text.contains(">")) { //signal to jump to next line
			String jumpLocation = text.substring(text.indexOf(">")+1);
			convo.skipTo(jumpLocation); //if meant to jump lines, jump
		}

		//{} -> speaker state changes / ; separates variable from state
		ArrayList<String> stateChanges = new ArrayList<String>();
		text = extractFromText(text,"{","}","",stateChanges);//set all entity variables that this line changes
		for(int i=0; i<stateChanges.size(); i++) {
			String[] parts = stateChanges.get(i).split(";");
			if(parts[1].contains("+")) {
				try {
					double total = 0;
					String[] nums = parts[1].split("\\+");
					for(int j=0; j<nums.length; j++) {
						total+=Double.parseDouble(nums[j]);
					}
					parts[1] = total+"";
				}catch(NumberFormatException e) {
					
				}
			}
			speaker.setState(parts[0], parts[1]);
		}
		
	}
	
	
	//public String getJump() {return jumpLocation;}
	
	public Dialogue getNext(int option){
		return followups.get(option);
	}
	
	public String getText() {
		return text;
	}
	public void setText(String t) {
		text = t;
	}
	public int testChoice() {
		if(chooser!=null && chooser.hasChosen()) {
			chooser.close();
			return chooser.getChosenIndex();
		}
		return -1;
	}
	public int getHover() {
		return chooser.getHoveredIndex();
	}
	public int getChoice() {
		return chooser.getChosenIndex();
	}
	//returns options that pass all variable tests
	public ArrayList<String> getOptions(){
		return optionText;
	}
	/*public ArrayList<String> getVariables(){
		return variableText;
	}
	public ArrayList<String> getReplace(){
		return replaceText;
	}*/
	public Entity getSpeaker() {return speaker;}
	public void createFollowups(BufferedReader reader) throws IOException { //creates dialogues for all followup dialogues
		followups = new ArrayList<Dialogue>();
		String pref = prefix+":";
		String curr = pref;
		reader.mark(100);
		while(curr!=null && curr.length()>=pref.length() && curr.substring(0,pref.length()).equals(pref)) {
			reader.mark(100);
			curr = reader.readLine();
			if(curr!=null && curr.length()>=pref.length() && curr.substring(0,pref.length()).equals(pref))
				followups.add(new Dialogue(board,speaker,convo,curr,pref,reader));
		}
		reader.reset();
	}
	public void display(boolean b) { //run when displayed
	  if(b) {
		  if((chooser==null || !chooser.inAction()) && !optionText.isEmpty())chooser = new Selection(board,getOptions(),0,-1,KeyEvent.VK_SPACE, KeyEvent.VK_ENTER);
	  }else {
		  if(chooser!=null)chooser.close();
	  }
	}
	
}