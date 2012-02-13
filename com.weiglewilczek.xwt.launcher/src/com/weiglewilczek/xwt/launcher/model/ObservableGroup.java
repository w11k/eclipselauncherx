package com.weiglewilczek.xwt.launcher.model;

import org.eclipse.core.databinding.observable.list.WritableList;

public class ObservableGroup {
	private final Group group;
	private final WritableList configurations;

	public ObservableGroup(Group group) {
		this.group = group;
		configurations = new WritableList();
		configurations.addAll(group.getConfigurations());
	}

	public Group getGroup() {
		return group;
	}

	public WritableList getConfigurations() {
		return configurations;
	}
	
	public void addConfiguration(LaunchConfiguration configuration) {
		group.addConfiguration(configuration);
		configurations.add(configuration);
	}
	
	public void removeConfiguration(LaunchConfiguration configuration) {
		group.removeConfiguration(configuration);
		configurations.add(configuration);
	}

}
