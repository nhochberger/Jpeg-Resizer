/*******************************************************************************
 * Copyright (c) 2011 Nico Hochberger.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Nico Hochberger - initial API and implementation
 ******************************************************************************/
package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import com.thebuzzmedia.imgscalr.Scalr;
import com.thebuzzmedia.imgscalr.Scalr.Method;

import controller.ImageResizerApplication;
import controller.listeners.FileSelectionListener;
import controller.listeners.ProgressListener;
import controller.listeners.ResizingFinishedListener;
import controller.listeners.StartResizingListener;

public class ResizeProcessor implements StartResizingListener, FileSelectionListener {

	private static final String JPEG_FORMAT = "JPG";
	private static final String SUBFOLDER_NAME = "/resized/";
	private final List<ProgressListener> progressListeners;
	private final List<ResizingFinishedListener> resizingFinishedListeners;
	private File[] files = {};

	public ResizeProcessor() {
		super();
		this.progressListeners = new LinkedList<ProgressListener>();
		this.resizingFinishedListeners = new LinkedList<ResizingFinishedListener>();
	}

	@Override
	public void starResizing(int desiredSize) {
		for (File file : this.files) {
			ImageResizerApplication.getLogger().debug("Resizing begins");
			resize(file, desiredSize);
			ImageResizerApplication.getLogger().debug("Resizing ends");
			notifyProgressListeners();
		}
		notifyResizingFinishedListeners();
	}

	private void resize(File file, int desiredSize) {
		createSubFolderIfRequired(file.getParent());
		try {
			BufferedImage image = readImageFrom(file);
			BufferedImage resizedImage = Scalr.resize(image, Method.QUALITY, desiredSize);
			File destinationFile = determineDestinationFileFor(file);
			writeImageTo(resizedImage, destinationFile);
		} catch (IOException e) {
			ImageResizerApplication.getLogger().error("Error while resizing image.", e);
		}
	}

	private void writeImageTo(BufferedImage resizedImage, File destination) throws IOException {
		ImageIO.write(resizedImage, JPEG_FORMAT, destination);
	}

	private void createSubFolderIfRequired(String parentDirectory) {
		String destinationDirectoryPath = parentDirectory + SUBFOLDER_NAME;
		File destinationDirectory = new File(destinationDirectoryPath);
		if (!destinationDirectory.exists()) {
			destinationDirectory.mkdir();
		}
	}

	private File determineDestinationFileFor(File originalFile) {
		File destination = new File(originalFile.getParentFile().getAbsolutePath() + SUBFOLDER_NAME + originalFile.getName());
		return destination;
	}

	private BufferedImage readImageFrom(File file) throws IOException {
		return ImageIO.read(file);
	}

	@Override
	public void filesSelected(File... files) {
		this.files = files;
	}

	public void addProgressListener(ProgressListener listener) {
		this.progressListeners.add(listener);
	}

	public void addResizingFinishedListener(ResizingFinishedListener listener) {
		this.resizingFinishedListeners.add(listener);
	}

	private void notifyProgressListeners() {
		ImageResizerApplication.getLogger().debug("Notifying progress listeners.");
		for (ProgressListener listener : this.progressListeners) {
			listener.progress();
		}
	}

	private void notifyResizingFinishedListeners() {
		ImageResizerApplication.getLogger().debug("Notifying finished listeners.");
		for (ResizingFinishedListener listener : this.resizingFinishedListeners) {
			listener.resizingFinished();
		}
	}
}
