package mote_carlo;



import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;

/**
 * this class generate the gaussian vector in the standard way
 * @author Sawyer
 *
 */
public class Gaussian_Random_Vector_Generator implements RandomVectorGenerator {
int N;
	
	public Gaussian_Random_Vector_Generator(int N){
		this.N = N;
	}
	//@Override
	public double[] getVector() {
		RandomGenerator rg = new JDKRandomGenerator();
		//rg.setSeed(17399225432l);
		GaussianRandomGenerator rawGenerator = new GaussianRandomGenerator(rg);
		double [] ret = new double[N];
		for(int i =0; i<N;i++){
			ret[i] = rawGenerator.nextNormalizedDouble();
		}
		return ret;
	}

}
