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

import gui.listeners.ProgressListener;
import hochberger.utilities.application.ApplicationProperties;
import controller.listeners.FileSelectionListener;
import controller.listeners.StartResizingListener;

public class GUI implements ProgressListener {

	private final ImageResizerMainFrame frame;

	public GUI(ApplicationProperties applicationProperties) {
		super();
		this.frame = new ImageResizerMainFrame(applicationProperties.title());
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
