/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.handlers;

import java.util.Properties;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

public class About extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		Properties properties = new Properties();
		try {
			properties.load(getClass().getClassLoader().getResourceAsStream(
					"xwt.launcher.version.properties"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Object versionObject = properties.get("build.version");
		Object releaseCandidateObject = properties.get("release.candidate");

		String version = "X.X.X";
		if (versionObject instanceof String) {
			version = (String) versionObject;

			if (releaseCandidateObject instanceof String
					&& ((String) releaseCandidateObject).length() > 0) {
				version = version + " " + releaseCandidateObject;
			}
		}

		MessageDialog
				.openInformation(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								.getShell(),
						"About",
						"EclipseLauncherX\n\nVersion: "
								+ version
								+ "\nAuthor: Daniela Blank\n\n(c) Copyright Weiglewilczek GmbH 2012. All rights reserved.\nVisit www.weiglewilczek.com");

		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
