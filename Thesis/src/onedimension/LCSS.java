package onedimension;

import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.distance.distancefunction.timeseries.LCSSDistanceFunction;

public class LCSS implements UserDistanceFunction{

	private LCSSDistanceFunction function;
	
	public LCSS(double delta, double epsilon) {
		function = new LCSSDistanceFunction(delta, epsilon);
	}
	@Override
	public double distance(NumberVector v1, NumberVector v2) {
		// TODO Auto-generated method stub
		return function.distance(v1, v2);
	}
	
	

}
