package pippin;
public class Processor  {
    private int accumulator;
    private int programCounter;
    private int programSize;
    
	//GETTERS AND SETTERS FOR BOTH FIELDS
    public void incrementCounter() {
        programCounter++;
    }
    
	public int getAccumulator() {
		return accumulator;
	}
	
	public void setAccumulator(int accumulator) {
		this.accumulator = accumulator;
	}
	
	public int getProgramCounter() {
		return programCounter;
	}
	
	public void setProgramCounter(int programCounter) {
		if(programCounter < 0 || programCounter > programSize) throw new IllegalArgumentException ("Program Counter Out of Range");
		this.programCounter = programCounter;
	}

	public void setProgramSize(int programSize) {
		this.programSize = programSize;
	}
    
}