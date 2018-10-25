![PDFusion_logo](https://raw.githubusercontent.com/TsimpDim/PDFusion/master/src/res/PDFusion_logo_full.png)

# PDFusion
PDFusion is a complete rewrite of [PDF_Merger](https://www.github.com/TsimpDim/Pdf_Merger) in Java using [iText 7 Community](https://itextpdf.com/itext-7-community).

### Status
As of now the application supports merging, splitting and watermarking files. A possible upcoming feature is interweaving of files.

### Why rewrite an existing program?
PDF_Merger is a testament to why you should research what you are trying to do *extensively* before actually doing it. Its spaghetti code, albeit (barely) functional, heavily restricts its capabilities. PDF_Merger is also very limited in scope as it is built with WPF.

Thus, with my new-found knowledge of Java (established through school work) i'm trying to create a much more mature, stable, and available program.

### Goals
As with PDF_Merger, the goal here is to create an application which will allow the user to  
 * Merge (both fully and selectively)(completed)  
 * Watermark (completed)
and  
 * Split (supported via merging functionality)
 
PDF files.

With those goals completed, what now? The goal now is long term support, maintenance and various QoL fixes.