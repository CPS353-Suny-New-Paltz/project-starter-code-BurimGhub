package conceptualapi;
import project.annotations.ConceptualAPI;

@ConceptualAPI
public interface ComputationApi {
	ComputeResponse compute(ComputeRequest request);
}
