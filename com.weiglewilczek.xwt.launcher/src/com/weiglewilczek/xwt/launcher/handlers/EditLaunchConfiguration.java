/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.weiglewilczek.xwt.launcher.Activator;
import com.weiglewilczek.xwt.launcher.Messages;
import com.weiglewilczek.xwt.launcher.managers.EclipseInstallationManager;
import com.weiglewilczek.xwt.launcher.managers.JavaInstallationManager;
import com.weiglewilczek.xwt.launcher.managers.LaunchConfigurationManager;
import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;
import com.weiglewilczek.xwt.launcher.model.JavaInstallation;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;
import com.weiglewilczek.xwt.launcher.model.LaunchConfigurationDataContext;
import com.weiglewilczek.xwt.launcher.model.ui.LaunchConfigurationDialog;

public class EditLaunchConfiguration extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Object currentSelection = HandlerUtil.getCurrentSelection(event);
		if (currentSelection != null
				&& currentSelection instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) currentSelection;

			if (selection.getFirstElement() != null
					&& selection.getFirstElement() instanceof LaunchConfiguration) {
				LaunchConfiguration configuration = (LaunchConfiguration) selection
						.getFirstElement();

				List<EclipseInstallation> eclipses = EclipseInstallationManager
						.getInstance().enumerateAll();
				WritableList writableEclipses = new WritableList();
				writableEclipses.addAll(eclipses);

				List<JavaInstallation> javas = JavaInstallationManager
						.getInstance().enumerateAll();
				WritableList writableJavas = new WritableList();
				writableJavas.addAll(javas);

				LaunchConfigurationDataContext dataContext = new LaunchConfigurationDataContext(
						configuration, writableEclipses, writableJavas);

				LaunchConfigurationDialog launchConfiguration = new LaunchConfigurationDialog(
						HandlerUtil.getActiveShell(event), dataContext);
				if (launchConfiguration.open() == LaunchConfigurationDialog.OK) {
					try {
						LaunchConfigurationManager.getInstance().update(
								configuration);
					} catch (Exception e) {
						Activator.logError(
								Messages.EditLaunchConfiguration_Error, e);
						MessageDialog
								.openError(
										PlatformUI.getWorkbench()
												.getActiveWorkbenchWindow()
												.getShell(),
										Messages.EditLaunchConfiguration_EditLaunchConfiguration,
										Messages.EditLaunchConfiguration_Error
												+ ": " + e.getMessage());
					}
				}
			}
		}

		return null;
	}

}
