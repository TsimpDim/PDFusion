package control;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import gui.ResultsWindow;
import gui.WtrmkResultsWindow;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PdfWorkspace{

	public static int totalFiles = 0;
	public static int totalFilesToInclude = 0;
	private ArrayList<PdfFile> allFiles = new ArrayList<>();
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
		if(totalFilesToInclude <= 0 || totalFiles <= 0) {
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
		totalFilesToInclude++;
	}
	
	/**
	 * Removes files from the {@link PdfWorkspace}
	 * @param rows The indices of the selected rows
	 * @return True if the file was removed successfully, False if the file was not in the {@link PdfWorkspace}
	 */
	public boolean removeFilesFromWorkspace(int[] rows) {
		Integer[] newRows = Arrays.stream(rows).boxed().toArray( Integer[]::new );
		Arrays.sort(newRows, Collections.reverseOrder());
		ArrayList<PdfFile> backupRemovedFiles = new ArrayList<>(removedFiles); // Keep a backup in case deletion fails

		removedFiles.clear(); // Forget previous deleted files
		for(Integer row : newRows) {
			if(row > allFiles.size()) {
				removedFiles = new ArrayList<>(backupRemovedFiles); // Restore the previous deleted files in case the deletion fails
				return false;
			}

			// Save removed files to the workspace for chance of undoing the deletion
			removedFiles.add(this.getFile(row));

			// Delete them
			allFiles.remove(allFiles.get(row));
			
			totalFiles--;
			totalFilesToInclude--;

			if(totalFiles < 0) totalFiles = 0;
			if(totalFilesToInclude < 0) totalFilesToInclude = 0;
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

	public void undoPreviousDeletion(){

		for(PdfFile file : removedFiles) {
			this.addFileToWorkspace(file);

			totalFiles--;
			totalFilesToInclude--;
		}

		removedFiles.clear();
	}

	@Override
	public String toString() {
		String finalString = "----- WORKSPACE\n";
		
		for(PdfFile f : allFiles)
			finalString += f.getPath() + ',' + String.valueOf(f.getFileId()) + ',' + String.valueOf(f.getToInclude()) + '\n';
		
		return finalString;
	}

	public ArrayList<PdfFile> getAllFiles() {
		return allFiles;
	}
	
	public PdfFile getFile(int index) {
		return allFiles.get(index);
	}

	/**
	 * Makes checks for watermarking and initiates it
	 * @param wtrmkOptions The watermarking options from the {@link WtrmkResultsWindow}
	 * @return -1 if watermarking can't start, else returns 0
	 */
	public int watermarkFiles(WatermarkOptions wtrmkOptions){

		// Check in case user wants to watermark selected files
		// But none of the selected is set to be included
		if(!wtrmkOptions.getWtrmkAllFiles()){
			int filesNotToInclude = 0;

			for(int index : wtrmkOptions.getSelectedFiles()){
				if(!getFile(index).getToInclude())
					filesNotToInclude++;
			}

			if(filesNotToInclude == wtrmkOptions.getSelectedFiles().length){
				JOptionPane.showMessageDialog(null, "None of your files are set to be included", "Warning", JOptionPane.WARNING_MESSAGE);
				return -1;
			}
		}else{ // If user wants to watermark all files but again, none are set to be included
			if(totalFilesToInclude == 0){
				JOptionPane.showMessageDialog(null, "None of your files are set to be included", "Warning", JOptionPane.WARNING_MESSAGE);
				return -1;
			}
		}

		
		WtrmkResultsWindow resWindow = new WtrmkResultsWindow((wtrmkOptions.getWtrmkAllFiles() ? totalFilesToInclude : wtrmkOptions.getSelectedFiles().length), "Gathering files");
		AsyncStamper watermarker = new AsyncStamper(resWindow, this.allFiles, wtrmkOptions);
		watermarker.start();

		return 0;
	}
}

/**
 * This inner class handles asynchronously the merging of {@link PdfFile}s.
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
			
			if(file.getToInclude()) {
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

/**
 * This inner class handles asynchronously the watermarking of {@link PdfFile}s.
 */
class AsyncStamper extends Thread{

	private WtrmkResultsWindow resWindow;
	private ArrayList<PdfFile> allFiles;
	private WatermarkOptions options;


	public AsyncStamper(WtrmkResultsWindow resWindow, ArrayList<PdfFile> allFiles, WatermarkOptions options){
	    this.allFiles = allFiles;
		this.resWindow = resWindow;
		this.options = options;
	}

	@Override
	public void run(){
		// Set which files are to be watermarked
		ArrayList<PdfFile> filesToWtrmk = new ArrayList<>(allFiles);
		if(!options.getWtrmkAllFiles()) {
			filesToWtrmk.clear();
			for (int index : options.getSelectedFiles())
				filesToWtrmk.add(allFiles.get(index));
		}

		// Start watermarking
		resWindow.changeLabel("Watermarking files");
		for (PdfFile curFile : filesToWtrmk){

			if(!curFile.getToInclude())
				continue;

			String dest = curFile.getPath().replace(".pdf", ".bak");
			PdfDocument pdfDoc;

			try {
				pdfDoc = new PdfDocument(new PdfReader(curFile.getPath()), new PdfWriter(dest));
			}catch(java.io.IOException e){
				JOptionPane.showMessageDialog(null, "Some files could not be read", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			Document doc = new Document(pdfDoc);


			// Watermark Text
			Paragraph p = new Paragraph(options.getWtrmkText());
			PdfCanvas over;

			// Opacity
			PdfExtGState gs1 = new PdfExtGState().setFillOpacity(options.getWtrmkOpac());

			// Page Size
			Rectangle pageSize;
			float x,y;

			// Watermark positions
            Integer wtrmkPos = options.getWtrmkPos();

            PdfFont pageFont;
            try {
                pageFont = PdfFontFactory.createFont(FontConstants.HELVETICA);
            }catch(java.io.IOException e){
                return;
            }

            p.setFont(pageFont);
            p.setFontSize(options.getWtrmkFontSize());

            Float x_offset = pageFont.getWidth(options.getWtrmkText(), options.getWtrmkFontSize())/2;
            Integer y_offset = pageFont.getAscent(options.getWtrmkText(), options.getWtrmkFontSize()); // Replace fontSize with WatermarkOptions property

            for(Integer pageIdx : curFile.getPages()) { // Watermark only selected page range
                PdfPage page = pdfDoc.getPage(pageIdx);
                pageSize = page.getPageSizeWithRotation();
                page.setIgnorePageRotationForContent(true);

                // Set watermark position
				if(wtrmkPos == 0) { // Top Left
					x = pageSize.getLeft() + x_offset;
					y = pageSize.getTop() - y_offset;
				}else if(wtrmkPos == 1){ // Top Right
					x = pageSize.getRight() - x_offset;
					y = pageSize.getTop() - y_offset;
				}else if(wtrmkPos == 2){ // Top Center
					x = (pageSize.getLeft() + pageSize.getRight()) / 2;
					y = pageSize.getTop() - y_offset;
				}else if(wtrmkPos == 3){ // Bottom Left
					x = pageSize.getLeft() + x_offset;
					y = pageSize.getBottom() + y_offset;
				}else if(wtrmkPos == 4){ // Bottom Right
					x = pageSize.getRight() - x_offset;
					y = pageSize.getBottom() + y_offset;
				}else if(wtrmkPos == 5){ // Bottom Center
					x = (pageSize.getLeft() + pageSize.getRight()) / 2;
					y = pageSize.getBottom() + y_offset;
				}else{ // Center
					x = (pageSize.getLeft() + pageSize.getRight()) / 2;
					y = (pageSize.getTop() + pageSize.getBottom()) / 2;
				}

				over = new PdfCanvas(page);
				over.saveState();
				over.setFontAndSize(pageFont, options.getWtrmkFontSize());
				over.setExtGState(gs1);
				doc.showTextAligned(p, x, y, pageIdx, TextAlignment.CENTER, VerticalAlignment.MIDDLE, (float)Math.toRadians(options.getWtrmkRot()));
			}

			doc.close();

			// Replace .pdf with .bak
			File fileWithoutWtrmk = new File(curFile.getPath());
			File fileWithWtrmk = new File(curFile.getPath().replace(".pdf", ".bak"));

			// Delete the file we want to overwrite
			fileWithoutWtrmk.delete();
			fileWithWtrmk.renameTo(new File(curFile.getPath().replace(".bak", ".pdf"))); // Rename .bak to .pdf

			resWindow.incrementValue();
		}

		resWindow.changeLabel("Watermarking completed");
	}
}