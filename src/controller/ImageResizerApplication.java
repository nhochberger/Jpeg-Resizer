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
import model.ResizeProcessor;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

public class ImageResizerApplication {

	public static Logger LOGGER = setUpLoggingServices();

	public static void main(String[] args) {
		try {
			ApplicationProperties applicationProperties = new ApplicationProperties();
			GUI gui = new GUI(applicationProperties);
			ResizeProcessor resizer = new ResizeProcessor();
			gui.activateGUI();
			gui.addFileSelectionListener(resizer);
			gui.addStartResizingListener(resizer);
			resizer.addProgressListener(gui);
		} catch (Exception e) {
			LOGGER.fatal("Error while starting application. Shutting down.", e);
		}
	}

	private static Logger setUpLoggingServices() {
		try {
			Logger logger = Logger.getLogger(ImageResizerApplication.class);
			Layout layout = new SimpleLayout();
			logger.addAppender(new ConsoleAppender(layout));
			logger.addAppender(new FileAppender(layout, "logs/main.log"));
			return logger;
		} catch (Exception e) {
			System.err.println("Error while setting up logging service. Application will be shut down.");
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}
}
