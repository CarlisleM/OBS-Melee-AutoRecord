// Created by Carlisle Miller aka Kami

// The purpose of this program is to detect particular pixels on the cpu select screen
// and stage select screen and save the data to individual files

// How to use: 
//    Use "Mofiki's Coordinate Finder" to locate 16 pixels around the "CPU" area of the player 1 slot
//	  Also find the width (x coordinate) difference between slot 1 and 2
//    Edit the pixel locations located inside the SetupCurrentCpuColours() function with the ones you've made
//	  Edit the x incrementer with the difference in width between slot 1 and slot 2
//    Run the program while on the cpu select screen with all slots set to cpu (make sure the player hand is not covering them)
//	  Press start to go to the stage select screen when prompted

// Screen shots included in the folder on how to set up for clarification //

import java.awt.*;
import java.io.*;

public class RecordData {

	static String[] fileNames = {"cpuSlot1", "cpuSlot2", "cpuSlot3", "cpuSlot4", "stage"};
	static CpuSlot cpu = new CpuSlot("cpuSlot1");	
	static Color[] cpuColors = null;
	static int totalPix = 0;
	static int x = 0;
	static int i = 0;
	
	private static void SetupCurrentCpuColours() throws Exception {

		cpu = new CpuSlot(fileNames[i]);

		// Sets up stage select pixels to be the same as CPU 1 location, pauses 5 seconds
		if (cpu.name == fileNames[4]) {
			x = 0;
			System.out.println("Enter stage select screen.");
			try {
				Thread.sleep(3000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
		
		// These 12 pixels define the "CPU" slot tag
		cpuColors[0] = (GetPixelInfo(748+x, 506));
		cpuColors[1] = (GetPixelInfo(739+x, 507));
		cpuColors[2] = (GetPixelInfo(748+x, 520));
		cpuColors[3] = (GetPixelInfo(738+x, 518));
		cpuColors[4] = (GetPixelInfo(760+x, 515));
		cpuColors[5] = (GetPixelInfo(759+x, 506));
		cpuColors[6] = (GetPixelInfo(773+x, 508));
		cpuColors[7] = (GetPixelInfo(772+x, 513));
		cpuColors[8] = (GetPixelInfo(783+x, 508));
		cpuColors[9] = (GetPixelInfo(783+x, 517));
		cpuColors[10] = (GetPixelInfo(795+x, 508));
		cpuColors[11] = (GetPixelInfo(796+x, 516));
		// These 4 pixels are the yellow pixels located on the "Back" part of the cpu select screen
		cpuColors[12] = (GetPixelInfo(1542, 182));
		cpuColors[13] = (GetPixelInfo(1569, 182));
		cpuColors[14] = (GetPixelInfo(1618, 182));
		cpuColors[15] = (GetPixelInfo(1663, 182));
		// These 4 pixels
		cpuColors[16] = (GetPixelInfo(728, 433));
		cpuColors[17] = (GetPixelInfo(727, 464));
		
		// Replace 150 with the distance between your CPU slot 1 and CPU slot 2
		x = x+227; 
		
		System.out.println(fileNames[i] + " is set up." );
		
		i++;
	}
	
	// Gets colour (RGB Values) at x and y coordinate
	private static Color GetPixelInfo(int x, int y) throws Exception {
		 Robot robot = new Robot();
		 Color color = robot.getPixelColor(x, y);
		 return color;
	}

	public static void main(String[] args) throws Exception {
	
		// Set pixel locations using Mofiki's Coordinate Finder
		totalPix = cpu.totalPixels;
		cpuColors = new Color[totalPix];
		
		int a;
		
		for (a = 0; a < 5; a++) {		
			// Get current pixels
			SetupCurrentCpuColours();
			
			// This section sets up new cpus pixel data
			try {				
				FileWriter wr = new FileWriter("PixelData//" + cpu.name + ".txt");
				wr.write(cpu.name);
				wr.write(System.getProperty( "line.separator" ));
				
				for (int i = 0; i < totalPix; i++) {
					cpu.pixels[i][0] = cpuColors[i].getRed();
					cpu.pixels[i][1] = cpuColors[i].getGreen();
					cpu.pixels[i][2] = cpuColors[i].getBlue();

					wr.write(new Integer(cpu.pixels[i][0]).toString());
					wr.write(System.getProperty( "line.separator" ));
					wr.write(new Integer(cpu.pixels[i][1]).toString());
					wr.write(System.getProperty( "line.separator" ));
					wr.write(new Integer(cpu.pixels[i][2]).toString());
					wr.write(System.getProperty( "line.separator" ));
				}	
				wr.close();
			}
			catch (IOException e) {
			//
			}
		}
	}
}