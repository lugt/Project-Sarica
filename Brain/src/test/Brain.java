package test;

import net.sarica.brain.MasterBrain;
import net.sarica.brain.Network;

public class Brain{
	public static void main(String[] args){
		Network n = new Network(5, 5, 5, 5, 5);
		MasterBrain b = new MasterBrain(n.getGenes().length, false, 100, 1.0f, 0.5);
		double[] in = new double[]{
			0, 1, 2, 3, 4
		};
		double[] out = new double[]{
			0, 1, 2, 1, 0
		};
		while(true){
			b.prepareGeneration();
			for(int i = 0; i<b.getGenerationSize(); i++){
				b.get(i, n.getGenes());
				b.score(i, n.getError(in, out));
			}
			System.out.println(b.getGeneration()+") "+b.getScore(b.getBestScore()));
			try{
				Thread.sleep(500);
			}catch(Exception exception){}
		}
	}
}
