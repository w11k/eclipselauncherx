/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
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

	public void addGroup(Group group) {
		groupToObservableMap.put(group, new ObservableGroup(group));
		groups.add(groupToObservableMap.get(group));
	}

	public ObservableGroup getGroup(Group group) {
		return groupToObservableMap.get(group);
	}
}
