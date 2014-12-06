package pippin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MemoryViewPanel implements Observer{
	private Memory memory;
	private JScrollPane scroller;
	private JTextField[] dataDecimal = new JTextField[Code.CODE_MAX];
	private JTextField[] dataHex = new JTextField[Code.CODE_MAX];
	private int lower;
	private int upper;
	
	public MemoryViewPanel(Machine machine, int lower, int upper){
		memory = machine.getMemory();
		this.lower = lower;
		this.upper = upper;
		machine.addObserver(this);
	}
	
	public JComponent createMemoryDisplay(){
		JPanel returnPanel = new JPanel(), 
				panel = new JPanel(), numPanel = new JPanel(), 
				decimalPanel = new JPanel(), hexPanel = new JPanel();
		//layout and dimensioning
		returnPanel.setPreferredSize(new Dimension(300,150));;
        returnPanel.setLayout(new BorderLayout());
        panel.setLayout(new BorderLayout());
        numPanel.setLayout(new GridLayout(0,1));
        decimalPanel.setLayout(new GridLayout(0,1));
        hexPanel.setLayout(new GridLayout(0,1));
        //set row numbers and texts
        for(int i = 0; i < Code.CODE_MAX; i++) {
        	numPanel.add(new JLabel(i+": ", JLabel.RIGHT));
        	dataDecimal[i] = new JTextField(10);
        	dataHex[i] = new JTextField(10);
        	decimalPanel.add(dataDecimal[i]);
        	hexPanel.add(dataHex[i]);
        }
        //Give the returnPanel a Label
        returnPanel.add(new JLabel("Data Memory View [" + lower + "-" + upper + "]", JLabel.CENTER), 
                BorderLayout.PAGE_START);
        //Assemble the JPanels, some side by side and some nested
        panel.add(numPanel, BorderLayout.LINE_START);
        panel.add(decimalPanel, BorderLayout.CENTER);
        panel.add(hexPanel, BorderLayout.LINE_END);
        scroller = new JScrollPane(panel);
        returnPanel.add(scroller);
        
        return returnPanel;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		for(int i = lower; i < upper; i++) {
            //if(memory.getData(i) != 0) System.out.println("DMupdate " + lower + " " + i + " " + memory.getData(i));
            dataDecimal[i].setText(""+memory.getData(i));
            dataHex[i].setText(Integer.toHexString(memory.getData(i)));
        }
	}
	
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MemoryViewPanel memoryViewPanel = new MemoryViewPanel(new Machine(),50,120);
                JFrame frame = new JFrame("Code View Panel");
                frame.add(memoryViewPanel.createMemoryDisplay());
                frame.setSize(300,600);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}
