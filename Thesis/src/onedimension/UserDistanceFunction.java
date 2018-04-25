package onedimension;

import de.lmu.ifi.dbs.elki.data.NumberVector;

public interface UserDistanceFunction {
	
	double distance(NumberVector v1, NumberVector v2);

}
