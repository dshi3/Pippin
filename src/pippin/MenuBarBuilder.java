package pippin;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuBarBuilder implements Observer {
    private JMenuItem assemble = new JMenuItem("Assemble Source...");
    private JMenuItem load = new JMenuItem("Load Program...");
    private JMenuItem exit = new JMenuItem("Exit");
    private JMenuItem go = new JMenuItem("Go");
    private Machine machine;

    public MenuBarBuilder(Machine machine) {
        this.machine = machine;
        machine.addObserver(this);
    }
    
    public JMenu createFileMenu(){
    	JMenu returnMenu = new JMenu("File");
    	returnMenu.setMnemonic(KeyEvent.VK_F);
    	assemble.setMnemonic(KeyEvent.VK_A);
    	load.setMnemonic(KeyEvent.VK_L);
    	exit.setMnemonic(KeyEvent.VK_E);
    	//Set the accelerator and event handler
    	assemble.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        assemble.addActionListener(e -> machine.assembleFile());
        load.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        load.addActionListener(e -> machine.loadFile());
        exit.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        exit.addActionListener(e -> machine.exit());
        //add to menu
        returnMenu.add(assemble);
        returnMenu.add(load);
        returnMenu.addSeparator();
        returnMenu.add(exit);
        
        return returnMenu;
    }
    
    public JMenu createExecuteMenu(){
    	JMenu returnMenu = new JMenu("Execute");
    	returnMenu.setMnemonic(KeyEvent.VK_X);
    	go.setMnemonic(KeyEvent.VK_G);
    	//Set the accelerator and event handler
    	go.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        go.addActionListener(e -> machine.execute());
        //add to menu
        returnMenu.add(go);
    	
    	return returnMenu;
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        assemble.setEnabled(machine.getState().getAssembleFileActive());
        load.setEnabled(machine.getState().getLoadFileActive());
        go.setEnabled(machine.getState().getStepActive());
    }
}