package control;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.lang.model.element.Element;
import javax.swing.text.Document;

import com.itextpdf.io.codec.Base64;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.font.FontInfo;

public class PdfWorkspace {

	public static int totalFiles = 0;
	private ArrayList<PdfFile> allFiles = new ArrayList<PdfFile>();
	
	/***
	 * Merge all the files in the allFiles ArrayList
	 * @return The amount of files merged
	 * @throws IOException 
	 */
	public int MergePDFs() throws IOException {
		
		PdfDocument pdf = new PdfDocument(new PdfWriter("MERGED_PDF.pdf"));
		PdfMerger merger = new PdfMerger(pdf);
		int files_merged = 0;
		
		
		for(PdfFile file : allFiles) {
			PdfDocument sourcePdf = new PdfDocument(new PdfReader(file.getPath()));
			try {
			merger.merge(sourcePdf, file.getPages());
			}catch(NullPointerException e) {
				sourcePdf.close();
				break;
			}
			files_merged++;
			sourcePdf.close();
		}
	


		pdf.close();
		return files_merged;
	}
	
	/**
	 * Add a new PdfFile into the PdfWorkspace
	 * @param p The PdfFile to add
	 * @return True if the file was added successfully, False if the file is already in the PdfWorkspace
	 */
	public boolean AddPdfToWorkspace(PdfFile p) {
		if(allFiles.contains(p))
			return false;
		else {
			allFiles.add(p);
			return true;
		}
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
}
