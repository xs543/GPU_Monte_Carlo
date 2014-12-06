package mote_carlo;
/**
 * Pair
 * used by Payout to hold the date and payout
 * @author Sawyer
 *
 * @param <Valtype1>
 * @param <Valtype2>
 */
public class Pair<Valtype1,Valtype2> {
	private Valtype1 left;
	private Valtype2 right;
	public Pair(Valtype1 left, Valtype2 right){
		this.left = left;
		this.right = right;
	}
	public Valtype1 getLeft(){
		return this.left;
	}
	public Valtype2 getRight(){
		return this.right;
	}
	public void setLeft(Valtype1 new_left){
		this.left = new_left;
	}
	public void setRight(Valtype2 new_right){
		this.right = new_right;
	}
	public boolean equals(Pair p2){
		try{
			if(!p2.getLeft().equals(this.left)){
				return false;
			}
			if(!p2.getRight().equals(this.right)){
				return false;
			}
		}catch (Exception e){
			return false;
		}
		return true;
	}
	public String toString(){
		String s = "";
		s += this.left.toString();
		s += " ";
		s += this.right.toString();
		return s;
	}
}
