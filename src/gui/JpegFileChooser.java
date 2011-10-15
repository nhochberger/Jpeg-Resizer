package gui;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JpegFileChooser extends JFileChooser {

	private static final long serialVersionUID = -557696490691461070L;

	public JpegFileChooser() {
		super();
		setMultiSelectionEnabled(true);
		setFileFilter(createJpegFileFilter());
	}

	private FileNameExtensionFilter createJpegFileFilter() {
		return new FileNameExtensionFilter("JPEG-Dateien", "jpg", "jpeg");
	}
}
