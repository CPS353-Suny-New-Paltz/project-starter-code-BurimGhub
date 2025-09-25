package project.checkpointtests;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import project.annotations.ConceptualAPI;
import project.annotations.ConceptualAPIPrototype;
import project.annotations.NetworkAPI;
import project.annotations.NetworkAPIPrototype;
import project.annotations.ProcessAPI;
import project.annotations.ProcessAPIPrototype;

/**
 * This test checks that all 3 APIs exist in the 'src' folder as interfaces, with the appropriate annotations, and that they all
 * have corresponding prototypes, also with the appropriate annotations.
 * 
 * Remember, it's easiest to build the prototype first, and use that to create the APIs - use Eclipse to auto-generate classes, 
 * interfaces, etc as you build out the prototype. This ensures that your API is written from the perspective of the client - the prototype
 * acts as a "pretend" client; while the prototype code will never actually run, it's a bare-bones check that some other fully-featured client
 * could easily use the API to do everything they need to.
 */
public class Checkpoint2TestSuite {
	
	@ParameterizedTest(name = ParameterizedTest.ARGUMENTS_PLACEHOLDER)
	@MethodSource("providePrototypeParams")
	public void checkPrototypesExist(Class<? extends Annotation> apiAnnotation, 
	        Class<? extends Annotation> prototypeAnnotation) throws Exception {
		int numPrototypesFound = 0;
		List<String> errors = new ArrayList<>();
		for (Class<?> clazz : Utils.loadAllClasses()) {
			if (!clazz.isInterface() && !clazz.isEnum() && !clazz.isAnnotation()) {
				for (Method m : clazz.getDeclaredMethods()) {
					if (m.isAnnotationPresent(prototypeAnnotation)) {
					    numPrototypesFound++;
						Parameter[] parameters = m.getParameters();
						if (parameters.length != 1) {
							errors.add("Prototype method should have a single parameter, the API being prototyped. "
									+ "If you need other data to simulate classes/objects that the client will receive from elsewhere"
									+ " (a website form, another componenet, etc), just create those as local variables set to null/0 for now. "
									+ "We'll cover mock objects for the next assignment");
						} else {
							Parameter param = parameters[0];
							if (!param.getType().isAnnotationPresent(apiAnnotation)) {
								errors.add("Prototype method should have a single parameter, the API being prototyped. "
										+ "Make sure your API interface has the appropriate annotation!");
							}
						}
					}
				}
			}
			
		}
		if (numPrototypesFound != 1) {
			errors.add("No (or more than one) class method with the " + prototypeAnnotation.getSimpleName() + " annotation was found in 'src'");
		}
		if (!errors.isEmpty()) {
			throw new IllegalStateException(errors.toString());
		}
	}

	private static Stream<Arguments> providePrototypeParams() {
	    return Stream.of(
	            Arguments.of(ConceptualAPI.class, ConceptualAPIPrototype.class),
	            Arguments.of(ProcessAPI.class, ProcessAPIPrototype.class),
	            Arguments.of(NetworkAPI.class, NetworkAPIPrototype.class)
	    );
	}
	
	@ParameterizedTest(name = ParameterizedTest.ARGUMENTS_PLACEHOLDER)
	@ValueSource(classes = {NetworkAPI.class, ProcessAPI.class, ConceptualAPI.class })
	public void checkAnnotationsExist(Class<? extends Annotation> apiAnnotation) throws Exception {
		int numApisFound = 0;
		
		for (Class<?> clazz : Utils.loadAllClasses()) {
			if (clazz.isInterface()) {
				if (clazz.isAnnotationPresent(apiAnnotation)) {
				    numApisFound++;
				}
			}
		}
		
		List<String> errors = new ArrayList<>();
		
		if (numApisFound != 1) {
			errors.add("No (or more than one) interface found with annotation " + apiAnnotation.getSimpleName() + " in 'src'");
			errors.add("Keep in mind that all APIs must be interfaces (not classes), and that each API annotation"
					+ " must be on a different interface in its own .java file");
		}
		if (!errors.isEmpty()) {
			throw new IllegalStateException(errors.toString());
		}
	}

	
}
