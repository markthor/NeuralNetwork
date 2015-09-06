package tools;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CollectionTool
{
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
	public static <T> T getRandomElement(List<T> list) {
		return list.get(MathTool.getIntFromZeroAndIncludingZeroExcludingUpper(list.size()));
	}
	
	public static int[] integerCollectionToIntegerArray(Collection<Integer> collection) {
		int[] result = new int[collection.size()];
		int i = 0;
		for(Integer integer: collection) {
			result[i] = integer;
			i++;
		}
		return result;
	}
}