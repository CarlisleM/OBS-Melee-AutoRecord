import java.awt.*;

public class CpuSlot {
	
	int red = 0;
	int green = 0;
	int blue = 0;
	String name = " ";
	
	Color[] pixelColors = new Color[18];
	int totalPixels = 18;
	int[][]pixels = new int[totalPixels][3];
	
	public CpuSlot(String n) {
		name = n;
	}

}