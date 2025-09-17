package conceptualapi;

public class ComputeRequest {
	
    private final int number;

    public ComputeRequest(int number) {
        if (number <= 0 || number >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Number must be positive and less than Integer.MAX_VALUE");
        }
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}