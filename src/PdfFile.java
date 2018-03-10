import java.util.ArrayList;

public class PdfFile {

	private String path;
	private ArrayList<Integer> pages = new ArrayList<Integer>();
	private Boolean toMerge;
	private int fileId;
	
	/***
	 * Initialize a PdfFile with all properties
	 * @param path
	 * @param pages
	 * @param toMerge
	 * @param fileId
	 */
	public PdfFile(String path, ArrayList<Integer> pages, Boolean toMerge, int fileId) {
		this.path = path;
		this.pages = pages;
		this.toMerge = toMerge;
		this.fileId = fileId;
	}
	
	/***
	 * Initialize a PdfFile without the pages ArrayList - i.e use all the pages of the file
	 * @param path
	 * @param toMerge
	 * @param fileId
	 */
	public PdfFile(String path, Boolean toMerge, int fileId) {
		this.path = path;
		this.pages = null;
		this.toMerge = toMerge;
		this.fileId = fileId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ArrayList<Integer> getPages() {
		return pages;
	}

	public void setPages(ArrayList<Integer> pages) {
		this.pages = pages;
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
	
	
	
	
}
