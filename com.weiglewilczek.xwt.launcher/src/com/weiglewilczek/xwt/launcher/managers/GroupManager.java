package com.weiglewilczek.xwt.launcher.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;

import com.weiglewilczek.xwt.launcher.model.Group;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;

public class GroupManager extends BaseManager<Group, GroupFields> {
	private static GroupManager instance;

	private final Group otherGroup;

	private GroupManager() {
		super();
		otherGroup = new Group();
		otherGroup.setId(-1l);
		otherGroup.setName("Others");
	}

	public static final GroupManager getInstance() {
		if (instance == null)
			instance = new GroupManager();

		return instance;
	}

	@Override
	public List<Group> enumerateAll() {
		List<Group> groups = super.enumerateAll(Group.class);

		Group others = reloadOtherGroup();
		groups.add(others);

		return groups;
	}

	@Override
	protected GroupFields[] getFields() {
		return GroupFields.values();
	}

	@Override
	protected PreferenceFields getNextIdField() {
		return PreferenceFields.GROUP_NEXT_ID;
	}

	@Override
	protected PreferenceFields getIdsListField() {
		return PreferenceFields.GROUPS;
	}

	@Override
	protected Group createModel() {
		return new Group();
	}

	@Override
	public void delete(Group group) throws Exception {
		if (group.getId() > -1) {
			super.delete(group);
		}
	}

	@Override
	public Group get(Long id) {
		if (id > -1) {
			return super.get(id);
		} else {
			return reloadOtherGroup();
		}
	}

	@Override
	public Group update(Group object) throws BackingStoreException {
		if (object.getId() > -1) {
			return super.update(object);
		} else {
			Group update = reloadOtherGroup();
			handleUpdated(update);
			return update;
		}
	}

	private Group reloadOtherGroup() {
		List<Group> groups = super.enumerateAll(Group.class);

		List<LaunchConfiguration> allConfigs = LaunchConfigurationManager
				.getInstance().enumerateAll();
		List<LaunchConfiguration> otherConfigs = new ArrayList<LaunchConfiguration>();
		for (LaunchConfiguration launchConfiguration : allConfigs) {
			boolean configInGroup = false;
			for (Group group : groups) {
				if (group.getConfigurations().contains(launchConfiguration)) {
					configInGroup = true;
					continue;
				}
			}

			if (!configInGroup) {
				otherConfigs.add(launchConfiguration);
			}
		}

		otherGroup.setConfigurations(otherConfigs);
		return otherGroup;
	}

	public Group getGroupForConfiguration(LaunchConfiguration configuration) {
		List<Group> groups = enumerateAll();
		for (Group group : groups) {
			if (group.getConfigurations().contains(configuration)) {
				return group;
			}
		}

		return reloadOtherGroup();
	}
}
