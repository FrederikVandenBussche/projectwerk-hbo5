package be.miras.programs.frederik.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Frederik Vanden Bussche
 * 
 * class met sorteer methodes
 */
public class Sorteer {

	public Sorteer() {
	}
	
	/**
	 * @param ongesorteerd Map<Integer, String
	 * @return Map<INteger, String>
	 * 
	 * sorteerd de geparameteriseerde map alfabetisch op value
	 */
	public static Map<Integer, String> SorteerMap( Map<Integer, String> ongesorteerd){
		List<Integer> mapKeys = new ArrayList<>(ongesorteerd.keySet());
		List<String> mapValues = new ArrayList<>(ongesorteerd.values());
		
		Collections.sort(mapValues);
		Collections.sort(mapKeys);

		Map<Integer, String> gesorteerd = new HashMap<Integer, String>();
		
		Iterator<String> valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			String val = valueIt.next();
			
			Iterator<Integer> keyIt = mapKeys.iterator();
			while (keyIt.hasNext()) {
				Integer key = keyIt.next();
				String comp1 = ongesorteerd.get(key);
				String comp2 = val;
				
				if (comp1.equals(comp2)) {
					keyIt.remove();
					gesorteerd.put(key, val);
					break;
				}
			}
		}
		return gesorteerd;
	}

}
