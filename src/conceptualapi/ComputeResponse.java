package conceptualapi;

public class ComputeResponse {
    private final ComputationResultCode resultCode;
    private final String result;

    public ComputeResponse(String result) {
        this.resultCode = ComputationResultCode.SUCCESS;
        this.result = result;
    }

    public ComputeResponse(ComputationResultCode failureCode) {
        if (failureCode == ComputationResultCode.SUCCESS) {
            throw new IllegalArgumentException("Use the other constructor for a successful response.");
        }
        this.resultCode = failureCode;
        this.result = null;
    }

    public boolean isSuccess() {
        return resultCode == ComputationResultCode.SUCCESS;
    }

    public ComputationResultCode getResultCode() {
        return resultCode;
    }

    public String getResult() {
        return result;
    }

    // Add this method
    public String getMessage() {
        switch (resultCode) {
            case SUCCESS:
                return "Computation successful";
            case INVALID_INPUT:
                return "The input number was invalid.";
            case ERROR:
                return "An internal error occurred during computation.";
            default:
                return "An unknown status occurred.";
        }
    }
}