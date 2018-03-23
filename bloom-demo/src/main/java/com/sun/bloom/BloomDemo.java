package com.sun.bloom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

public class BloomDemo {

	public static void main(String[] args) {
		testBloom();
	}
	
	// initialization 100w.
	private static final int inssertions = 1000000;
	
	public static void testBloom () {
		// initialization bloom filter to storage string. size is 100w.
		BloomFilter<String> bloomFilter  = 
				BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), inssertions, 0.08);
		// initialization set to storage string. size is 100w.
		// the sets ensure the stored data is unique.
		Set<String> sets = new HashSet<String>(inssertions);
		// initialization list to storage string. size is 100w.
		// the lists to save all data and sure this data must in bloom filter.
		List<String> lists = new ArrayList<String>(inssertions);
		// initialization data with bloom filter.
		for (int i = 0; i < inssertions; i++) {
			// Generating random UUID.
			String uuid = UUID.randomUUID().toString();
			// add data with bloomFilter、sets、lists.
			bloomFilter.put(uuid);
			sets.add(uuid);
			lists.add(uuid);
		}
		// in the bloom filter, the number of judge error.
		int wrong = 0;
		// in the bloom filter, the number of judge right.
		int right = 0;
		// Test the number of errors in 10000.
		for (int i = 0; i < 10000; i++) {
			// according to the conditions. 
			//if i%100 == 0. the test must in bloom filter.
			String test = i%100 == 0 ? lists.get(i/100) : UUID.randomUUID().toString();
			if (bloomFilter.mightContain(test)) {
				// if the test in bloom filter and it in sets, it is right.
				if (sets.contains(test)) {
					right ++;
				} else {
					// if the test in bloom filter but it not in sets, it is wrong.
					wrong ++;
				}
			}
		}
		System.out.println("right = " + right);
		System.out.println("wrong = " + wrong);
	}

}
