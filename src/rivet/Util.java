package rivet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import rivet.core.Words;
import rivet.core.RIV;

public final class Util {	
	private Util(){}
		
	public static <K, V> HashMap<K, V> safeCopy (Map<K, V> hash) {
		HashMap<K, V> res = new HashMap<>();
		res.putAll(hash);
		return res;
	}
	public static <K, V> TreeMap<K, V> safeCopy (NavigableMap<K, V> map) {
		TreeMap<K, V> res = new TreeMap<>();
		res.putAll(map);
		return res;
	}
	public static Words safeCopy (Words words) {
	    return (Words)safeCopy((HashMap<String, RIV>)words);
	}
	public static <T> ArrayList<T> safeCopy (List<T> lis) {
		ArrayList<T> res = new ArrayList<>();
		res.addAll(lis);
		return res;
	}
	public static <T> HashSet<T> safeCopy (Set<T> set) {
		HashSet<T> res = new HashSet<>();
		res.addAll(set);
		return res;
	}

	
	public static <A, R> List<R> mapToList (A[] arr, Function<A, R> f) {
		return Arrays.stream(arr)
				.map(f)
				.collect(Collectors.toList());
	}
	
	public static <K> Set<K> getMatchingKeys (HashMap<K, ?> hashA, HashMap<K, ?> hashB) {
		Set<K> res = safeCopy(hashA.keySet());
		res.retainAll(hashB.keySet());
		return res;
	}
	
	public static <T> List<T> shuffleList(List<T> lis, Random r, int c) {
		if (c > 9) return lis;
		else {
			int s = lis.size();
			List<T> l = Util.safeCopy(lis);
			lis.forEach((item) -> {
				int i = r.nextInt(s);
				l.set(i, item);
			});
			return shuffleList(l, r, (c + 1));
		}
	}
	public static <T> List<T> shuffleList(List<T> lis, long seed) {
		return shuffleList(lis, new Random(seed), 0); 
	}
	
	public static List<Integer> randIntList(int bound, int length, long seed) {
		Random r = new Random(seed);
		return range(length).stream()
				.map((x) -> r.nextInt(bound))
				.collect(Collectors.toList());
	}
	
	public static <T> List<T> setToList (Set<T> s) {
		return s.stream().collect(Collectors.toList());
	}
	
	public static <T> List<T> butCenter (List<T> arr, int r) {
		int s = arr.size();
		return range(r * 2).stream()
				.map((x) -> arr.get(
						(x < r) 
						? x
						: (s + x) - (2 * r)))
				.collect(Collectors.toList());
	}
	
	
	private static List<Integer> range (List<Integer> r, int stop, int step, int c) {
		if ((step > 0) ? c >= stop : c <= stop) 
			return r;
		r.add(c);
		return range(r, stop, step, (c + step));
	}
	public static List<Integer> range (int start, int bound, int step) { 
		List<Integer> ret = new ArrayList<>();
		return (step == 0) 
				? ret
				: range(ret, bound, step, start);
	}
	public static List<Integer> range (int start, int bound) { 
		return range(start, bound, (start == bound)
									? 0
									: (start < bound)
										? 1
										: -1);
	}
	public static List<Integer> range (int bound) {	return range(0, bound);	}
	
	public static List<Integer> rangeNegToPos (int c) {
		return range(0 - c, c + 1);
	}
	
	public static List<String> getContextWindow (int index, int cr, List<String> text) {
		int s = text.size();
		return butCenter(rangeNegToPos(cr), cr).stream()
				.map((x) -> x + index)
				.filter((x) -> x >= 0 && x < s)
				.map((x) -> text.get(x))
				.collect(Collectors.toList());
	}
	
	private static <T> List<T> takeBy (int num, Function<T, Double> keyfn, ListIterator<T> lis, TreeMap<Double, T> res) {
		if (!lis.hasNext()) 
			return res.descendingMap()
					.values()
					.stream()
					.collect(Collectors.toList());
		T item = lis.next();
		double ret = keyfn.apply(item);
		if (res.floorKey(ret) != null) res.put(ret, item);
		while (res.size() >= num) res.remove(res.firstKey());
		return takeBy(num, keyfn, lis, res);
	}
	public static <T> List<T> takeBy (int num, Function<T, Double> keyfn, List<T> lis) {
		TreeMap<Double, T> res = new TreeMap<>();
		T item = lis.get(0);
	    res.put(keyfn.apply(item), item);
		return takeBy(num, keyfn, lis.listIterator(), res);
	}
}
