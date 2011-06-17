package com.weiglewilczek.xwt.launcher.model;

import org.eclipse.core.databinding.observable.list.WritableList;

import com.weiglewilczek.xwt.launcher.listener.IListener;
import com.weiglewilczek.xwt.launcher.listener.ListenerType;
import com.weiglewilczek.xwt.launcher.managers.LaunchConfigurationManager;

public class WorkspaceListDataContext {

	private WritableList launchConfigurations;

	public WorkspaceListDataContext() {
	}

	public WritableList getLaunchConfigurations() {
		return launchConfigurations;
	}

	public void setLaunchConfigurations(WritableList launchConfigurations) {
		this.launchConfigurations = launchConfigurations;
	}
}
