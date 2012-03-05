package com.weiglewilczek.xwt.launcher.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;

public class LaunchConfigurationManager extends
		BaseManager<LaunchConfiguration, LaunchConfigurationFields> {
	private static Map<ManagerContext, LaunchConfigurationManager> instances = new HashMap<ManagerContext, LaunchConfigurationManager>();

	private LaunchConfigurationManager() {
		super();
	}

	public LaunchConfigurationManager(ManagerContext contextType,
			Properties properties) {
		super(contextType, properties);
		instances.put(contextType, this);
	}

	public static final LaunchConfigurationManager getInstance() {
		if (instances.isEmpty())
			instances.put(ManagerContext.APPLICATION,
					new LaunchConfigurationManager());

		return instances.get(ManagerContext.APPLICATION);
	}

	public static final LaunchConfigurationManager getInstance(
			ManagerContext contextType) {
		if (!instances.containsKey(contextType)) {
			throw new RuntimeException("Instance not initialized");
		}

		return instances.get(contextType);
	}

	@Override
	public List<LaunchConfiguration> enumerateAll() {
		return super.enumerateAll(LaunchConfiguration.class);
	}

	@Override
	protected LaunchConfigurationFields[] getFields() {
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
