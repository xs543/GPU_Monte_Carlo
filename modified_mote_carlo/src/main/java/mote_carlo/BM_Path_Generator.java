package mote_carlo;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
/**
 * generate the browninan motion path of the stock path
 * @author Sawyer
 *
 */
public class BM_Path_Generator implements StockPath {
	private double rate;
	private double sigma;
	private double s0;
	private int N;
	private DateTime startDate;
	private DateTime endDate;
	private RandomVectorGenerator rvg; 
	
	public BM_Path_Generator(double rate,double sigma,double s0,int N, DateTime startDate, DateTime endDate, RandomVectorGenerator rvg){
		this.rate = rate;
		this.sigma = sigma;
		this.s0 = s0;
		this.N = N;
		this.rvg = rvg;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	//@Override
	public List<Pair<DateTime, Double>> getPrices() { // get the path that follows the brownian motion
		// TODO Auto-generated method stub
		double[] rv;
		try {
			rv = rvg.getVector();
		
			List<Pair<DateTime,Double>> path = new LinkedList<Pair<DateTime,Double>>();
			DateTime current = new DateTime(startDate.getMillis());
			long delta = (endDate.getMillis() - startDate.getMillis())/N;
			path.add(new Pair<DateTime,Double>(current,this.s0));
			for(int i = 1; i<N;i++){
				current=current.plusMillis((int) delta);
				double price = path.get(path.size()-1).getRight() * Math.exp((rate-sigma*sigma/2)+sigma * rv[i-1]); // the actual generation of browninan motion from gaussian random variable
				path.add(new Pair<DateTime, Double> (current,price));
			}
			return path;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

}
