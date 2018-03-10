package control;
import gui.MainWindow;

public class Main {

	public static void main(String[] args) {

		PdfWorkspace workspace = new PdfWorkspace();
		new MainWindow(workspace);

	}

}
