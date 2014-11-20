package pippin;

import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

public class Machine extends Observable {
	public final Map<String, Instruction> INSTRUCTION_MAP = new TreeMap<>();
	private Memory memory = new Memory();
	private Processor cpu = new Processor();

	// DELEGATE METHODS FOR int setData, int getData, and int[] getData from memory
	// all the setters and getters of cpu, and the incrementCounter
	public int[] getData(){
		return memory.getData();
	}

	public void setData(int index, int value) {
		memory.setData(index, value);
	}

	public int getData(int index) {
		return memory.getData(index);
	}

	public void incrementCounter() {
		cpu.incrementCounter();
	}

	public int getAccumulator() {
		return cpu.getAccumulator();
	}

	public void setAccumulator(int accumulator) {
		cpu.setAccumulator(accumulator);
	}

	public int getProgramCounter() {
		return cpu.getProgramCounter();
	}

	public void setProgramCounter(int programCounter) {
		cpu.setProgramCounter(programCounter);
	}
	
	//??
	public Instruction get(String key) {
		return INSTRUCTION_MAP.get(key);
	}

	// Here are two lambda expressions for instructions
	public Machine() {
		//Data Flow Instructions
		INSTRUCTION_MAP.put("LOD", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				cpu.setAccumulator(arg);
			} else if(indirect){
				int arg1 = memory.getData(arg);
				cpu.setAccumulator(memory.getData(arg1));
			} else {
				cpu.setAccumulator(memory.getData(arg));
			}
		});
		INSTRUCTION_MAP.put("STO", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				
			} else if(indirect){
				
			} else {
				
			}
		});
		//Control Instructions
		INSTRUCTION_MAP.put("JUMP", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				
			} else if(indirect){
				
			} else {
				
			}
		});
		INSTRUCTION_MAP.put("JMPZ", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				
			} else if(indirect){
				
			} else {
				
			}
		});
		INSTRUCTION_MAP.put("NOP", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				
			} else if(indirect){
				
			} else {
				
			}
		});
		INSTRUCTION_MAP.put("HALT", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				
			} else if(indirect){
				
			} else {
				
			}
		});
		//Arithmetic-logic Instructions
		INSTRUCTION_MAP.put("ADD",(int arg, boolean immediate, boolean indirect) -> {
			if (immediate) {
				cpu.setAccumulator(cpu.getAccumulator() + arg);
			} else if (indirect) {
				int arg1 = memory.getData(arg);
				cpu.setAccumulator(cpu.getAccumulator() + memory.getData(arg1));                    
			} else {
				cpu.setAccumulator(cpu.getAccumulator() + memory.getData(arg));         
			}
			cpu.incrementCounter();
		});
		INSTRUCTION_MAP.put("SUB", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				
			} else if(indirect){
				
			} else {
				
			}
		});
		INSTRUCTION_MAP.put("MUL", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				
			} else if(indirect){
				
			} else {
				
			}
		});
		INSTRUCTION_MAP.put("DIV", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				
			} else if(indirect){
				
			} else {
				
			}
		});
		INSTRUCTION_MAP.put("AND", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				
			} else if(indirect){
				
			} else {
				
			}
		});
		INSTRUCTION_MAP.put("NOT", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				
			} else if(indirect){
				
			} else {
				
			}
		});
		INSTRUCTION_MAP.put("CMPZ",(int arg, boolean immediate, boolean indirect) -> {
			int operand = memory.getData(arg);
			if (immediate) {
				throw new IllegalInstructionModeException("attempt to execute indirect AND");
			} else if (indirect) {
				throw new IllegalInstructionModeException("attempt to execute indirect AND");
			} 
			if(operand == 0) {
				cpu.setAccumulator(1);          
			} else {
				cpu.setAccumulator(0);          
			}
			cpu.incrementCounter();
		});
		INSTRUCTION_MAP.put("CMPL", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				
			} else if(indirect){
				
			} else {
				
			}
		});
	}
}