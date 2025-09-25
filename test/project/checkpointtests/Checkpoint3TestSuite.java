package project.checkpointtests;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import project.annotations.ConceptualAPI;
import project.annotations.NetworkAPI;
import project.annotations.ProcessAPI;
import spoon.Launcher;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.visitor.Filter;

/**
 * Important note if you're experiencing test failures:
 * An entirely annotation-based injection framework for the tests is NOT allowed for checkpoint 3. 
 * If you would like to migrate to such a framework for subsequent checkpoints, talk to Katherine about modifying this test
 */
public class Checkpoint3TestSuite {
    
    private static Collection<CtType<?>> allSrcTypes;
    private static Collection<CtType<?>> allTestTypes;
    
    @BeforeAll
    public static void initializeAst() throws Exception {
        List<Class<?>> allTestClasses = Utils.loadAllTestClasses();
        Launcher launcher = new Launcher();
        launcher.addInputResource("test");
        launcher.addInputResource("src");
        launcher.getEnvironment().setNoClasspath(true);
        launcher.getEnvironment().setAutoImports(true);
        
        // there's no built-in way to link to the src classes but exclude them from the root types
        // in the model, so handle that as a post-filter
        allTestTypes = launcher.buildModel().getAllTypes().stream()
                .filter(clazz -> allTestClasses.contains(clazz.getActualClass())).toList();
        
        Launcher srcLauncher = new Launcher();
        srcLauncher.addInputResource("src");
        srcLauncher.getEnvironment().setNoClasspath(true);
        srcLauncher.getEnvironment().setAutoImports(true);
        allSrcTypes = srcLauncher.buildModel().getAllTypes();
    }
    
    // Checks that implementations for each API exist
    @ParameterizedTest
    @ValueSource(classes = {NetworkAPI.class, ProcessAPI.class, ConceptualAPI.class })
    public void checkImplExists(Class<? extends Annotation> apiAnnotation) throws Exception {
        Class<?> interfaceType = getApiClass(apiAnnotation);
        Assertions.assertTrue(Utils.loadAllClasses().stream().anyMatch(clazz -> !clazz.isInterface() && interfaceType.isAssignableFrom(clazz)),
                "No implementation found for the " + apiAnnotation.getName());
    }
    
    // Checks that each implementation is instantiated in at least one test
    @ParameterizedTest
    @ValueSource(classes = {NetworkAPI.class, ProcessAPI.class, ConceptualAPI.class })
    public void checkSmokeTest(Class<? extends Annotation> apiAnnotation) throws Exception {
        Class<?> apiImpl = getApiImplClass(apiAnnotation); 
        
        Filter<CtExecutableReference<?>> ctorFilter = (var) -> {
            return var.getDeclaringType().getActualClass().equals(apiImpl) &&
                    var.isConstructor();
        };
        
        Assertions.assertTrue(allTestTypes.stream().anyMatch( clazz -> !clazz.getElements(ctorFilter).isEmpty()),
                "No smoke test found for the " + apiAnnotation.getName());
    }
    
    // Checks that a test implementation of the process API exists
    @Test
    public void checkTestImplExists() throws Exception {
        Class<?> interfaceType = getApiClass(ProcessAPI.class);
        Assertions.assertTrue(allTestTypes.stream().anyMatch(clazz -> !clazz.isInterface() && interfaceType.isAssignableFrom(clazz.getActualClass())),
                "No test implementation found for the data store");
    }
    
    // Checks that the integration test has the test data store and non-mock versions of the other components
    @Test
    public void checkIntegrationTestStructure() throws Exception {
        Filter<CtExecutableReference<?>> api1Real = (var) -> {
            return var.getDeclaringType().getActualClass().equals(getApiImplClass(NetworkAPI.class)) &&
                    var.isConstructor();
        };
        Filter<CtExecutableReference<?>> api3Real = (var) -> {
            return var.getDeclaringType().getActualClass().equals(getApiImplClass(ConceptualAPI.class)) &&
                    var.isConstructor();
        };
        Class<?> interfaceType = getApiClass(ProcessAPI.class);
        CtType<?> testDataStoreClass = allTestTypes.stream().filter(clazz -> !clazz.isInterface() && 
                interfaceType.isAssignableFrom(clazz.getActualClass())).findFirst().get();
        Filter<CtExecutableReference<?>> api2Test = (var) -> {
            return var.getDeclaringType().getActualClass().equals(testDataStoreClass.getActualClass()) &&
                    var.isConstructor();
        };
        
        Assertions.assertTrue(allTestTypes.stream().anyMatch(clazz -> !clazz.getElements(api1Real).isEmpty() &&
                !clazz.getElements(api3Real).isEmpty() &&
                !clazz.getElements(api2Test).isEmpty()),
                "No integration test found using the correct test infrastructure!");
        
    }
    
    private Class<?> getApiClass(Class<? extends Annotation> apiAnnotation) {
        for (CtType<?> clazz : allSrcTypes) {
            if (clazz.isInterface()) {
                if (clazz.hasAnnotation(apiAnnotation)) {
                   return clazz.getActualClass();
                }
            }
        }
        throw new IllegalStateException("No interface found with annotation " + apiAnnotation);
    }
    
    private Class<?> getApiImplClass(Class<? extends Annotation> apiAnnotation) {
        Class<?> interfaceType = getApiClass(apiAnnotation);
        return allSrcTypes.stream().filter(clazz -> !clazz.isInterface() && interfaceType.isAssignableFrom(clazz.getActualClass())).findFirst().get().getActualClass();
    }
}