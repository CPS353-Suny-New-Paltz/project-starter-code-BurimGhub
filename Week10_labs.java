package jdbcExersice;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.*;
import java.util.function.*;
import java.util.Map;

/** The goal of this practice is not to "get the answer" through AI or some other means, but for you to think through the questions and
* come up with a strategy. You can decide not to do it at your own cost.
*/

/**
* In the following, write code to achieve what's asked. You don't need to but if you want to very the accuracy of your code,
* include statements to print the result.
*/

/** Submit to the TA, and he will assign you a grade based on a few selected responses.  */





public class Week10_labs
{
    public static void main(String[] args)
	{
        List<String> fruit = Arrays.asList("cherry","banana","berry","apple","cherry","kiwi","fig","date","lemon","honeydew","cherry","elderberry","apple","banana","grape");

		// Collect elements into a Set
        Set<String> fruitSet = fruit.stream().collect(Collectors.toSet()); // Puts elements into a set using toSet
        System.out.println("FruitSet: " + fruitSet);

        // Collect the fruit into groups based on their first character
        Map<Character, List<String>> groupedByFirstChar = fruit.stream().collect(Collectors.groupingBy(s -> s.charAt(0)));	// keys = first letter
        System.out.println("\nGrouped by first character: " + groupedByFirstChar);

		// Group fruit by the length of the name
        Map<Integer, List<String>> groupedByLength = fruit.stream().collect(Collectors.groupingBy(String::length));	// keys = word length
        System.out.println("\nGrouping fruit by length of name: " + groupedByLength);

		//Collect the fruit that has erry in it
        List<String> berries = fruit.stream().filter(s -> s.contains("erry")).collect(Collectors.toList());	// keep if "erry" is present, stores in list
        System.out.println("\nFruit that has 'erry' in it: " + berries);

		//Create a partition of fruit based on if it contains erry
        Map<Boolean, List<String>> partitionedBerries = fruit.stream().collect(Collectors.partitioningBy(s -> s.contains("erry")));	// split true/false
        System.out.println("\nParition of fruit based on if it contains 'erry': " + partitionedBerries);

		//collect/ the fruit that has 5 or less symbols
        List<String> shortFruit = fruit.stream().filter(s -> s.length() <= 5).collect(Collectors.toList());	// keep if length <= 5
        System.out.println("\nFruit that has 5 or less symbols: " + shortFruit);

		//find the total number of symbols in all the fruit stored
        int totalSymbols = fruit.stream().collect(Collectors.summingInt(String::length));	// add all lengths
        System.out.println("\nTotal number of symbols in all the fruit stores: " + totalSymbols);
        
        System.out.println("\n===========================================================================================");


		List<Integer> data = Arrays.asList(87, 23, 45, 100, 6, 78, 92, 44, 13, 56, 34, 99, 82, 19, 1012, 78, 45, 90, 23, 56, 78, 100, 3, 43, 67, 89, 21, 34, 10);

        // Partition data based on if >=50
		Map<Boolean, List<Integer>> partitionData = data.stream().collect(Collectors.partitioningBy(n -> n >=50));	// split by threshold
		System.out.println("\nPartition of data based on if >=50: " + partitionData);

		//divide data into groups based on the remainder when divided by 7
		Map<Integer, List<Integer>> groupedByRemainder = data.stream().collect(Collectors.groupingBy(n -> n % 7));	// keys = remainder
		System.out.println("\nGroups of data based on remainder when divided by 7: " + groupedByRemainder);


		//find the sum of the data
		int sum = data.stream().collect(Collectors.summingInt(Integer::intValue));	// sum of all numbers
		System.out.println("\nSum of the dat: " + sum);


		//collect the unique values
		Set<Integer> uniqueValues = data.stream().collect(Collectors.toSet());	// keep unique values only
		System.out.println("\nUnique values: " + uniqueValues);

        //compute the cube of each values
		List<Integer> cubes = data.stream().map(n -> n * n * n).collect(Collectors.toList());	// transform n to n^3
		System.out.println("\nCube of each values: " + cubes);

		//find the sum of the cubes of each value
		long sumOfCubes = data.stream().collect(Collectors.summingLong(n -> (long) n * n * n));	// sum of cubes
		System.out.println("\nSum of the cubes of each value: " + sumOfCubes);

		//increase the value of each element by 5
		List<Integer> increaseByFive = data.stream().map(n -> n + 5).collect(Collectors.toList());	// adds 5 to each element (n + 5)
		System.out.println("\nIncrease value of each element by 5: " + increaseByFive);

		//compute the cube of the even values
		List<Integer> evenCubes = data.stream().filter(n -> n % 2 == 0).map(n -> n * n * n).collect(Collectors.toList());	// keep if divisible by 2, transform even to cube
		System.out.println("\nCube of the even values: " + evenCubes);

   }
}
