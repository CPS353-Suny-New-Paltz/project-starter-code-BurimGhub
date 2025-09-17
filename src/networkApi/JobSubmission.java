package networkapi;

/**
 * This class holds the information about where a job
 * gets its input, where to put the output, and what delimiters to use
 * 
 * @author Burim Gashi
 * 
 * Status: Working
 */
public class JobSubmission {
	
    private String inputSource;
    private String outputSource;
    private Delimiters delimiters;

    /**
     * Makes a job submission with input, output, and delimiter (default used when null)
     * 
     * @param inputSource
     * @param outputSource
     * @param delimiters
     */
    public JobSubmission(String inputSource, String outputSource, Delimiters delimiters) {
        this.inputSource = inputSource;
        this.outputSource = outputSource;
        if(delimiters != null) {
            this.delimiters = delimiters;
        } else {
            this.delimiters = Delimiters.createDefault();
        }
    }
    
    /**
     * Makes a job submission with input and output
     * using default delimiters
     * 
     * @param inputSource
     * @param outputSource
     */
    public JobSubmission(String inputSource, String outputSource) {
        this(inputSource, outputSource, null);
    }

    /**
     * Gets input source
     * 
     * @return the input source
     */
    public String getInputSource() {
        return inputSource;
    }

    /**
     * Gets output source
     * 
     * @return the output source
     */
    public String getOutputSource() {
        return outputSource;
    }

    /**
     * Gets the delimiters
     * 
     * @return the delimiters
     */
    public Delimiters getDelimiters() {
        return delimiters;
    }
}