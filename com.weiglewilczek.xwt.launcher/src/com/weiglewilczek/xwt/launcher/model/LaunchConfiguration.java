/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.model;

import java.io.Serializable;

import com.weiglewilczek.xwt.launcher.managers.LaunchConfigurationFields;

public class LaunchConfiguration extends
		ModelElement<LaunchConfigurationFields> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	private String workspacePath;

	private EclipseInstallation eclipse;

	private JavaInstallation java;

	private String vmArgs;

	private String eclipseArgs;

	public LaunchConfiguration(Long id, String name, String workspacePath,
			EclipseInstallation eclipse, JavaInstallation java, String vmArgs,
			String eclipseArgs) {
		setId(id);
		this.name = name;
		this.workspacePath = workspacePath;
		this.eclipse = eclipse;
		this.java = java;
		this.vmArgs = vmArgs;
		this.eclipseArgs = eclipseArgs;
	}

	public LaunchConfiguration(String name, String workspacePath,
			EclipseInstallation eclipse, JavaInstallation java, String vmArgs,
			String eclipseArgs) {
		this.name = name;
		this.workspacePath = workspacePath;
		this.eclipse = eclipse;
		this.java = java;
		this.vmArgs = vmArgs;
		this.eclipseArgs = eclipseArgs;
	}

	public LaunchConfiguration() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWorkspacePath() {
		return workspacePath;
	}

	public void setWorkspacePath(String workspacePath) {
		this.workspacePath = workspacePath;
	}

	public EclipseInstallation getEclipse() {
		return eclipse;
	}

	public void setEclipse(EclipseInstallation eclipse) {
		this.eclipse = eclipse;
	}

	public JavaInstallation getJava() {
		return java;
	}

	public void setJava(JavaInstallation java) {
		this.java = java;
	}

	public String getVmArgs() {
		return vmArgs;
	}

	public void setVmArgs(String vmArgs) {
		this.vmArgs = vmArgs;
	}

	public String getEclipseArgs() {
		return eclipseArgs;
	}

	public void setEclipseArgs(String eclipseArgs) {
		this.eclipseArgs = eclipseArgs;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof LaunchConfiguration) {
			return super.equals(obj);
		}

		return false;
	}

	@Override
	public String toString() {
		return "LaunchConfiguration[id=" + getId() + ", name=" + name
				+ ", workspace=" + workspacePath + ", eclipse="
				+ eclipse.toString() + ", java=" + java.toString()
				+ ", vmArgs=" + vmArgs + ", eclipseArgs=" + eclipseArgs + "]";
	}

	@Override
	public Object getProperty(LaunchConfigurationFields field) {
		switch (field) {
		case NAME:
			return getName();
		case WORKSPACE_PATH:
			return getWorkspacePath();
		case ECLIPSE_INSTALLATION:
			return getEclipse();
		case JAVA_INSTALLATION:
			return getJava();
		case ECLIPSE_ARGS:
			return getEclipseArgs();
		case VMARGS:
			return getVmArgs();
		default:
			return null;
		}
	}

	@Override
	public void setProperty(LaunchConfigurationFields field, Object value) {
		switch (field) {
		case NAME:
			setName((String) value);
			break;
		case WORKSPACE_PATH:
			setWorkspacePath((String) value);
			break;
		case ECLIPSE_INSTALLATION:
			setEclipse((EclipseInstallation) value);
			break;
		case JAVA_INSTALLATION:
			setJava((JavaInstallation) value);
			break;
		case ECLIPSE_ARGS:
			setEclipseArgs((String) value);
			break;
		case VMARGS:
			setVmArgs((String) value);
			break;
		}
	}

}
