package com.weiglewilczek.xwt.launcher.model;

import org.eclipse.core.databinding.observable.list.WritableList;

import com.weiglewilczek.xwt.launcher.listener.IListener;
import com.weiglewilczek.xwt.launcher.listener.ListenerType;
import com.weiglewilczek.xwt.launcher.managers.LaunchConfigurationManager;

public class JavaInstallationsListDataContext {

	private WritableList javaInstallations;

	public JavaInstallationsListDataContext() {
	}

	public WritableList getJavaInstallations() {
		return javaInstallations;
	}

	public void setJavaInstallations(WritableList javaInstallations) {
		this.javaInstallations = javaInstallations;
	}
}
