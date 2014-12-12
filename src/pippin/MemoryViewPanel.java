package pippin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MemoryViewPanel implements Observer{
	private Memory memory;
	private JScrollPane scroller;
	private JTextField[] dataDecimal = new JTextField[Memory.DATA_SIZE];
	private JTextField[] dataHex = new JTextField[Memory.DATA_SIZE];
	private int lower;
	private int upper;
	private static int previousColor = -1;
	
	public MemoryViewPanel(Machine machine, int lower, int upper){
		memory = machine.getMemory();
		this.lower = lower;
		this.upper = upper;
		machine.addObserver(this);
	}
	
	public void resetPreviousColor(){
		previousColor = -1;
	}
	
	public JComponent createMemoryDisplay(){
		JPanel returnPanel = new JPanel(), 
				panel = new JPanel(), numPanel = new JPanel(), 
				decimalPanel = new JPanel(), hexPanel = new JPanel(),
				labelPanel = new JPanel();
		//layout and dimensioning
		returnPanel.setPreferredSize(new Dimension(300,150));;
        returnPanel.setLayout(new BorderLayout());
        panel.setLayout(new BorderLayout());
        numPanel.setLayout(new GridLayout(0,1));
        decimalPanel.setLayout(new GridLayout(0,1));
        hexPanel.setLayout(new GridLayout(0,1));
        //set row numbers and texts
        for(int i = lower; i < upper; i++) {
//      for(int i = 0; i < Memory.DATA_SIZE; i++) {
        	numPanel.add(new JLabel(i+": ", JLabel.RIGHT));
        	dataDecimal[i] = new JTextField(10);
        	dataHex[i] = new JTextField(10);
        	decimalPanel.add(dataDecimal[i]);
        	hexPanel.add(dataHex[i]);
        }
        //Give the returnPanel a Label
        labelPanel.setLayout(new BorderLayout());
        labelPanel.add(new JLabel("Data Memory View [" + lower + "-" + (upper-1) + "]", JLabel.CENTER), 
                BorderLayout.NORTH);
        labelPanel.add(new JLabel("Decimal", JLabel.CENTER), 
                BorderLayout.CENTER);
        labelPanel.add(new JLabel("Hexadecimal", JLabel.LEFT), 
                BorderLayout.LINE_END);
        returnPanel.add(labelPanel, BorderLayout.PAGE_START);
        //Assemble the JPanels, some side by side and some nested
        panel.add(numPanel, BorderLayout.LINE_START);
        panel.add(decimalPanel, BorderLayout.CENTER);
        panel.add(hexPanel, BorderLayout.LINE_END);
        scroller = new JScrollPane(panel);
        returnPanel.add(scroller);
        
        return returnPanel;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if(previousColor == -1){
			previousColor = memory.getChangedIndex();
		}
		
		for(int i = lower; i < upper; i++) {
            //if(memory.getData(i) != 0) System.out.println("DMupdate " + lower + " " + i + " " + memory.getData(i));
            dataDecimal[i].setText(""+memory.getData(i));
            dataHex[i].setText(Integer.toHexString(memory.getData(i)));
        }
//		System.out.println(this.toString() + previousColor + " " + lower + " " + upper + " " + memory.getChangedIndex());
		if(arg1 != null && arg1.equals("Clear")){
			for(int i = lower; i < upper; i++) {
	            dataDecimal[i].setText("");
	            dataHex[i].setText("");
	        }
			if(previousColor >=0){
				dataDecimal[previousColor].setBackground(Color.WHITE);
				dataHex[previousColor].setBackground(Color.WHITE);
				previousColor = -1;
			}
		} else if(previousColor >= lower && previousColor < upper){
			dataDecimal[previousColor].setBackground(Color.WHITE);
			dataHex[previousColor].setBackground(Color.WHITE);
//			System.out.print("###" + memory.getChangedIndex());
			previousColor = memory.getChangedIndex();
//			System.out.println(this.toString() + " ~ " + previousColor);
			if(previousColor >= lower && previousColor < upper){
				dataDecimal[previousColor].setBackground(Color.GREEN);
				dataHex[previousColor].setBackground(Color.GREEN);
			}
		}
		if(scroller != null && memory != null) {
            JScrollBar bar= scroller.getVerticalScrollBar();
            if (memory.getChangedIndex() >= lower &&
                    memory.getChangedIndex() < upper && 
                    dataDecimal[memory.getChangedIndex()] != null) {
                Rectangle bounds = dataDecimal[memory.getChangedIndex()].getBounds();
                bar.setValue(Math.max(0, bounds.y - 15*bounds.height));
            }
        }
	}
	
//    public static void main(String[] args) {
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                MemoryViewPanel memoryViewPanel = new MemoryViewPanel(new Machine(),50,120);
//                JFrame frame = new JFrame("Code View Panel");
//                frame.add(memoryViewPanel.createMemoryDisplay());
//                frame.setSize(300,600);
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.setVisible(true);
//            }
//        });
//    }
}
