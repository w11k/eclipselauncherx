/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.weiglewilczek.xwt.launcher.Messages;

public class OpenLaunchConfigurationListView extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("com.weiglewilczek.xwt.launcher.views.LaunchConfigurations");
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage()
					.showView("com.weiglewilczek.xwt.launcher.views.Groups");
		} catch (PartInitException e) {
			// throw new
			// ExecutionException("Error opening launch configuration list view",
			// e);
			throw new ExecutionException(Messages.OpenLaunchConfigurationListView_Error, e);
		}
		return null;
	}

}
