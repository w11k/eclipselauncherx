package com.weiglewilczek.xwt.launcher.managers;

import java.util.List;

import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;

public class LaunchConfigurationManager extends BaseManager<LaunchConfiguration> {
	private static LaunchConfigurationManager instance;

	private LaunchConfigurationManager() {
		super();
	}

	public static final LaunchConfigurationManager getInstance() {
		if(instance == null)
			instance = new LaunchConfigurationManager();
		
		return instance;
	}

	@Override
	public List<LaunchConfiguration> enumerateAll() {
		return super.enumerateAll(LaunchConfiguration.class);
	}

	@Override
	protected  LaunchConfigurationFields[] getFields() {
		return LaunchConfigurationFields.values();
	}

	@Override
	protected PreferenceFields getNextIdField() {
		return PreferenceFields.LAUNCH_CONFIGURATION_NEXT_ID;
	}

	@Override
	protected PreferenceFields getIdsListField() {
		return PreferenceFields.LAUNCH_CONFIGURATIONS;
	}

	@Override
	protected LaunchConfiguration createModel() {
		return new LaunchConfiguration();
	}
}
