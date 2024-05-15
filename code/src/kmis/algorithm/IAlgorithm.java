package kmis.algorithm;

import kmis.structure.Instance;
import kmis.structure.Result;
import kmis.structure.Solution;

public interface IAlgorithm {
    public Solution execute(Instance instance, long t1);
}
