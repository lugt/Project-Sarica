package net.sarica.brain;

public class MasterBrain{
	private final int geneSize;
	private final boolean highScore;
	private final float randomnessFactor;
	private final double[] currentScores;
	private final float[] currentGenes;
	private final double killAgressivness;
	private final boolean[] survivors;
	private int generation;
	private int killed;
	public MasterBrain(int geneSize, boolean highScore, int generationSize, float randomnessFactor, double killAgressiveness){
		this.highScore = highScore;
		this.randomnessFactor = randomnessFactor;
		this.geneSize = geneSize;
		killAgressivness = killAgressiveness;
		currentScores = new double[generationSize];
		currentGenes = new float[geneSize*generationSize];
		survivors = new boolean[generationSize];
	}
	public void get(int index, float[] out){
		int offset = index*out.length;
		for(int i = 0; i<out.length; i++)
			out[i] = currentGenes[offset+i];
	}
	public int getBestScore(){
		double h = Double.NEGATIVE_INFINITY;
		double l = Double.POSITIVE_INFINITY;
		for(double d : currentScores){
			h = Math.max(h, d);
			l = Math.min(l, d);
		}
		for(int i = 0; i<currentScores.length; i++)
			if(currentScores[i]==(highScore?h:l))
				return i;
		throw new RuntimeException();
	}
	public int getGeneration(){
		return generation;
	}
	public int getGenerationSize(){
		return currentScores.length;
	}
	public int getKilled(){
		return killed;
	}
	public double getScore(int index){
		return currentScores[index];
	}
	public void prepareGeneration(){
		if(generation==0){
			int j, k;
			for(j = 0; j<currentScores.length; j++){
				for(k = 0; k<geneSize; k++)
					currentGenes[k+geneSize*j] = (float)(Math.random()*2-1);
			}
		}else{
			killed = 0;
			int i, k, r;
			for(i = 0; i<survivors.length; i++)
				survivors[i] = Math.pow(Math.random(), killAgressivness)<getScorePercentile(i);
			for(i = 0; i<survivors.length; i++)
				if(!survivors[i]){
					killed++;
					r = randomSurvivor();
					for(k = 0; k<geneSize; k++)
						currentGenes[k+geneSize*i] = currentGenes[k+geneSize*r];
					r = (int)(Math.random()*geneSize);
					currentGenes[r+geneSize*i] = (float)(currentGenes[r+geneSize*i]+(Math.random()*2-1)*randomnessFactor);
				}
		}
		generation++;
	}
	public void score(int index, double score){
		currentScores[index] = score;
	}
	private double getScorePercentile(int index){
		double h = Double.NEGATIVE_INFINITY;
		double l = Double.POSITIVE_INFINITY;
		for(double d : currentScores){
			h = Math.max(h, d);
			l = Math.min(l, d);
		}
		if(highScore)
			return (currentScores[index]-l)/(h-l);
		return 1.0-(currentScores[index]-l)/(h-l);
	}
	private int randomSurvivor(){
		int r;
		do{
			r = (int)(Math.random()*survivors.length);
			if(survivors[r])
				return r;
		}while(true);
	}
}
