package controller.feedback;

import java.awt.Toolkit;

import controller.listeners.ResizingFinishedListener;

public class AcousticFinishedSignalPlayer implements ResizingFinishedListener {

	public AcousticFinishedSignalPlayer() {
		super();
	}

	@Override
	public void resizingFinished() {
		final Runnable asteriskPlayer = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.asterisk");
		if (asteriskPlayer != null) {
			asteriskPlayer.run();
		}
	}

}
