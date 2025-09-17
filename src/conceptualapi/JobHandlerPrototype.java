package conceptualapi;
import project.annotations.ConceptualAPIPrototype;


public class JobHandlerPrototype {

	@ConceptualAPIPrototype
    public void prototype(ComputationApi logic) {


        // 1. Create and process a request for the number 12345
        ComputeRequest request1 = new ComputeRequest(12345);
        ComputeResponse response1 = logic.compute(request1);

        if (response1.isSuccess()) {
            System.out.println("Input: " + request1.getNumber() + " -> Result: '" + response1.getResult() + "'");
        } else {
            System.out.println("Computation failed for " + request1.getNumber() + ": " + response1.getMessage());
        }

        // 2. Demonstrate with another number, 987
        ComputeRequest request2 = new ComputeRequest(987);
        ComputeResponse response2 = logic.compute(request2);

        if (response2.isSuccess()) {
            System.out.println("Input: " + request2.getNumber() + " -> Result: '" + response2.getResult() + "'");
        } else {
            System.out.println("Computation failed for " + request2.getNumber() + ": " + response2.getMessage());
        }
    }
}