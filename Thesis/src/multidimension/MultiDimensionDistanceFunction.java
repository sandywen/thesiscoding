package multidimension;

import java.util.ArrayList;

import de.lmu.ifi.dbs.elki.data.NumberVector;

public interface MultiDimensionDistanceFunction {
	
	double distance(NumberVector[] v1, NumberVector[] v2);

}
