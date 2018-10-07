// Created by Carlisle Miller aka Kami

// The purpose of this program is to detect particular pixels on the character select screen
// and stage select screen and determine when to start and stop recording based off  of the
// current screen data in comparison to stored data of each location.

// How to use: 
//	  First edit "RecordData" and following those instructions.
//    Copy paste the pixel locations you made in "RecordData" over top of the ones here in 'CurrentSlotColours'
//	  Edit the x incrementer with the difference in width between slot 1 and slot 2
//    Enter the stage select screen, begin recording, run this program.

// Screen shots included on how to set up for clarification //

import java.io.*;
import java.awt.*;
import java.util.*;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

public class AutoRecord {
	
	// Declarations
	static Color[] cpuColors = null;
	static CpuSlot[] allCpus = new CpuSlot[4];
	static CpuSlot[] ToCompareTo = new CpuSlot[5];
	static int RGBvaluesMatchUp;
		
	// GetPixelInfo function
	// Gets colour (RGB Values) at x and y cordinate
	private static Color GetPixelInfo(int x, int y) throws Exception {
		 Robot robot = new Robot();
		 Color color = robot.getPixelColor(x, y);
		 return color;
	}
	
	// CurrentSlotColours function
	// Gets current colours of all 4 slots currently displayed
	private static void CurrentSlotColours() throws Exception {	
		
		int i;
		int x = 0;
		
		for (i = 0; i < 4; i++) {
			
			String[] cpuNames = { "cpuSlot1", "cpuSlot2", "cpuSlot3", "cpuSlot4"};
			allCpus[i] = new CpuSlot(cpuNames[i]);

			// Declare pixel locations of cpu slot 1
			cpuColors = allCpus[i].pixelColors;
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
			// These 2 pixels are the red on "addition rules"
			cpuColors[16] = (GetPixelInfo(728, 433));
			cpuColors[17] = (GetPixelInfo(727, 464));
			
			for (int p = 0; p < 18; p++) {
				allCpus[i].pixelColors[p] = cpuColors[p];
			}
			
			// Increment x co-ordinate values
			x = x+227;
			
			for (int d = 0; d < allCpus[i].totalPixels; d++) {		
				allCpus[i].pixels[d][0] = cpuColors[d].getRed();
				allCpus[i].pixels[d][1] = cpuColors[d].getGreen();
				allCpus[i].pixels[d][2] = cpuColors[d].getBlue();
			}
		}		
	}
	
