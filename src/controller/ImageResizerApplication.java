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
package controller;

import gui.GUI;
import hochberger.utilities.application.ApplicationProperties;
import hochberger.utilities.application.BasicLoggedApplication;
import model.ResizeProcessor;
import controller.feedback.AcousticFinishedSignalPlayer;

public class ImageResizerApplication extends BasicLoggedApplication {

	public static void main(String[] args) {
		setUpLoggingServices(ImageResizerApplication.class);
		try {
			ApplicationProperties applicationProperties = new ApplicationProperties();
			GUI gui = new GUI(applicationProperties);
			ResizeProcessor resizer = new ResizeProcessor();
			gui.activateGUI();
			gui.addFileSelectionListener(resizer);
			gui.addStartResizingListener(resizer);
			resizer.addProgressListener(gui);
			resizer.addResizingFinishedListener(gui);
			resizer.addResizingFinishedListener(new AcousticFinishedSignalPlayer());
		} catch (Exception e) {
			getLogger().fatal("Error while starting application. Shutting down.", e);
		}
	}
}
