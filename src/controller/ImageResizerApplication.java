package controller;

import gui.GUI;
import model.ResizeProcessor;

public class ImageResizerApplication {

	public static void main(String[] args) {
		GUI gui = new GUI();
		ResizeProcessor resizer = new ResizeProcessor();
		gui.activateGUI();
		gui.addFileSelectionListener(resizer);
		gui.addStartResizingListener(resizer);
		resizer.addProgressListener(gui);
	}

}
