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
