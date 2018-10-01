package control;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JOptionPane;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;

import gui.ResultsWindow;

public class PdfWorkspace{

	public static int totalFiles = 0;
	public static int totalFilesToMerge = 0;
	private ArrayList<PdfFile> allFiles = new ArrayList<PdfFile>();
	private ArrayList<PdfFile> removedFiles = new ArrayList<>();
	
	/***
	 * Merges all the files in the allFiles {@link ArrayList}
	 * @param destination The full path of the resulting file location
	 * @param progBar The results window
	 * @return 0 if merge finished correctly, else return -1
	 */
	public int mergeFiles(String destination, ResultsWindow progBar){
		
		PdfDocument pdf = null;
		boolean targetFileExists = false;
		
		// Check if the conditions of the Workspace allow for files to be merged
		if(totalFilesToMerge <= 0 || totalFiles <= 0) {
			JOptionPane.showMessageDialog(null, "No files to merge found.", "Warning", JOptionPane.WARNING_MESSAGE);
			return -1;
		}
		
		// Check if target file already exists
		File testFile = new File(destination);
		if(testFile.exists()) {
			targetFileExists = true;
			
			int returnVal = JOptionPane.showOptionDialog(null, // Component
					"Target file already exists. Do you want to overwrite it?", // Message
					"File Warning", // Title
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.WARNING_MESSAGE,
					null, // Icon
					new Object[] {"Yes" , "No"}, // Options
					"Yes" // Default selected value
					);
			
			if(returnVal == JOptionPane.NO_OPTION)
				return -1;
			
			destination = destination.replace(".pdf", ".bak");
		}
		
		try {
			
			// Open the new file
			pdf = new PdfDocument(new PdfWriter(destination));
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Could not save document to specified destination.", "Warning", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
		
		
		PdfMerger merger = new PdfMerger(pdf);
		
		progBar.showBar();
		AsyncMerger asyncMerger = new AsyncMerger(allFiles,targetFileExists,progBar,destination,testFile,merger,pdf);
		asyncMerger.start();
		
		return 0;
	}
	
	/**
	 * Adds a new {@link PdfFile} into the {@link PdfWorkspace}
	 * @param p The {@link PdfFile} to add
	 */
	public void addFileToWorkspace(PdfFile p) {
		allFiles.add(p);
		totalFiles++;
		totalFilesToMerge++;
	}
	
	/**
	 * Removes files from the {@link PdfWorkspace}
	 * @param rows The indices of the selected rows
	 * @return True if the file was removed successfully, False if the file was not in the {@link PdfWorkspace}
	 */
	public boolean removeFilesFromWorkspace(int[] rows) {
		Integer[] newRows = Arrays.stream(rows).boxed().toArray( Integer[]::new );
		Arrays.sort(newRows, Collections.reverseOrder());
		for(Integer row : newRows) {
			if(row > allFiles.size())
				return false;
			
			allFiles.remove(allFiles.get(row));
			
			totalFiles--;
			totalFilesToMerge--;
		}
		
		return true;
	}
	
	/**
	 * Duplicates the files lying on the rows given
	 * @param indices The indices of the selected rows
	 */
	public void duplicateFiles(int[] indices) {
		if(indices.length > 0) {
			for(int row : indices) {
				PdfFile master = getFile(row);
				PdfFile copy = new PdfFile(master);
				copy.setFileId(totalFiles);
				addFileToWorkspace(copy);
			}
		}
	}

	/**
	 * Moves selected files higher in the workspace
	 * @param indices The indices of the selected rows
	 * @return True if the files where moved successfully, else false
	 */
	public boolean moveFilesUp(int[] indices) {
		for(Integer index : indices) {
			if(index > -1)
				Collections.swap(allFiles, index, index - 1);
			else
				return false;
		}
		return true;
	}
	
	/**
	 * Moves selected files lower in the workspace
	 * @param indices The indices of the selected rows
	 * @return True if the files where moved successfully, else false
	 */
	public boolean moveFilesDown(int[] indices) {
		for(int i = indices.length - 1; i >= 0; i--) {
			int el = indices[i];
			if(indices.length + indices[0] < allFiles.size())
				Collections.swap(allFiles, el, el + 1);
			else
				return false;
		}
		return true;
		
	}

	public void addRemovedFile(PdfFile file){
		removedFiles.add(file);
	}

	public void undoPreviousDeletion(){

		for(PdfFile file : removedFiles) {
			this.addFileToWorkspace(file);

			totalFiles--;
			totalFilesToMerge--;
		}

		removedFiles.clear();

	}

	@Override
	public String toString() {
		String finalString = "----- WORKSPACE\n";
		
		for(PdfFile f : allFiles)
			finalString += f.getPath() + ',' + String.valueOf(f.getFileId()) + ',' + String.valueOf(f.getToMerge()) + '\n';
		
		return finalString;
	}

	public ArrayList<PdfFile> getAllFiles() {
		return allFiles;
	}
	
	public PdfFile getFile(int index) {
		return allFiles.get(index);
	}
}

/**
 * This inner class is responsible for all the actions that have to happen in a separate thread during merge
 * It is also responsible for altering the ResultsWindow object, updating the progress bar and the text next to it
 * @author TsimpDim
 *
 */
class AsyncMerger extends Thread {
	
	ArrayList<PdfFile> allFiles;
	boolean targetFileExists;
	ResultsWindow progBar; 
	String destination;
	File testFile;
	PdfMerger merger;
	PdfDocument pdf;

	/**
	 * Constructs an AsyncMerger object
	 * @param allFiles An {@link ArrayList} containing all the files
	 * @param targetFileExists Flag about whether the file we are trying to save to already exists (and needs to be overwritten) or not
	 * @param progBar The {@link ResultsWindow} object we want to show.
	 * @param destination The full path of the merged file save location
	 * @param testFile The {@link File} we use to test whether or not the file we are saving to already exists or not
	 * @param merger The {@link PdfMerger} we want to use
	 * @param pdf The final {@link PdfDocument}.
	 */
	public AsyncMerger(ArrayList<PdfFile> allFiles, boolean targetFileExists, ResultsWindow progBar,
			String destination, File testFile, PdfMerger merger, PdfDocument pdf) {

		this.allFiles = allFiles;
		this.targetFileExists = targetFileExists;
		this.progBar = progBar;
		this.destination = destination;
		this.testFile = testFile;
		this.merger = merger;
		this.pdf = pdf;
	}

	@Override
	public void run(){
		
		progBar.changeLabel("Merging files...");
		
		// Parse every file in the workspace and add it to the new file
		for(PdfFile file : allFiles) {
			
			if(file.getToMerge()) {
				PdfDocument sourcePdf = null;
				try {
					sourcePdf = new PdfDocument(new PdfReader(file.getPath()));
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Could not read file at " + file.getPath(), "Warning", JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
					continue;
				}

				merger.merge(sourcePdf, file.getPages());
			
				sourcePdf.close();
				
				progBar.incrementValue();
			}
		}

		pdf.close();
		
		progBar.changeLabel("Making final preparations...");

		if(targetFileExists) {
			testFile.delete(); // Delete the file we want to overwrite
			
			File finalFile = new File(destination); // Get the file we renamed to <destination.bak>
			finalFile.renameTo(new File(destination.replace(".bak", ".pdf"))); // Rename back to .pdf
		}
		
		progBar.changeLabel("Done");
	}
}
