package emptyimplementations;

import java.util.HashMap;
import java.util.Map;

import conceptualapi.ComputationApi;
import conceptualapi.ComputationResultCode;
import conceptualapi.ComputeRequest;
import conceptualapi.ComputeResponse;

//Bottleneck identified with JConsole: high CPU usage in ConceptualAPIImpl.convertNumberToWords()
//when processing large batches of numbers due to repeated string concatenation.
//Optimization 1: Replaced string concatenation with StringBuilder to reduce allocations.
//Optimization 2: Added HashMap cache to avoid recomputing the same number conversions.
//Optimization 3: Added cache (max 20,000 entries) that clears when full to prevent memory growth.
public class OptimizedConceptualAPIImpl implements ComputationApi {

	String[] units = { "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven",
			"twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen" };

	String[] tens = { "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety" };

	String[] scales = { "", "thousand", "million", "billion" };
	
	// Max cache size
	private static final int MAX_CACHE_SIZE = 20_000;
    // Cache for memoization (number -> words)
    private final Map<Integer, String> cache = new HashMap<>();

    @Override
    public ComputeResponse compute(ComputeRequest request) {
        if (request == null) {
            return new ComputeResponse(ComputationResultCode.INVALID_INPUT);
        }
        try {
            int number = request.getNumber();
            if (number <= 0) {
                return new ComputeResponse(ComputationResultCode.INVALID_INPUT);
            }
            String words = convertNumberToWords(number);
            return new ComputeResponse(words);
        } catch (Exception e) {
            return new ComputeResponse(ComputationResultCode.ERROR);
        }
    }

    // Public API used by rest of code
    public String convertNumberToWords(int number) {
        if (number == 0) {
            return "zero";
        }
        if (number < 0) {
            return "negative " + convertNumberToWords(-number);
        }
        String cached = cache.get(number);
        if (cached != null) {
            return cached;
        }
        String result = convertPositiveNumber(number);
     // Clear cache if it gets too big, then add new entry
        if (cache.size() >= MAX_CACHE_SIZE) {
            cache.clear();
        }
        cache.put(number, result);
        return result;
    }

    // Optimized positive conversion using StringBuilder
    private String convertPositiveNumber(int number) {
        StringBuilder result = new StringBuilder();

        if (number < 20) {
            result.append(units[number]);
            return result.toString();
        }

        if (number < 100) {
            result.append(tens[number / 10]);
            if (number % 10 != 0) {
                result.append("-").append(units[number % 10]);
            }
            return result.toString();
        }

        if (number < 1000) {
            result.append(units[number / 100]).append(" hundred");
            if (number % 100 != 0) {
                result.append(" ").append(convertPositiveNumber(number % 100));
            }
            return result.toString();
        }

        // thousands, millions, billions
        for (int i = scales.length - 1; i >= 1; i--) {
            int scale = (int) Math.pow(1000, i);
            if (number >= scale) {
                result.append(convertPositiveNumber(number / scale))
                        .append(" ")
                        .append(scales[i]);
                int remainder = number % scale;
                if (remainder != 0) {
                    result.append(" ").append(convertPositiveNumber(remainder));
                }
                return result.toString();
            }
        }
        return result.toString();
    }
}