	// LoadCharacterPixelData function
	// Loads data from all files in "PixelData"
	private static void LoadCharacterPixelData() throws IOException {
		
		File folder = new File("PixelData");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			
			if (file.isFile() && file.getName().endsWith(".txt")) {
				try {
					FileInputStream fis = new FileInputStream(file);
					BufferedReader br = new BufferedReader(new InputStreamReader(fis));
					
					String line = null;
					
					ToCompareTo[i] = new CpuSlot("");
					
					ToCompareTo[i].name = br.readLine();

					line = br.readLine();

					int a = 0;
					
					while (line != null) {
						
						int b = 0;
						
						for (b = 0; b < 3; b++) {							
							ToCompareTo[i].pixels[a][b] = Integer.parseInt(line);
							line = br.readLine();							
							//System.out.println(line);
						}						
						a++;
					}
					br.close();
				}
				catch (FileNotFoundException e) {
					//
				}
			}		
		}
	}
		
	// Main program
	public static void main(String[] args) throws Exception {
	
		System.out.println("Program is running");

		Robot robot = new Robot();
		
		// Boolean to make each action loop only perform once until conditions are changed
		boolean recording = false;
		
		// Make all null
		Arrays.fill(allCpus, null);
		
		// Threshold (Minimum amount of pixels that must match to be considered correct)
		int threshold = 16;
		int stage_threshold = 34;
		int setCounter = 0;
		int b;
		
		// ********** START OF MAIN LOOP ********** // 
		while (true) {

			// Loads all pixel data from "PixelData"
			LoadCharacterPixelData();
			
			// Gets current on screen pixels
			CurrentSlotColours();
			
			// Declarations
			int stageRGBvalues = 0;
			int[] characterSummary = new int[4];
			boolean isStageSelect = false;
			
			for (b = 0; b < 4; b++) {
				
				// Counter and leniency for RGB values
				int lowerBound = 0;
				int upperBound = 0;
				RGBvaluesMatchUp = 0;
				
				// ONLY for player 1 pixel data, check if stage select //
				if (b == 1) {
					for (int _c = 0; _c < 18; _c++) {						
						for (int _p = 0; _p < 3; _p++) {
							lowerBound = allCpus[b].pixels[_c][_p] - 10;
							upperBound = allCpus[b].pixels[_c][_p] + 10;
							
							if (ToCompareTo[4].pixels[_c][_p] > lowerBound && ToCompareTo[4].pixels[_c][_p] < upperBound) {
								stageRGBvalues++;
								// It is within the set range
								if (stageRGBvalues >= stage_threshold) {
									isStageSelect = true;
								}
							}
						}
					}
				}
				
				// **** Check against cpu data ****** //
				for (int c = 0; c < 18; c++) {
				
					int isInBounds = 0;
								
					for (int p = 0; p < 3; p++) {						
						lowerBound = allCpus[b].pixels[c][p] - 20;
						upperBound = allCpus[b].pixels[c][p] + 20;
						
						if (ToCompareTo[b].pixels[c][p] > lowerBound && ToCompareTo[b].pixels[c][p] < upperBound) {		
							// It is within the set range
							isInBounds++;
							if (isInBounds == 2) {								
								RGBvaluesMatchUp++;
							}
						}						
					}			
				}
				
				//System.out.println("Matching pixels for player " + (b+1) + " : " +RGBvaluesMatchUp );
				
				// Check if amount of pixels matched is above the threshold
				if (RGBvaluesMatchUp >= threshold) {
					characterSummary[b] = 1;
					//System.out.println("Character slot " + b + " is a CPU. " + "Amount of pixels matched: " + RGBvaluesMatchUp );
				} else {
					characterSummary[b] = 0;
				}			
			}	
			
			if (isStageSelect) {			
				// If on stage select screen perform these actions
				if (recording == false) {
					System.out.println("Stage select screen detected. Performing actions...");
					System.out.println("Stage select matched with " + stageRGBvalues + " pixels" );
					
					// Stop preview
					//robot.mouseMove(861, 664);
					//robot.mousePress(InputEvent.BUTTON1_MASK);
					//robot.mouseRelease(InputEvent.BUTTON1_MASK);
					System.out.println("    Previewing stopped");					
					
					// Start recording
					//robot.keyPress(KeyEvent.VK_1);
					//robot.keyRelease(KeyEvent.VK_1);
					System.out.println("    Recording started");
					
					setCounter++;
					
					System.out.println("Set number: " + setCounter);
					
					recording = true;
				}				
			}
			
			// ********** Summary ********** //
			for (int u = 0; u < 4; u++) {
				if (characterSummary[u] == 1) {
					// If on character select screen perform these actions
					 if (recording == true) {			 
						System.out.println("Character select screen detected. Performing actions...");						
						System.out.println("Character slot " + b + " is a CPU. " + "Amount of pixels matched: " + RGBvaluesMatchUp );
						
						// Stop recording
						//robot.keyPress(KeyEvent.VK_2);
						//robot.keyRelease(KeyEvent.VK_2);
						System.out.println("    Recording stopped");
						
						//robot.delay(500);
						
						// Start preview
						//robot.mouseMove(861, 664);
						//robot.mousePress(InputEvent.BUTTON1_MASK);
						//robot.mouseRelease(InputEvent.BUTTON1_MASK);
						System.out.println("    Previewing started");
						
						//robot.delay(2000);
						
						recording = false;
					}
				} else { 

				}			
			}					
		} 
		// ********** END OF MAIN LOOP ********** //
	}		
}