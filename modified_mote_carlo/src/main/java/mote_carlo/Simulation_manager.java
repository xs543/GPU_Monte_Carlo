package mote_carlo;
import org.apache.commons.math3.*;
import org.apache.commons.math3.distribution.NormalDistribution;
/**
 * the simulation manager manages the simulation of option prices
 * @author Sawyer
 *
 */
public class Simulation_manager {
	double prob; // the probability that we want the price of option to fall into true price +- sd
	double y; //
	double sd; //standard deviation
	StockPath sp;
	RandomVectorGenerator rvg; 
	double rate;
	PayOut payout;
	int N;
	int simulation_num;
	Error_Estimate ee;
	public Simulation_manager(double prob,
			double sd,
			StockPath sp,
			RandomVectorGenerator rvg,
			double rate,
			int N,
			PayOut payout)
	{
		this.prob = prob;
		this.sd = sd;
		this.sp = sp;
		this.rvg =rvg;
		this.rate = rate;
		this.N = N;
		this.payout = payout;
		NormalDistribution n = new NormalDistribution();
		this.y = n.inverseCumulativeProbability(1-(1- prob)/2);
		//System.out.println(this.y);
		ee = new Error_Estimate();
		simulation_num = 0;
	}
	public double simulate(){

		Error_Estimate ee = new Error_Estimate();
		int i=0;
		for(;i<50000;i++){ // first run simulation for 50000 times
			double po = payout.getPayOut(sp);
			//System.out.println(po);
			ee.update(po);
		}
		while( Math.sqrt(ee.get_error())*y/Math.sqrt(i) > sd){ // if we need to run more, then run more
			double po = payout.getPayOut(sp);
			//System.out.println(po);
			ee.update(po);
			//System.out.println(ee.get_mean());
			i++;
		}
		return ee.get_mean();
	}
	
	
}
