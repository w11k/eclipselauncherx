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
import org.eclipse.ui.handlers.HandlerUtil;

import com.weiglewilczek.xwt.launcher.managers.JavaInstallationManager;
import com.weiglewilczek.xwt.launcher.model.JavaInstallation;
import com.weiglewilczek.xwt.launcher.model.ui.JavaInstallationDialog;

public class NewJavaInstallation extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		JavaInstallation java = new JavaInstallation();
		JavaInstallationDialog dialog = new JavaInstallationDialog(
				HandlerUtil.getActiveShell(event), java);
		if (dialog.open() == JavaInstallationDialog.OK) {
			try {
				JavaInstallationManager.getInstance().create(java);
			} catch (Exception e) {
				throw new ExecutionException(
						"Error creating java installation: " + java.getName(),
						e);
			}
		}

		return null;
	}
}
