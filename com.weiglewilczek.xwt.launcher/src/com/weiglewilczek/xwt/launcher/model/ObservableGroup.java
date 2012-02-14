package com.weiglewilczek.xwt.launcher.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

	public void synchronizeConfigurations() {
		for (LaunchConfiguration configuration : group.getConfigurations()) {
			if (!configurations.contains(configuration)) {
				configurations.add(configuration);
			}
		}
		List<LaunchConfiguration> toRemove = new ArrayList<LaunchConfiguration>();

		for (Iterator<?> iterator = configurations.iterator(); iterator
				.hasNext();) {
			LaunchConfiguration configuration = (LaunchConfiguration) iterator
					.next();
			if (!group.getConfigurations().contains(configuration)) {
				toRemove.add(configuration);
			}
		}

		configurations.removeAll(toRemove);
	}
}
