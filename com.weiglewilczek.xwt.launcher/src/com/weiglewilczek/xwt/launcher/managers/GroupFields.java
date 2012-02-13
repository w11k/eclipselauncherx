package com.weiglewilczek.xwt.launcher.managers;

import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;

public enum GroupFields implements BaseFields<GroupFields> {
	ID("GroupId", new Long(0)), NAME("GroupName", new String()), CONFIGURATIONS(
			"GroupConfigurations", new LaunchConfiguration[0]);

	private final String name;
	private final Object typeInstance;

	private GroupFields(String name, Object typeInstance) {
		this.name = name;
		this.typeInstance = typeInstance;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getTypeInstance() {
		return typeInstance;
	}

	@Override
	public GroupFields findForName(String name) {
		for (GroupFields field : GroupFields.values()) {
			if (field.getName().equals(name))
				return field;
		}

		return null;
	}
}
