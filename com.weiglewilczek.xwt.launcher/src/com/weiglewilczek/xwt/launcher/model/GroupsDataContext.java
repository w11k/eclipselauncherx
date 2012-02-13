package com.weiglewilczek.xwt.launcher.model;

import java.util.HashMap;
import java.util.List;

import org.eclipse.core.databinding.observable.list.WritableList;

public class GroupsDataContext {

	private final WritableList groups;
	private final HashMap<Group, ObservableGroup> groupToObservableMap;

	public GroupsDataContext(List<Group> groups) {
		this.groups = new WritableList();
		groupToObservableMap = new HashMap<Group, ObservableGroup>();
		
		for (Group group : groups) {
			ObservableGroup obserableGroup = new ObservableGroup(group);
			groupToObservableMap.put(group, obserableGroup);
			this.groups.add(obserableGroup);
		}
	}

	public WritableList getGroups() {
		return groups;
	}
	
	public ObservableGroup getGroup(Group group) {
		return groupToObservableMap.get(group);
	}
}
