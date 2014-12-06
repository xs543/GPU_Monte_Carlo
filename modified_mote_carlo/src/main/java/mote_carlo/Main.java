package mote_carlo;

import org.joda.time.DateTime;

public class Main {
	public static void main(String[] args) {
		//set parameters
		int N = 252;
		Double s0 = 152.35;
		double sd = 0.01;
		double sigma = 0.01;
		double rate = 0.0001;
		double K = 165.0;
		DateTime start = new DateTime(2014, 10, 15, 0, 0);
		DateTime end = start.plusDays(N);
		// set the GPU_Gaussian as the random vector generator
		RandomVectorGenerator rvg = new AntiTheticVectorGenerator(new GPU_Gaussian(N));
		//RandomVectorGenerator rvg = new AntiTheticVectorGenerator(new Gaussian_Random_Vector_Generator(N));
		StockPath sp = new BM_Path_Generator(rate, sigma, s0, N, start, end, rvg);
		
		// calculate the european call prirce
		Eur_Call ec = new Eur_Call(K, N, rate);
		Simulation_manager sm = new Simulation_manager(.997, sd, sp, rvg, rate, N,ec);
		double ec_price = sm.simulate();
		System.out.printf("the Euprpean Call option prices is %5.4f\n",ec_price);
		
		// calculate the asian call price
		K = 164.00;
		Asia_Call as = new Asia_Call(K,N,rate);
		Simulation_manager sm2 =  new Simulation_manager(.997, sd, sp, rvg, rate, N,as);
		//Simulation_manager sm2 =  new Simulation_manager(.90, sd, sp, rvg, rate, N,as);
		double as_price = sm2.simulate();
		System.out.printf("the Asian Call option prices is %5.4f\n",as_price);
	}
}
