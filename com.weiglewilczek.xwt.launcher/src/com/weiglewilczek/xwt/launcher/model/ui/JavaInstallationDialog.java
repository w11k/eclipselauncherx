/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.model.ui;

import java.net.URL;

import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.databinding.IBindingContext;
import org.eclipse.e4.xwt.jface.AbstractDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.weiglewilczek.xwt.launcher.model.JavaInstallation;

public class JavaInstallationDialog extends AbstractDialog {

	public JavaInstallationDialog(Shell shell, JavaInstallation java) {
		super(shell, "New Java Installation", java);
	}

	@Override
	protected URL getContentURL() {
		return this.getClass().getResource("JavaInstallationDialog.xwt");
	}

	public JavaInstallation getDataContext() {
		return (JavaInstallation) dataContext;
	}

	public void performSelectionEvent(Event event) {
		FileDialog dialog = new FileDialog(getShell());
		String path = dialog.open();
		if (path != null && path.length() > 0) {
			Text pathText = (Text) XWT.findElementByName(event.widget, "path");
			if (pathText != null)
				pathText.setText(path);
		}

		IBindingContext context = XWT.getBindingContext(event.widget);
		context.updateModels();
	}
}
