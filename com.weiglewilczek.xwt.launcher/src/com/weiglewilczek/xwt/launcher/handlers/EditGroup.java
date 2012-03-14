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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.weiglewilczek.xwt.launcher.Activator;
import com.weiglewilczek.xwt.launcher.Messages;
import com.weiglewilczek.xwt.launcher.managers.GroupManager;
import com.weiglewilczek.xwt.launcher.model.Group;
import com.weiglewilczek.xwt.launcher.model.ObservableGroup;
import com.weiglewilczek.xwt.launcher.model.ui.GroupDialog;

public class EditGroup extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Object currentSelection = HandlerUtil.getCurrentSelection(event);
		if (currentSelection != null
				&& currentSelection instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) currentSelection;

			if (selection.getFirstElement() != null
					&& selection.getFirstElement() instanceof ObservableGroup) {
				Group group = ((ObservableGroup) selection.getFirstElement())
						.getGroup();

				GroupDialog groupDialog = new GroupDialog(
						HandlerUtil.getActiveShell(event), group);
				if (groupDialog.open() == GroupDialog.OK) {
					try {
						GroupManager.getInstance().update(group);
					} catch (Exception e) {
						Activator.logError(Messages.EditGroup_Error, e);
						MessageDialog.openError(
								PlatformUI.getWorkbench()
										.getActiveWorkbenchWindow().getShell(),
								Messages.EditGroup_EditGroup,
								Messages.EditGroup_Error + ": "
										+ e.getMessage());
					}
				}
			}
		}

		return null;
	}

}
