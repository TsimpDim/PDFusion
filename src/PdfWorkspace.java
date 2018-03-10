import java.util.ArrayList;

public class PdfWorkspace {

	private static int totalFiles;
	private ArrayList<PdfFile> allFiles = new ArrayList<PdfFile>();
	
	/***
	 * Merge all the files in the allFiles ArrayList
	 * @return The amount of files merged
	 */
	public static int MergePDFs() {
		
		return 0;
	}
	
	/**
	 * Add a new PdfFile into the PdfWorkspace
	 * @param p The PdfFile to add
	 * @return True if the file was added successfully, False if the file is already in the PdfWorkspace
	 */
	public boolean AddPdfToWorkspace(PdfFile p) {
		if(allFiles.contains(p))
			return false;
		else
			allFiles.add(p);
			return true;
	}
}
