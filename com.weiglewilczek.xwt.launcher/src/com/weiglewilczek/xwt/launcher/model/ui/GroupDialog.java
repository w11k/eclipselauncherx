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

import org.eclipse.e4.xwt.jface.AbstractDialog;
import org.eclipse.swt.widgets.Shell;

import com.weiglewilczek.xwt.launcher.model.Group;

public class GroupDialog extends AbstractDialog {

	public GroupDialog(Shell shell, Group group) {
		super(shell, "New Group", group);
	}

	@Override
	protected URL getContentURL() {
		return this.getClass().getResource("GroupDialog.xwt");
	}

	public Group getDataContext() {
		return (Group) dataContext;
	}
}
