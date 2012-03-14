/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.weiglewilczek.xwt.launcher.Activator;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;
import com.weiglewilczek.xwt.launcher.util.ProgramFactory;

public class Launch extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil
				.getCurrentSelection(event);
		LaunchConfiguration selectedConfiguration = (LaunchConfiguration) selection
				.getFirstElement();
		try {
			runLaunchConfiguration(selectedConfiguration);
		} catch (Exception e) {
			Activator.logError("Error launching configuration", e);
			MessageDialog.openInformation(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), "Launch",
					"Error launching configuration: " + e.getMessage());
		}

		return null;
	}

	private void runLaunchConfiguration(LaunchConfiguration launchConfiguration)
			throws Exception {
		List<String> commands = new ArrayList<String>();

		if (Platform.getOS().equals(Platform.OS_MACOSX)
				|| Platform.getOS().equals(Platform.OS_LINUX)
				|| Platform.getOS().equals(Platform.OS_WIN32)) {
			if (launchConfiguration.getJava() != null) {
				commands.add("-vm");
				commands.add("\""
						+ launchConfiguration.getJava().getPathToExecutable()
						+ "\"");
			}

			if (launchConfiguration.getWorkspacePath() != null
					&& launchConfiguration.getWorkspacePath().length() > 0) {
				commands.add("-data");
				commands.add("\"" + launchConfiguration.getWorkspacePath()
						+ "\"");
			}

			if (launchConfiguration.getEclipseArgs() != null
					&& launchConfiguration.getEclipseArgs().length() > 0) {
				commands.add(launchConfiguration.getEclipseArgs());
			}

			commands.add("-showlocation");

			if (launchConfiguration.getVmArgs() != null
					&& launchConfiguration.getVmArgs().length() > 0) {
				commands.add("-vmargs " + launchConfiguration.getVmArgs());
			}

			String[] command = commands.toArray(new String[0]);

			ProgramFactory.execute("\""
					+ launchConfiguration.getJava().getPathToExecutable()
					+ "\"", command);
		} else {
			MessageDialog.openInformation(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), "Launch",
					"Error launching configuration: Platform not supported");
		}
	}

}
