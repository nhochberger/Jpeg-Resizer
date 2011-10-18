package gui;

import edt.EDT;
import gui.listeners.ProgressListener;
import hochberger.utilities.gui.EDTSafeFrame;
import hochberger.utilities.gui.StretchingBackgroundedPanel;
import hochberger.utilities.gui.input.ValidatingTextField;
import hochberger.utilities.gui.input.validator.InputValidator;
import hochberger.utilities.gui.lookandfeel.SetLookAndFeelTo;
import hochberger.utilities.images.loader.ImageLoader;
import hochberger.utilities.threading.ThreadRunner;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import controller.listeners.StartResizingListener;

public class ImageResizerMainFrame extends EDTSafeFrame implements FileSelectionListener, ProgressListener {

	private static final String DEFAULT_IMAGE_SIZE = "800";
	private final List<FileSelectionListener> fileSelectionListeners;
	private final List<StartResizingListener> startResizingListeners;
	private JFileChooser fileChooser;
	private JProgressBar progressBar;
	private ValidatingTextField sizeTextField;

	public ImageResizerMainFrame() {
		super();
		this.fileSelectionListeners = new LinkedList<FileSelectionListener>();
		this.startResizingListeners = new LinkedList<StartResizingListener>();
		addFileSelectionListener(this);
	}

	@Override
	protected void buildUI() {
		SetLookAndFeelTo.nimbusLookAndFeel();
		frame().setContentPane(new StretchingBackgroundedPanel(ImageLoader.loadImage("graphics/bkg.png")));
		notResizable();
		exitOnClose();
		setTitle("JPEG Resizer");
		useLayoutManager(new MigLayout("", "10[180]10[180]10[right]10", "10[]10[]10[nogrid]10"));
		this.fileChooser = new JpegFileChooser();
		add(new SequenceNumberPanel(1, "Dateien wählen"));
		add(new SequenceNumberPanel(2, "Größe wählen"));
		add(new SequenceNumberPanel(3, "Starten"), "wrap");
		add(createFileChooserButton());
		add(createSettingsPanel());
		add(createStartButton(), "wrap");
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
		final ValidatingTextField result = new ValidatingTextField(DEFAULT_IMAGE_SIZE);
		result.setHorizontalAlignment(JTextField.RIGHT);
		result.addValidator(new InputValidator<String>() {
			@Override
			public boolean isValid(String input) {
				try {
					Integer.parseInt(input);
					return true;
				} catch (NumberFormatException e) {
					return false;
				}
			}
		});
		result.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				super.keyTyped(e);
				if (result.validateInput()) {
					result.setForeground(Color.BLACK);
					return;
				}
				result.setForeground(Color.RED);
			}
		});
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
}
