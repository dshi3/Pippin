package pippin;

import java.util.Arrays;

public class Memory {
    public static final int DATA_SIZE = 512;
    private int[] data = new int[DATA_SIZE];
    private int changedIndex = -1;
    
	public void setData(int index, int value) {
        data[index] = value;
        changedIndex = index;
    }
    public int getData(int index) {
        return data[index];
    }
    
    public void clear(){
    	Arrays.fill(data, 0);
    	changedIndex = -1;
    }
    
    int[] getData() {
        return data;
    }
    
    public int getChangedIndex() {
		return changedIndex;
	}
}