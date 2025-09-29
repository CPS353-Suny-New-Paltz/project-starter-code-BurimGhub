package emptyimplementations;

import conceptualapi.ComputationApi;
import conceptualapi.ComputationResultCode;
import conceptualapi.ComputeRequest;
import conceptualapi.ComputeResponse;

// An empty implementation of the ComputationAPI.
public class ConceptualAPIImpl implements ComputationApi {

	// Arrays for number-to-words conversion
	String[] UNITS = { "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven",
			"twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen" };

	String[] TENS = { "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety" };

	String[] SCALES = { "", "thousand", "million", "billion" };

	// Implementation of the compute method
	@Override
	public ComputeResponse compute(ComputeRequest request) {
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

	// Method to convert number to words
	public String convertNumberToWords(int number) {
		// Handle zero and negative numbers
		if (number == 0)
			return "zero";
		if (number < 0)
			return "negative " + convertNumberToWords(-number);
		return convertPositiveNumber(number).trim();
	}

	// Helper method to convert positive numbers to words
	public String convertPositiveNumber(int number) {
		// Handle units and teens
		if (number < 20) {
			return UNITS[number];
		}
		// Handle tens
		if (number < 100) {
			return TENS[number / 10] + (number % 10 != 0 ? "-" + UNITS[number % 10] : "");
		}
		// Handle hundreds
		if (number < 1000) {
			return UNITS[number / 100] + " hundred"
					+ (number % 100 != 0 ? " " + convertPositiveNumber(number % 100) : "");
		}

		// Handle thousands, millions, billions
		for (int i = SCALES.length - 1; i >= 1; i--) {
			int scale = (int) Math.pow(1000, i);
			if (number >= scale) {
				return convertPositiveNumber(number / scale) + " " + SCALES[i]
						+ (number % scale != 0 ? " " + convertPositiveNumber(number % scale) : "");
			}
		}

		return "";
	}
}