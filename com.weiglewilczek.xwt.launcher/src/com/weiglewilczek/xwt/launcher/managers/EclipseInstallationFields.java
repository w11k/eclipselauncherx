/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.managers;

public enum EclipseInstallationFields implements
		BaseFields<EclipseInstallationFields> {
	ID("EclipseInstallationId", new Long(0)), NAME("EclipseInstallationName",
			new String()), PATH_TO_EXECUTABLE(
			"EclipseInstallationPathToExecutable", new String());

	private final String name;
	private final Object typeInstance;

	private EclipseInstallationFields(String name, Object typeInstance) {
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
	public EclipseInstallationFields findForName(String name) {
		for (EclipseInstallationFields field : EclipseInstallationFields
				.values()) {
			if (field.getName().equals(name))
				return field;
		}

		return null;
	}
}
