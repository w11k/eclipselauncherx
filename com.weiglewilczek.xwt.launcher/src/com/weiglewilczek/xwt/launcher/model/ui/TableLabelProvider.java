/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.model.ui;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.weiglewilczek.xwt.launcher.model.Installation;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;
import com.weiglewilczek.xwt.launcher.model.ObservableGroup;

public class TableLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof LaunchConfiguration) {
			LaunchConfiguration launchConfiguration = (LaunchConfiguration) element;
			switch (columnIndex) {
			case 0:
				return launchConfiguration.getName();
			case 1:
				return launchConfiguration.getWorkspacePath();
			case 2:
				if (launchConfiguration.getEclipse() != null)
					return launchConfiguration.getEclipse().getName();
				break;
			case 3:
				if (launchConfiguration.getJava() != null)
					return launchConfiguration.getJava().getName();
				break;
			case 4:
				return launchConfiguration.getVmArgs();

			default:
				break;
			}
		} else if (element instanceof Installation<?>) {
			Installation<?> installation = (Installation<?>) element;
			switch (columnIndex) {
			case 0:
				return installation.getName();
			case 1:
				return installation.getPathToExecutable();

			default:
				break;
			}
		} else if (element instanceof ObservableGroup) {
			ObservableGroup group = (ObservableGroup) element;
			switch (columnIndex) {
			case 0:
				return group.getGroup().getName();

			default:
				return "";
			}
		}
		return null;
	}

}
