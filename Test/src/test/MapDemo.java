package test;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapDemo {

	public static void main(String[] args) {
		Map<String, Integer> unsortMap = new HashMap<>();
        unsortMap.put("z", 10);
        unsortMap.put("b", 5);
        unsortMap.put("a", 6);
        unsortMap.put("c", 20);
        unsortMap.put("d", 1);
        unsortMap.put("e", 7);
        unsortMap.put("y", 8);
        unsortMap.put("n", 20);
        unsortMap.put("g", 5);
        unsortMap.put("m", 20);
        unsortMap.put("f", 9);
        
        Set<Integer> set = new HashSet<Integer>();
        
        Map<String,Integer> sortmap = unsortMap.entrySet().stream().sorted(Map.Entry.<String,Integer>comparingByValue().reversed()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1,e2)-> e1,LinkedHashMap<String,Integer>::new));
        System.out.println(sortmap);
        //Optional<Entry<String, Integer>> e = sortmap.entrySet().stream().findFirst();
        
        
        Map<String,Integer> uniquemap = unsortMap.entrySet().stream().filter(e->set.add(e.getValue())).sorted(Map.Entry.<String,Integer>comparingByKey().reversed()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1,e2)-> e1,LinkedHashMap<String,Integer>::new));
        System.out.println(uniquemap);
        
        
        Map<String,Integer> sortmaps = unsortMap.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1,e2)-> e1,LinkedHashMap<String,Integer>::new));
        System.out.println(sortmaps);
        
        
        Entry<String, Integer> maxEntry = unsortMap.entrySet().stream().max(Map.Entry.comparingByKey()).get();
        System.out.println(maxEntry.getKey()+"=="+maxEntry.getValue());
		/*
		 * //sort map based on value in desc order Map<String, Integer> map =
		 * unsortMap.entrySet().stream().sorted(Map.Entry.<String,
		 * Integer>comparingByValue().reversed()).collect(Collectors.toMap(Map.Entry::
		 * getKey, Map.Entry::getValue,(e1,e2)-> e1,LinkedHashMap<String,
		 * Integer>::new)); // System.out.println(map); //remove duplicate value from
		 * map Set<Integer> set = new HashSet<Integer>(); Map<String, Integer> uniqueMap
		 * = unsortMap.entrySet().stream().filter(value ->
		 * set.add(value.getValue())).collect(Collectors.toMap(Map.Entry::getKey,
		 * Map.Entry::getValue)); // System.out.println(uniqueMap);
		 * 
		 * Map mapp =
		 * unsortMap.values().stream().collect(Collectors.groupingBy(Function.identity()
		 * )); System.out.println(mapp);
		 */
	    
	}
}
