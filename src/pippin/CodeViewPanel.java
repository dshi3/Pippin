package pippin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class CodeViewPanel implements Observer{
	private Code code;
	private Processor cpu;
	private JScrollPane scroller;
	private JTextField[] codeText = new JTextField[Code.CODE_MAX];
	private JTextField[] codeHex = new JTextField[Code.CODE_MAX];
	private int previousColor = -1;
	
	public CodeViewPanel (Machine machine){
		this.code = machine.getCode();
		this.cpu = machine.getCpu();
		machine.addObserver(this);
	}
	
	public JComponent createCodeDisplay(){
		JPanel returnPanel = new JPanel(), 
				panel = new JPanel(), numPanel = new JPanel(), 
				sourcePanel = new JPanel(), hexPanel = new JPanel();
		//layout and dimensioning
		returnPanel.setPreferredSize(new Dimension(300,150));;
        returnPanel.setLayout(new BorderLayout());
        panel.setLayout(new BorderLayout());
        numPanel.setLayout(new GridLayout(0,1));
        sourcePanel.setLayout(new GridLayout(0,1));
        hexPanel.setLayout(new GridLayout(0,1));
        //set row numbers and texts
        for(int i = 0; i < Code.CODE_MAX; i++) {
        	numPanel.add(new JLabel(i+": ", JLabel.RIGHT));
        	codeText[i] = new JTextField(10);
        	codeHex[i] = new JTextField(10);
        	sourcePanel.add(codeText[i]);
        	hexPanel.add(codeHex[i]);
        }
        //Give the returnPanel a Border
        Border border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Code Memory View",
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
        returnPanel.setBorder(border);
        //Assemble the JPanels, some side by side and some nested
        panel.add(numPanel, BorderLayout.LINE_START);
        panel.add(sourcePanel, BorderLayout.CENTER);
        panel.add(hexPanel, BorderLayout.LINE_END);
        scroller = new JScrollPane(panel);
        returnPanel.add(scroller);
        
        return returnPanel;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 != null && arg1.equals("Load Code")) {
            for(int i = 0; i < Code.CODE_MAX; i++) {
                codeText[i].setText(code.getCodeText(i));
                codeHex[i].setText(code.getCodeHex(i));
            }
            previousColor = cpu.getProgramCounter();
            codeText[previousColor].setBackground(Color.YELLOW);
            codeHex[previousColor].setBackground(Color.YELLOW);
        }
		if(arg1 != null && arg1.equals("Clear")){
			for(int i = 0; i < codeText.length; i++){
				codeText[i].setText("");
			}
			for(int i = 0; i < codeHex.length; i++){
				codeHex[i].setText("");
			}
			if(previousColor >= 0){
				codeText[previousColor].setBackground(Color.WHITE);
	            codeHex[previousColor].setBackground(Color.WHITE);
			}
			previousColor = -1;
		}
		if(previousColor >= 0){
//			try{
				codeText[previousColor].setBackground(Color.WHITE);
				codeHex[previousColor].setBackground(Color.WHITE);
				previousColor = cpu.getProgramCounter();
				codeText[previousColor].setBackground(Color.YELLOW);
				codeHex[previousColor].setBackground(Color.YELLOW);
//			} catch(ArrayIndexOutOfBoundsException e){
//			}
		}
		if(scroller != null && code != null && cpu!= null) {
            JScrollBar bar= scroller.getVerticalScrollBar();
            if(cpu.getProgramCounter() < Code.CODE_MAX && codeText[cpu.getProgramCounter()] != null) {
                Rectangle bounds = codeText[cpu.getProgramCounter()].getBounds();
                bar.setValue(Math.max(0, bounds.y - 15*bounds.height));
            }
        }
	}
	
//	public static void main(String[] args) {
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                CodeViewPanel codeViewPanel = new CodeViewPanel(new Machine());
//                JFrame frame = new JFrame("Code View Panel");
//                frame.add(codeViewPanel.createCodeDisplay());
//                frame.setSize(300,600);
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.setVisible(true);
//            }
//        });
//    }
}
