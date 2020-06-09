package items;
import ce3.*;

public class ItemShrink extends Item{
	
	private int new_radius;
	private int start_radius;
	
	public ItemShrink(Board frame) {
		super(frame);
		name = "Shrink";
		desc="Decreases user's size.`Amplify: More shrinkage";
		new_radius = 20;
	}
	public void prepare() {
		
	}
	public void initialize() {
		start_radius = user.getRadius();
		user.setRadius(new_radius);
	}
	public void execute() {
		if(checkCanceled())return;
	}
	public void end(boolean interrupted) {
		user.setRadius(start_radius);
	}
	public void amplify() {
		super.amplify();
		new_radius/=2;
	}
	public void deamplify() {
		super.deamplify();
		new_radius*=2;
	}
}
