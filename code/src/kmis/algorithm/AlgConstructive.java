package kmis.algorithm;

import java.time.Instant;

import kmis.Main;
import kmis.constructive.IConstructive;
import kmis.localSearch.ILocalSearch;
import kmis.structure.Instance;
import kmis.structure.Result;
import kmis.structure.Solution;

public class AlgConstructive implements IAlgorithm {

    private final int numSolutions;
    private final IConstructive constructive;
    private final ILocalSearch localSearch;
    private boolean useLocalSearch=true;

    public AlgConstructive(int numSolutions, IConstructive constructive,ILocalSearch localSearch){
        this.numSolutions=numSolutions;
        this.constructive=constructive;
        this.localSearch=localSearch;
        useLocalSearch=true;
    }

    public AlgConstructive(int numSolutions, IConstructive constructive){
        this.numSolutions=numSolutions;
        this.constructive=constructive;
        this.localSearch = null;
        useLocalSearch=false;
    }

    @Override
    public Solution execute(Instance instance, long t1) {
        Solution bestSolution = null;
        int bestCardinality = 0;
        long totalTime = System.currentTimeMillis(), t2 = 0;
        Result result = new Result(instance.getName());

        double elapsedTime = 0.0f;
        float secs;
        while (elapsedTime <= (double) instance.getNumElementsSol() / 10) {
            Solution sol = constructive.construct(instance);

            if (useLocalSearch) {
                sol = localSearch.execute(sol, instance);
                t2 = System.currentTimeMillis();
            }

            int solCardinality = sol.getObjectiveFunction();

            if (bestSolution == null || solCardinality > bestCardinality) {
                bestCardinality = solCardinality;
                bestSolution = sol;
                elapsedTime = (t2 - t1) / 1000f;
                bestSolution.setTimeFound(elapsedTime);
            }
            
            t2 = System.currentTimeMillis();
            elapsedTime = (t2 - t1) / 1000f;

            /*if (Main.de100en100 && i % 100 == 0 && i > 0) {
                long currentTime = System.currentTimeMillis() - totalTime;
                secs = currentTime / 1000f;
                result.add("OF_"+i,bestSolution.getObjectiveFunction());
                result.add("time_"+i,secs);
            }*/
        }

        totalTime = System.currentTimeMillis() - totalTime;
        secs = totalTime / 1000f;

        if (Main.DEBUG) {
            System.out.print(instance.getName() + "\t");
            System.out.println(bestSolution.getObjectiveFunction() + "\t" + secs);
        } else {
            System.out.println(bestSolution.getObjectiveFunction());
        }

        return bestSolution;

        // result.add("best", bestSolution.getObjectiveFunction());
        // result.add("time"+Main.iter, secs);
        

        // return result;
    }

    public String toString() {

        String localSearchStr=useLocalSearch?","+localSearch.toString()+",":",";
        return this.getClass().getSimpleName()+"("+constructive.toString()+localSearchStr+numSolutions+")";
    }
}
