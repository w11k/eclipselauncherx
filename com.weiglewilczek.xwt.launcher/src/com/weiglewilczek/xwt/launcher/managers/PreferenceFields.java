package com.weiglewilczek.xwt.launcher.managers;


public enum PreferenceFields {
	/**
	 * List of ids of LaunchConfigurations, used as preference prefix of LaunchConfigurationFields
	 */
	LAUNCH_CONFIGURATIONS("LaunchConfigurations"),
	/**
	 * List of ids of EclipseInstallations, used as preference prefix of EclipseInstallationFields
	 */
	ECLIPSE_INSTALLATIONS("EclipseInstallations"),
	/**
	 * List of ids of JavaInstallations, used as preference prefix of JavaInstallationFields
	 */
	JAVA_INSTALLATIONS("JavaInstallations"),
	LAUNCH_CONFIGURATION_NEXT_ID("LaunchConfigurationNextId"),
	ECLIPSE_INSTALLATION_NEXT_ID("EclipseInstallationNextId"),
	JAVA_INSTALLATION_NEXT_ID("JavaInstallationNextId");

	private String name;

	private PreferenceFields(String name) {
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public PreferenceFields findForName(String name)
	{
		for (PreferenceFields field : PreferenceFields.values()) {
			if(field.getName().equals(name))
				return field;
		}
		
		return null;
	}
}
