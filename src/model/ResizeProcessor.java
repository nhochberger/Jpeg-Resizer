package model;

import gui.listeners.ProgressListener;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import controller.listeners.FileSelectionListener;
import controller.listeners.StartResizingListener;

public class ResizeProcessor implements StartResizingListener, FileSelectionListener {

	private final List<ProgressListener> progressListeners;
	private File[] files = {};

	public ResizeProcessor() {
		super();
		this.progressListeners = new LinkedList<ProgressListener>();
	}

	@Override
	public void startResizing() {
		for (File file : this.files) {
			try {
				// TODO
				System.err.println("resizing began");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			notifyProgressListeners();
			System.err.println("resizing finished");
		}
	}

	@Override
	public void filesSelected(File... files) {
		this.files = files;
	}

	public void addProgressListener(ProgressListener listener) {
		this.progressListeners.add(listener);
	}

	private void notifyProgressListeners() {
		for (ProgressListener listener : this.progressListeners) {
			listener.progress();
		}
	}
}
