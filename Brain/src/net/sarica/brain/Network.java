package net.sarica.brain;

public class Network{
	private static int indexAt(int[] layerSizes, int i, int layer){
		int x = i;
		for(int y = 0; y<layer; y++)
			x += layerSizes[y]+1;
		return x;
	}
	private final int[] connections;
	private final float[] weights;
	private final int[] indices;
	private final int[] weightIndices;
	private final double[] tempData;
	private final int inputNeurons;
	private final int outputNeurons;
	private final boolean[] editable;
	public Network(int... layerSizes){
		if(layerSizes.length<2)
			throw new IllegalArgumentException("Must have at least 2 layers!");
		inputNeurons = layerSizes[0];
		outputNeurons = layerSizes[layerSizes.length-1];
		int w = 0;
		int cSize = 0;
		int iSize = 0;
		int wiSize = 0;
		for(int a = 0; a<layerSizes.length; a++){
			if(a<layerSizes.length-1){
				cSize += (layerSizes[a]+1)*(layerSizes[a+1]+1);
				iSize += layerSizes[a]+1;
				wiSize += layerSizes[a]+1;
			}else{
				cSize += layerSizes[a];
				iSize += layerSizes[a];
				wiSize += layerSizes[a];
			}
		}
		connections = new int[cSize];
		weightIndices = new int[wiSize];
		indices = new int[iSize];
		tempData = new double[iSize];
		int ci = 0;
		int ii = 0;
		int wii = 0;
		for(int a = 0; a<layerSizes.length; a++){
			if(a<layerSizes.length-1){
				for(int j = 0; j<=layerSizes[a]; j++){
					indices[ii++] = ci;
					weightIndices[wii++] = w;
					w += 4+layerSizes[a+1];
					connections[ci++] = layerSizes[a+1];
					for(int k = 0; k<layerSizes[a+1]; k++)
						connections[ci++] = indexAt(layerSizes, k, a+1);
				}
			}else{
				for(int j = 0; j<layerSizes[a]; j++){
					indices[ii++] = ci;
					weightIndices[wii++] = w;
					w += 4;
					connections[ci++] = 0;
				}
			}
		}
		weights = new float[w];
		editable = new boolean[w];
		for(int i = 0; i<editable.length; i++)
			editable[i] = true;
	}
	public double getError(double[] in, double[] out){
		setInput(in);
		run();
		double total = 0;
		for(int i = 0; i<outputNeurons; i++)
			total += Math.abs(out[i]-tempData[tempData.length-outputNeurons+i]);
		total /= outputNeurons;
		return total*total;
	}
	public float[] getGenes(){
		return weights;
	}
	public int getInputCount(){
		return inputNeurons;
	}
	public boolean[] getIsEditable(){
		return editable;
	}
	public void getOutput(double[] out){
		if(out.length!=outputNeurons)
			throw new IllegalArgumentException("Inconsistant number of output neurons! "+out.length+" != "+outputNeurons);
		for(int i = 0; i<outputNeurons; i++)
			out[i] = tempData[tempData.length-outputNeurons+i];
	}
	public int getOutputCount(){
		return outputNeurons;
	}
	public long run(){
		// Hopefully, as this is built to use only array, no objects, it should be fairly quick.
		// No math is done outside of adding and multiplying.
		// Clear all old data, skipping input neurons.
		long time = System.nanoTime();
		int i, a;
		for(i = inputNeurons; i<tempData.length; i++)
			tempData[i] = 0;
		// Process each neuron.
		for(i = 0; i<indices.length; i++){
			// Run input function.
			runFunction(i, tempData[i]);
			// Send processed data to all children.
			for(a = 0; a<connections[indices[i]]; a++)
				tempData[connections[indices[i]+a+1]] += tempData[i]*getWeight(i, a);
		}
		return System.nanoTime()-time;
	}
	public void setInput(double... in){
		if(in.length!=inputNeurons)
			throw new IllegalArgumentException("Inconsistant number of input neurons! "+in.length+" != "+inputNeurons);
		for(int i = 0; i<inputNeurons; i++)
			tempData[i] = in[i];
	}
	private float getWeight(int a, int index){
		return weights[weightIndices[a]+4+index];
	}
	private double runFunction(int a, double x){
		int wi = weightIndices[a];
		return weights[wi]+weights[wi+1]*x+weights[wi+2]*x*x+weights[wi+3]*x*x*x;
	}
}
