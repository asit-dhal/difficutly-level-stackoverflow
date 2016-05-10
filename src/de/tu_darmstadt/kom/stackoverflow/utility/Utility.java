package de.tu_darmstadt.kom.stackoverflow.utility;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


/**
 * @author Asit
 * Utility class
 *
 */
public class Utility {
	
	/**
	 * Convert String in Stackoverflow date format to java date object 
	 * @param text Date string
	 */
	public static Date stackStringToDate(String text) throws ParseException {
		// 2008-07-31T21:42:52.667 to Java Date
		text = text.replaceAll("T", " ");
		Date dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",
				Locale.ENGLISH).parse(text);
		return dt;
	}
	
	/**
	 * Print Stacktrace 
	 * @param t Throwable object
	 */
	public static String stackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}

	
	// http://beginnersbook.com/2014/07/how-to-sort-a-treemap-by-value-in-java/
	/**
	 * Sort by value of a java map 
	 * @param Map<K, V> map to sort by value
	 * @return Map<K, V> map sorted by value
	 */
	public static <K, V extends Comparable<V>> Map<K, V> sortByValues(
			final Map<K, V> map) {
		Comparator<K> valueComparator = new Comparator<K>() {
			public int compare(K k1, K k2) {
				int compare = map.get(k1).compareTo(map.get(k2));
				if (compare == 0)
					return 1;
				else
					return compare * -1;
			}
		};

		Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
		sortedByValues.putAll(map);
		return sortedByValues;
	}
}
