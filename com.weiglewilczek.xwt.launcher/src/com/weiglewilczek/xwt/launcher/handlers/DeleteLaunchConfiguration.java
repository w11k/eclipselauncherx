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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.weiglewilczek.xwt.launcher.managers.GroupManager;
import com.weiglewilczek.xwt.launcher.managers.LaunchConfigurationManager;
import com.weiglewilczek.xwt.launcher.model.Group;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;

public class DeleteLaunchConfiguration extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil
				.getCurrentSelection(event);
		LaunchConfiguration lc = (LaunchConfiguration) selection
				.getFirstElement();
		try {
			Group group = GroupManager.getInstance().getGroupForConfiguration(
					lc);
			group.removeConfiguration(lc);

			LaunchConfigurationManager.getInstance().delete(lc);

			GroupManager.getInstance().update(group);
		} catch (Exception e) {
			throw new ExecutionException(
					"Error deleting launch configuration: " + lc.getName(), e);
		}

		return null;
	}

}
