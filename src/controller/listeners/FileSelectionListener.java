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
package controller.listeners;

import java.io.File;

public interface FileSelectionListener {

	public void filesSelected(File... files);
}
