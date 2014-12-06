package mote_carlo;
import java.util.List;


import org.joda.time.DateTime;

public interface StockPath {
	public List<Pair<DateTime,Double>> getPrices();
}
