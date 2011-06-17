package com.weiglewilczek.xwt.launcher.model;

import org.eclipse.core.databinding.observable.list.WritableList;

import com.weiglewilczek.xwt.launcher.listener.IListener;
import com.weiglewilczek.xwt.launcher.listener.ListenerType;
import com.weiglewilczek.xwt.launcher.managers.LaunchConfigurationManager;

public class EclipseInstallationsListDataContext {

	private WritableList eclipseInstallations;

	public EclipseInstallationsListDataContext() {
	}

	public WritableList getEclipseInstallations() {
		return eclipseInstallations;
	}

	public void setEclipseInstallations(WritableList eclipseInstallations) {
		this.eclipseInstallations = eclipseInstallations;
	}
}
