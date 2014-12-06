package mote_carlo;

import java.util.List;

import org.joda.time.DateTime;
/**
 * This class calculate the payout of the european style options
 * @author Sawyer
 *
 */
public class Eur_Call implements PayOut {
	private double K; // strike price
	private int N;
	private double rate;
	public Eur_Call(double K,int N, double rate){
		this.K = K;
		this.N=N;
		this.rate=rate;
	}
	//@Override
	public double getPayOut(StockPath path) { // take the stock path and return the present value of the payout
		// TODO Auto-generated method stub
		List<Pair<DateTime, Double>> prices = path.getPrices();
		double furture_val =Math.max(0,prices.get(prices.size()-1).getRight() - K);//calculate the payout of option from price of option
		double present_val =furture_val*Math.exp(-1*N*rate); //calculate the present value of payout
		return present_val;
	}

}
