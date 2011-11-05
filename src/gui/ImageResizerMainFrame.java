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
package gui;

import edt.EDT;
import hochberger.utilities.gui.EDTSafeFrame;
import hochberger.utilities.gui.StretchingBackgroundedPanel;
import hochberger.utilities.gui.input.SelfHighlightningValidatingTextField;
import hochberger.utilities.gui.input.ValidatingTextField;
import hochberger.utilities.gui.input.validator.IntegerStringInputValidator;
import hochberger.utilities.gui.lookandfeel.SetLookAndFeelTo;
import hochberger.utilities.images.loader.ImageLoader;
import hochberger.utilities.threading.ThreadRunner;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import controller.listeners.FileSelectionListener;
import controller.listeners.ProgressListener;
import controller.listeners.ResizingFinishedListener;
import controller.listeners.StartResizingListener;

public class ImageResizerMainFrame extends EDTSafeFrame implements FileSelectionListener, ProgressListener, StartResizingListener, ResizingFinishedListener {

	private static final String DEFAULT_IMAGE_SIZE = "800";
	private final List<FileSelectionListener> fileSelectionListeners;
	private final List<StartResizingListener> startResizingListeners;
	private final List<Component> stateDependentComponents;
	private JFileChooser fileChooser;
	private JProgressBar progressBar;
	private ValidatingTextField sizeTextField;

	public ImageResizerMainFrame(String title) {
		super(title);
		this.fileSelectionListeners = new LinkedList<FileSelectionListener>();
		this.startResizingListeners = new LinkedList<StartResizingListener>();
		this.stateDependentComponents = new LinkedList<Component>();
		addFileSelectionListener(this);
		addStartResizingListener(this);
	}

	@Override
	protected void buildUI() {
		SetLookAndFeelTo.nimbusLookAndFeel();
		frame().setContentPane(new StretchingBackgroundedPanel(ImageLoader.loadImage("graphics/bkg.png")));
		notResizable();
		exitOnClose();
		useLayoutManager(new MigLayout("", "10[180]10[180]10[right]10", "10[]10[]10[nogrid]10"));
		this.fileChooser = new JpegFileChooser();
		add(new SequenceNumberPanel(1, "Dateien wählen"));
		add(new SequenceNumberPanel(2, "Größe wählen"));
		add(new SequenceNumberPanel(3, "Starten"), "wrap");
		JButton fileChooserButton = createFileChooserButton();
		JComponent settingsPanel = createSettingsPanel();
		JComponent startButton = createStartButton();
		this.stateDependentComponents.add(fileChooserButton);
		this.stateDependentComponents.add(settingsPanel);
		this.stateDependentComponents.add(startButton);
		add(fileChooserButton);
		add(settingsPanel);
		add(startButton, "wrap");
		add(createProgressBar(), "growx");
		frame().pack();
		frame().setLocationRelativeTo(null);
	}

	private JButton createFileChooserButton() {
		JButton result = new JButton(ImageLoader.loadIcon("graphics/folder.png"));
		result.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ImageResizerMainFrame.this.fileChooser.showOpenDialog(frame());
				notifyFileSelectionListeners();
			}
		});
		return result;
	}

	private JComponent createSettingsPanel() {
		JPanel result = new JPanel(new MigLayout("", "[50]5[]"));
		result.setOpaque(false);
		this.sizeTextField = createSizeTextField();
		this.sizeTextField.setToolTipText("Gewünschte Anzahl Pixel der größeren Seite");
		result.add(this.sizeTextField, "growx");
		JLabel label = new JLabel("Pixel");
		label.setForeground(Color.LIGHT_GRAY);
		result.add(label);
		return result;
	}

	private ValidatingTextField createSizeTextField() {
		final ValidatingTextField result = new SelfHighlightningValidatingTextField(DEFAULT_IMAGE_SIZE);
		result.setHorizontalAlignment(JTextField.RIGHT);
		result.addValidator(new IntegerStringInputValidator());
		return result;
	}

	private JComponent createStartButton() {
		JButton result = new JButton(ImageLoader.loadIcon("graphics/accept.png"));
		result.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (ImageResizerMainFrame.this.sizeTextField.validateInput()) {
					notifyStartResizingListeners();
				}
			}
		});
		return result;
	}

	private JProgressBar createProgressBar() {
		this.progressBar = new JProgressBar();
		this.progressBar.setValue(0);
		return this.progressBar;
	}

	public void addFileSelectionListener(FileSelectionListener listener) {
		this.fileSelectionListeners.add(listener);
	}

	public void addStartResizingListener(StartResizingListener listener) {
		this.startResizingListeners.add(listener);
	}

	private void notifyFileSelectionListeners() {
		EDT.always();
		ThreadRunner.startThread(new Runnable() {
			@Override
			public void run() {
				EDT.never();
				for (FileSelectionListener listener : ImageResizerMainFrame.this.fileSelectionListeners) {
					listener.filesSelected(ImageResizerMainFrame.this.fileChooser.getSelectedFiles());
				}
			}
		});
	}

	private void notifyStartResizingListeners() {
		EDT.always();
		ThreadRunner.startThread(new Runnable() {
			@Override
			public void run() {
				EDT.never();
				for (StartResizingListener listener : ImageResizerMainFrame.this.startResizingListeners) {
					listener.starResizing(getDesiredSize());
				}
			}
		}, "ResizeImagesThread");
	}

	private int getDesiredSize() {
		return Integer.parseInt(this.sizeTextField.getText());
	}

	private void disableStateDependentComponents() {
		for (Component component : this.stateDependentComponents) {
			component.setEnabled(false);
		}
	}

	private void enableStateDependentComponents() {
		for (Component component : this.stateDependentComponents) {
			component.setEnabled(true);
		}
	}

	@Override
	public void filesSelected(final File... files) {
		performBlockingOnEDT(new Runnable() {
			@Override
			public void run() {
				ImageResizerMainFrame.this.progressBar.setMaximum(files.length);
				ImageResizerMainFrame.this.progressBar.setValue(0);
			}
		});
	}

	@Override
	public void progress() {
		performBlockingOnEDT(new Runnable() {
			@Override
			public void run() {
				ImageResizerMainFrame.this.progressBar.setValue(ImageResizerMainFrame.this.progressBar.getValue() + 1);
			}
		});
	}

	@Override
	public void starResizing(int desiredSize) {
		disableStateDependentComponents();
	}

	@Override
	public void resizingFinished() {
		enableStateDependentComponents();
	}
}
