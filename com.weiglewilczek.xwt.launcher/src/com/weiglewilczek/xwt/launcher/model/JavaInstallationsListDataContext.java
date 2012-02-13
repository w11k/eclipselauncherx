package com.weiglewilczek.xwt.launcher.model;

import org.eclipse.core.databinding.observable.list.WritableList;

public class JavaInstallationsListDataContext {

	private final WritableList javaInstallations;

	public JavaInstallationsListDataContext(WritableList javaInstallations) {
		this.javaInstallations = javaInstallations;
	}

	public WritableList getJavaInstallations() {
		return javaInstallations;
	}
}
