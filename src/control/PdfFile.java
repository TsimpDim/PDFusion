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
	 * Initialize a new PdfFile with page restrictions
	 * @param path The file path
	 * @param pages An ArrayList with all the pages to use. Set null for full pages.
	 * @param toMerge 
	 * @param fileId
	 */
	public PdfFile(String path, ArrayList<Integer> pages, Boolean toMerge, int fileId) {
		super(path);
		this.path = path;
		this.pages = pages;
		this.toMerge = toMerge;
		this.fileId = fileId;
	}
	
	/**
	 * Initialize a new PdfFile without page restrictions
	 * @param path The file path
	 * @param toMerge 
	 * @param fileId
	 */
	public PdfFile(String path, Boolean toMerge, int fileId) {
		super(path);
		this.path = path;
		this.pages = null;
		this.toMerge = toMerge;
		this.fileId = fileId;
	}
	
	/**
	 * Initialize a new PdfFile with a string pages parameter
	 * @param path The file path
	 * @param pages A String containing the pages to include
	 * @param toMerge
	 * @param fileId
	 */
	public PdfFile(String path, String pages, Boolean toMerge, int fileId) {
		super(path);
		this.path = path;
		this.setPages(pages);
		this.toMerge = toMerge;
		this.fileId = fileId;
	}

	/**
	 * Create a new PdfFile from an existing one
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
	 * @return All available pages of current file in an ArrayList
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
	 * Transforms a range String into an ArrayList
	 * @param pages A String representation of the selection range
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
					wrongInputError();
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
					wrongInputError();
					return;
				}
					
				
				// Add all pages within range
				for(int i = start; i < end+1; i++) 
					new_pages.add(i);
				
			}else {
				try {
					new_pages.add(Integer.valueOf(str));
				}catch(NumberFormatException e) {
					wrongInputError();
					return;
				}
			}
		}
			
		this.pages = new_pages;
	}

	public void wrongInputError() {
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
