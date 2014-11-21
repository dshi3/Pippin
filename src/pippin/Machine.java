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
				cpu.setAccumulator(memory.getData(memory.getData(arg)));
			} else {
				cpu.setAccumulator(memory.getData(arg));
			}
			cpu.incrementCounter();
		});
		INSTRUCTION_MAP.put("STO", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				throw new IllegalInstructionModeException("attempt to execute immediate STO");
			} else if(indirect){
				memory.setData(memory.getData(arg), getAccumulator());
				cpu.incrementCounter();
			} else {
				memory.setData(arg, getAccumulator());
				cpu.incrementCounter();
			}
		});
		//Control Instructions
		INSTRUCTION_MAP.put("JUMP", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				throw new IllegalInstructionModeException("attempt to execute immediate JUMP");
			} else if(indirect){
				cpu.setProgramCounter(memory.getData(arg));
			} else {
				cpu.setProgramCounter(arg);
			}
		});
		INSTRUCTION_MAP.put("JMPZ", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				throw new IllegalInstructionModeException("attempt to execute immediate JUMPZ");
			} else if(indirect){
				if(cpu.getProgramCounter()==0){
					cpu.setProgramCounter(memory.getData(arg));
				} else {
					cpu.incrementCounter();
				}
			} else {
				if(cpu.getProgramCounter()==0){
					cpu.setProgramCounter(arg);
				} else {
					cpu.incrementCounter();
				}
			}
		});
		INSTRUCTION_MAP.put("NOP", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				throw new IllegalInstructionModeException("attempt to execute immediate NOP");
			} else if(indirect){
				throw new IllegalInstructionModeException("attempt to execute indirect NOP");
			} else {
				cpu.incrementCounter();
			}
		});
		INSTRUCTION_MAP.put("HALT", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				throw new IllegalInstructionModeException("attempt to execute immediate HALT");
			} else if(indirect){
				throw new IllegalInstructionModeException("attempt to execute indirect HALT");
			} else {
				//TODO: Machine.halt()
//				this.halt();
			}
		});
		//Arithmetic-logic Instructions
		INSTRUCTION_MAP.put("ADD",(int arg, boolean immediate, boolean indirect) -> {
			if (immediate) {
				cpu.setAccumulator(cpu.getAccumulator() + arg);
			} else if (indirect) {
				cpu.setAccumulator(cpu.getAccumulator() + memory.getData(memory.getData(arg)));                    
			} else {
				cpu.setAccumulator(cpu.getAccumulator() + memory.getData(arg));         
			}
			cpu.incrementCounter();
		});
		INSTRUCTION_MAP.put("SUB", (int arg, boolean immediate, boolean indirect) -> {
			if (immediate) {
				cpu.setAccumulator(cpu.getAccumulator() - arg);
			} else if (indirect) {
				cpu.setAccumulator(cpu.getAccumulator() - memory.getData(memory.getData(arg)));                    
			} else {
				cpu.setAccumulator(cpu.getAccumulator() - memory.getData(arg));         
			}
			cpu.incrementCounter();
		});
		INSTRUCTION_MAP.put("MUL", (int arg, boolean immediate, boolean indirect) -> {
			if (immediate) {
				cpu.setAccumulator(cpu.getAccumulator() * arg);
			} else if (indirect) {
				cpu.setAccumulator(cpu.getAccumulator() * memory.getData(memory.getData(arg)));                    
			} else {
				cpu.setAccumulator(cpu.getAccumulator() * memory.getData(arg));         
			}
			cpu.incrementCounter();
		});
		INSTRUCTION_MAP.put("DIV", (int arg, boolean immediate, boolean indirect) -> {
			if (immediate) {
				if (arg == 0){
					throw new DivideByZeroException("Can't divide 0");
				} else {
					cpu.setAccumulator(cpu.getAccumulator() / arg);
				}
			} else if (indirect) {
				if (memory.getData(memory.getData(arg)) == 0){
					throw new DivideByZeroException("Can't divide 0");
				} else {
					cpu.setAccumulator(cpu.getAccumulator() / memory.getData(memory.getData(arg)));
				}
			} else {
				if (memory.getData(arg) == 0){
					throw new DivideByZeroException("Can't divide 0");
				} else {
					cpu.setAccumulator(cpu.getAccumulator() / memory.getData(arg));
				}
			}
			cpu.incrementCounter();
		});
		INSTRUCTION_MAP.put("AND", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				if (cpu.getAccumulator() == 0 && arg == 0){
					cpu.setAccumulator(1);
				} else {
					cpu.setAccumulator(0);
				}
				cpu.incrementCounter();
			} else if(indirect){
				throw new IllegalInstructionModeException("attempt to execute indirect AND");
			} else {
				if (cpu.getAccumulator() == 0 && memory.getData(arg) == 0){
					cpu.setAccumulator(1);
				} else {
					cpu.setAccumulator(0);
				}
				cpu.incrementCounter();
			}
		});
		INSTRUCTION_MAP.put("NOT", (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				throw new IllegalInstructionModeException("attempt to execute immediate NOT");
			} else if(indirect){
				throw new IllegalInstructionModeException("attempt to execute indirect NOT");
			} else {
				if (cpu.getAccumulator() == 0){
					cpu.setAccumulator(1);
				} else {
					cpu.setAccumulator(0);
				}
				cpu.incrementCounter();
			}
		});
		INSTRUCTION_MAP.put("CMPZ",(int arg, boolean immediate, boolean indirect) -> {
			int operand = memory.getData(arg);
			if (immediate) {
				throw new IllegalInstructionModeException("attempt to execute immediate CMPZ");
			} else if (indirect) {
				throw new IllegalInstructionModeException("attempt to execute indirect CMPZ");
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
				throw new IllegalInstructionModeException("attempt to execute immediate CMPL");
			} else if(indirect){
				throw new IllegalInstructionModeException("attempt to execute indirect CMPL");
			} else {
				if(memory.getData(arg) < 0){
					cpu.setAccumulator(1);          
				} else {
					cpu.setAccumulator(0);          
				}
				cpu.incrementCounter();
			}
		});
	}
}
