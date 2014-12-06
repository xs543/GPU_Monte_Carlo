package mote_carlo;
/**
 * the antithetic vector
 * @author Sawyer
 *
 */
public class AntiTheticVectorGenerator implements RandomVectorGenerator {
	private RandomVectorGenerator rvg;
	double[] last_Vector; // hold the last generated vector
	
	public AntiTheticVectorGenerator(RandomVectorGenerator rvg){
		this.rvg=rvg;
		last_Vector = null;
	}
	
	//@Override
	public double[] getVector() throws Exception { // takes a vector generator, generate a vector and next time return the negatives of the first one 
		if(last_Vector == null){ // if the last_vector is null, generate new vector
			last_Vector = rvg.getVector();
			return last_Vector;
		}
		double[] temp = new double[last_Vector.length];
		for(int i=0 ; i<last_Vector.length;i++ ){
			temp[i] = -1*last_Vector[i];
		}
		last_Vector = null;
		return temp;
	}

}
