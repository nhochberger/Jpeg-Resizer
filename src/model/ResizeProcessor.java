package model;

import gui.listeners.ProgressListener;
import hochberger.utilities.images.scaler.PictureScaler;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import controller.listeners.FileSelectionListener;
import controller.listeners.StartResizingListener;

public class ResizeProcessor implements StartResizingListener, FileSelectionListener {

	private static final String JPEG_FORMAT = "JPG";
	private static final String SUBFOLDER_NAME = "/resized/";
	private final List<ProgressListener> progressListeners;
	private File[] files = {};
	private PictureScaler scaler;

	public ResizeProcessor() {
		super();
		this.progressListeners = new LinkedList<ProgressListener>();
	}

	@Override
	public void starResizing(int desiredSize) {
		this.scaler = new PictureScaler();
		for (File file : this.files) {
			resize(file, desiredSize);
			notifyProgressListeners();
			System.err.println("resizing finished");
		}
		this.scaler = null;
	}

	private void resize(File file, int desiredSize) {
		createSubFolderIfRequired(file.getParent());
		try {
			Image image = readImageFrom(file);
			Dimension newDimension = deterimeNewDimension(image, desiredSize);
			BufferedImage resizedImage = this.scaler.scale(image, newDimension);
			File destinationFile = determineDestinationFileFor(file);
			writeImageTo(resizedImage, destinationFile);
		} catch (IOException e) {
			// TODO: error message
			e.printStackTrace();
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

	private Dimension deterimeNewDimension(Image image, int desiredSize) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		float scalingFactor = determineScalingFactorOf(image, desiredSize);
		return new Dimension((int) (width * scalingFactor), (int) (height * scalingFactor));
	}

	private float determineScalingFactorOf(Image image, int desiredSize) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		if (width > height) {
			return ((float) desiredSize) / ((float) width);
		} else {
			return ((float) desiredSize) / ((float) height);
		}
	}

	private Image readImageFrom(File file) throws IOException {
		return ImageIO.read(file);
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
