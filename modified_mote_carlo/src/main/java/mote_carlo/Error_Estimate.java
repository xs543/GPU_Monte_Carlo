package mote_carlo;
/**
 * This class tracks the error of the generated pay out
 * This class is used by simulation manager to determine when to terminate the simulation
 * @author Sawyer
 *
 */
public class Error_Estimate {
	private int num;
	private double mean;
	private double smean;
	
	public Error_Estimate(){
		this.num =0;
		this.mean = 0.0;
		this.smean = 0.0;
	}
	public void reset(){
		this.num =0;
		this.mean = 0.0;
		this.smean = 0.0;
	}
	public void update(double x){ // add a new payout into this class and update the mean and variance
		this.num++;
		mean = ((double)(num-1)/(double)num)*mean + x/(double)num;
		smean =  ((double)((num-1)/(double)num))*smean+(x*x)/(double)num;
	}
	public double get_mean(){ 
		return mean;
	}
	public double get_smean(){
		return smean;
	}
	public int get_num(){
		return num;
	}
	public double get_error(){
		return (smean - mean*mean);
	}
}
