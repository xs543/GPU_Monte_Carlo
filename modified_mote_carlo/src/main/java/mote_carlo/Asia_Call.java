package mote_carlo;

import java.util.List;

import org.joda.time.DateTime;
/**
 * the asian call payout calculation class
 * @author Sawyer
 *
 */
public class Asia_Call implements PayOut {
	private double K;
	private int N;
	private double rate;
	public Asia_Call(double K, int N,double rate){
		this.K = K;
		this.N= N;
		this.rate=rate;
	}
	//@Override
	public double getPayOut(StockPath path) { // get the payout of the path
		List<Pair<DateTime, Double>> prices = path.getPrices();
		double avg_price=0.0;
		for(Pair<DateTime, Double> p : prices){ // calculate the sum
			avg_price += p.getRight();
		}
		avg_price = avg_price/prices.size(); // calculate the average from the sum
		double future_val = Math.max(0, avg_price-K);;
		double present_val = future_val * Math.exp(-1*rate*N);
		return present_val;
	}
}
