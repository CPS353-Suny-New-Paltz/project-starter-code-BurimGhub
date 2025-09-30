package emptyimplementations;

import conceptualapi.ComputationApi;
import conceptualapi.ComputationResultCode;
import conceptualapi.ComputeRequest;
import conceptualapi.ComputeResponse;

// An empty implementation of the ComputationAPI.
public class ConceptualAPIImpl implements ComputationApi {

	// Arrays for number-to-words conversion
	String[] units = { "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven",
			"twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen" };

	String[] tens = { "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety" };

	String[] scales = { "", "thousand", "million", "billion" };

	// Implementation of the compute method
	@Override
	public ComputeResponse compute(ComputeRequest request) {
	    try {
	        if (request == null || request.getNumber() <= 0) {
	            return new ComputeResponse(ComputationResultCode.INVALID_INPUT);
	        }
	        String words = convertPositiveNumber(request.getNumber()).trim();
	        return new ComputeResponse(words);
	    } catch (Exception e) {
	        return new ComputeResponse(ComputationResultCode.ERROR);
	    }
	}

	// Method to convert number to words
	public String convertNumberToWords(int number) {
		// Handle zero and negative numbers
		if (number == 0) {
			return "zero";
		}
		if (number < 0) {
			return "negative " + convertNumberToWords(-number);
		}
		return convertPositiveNumber(number).trim();

	}

	// Helper method to convert positive numbers to words
	public String convertPositiveNumber(int number) {
		// Handle units and teens
		if (number < 20) {
			return units[number];
		}
		// Handle tens
		if (number < 100) {
			return tens[number / 10] + (number % 10 != 0 ? "-" + units[number % 10] : "");
		}
		// Handle hundreds
		if (number < 1000) {
			return units[number / 100] + " hundred"
					+ (number % 100 != 0 ? " " + convertPositiveNumber(number % 100) : "");
		}

		// Handle thousands, millions, billions
		for (int i = scales.length - 1; i >= 1; i--) {
			int scale = (int) Math.pow(1000, i);
			if (number >= scale) {
				return convertPositiveNumber(number / scale) + " " + scales[i]
						+ (number % scale != 0 ? " " + convertPositiveNumber(number % scale) : "");
			}
		}

		return "";
	}
}