package pippin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Machine extends Observable {
	public final Map<Integer, Instruction> INSTRUCTION_MAP = new TreeMap<>();
	private Memory memory = new Memory();
	private Processor cpu = new Processor();
	private Code code;
	private States state;
	private  CodeViewPanel codeViewPanel;
	private MemoryViewPanel memoryViewPanel1;
	private MemoryViewPanel memoryViewPanel2;
	private MemoryViewPanel memoryViewPanel3;
	private ControlPanel controlPanel;
	private ProcessorViewPanel processorPanel;
	private JFrame frame;
	
	public Code getCode() {
		return code;
	}

	public Processor getCpu(){
		return cpu;
	}
	
	public Memory getMemory() {
		return memory;
	}
	
	public States getState(){
		return state;
	}
	
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
	
	public Instruction get(int key) {
        return INSTRUCTION_MAP.get(key);
    }
	
	public void step(){
		
	}
	
	public void clearAll(){
		
	}
	
	public void reload(){
		
	}
	
	public void toggleAutoStep(){
		
	}
	
	public void halt(){
		
	}
	
	private void createAndShowGUI(){
		codeViewPanel = new CodeViewPanel(this);
        memoryViewPanel1 = new MemoryViewPanel(this, 0, 160);
        memoryViewPanel2 = new MemoryViewPanel(this, 160, 240);
        memoryViewPanel3 = new MemoryViewPanel(this, 240, Memory.DATA_SIZE);
        controlPanel = new ControlPanel(this);
        processorPanel = new ProcessorViewPanel(this);
        frame = new JFrame("Pippin Simulator");
        //Add a new JPanel called center
        JPanel center = new JPanel();
        //Set the layouts and the size of the frame
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout(1,1));
        content.setBackground(Color.BLACK);
        frame.setSize(1200,600);
        center.setLayout(new GridLayout(1,3));
        //Locate all the pieces
        frame.add(codeViewPanel.createCodeDisplay(),BorderLayout.LINE_START);
        frame.add(center,BorderLayout.CENTER);
        center.add(memoryViewPanel1.createMemoryDisplay());
        center.add(memoryViewPanel2.createMemoryDisplay());
        center.add(memoryViewPanel3.createMemoryDisplay());
        frame.add(controlPanel.createControlDisplay(),BorderLayout.PAGE_END);
        frame.add(processorPanel.createProcessorDisplay(),BorderLayout.PAGE_START);
        //Set up the usual closing protocol
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//      frame.addWindowListener(new ExitAdapter());
        //Set up the initial sate of the machine
        state = States.NOTHING_LOADED;
        state.enter();
        setChanged();
        notifyObservers();
        frame.setVisible(true);
        //TODO: setup a Timer
        
	}
	
	public Machine() {
		//Data Flow Instructions
		//LOD
		INSTRUCTION_MAP.put(0x1, (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				cpu.setAccumulator(arg);
			} else if(indirect){
				cpu.setAccumulator(memory.getData(memory.getData(arg)));
			} else {
				cpu.setAccumulator(memory.getData(arg));
			}
			cpu.incrementCounter();
		});
		//STO
		INSTRUCTION_MAP.put(0x2, (int arg, boolean immediate, boolean indirect) -> {
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
		//JUMP
		INSTRUCTION_MAP.put(0xB, (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				throw new IllegalInstructionModeException("attempt to execute immediate JUMP");
			} else if(indirect){
				cpu.setProgramCounter(memory.getData(arg));
			} else {
				cpu.setProgramCounter(arg);
			}
		});
		//JMPZ
		INSTRUCTION_MAP.put(0xC, (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				throw new IllegalInstructionModeException("attempt to execute immediate JUMPZ");
			} else if(indirect){
				if(cpu.getAccumulator()==0){
					cpu.setProgramCounter(memory.getData(arg));
				} else {
					cpu.incrementCounter();
				}
			} else {
				if(cpu.getAccumulator()==0){
					cpu.setProgramCounter(arg);
				} else {
					cpu.incrementCounter();
				}
			}
		});
		//NOP
		INSTRUCTION_MAP.put(0x0, (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				throw new IllegalInstructionModeException("attempt to execute immediate NOP");
			} else if(indirect){
				throw new IllegalInstructionModeException("attempt to execute indirect NOP");
			} else {
				cpu.incrementCounter();
			}
		});
		//HALT
		INSTRUCTION_MAP.put(0xF, (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				throw new IllegalInstructionModeException("attempt to execute immediate HALT");
			} else if(indirect){
				throw new IllegalInstructionModeException("attempt to execute indirect HALT");
			} else {
				halt();
			}
		});
		//Arithmetic-logic Instructions
		//ADD
		INSTRUCTION_MAP.put(0x3,(int arg, boolean immediate, boolean indirect) -> {
			if (immediate) {
				cpu.setAccumulator(cpu.getAccumulator() + arg);
			} else if (indirect) {
				cpu.setAccumulator(cpu.getAccumulator() + memory.getData(memory.getData(arg)));                    
			} else {
				cpu.setAccumulator(cpu.getAccumulator() + memory.getData(arg));         
			}
			cpu.incrementCounter();
		});
		//SUB
		INSTRUCTION_MAP.put(0x4, (int arg, boolean immediate, boolean indirect) -> {
			if (immediate) {
				cpu.setAccumulator(cpu.getAccumulator() - arg);
			} else if (indirect) {
				cpu.setAccumulator(cpu.getAccumulator() - memory.getData(memory.getData(arg)));                    
			} else {
				cpu.setAccumulator(cpu.getAccumulator() - memory.getData(arg));         
			}
			cpu.incrementCounter();
		});
		//MUL
		INSTRUCTION_MAP.put(0x5, (int arg, boolean immediate, boolean indirect) -> {
			if (immediate) {
				cpu.setAccumulator(cpu.getAccumulator() * arg);
			} else if (indirect) {
				cpu.setAccumulator(cpu.getAccumulator() * memory.getData(memory.getData(arg)));                    
			} else {
				cpu.setAccumulator(cpu.getAccumulator() * memory.getData(arg));         
			}
			cpu.incrementCounter();
		});
		//DIV
		INSTRUCTION_MAP.put(0x6, (int arg, boolean immediate, boolean indirect) -> {
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
		//AND
		INSTRUCTION_MAP.put(0x7, (int arg, boolean immediate, boolean indirect) -> {
			if(immediate){
				if (cpu.getAccumulator() != 0 && arg != 0){
					cpu.setAccumulator(1);
				} else {
					cpu.setAccumulator(0);
				}
				cpu.incrementCounter();
			} else if(indirect){
				throw new IllegalInstructionModeException("attempt to execute indirect AND");
			} else {
				if (cpu.getAccumulator() != 0 && memory.getData(arg) != 0){
					cpu.setAccumulator(1);
				} else {
					cpu.setAccumulator(0);
				}
				cpu.incrementCounter();
			}
		});
		//NOT
		INSTRUCTION_MAP.put(0x8, (int arg, boolean immediate, boolean indirect) -> {
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
		//CMPZ
		INSTRUCTION_MAP.put(0x9,(int arg, boolean immediate, boolean indirect) -> {
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
		//CMPL
		INSTRUCTION_MAP.put(0xA, (int arg, boolean immediate, boolean indirect) -> {
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
		
		createAndShowGUI();
	}
	
    /**
     * Main method that drives the whole simulator
     * @param args command line arguments are not used
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Machine(); 
            }
        });
    }
}
