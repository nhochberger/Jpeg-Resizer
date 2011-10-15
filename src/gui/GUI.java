package gui;

import gui.listeners.ProgressListener;
import controller.listeners.FileSelectionListener;
import controller.listeners.StartResizingListener;

public class GUI implements ProgressListener {

	private final ImageResizerMainFrame frame;

	public GUI() {
		super();
		this.frame = new ImageResizerMainFrame();
	}

	public void activateGUI() {
		this.frame.show();
	}

	public void addFileSelectionListener(FileSelectionListener listener) {
		this.frame.addFileSelectionListener(listener);
	}

	public void addStartResizingListener(StartResizingListener listener) {
		this.frame.addStartResizingListener(listener);
	}

	@Override
	public void progress() {
		this.frame.progress();
	}
}
