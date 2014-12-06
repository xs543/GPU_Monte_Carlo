package mote_carlo;


import static org.bridj.Pointer.allocateFloats;

import java.util.Random;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.log;
import static java.lang.Math.PI;
import static java.lang.Math.sqrt;

import org.bridj.Pointer;

import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLContext;
import com.nativelibs4java.opencl.CLDevice;
import com.nativelibs4java.opencl.CLEvent;
import com.nativelibs4java.opencl.CLKernel;
import com.nativelibs4java.opencl.CLMem;
import com.nativelibs4java.opencl.CLPlatform;
import com.nativelibs4java.opencl.CLProgram;
import com.nativelibs4java.opencl.CLQueue;
import com.nativelibs4java.opencl.JavaCL;

/**
 * implement box-muller transform on GPU computation
 *  take u1 u2 the uniform random variables on interval[0,1]
 *  generate z1 z2
 *  z1 = sqrt(-2*ln(u1))*cos(2*pi*u2)
 *  z2 = sqrt(-2*ln(u1))*sin(2*pi*u2)
 * @author Sawyer
 *
 */
public class GPU_Gaussian implements RandomVectorGenerator {

	int N;
	Random uniform_g;
	private boolean isEmpty; // the flag that indicate if the current buff is empty, if true, we need to generate more guaussian random variables
	final int batch_size = 1024*1024; // number values in each batch
	private int index; // the current index that we are at in the array gauss[]
	private double[] gauss; // the array that holds all the generated gaussian variables
	public GPU_Gaussian(int N) {
		this.N = N;
		gauss = new double[2*batch_size];
		uniform_g = new Random();
		generate_new_batch();
	}
	//@Override
	public double[] getVector()  { // get the random gaussian vector
		double [] ret = new double[N];
		for(int i=0;i<N;i++){
			if(index == 2*batch_size -1){ // if all genreated gaussians have been used, generate more
				generate_new_batch();
			}
			ret[i] = gauss[index++];
		}
		return ret;
	}
	
	public void generate_new_batch() {
		index =0; //reset the index
		//System.out.println("generate new");
		CLPlatform clPlatform = JavaCL.listPlatforms()[0];
        // Getting the GPU device
        CLDevice device = clPlatform.getBestDevice();
        // Let's make a context
        CLContext context = JavaCL.createContext(null, device);
        // Lets make a default FIFO queue.
        CLQueue queue = context.createDefaultQueue();
        // Read the program sources and compile them :
        String src = 
                "__kernel void fill_in_values(__global float* u1, __global float* u2, __global float* g1, __global float* g2, float PI,int n) \n" +
                "{\n" +
                "    int i = get_global_id(0);\n" +
                "    if (i >= n)\n" +
                "        return;\n" +
                "\n" +
                "    g1[i] = sqrt(-2*log(u1[i]))*cos(2*PI*u2[i]);\n" +
                "    g2[i] = sqrt(-2*log(u1[i]))*sin(2*PI*u2[i]);\n" +
                "}";
        CLProgram program = context.createProgram(src);
        program.build();
        CLKernel kernel = program.createKernel("fill_in_values");
        //final long tmp = System.currentTimeMillis();
        final int n = batch_size;
        final Pointer<Float>
                u1Ptr = allocateFloats(n), //the first uniform random variable array
                u2Ptr = allocateFloats(n); // the second uniform random variable array
        //System.out.println((System.currentTimeMillis() - tmp));

        for (int i = 0; i < n; i++) { // generate uniform random variables
            float temp;
        	while((temp = uniform_g.nextFloat()) == (float)0){	}
        	u1Ptr.set(i, temp);	
        	temp = uniform_g.nextFloat();
            u2Ptr.set(i, temp);
        }
        // Create OpenCL input buffers (using the native memory pointers aPtr and bPtr) :
        CLBuffer<Float>
                u1 = context.createFloatBuffer(CLMem.Usage.Input, u1Ptr),
                u2 = context.createFloatBuffer(CLMem.Usage.Input, u2Ptr);
        // Create an OpenCL output buffer :
        CLBuffer<Float> g1 = context.createFloatBuffer(CLMem.Usage.Output, batch_size);
        CLBuffer<Float> g2 = context.createFloatBuffer(CLMem.Usage.Output, batch_size);
        float pi = (float)PI;
        kernel.setArgs(u1, u2, g1,g2,pi, batch_size);
        //System.out.println((System.currentTimeMillis() - tmp));
        CLEvent event = kernel.enqueueNDRange(queue, new int[]{n}, new int[]{128});
        event.invokeUponCompletion(new Runnable() {
            //@Override
            public void run() {
                //System.out.println((System.currentTimeMillis() - tmp));
            }
        });
        final Pointer<Float> g1Ptr = g1.read(queue,event);
        final Pointer<Float> g2Ptr = g2.read(queue,event);
      //  System.out.println((System.currentTimeMillis() - tmp));
        for(int i =0; i< batch_size;i++){ // assign values from pointers to the array
        	gauss[2*i] = (double)g1Ptr.get(i);
        	gauss[2*i+1] = (double)g2Ptr.get(i);
        }
    }
}
