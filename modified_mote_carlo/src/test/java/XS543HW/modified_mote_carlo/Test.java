package XS543HW.modified_mote_carlo;

import mote_carlo.AntiTheticVectorGenerator;
import mote_carlo.Asia_Call;
import mote_carlo.BM_Path_Generator;
import mote_carlo.Gaussian_Random_Vector_Generator;
import mote_carlo.RandomVectorGenerator;
import mote_carlo.Simulation_manager;
import mote_carlo.StockPath;

import org.joda.time.DateTime;

import junit.framework.TestCase;

public class Test extends TestCase {
	
	public void test() {
		int N = 252;
		Double s0 = 152.35;
		double sd = 0.01;
		double sigma = 0.01;
		double rate = 0.0001;
		double K = 165.0;
		DateTime start = new DateTime(2014, 10, 15, 0, 0);
		DateTime end = start.plusDays(N);
		RandomVectorGenerator rvg = new AntiTheticVectorGenerator(new Gaussian_Random_Vector_Generator(N));
		StockPath sp = new BM_Path_Generator(rate, sigma, s0, N, start, end, rvg);
		//Eur_Call ec = new Eur_Call(K, N, rate);
		//Simulation_manager sm = new Simulation_manager(.80, sd, sp, rvg, rate, N,ec);
		//double ec_price = sm.simulate();
		//System.out.println(ec_price);
		
		
		
		K = 164.00;
		Asia_Call as = new Asia_Call(K,N,rate);
		Simulation_manager sm2 =  new Simulation_manager(.90, sd, sp, rvg, rate, N,as);
		double as_price = sm2.simulate();
		System.out.println(as_price);
	}
}
