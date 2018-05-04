package control;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;

public class PdfFile extends File{


	private static final long serialVersionUID = -8832254767996433033L;
	
	private String path;
	private ArrayList<Integer> pages = new ArrayList<Integer>();
	private Boolean toMerge;
	private int fileId;
	
	/**
	 * Initializes a new {@link PdfFile} with page restrictions
	 * @param path The file path
	 * @param pages An {@link ArrayList} with all the pages to use. Set null for full pages.
	 * @param toMerge Whether or not this file should be merged
	 * @param fileId File specific id
	 */
	public PdfFile(String path, ArrayList<Integer> pages, Boolean toMerge, int fileId) {
		super(path);
		this.path = path;
		this.pages = pages;
		this.toMerge = toMerge;
		this.fileId = fileId;
	}
	
	/**
	 * Initializes a new {@link PdfFile} without page restrictions
	 * @param path The file path
	 * @param toMerge Whether or not this file should be merged
	 * @param fileId File specific id
	 */
	public PdfFile(String path, Boolean toMerge, int fileId) {
		super(path);
		this.path = path;
		this.pages = null;
		this.toMerge = toMerge;
		this.fileId = fileId;
	}
	
	/**
	 * Initializes a new {@link PdfFile} with a string pages parameter
	 * @param path The file path
	 * @param pages A {@link String} containing the pages to include
	 * @param toMerge Whether or not this file should be merged
	 * @param fileId File specific id
	 */
	public PdfFile(String path, String pages, Boolean toMerge, int fileId) {
		super(path);
		this.path = path;
		this.setPages(pages);
		this.toMerge = toMerge;
		this.fileId = fileId;
	}

	/**
	 * Creates a new {@link PdfFile} from an existing one
	 * @param master The prototype for the new object
	 */
	public PdfFile(PdfFile master) {
		super(master.path);
		this.path = master.path;
		this.pages = master.pages;
		this.toMerge = master.toMerge;
		this.fileId = master.fileId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ArrayList<Integer> getPages() {
		
		if(pages != null)
			return pages;
		else 
			return getAvailablePages();
	}

	/**
	 * Opens the current file and reads the amount of pages it has
	 * @return An {@link ArrayList} with the same size as the pages on the current file
	 */
	public ArrayList<Integer> getAvailablePages(){
		PdfDocument sourcePdf = null;
		int numPages = -1;
		ArrayList<Integer> pages = new ArrayList<>();
		
		try {
			sourcePdf = new PdfDocument(new PdfReader(this.path));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		numPages = sourcePdf.getNumberOfPages();
		sourcePdf.close();
		
		for(int i = 1; i < numPages + 1; i++)
			pages.add(i);
		
		return pages;
	}
	
	public void setPages(ArrayList<Integer> pages) {
		this.pages = pages;
	}
	
	/**
	 * Transforms a range {@link String} into an {@link ArrayList}
	 * e.g "1,3-5" = {1,3,4,5}<br>
	 * e.g "2-" = {2,3,4,5,6....}<br>
	 * e.g "1-3,5,6-8" = {1,2,3,5,6,7,8}<br>
	 * e.g "-5" = <strong>error</strong><br>
	 * e.g "10-15" = <strong>error</strong> <i>if the file does not have a 10th page</i><br>
	 * e.g "12-32" = <strong>error</strong> <i>if the file does have a 12th page but not 32 pages</i><br>
	 * @param pages A {@link String} representation of the selection range
	 */
	public void setPages(String pages) {
		ArrayList<Integer> new_pages = new ArrayList<>();
		String[] splitStr = pages.split(Pattern.quote(","));
		
		if(pages.toLowerCase().equals("all")) {
			this.pages = null;
			return;
		}
		
		
		for(String str : splitStr) {
			if (str.contains("-")) {
				
				// Get all values within given range
				String[] splitRange = str.split(Pattern.quote("-"));

				int end = 1;
				int start = 1;
				
				// No start given
				try {
					start = Integer.valueOf(splitRange[0]);
				}catch(NumberFormatException e) {
					showWrongInputError();
					return;
				}
				
				// No end given
				try {
					end = Integer.valueOf(splitRange[1]);
				}catch(NumberFormatException | ArrayIndexOutOfBoundsException e) {
					end = getAvailablePages().size(); // If no end is given, assume range till end of file
				}
				
				// Start out of range
				if(start > getAvailablePages().size()) {
					showWrongInputError();
					return;
				}
					
				
				// Add all pages within range
				for(int i = start; i < end+1; i++) 
					new_pages.add(i);
				
			}else {
				try {
					new_pages.add(Integer.valueOf(str));
				}catch(NumberFormatException e) {
					showWrongInputError();
					return;
				}
			}
		}
			
		this.pages = new_pages;
	}

	public void showWrongInputError() {
		JOptionPane.showMessageDialog(null, "Wrong input given. Only numbers, commas and dashes are allowed.\n"
				+ "Make sure input is within available pages.", "Warning", JOptionPane.WARNING_MESSAGE);
		this.pages = null;
	}
	public Boolean getToMerge() {
		return toMerge;
	}

	public void setToMerge(Boolean toMerge) {
		this.toMerge = toMerge;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	/**
	 * @return A {@link String} version of the {@link ArrayList} containing the selected pages
	 */
	public String getPagesString() {
		String finalString = "";
		
		if(pages == null)
			finalString =  "All";
		else {
			for(Integer pg : pages) 
				finalString += String.valueOf(pg) + ',';
			
			finalString = finalString.substring(0,finalString.length() - 1); // Delete final comma
		}
		
		return finalString;
	}
	
	
	
	
}
