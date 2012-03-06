/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.model;

import org.eclipse.core.databinding.observable.list.WritableList;

public class EclipseInstallationsListDataContext {

	private final WritableList eclipseInstallations;

	public EclipseInstallationsListDataContext(WritableList eclipseInstallations) {
		this.eclipseInstallations = eclipseInstallations;
	}

	public WritableList getEclipseInstallations() {
		return eclipseInstallations;
	}
}
