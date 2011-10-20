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

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JpegFileChooser extends JFileChooser {

	private static final long serialVersionUID = -557696490691461070L;

	public JpegFileChooser() {
		super();
		setMultiSelectionEnabled(true);
		setFileFilter(createJpegFileFilter());
	}

	private FileNameExtensionFilter createJpegFileFilter() {
		return new FileNameExtensionFilter("JPEG-Dateien", "jpg", "jpeg");
	}
}
