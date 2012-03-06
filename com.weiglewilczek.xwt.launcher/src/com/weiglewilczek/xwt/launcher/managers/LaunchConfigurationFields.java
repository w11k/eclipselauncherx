/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.managers;

import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;
import com.weiglewilczek.xwt.launcher.model.JavaInstallation;

public enum LaunchConfigurationFields implements
		BaseFields<LaunchConfigurationFields> {
	ID("LaunchConfigurationId", new Long(0)), NAME("LaunchConfigurationName",
			new String()), WORKSPACE_PATH("LaunchConfigurationWorkspacePath",
			new String()), ECLIPSE_INSTALLATION(
			"LaunchConfigurationEclipseInstallation", new EclipseInstallation()), JAVA_INSTALLATION(
			"LaunchConfigurationJavaInstallation", new JavaInstallation()), VMARGS(
			"LaunchConfigurationVmArgs", new String()), ECLIPSE_ARGS(
			"LaunchConfigurationEclipseArgs", new String());

	private final String name;
	private final Object typeInstance;

	private LaunchConfigurationFields(String name, Object typeInstance) {
		this.name = name;
		this.typeInstance = typeInstance;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getTypeInstance() {
		return typeInstance;
	}

	@Override
	public LaunchConfigurationFields findForName(String name) {
		for (LaunchConfigurationFields field : LaunchConfigurationFields
				.values()) {
			if (field.getName().equals(name))
				return field;
		}

		return null;
	}
}
