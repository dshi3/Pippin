package pippin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Loader {
	public static void load(Memory mem, Code code, File file) throws FileNotFoundException{
		try {
			Scanner input = new Scanner(file);
			boolean isCode = true;
			while(input.hasNextInt(16)){
				int inp = input.nextInt(16);
				if (inp == -1){
					isCode = false;
				} else if (isCode){
					int arg = input.nextInt(16);
					code.setCode(inp, arg);
				} else {
					int val = input.nextInt(16);
					mem.setData(inp, val);
				}
			}
			input.close();
		} catch (ArrayIndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null, 
					e.getMessage(), 
					"Failure loading data", JOptionPane.WARNING_MESSAGE);
		}
	}
}
