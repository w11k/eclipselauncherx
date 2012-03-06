/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.weiglewilczek.xwt.launcher.model.JavaInstallation;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;

public class JavaInstallationManager extends
		BaseManager<JavaInstallation, JavaInstallationFields> {
	private static Map<ManagerContext, JavaInstallationManager> instances = new HashMap<ManagerContext, JavaInstallationManager>();

	private JavaInstallationManager() {
		super();
	}

	public JavaInstallationManager(ManagerContext contextType,
			Properties properties) {
		super(contextType, properties);
		instances.put(contextType, this);
	}

	public static final JavaInstallationManager getInstance() {
		if (instances.isEmpty())
			instances.put(ManagerContext.APPLICATION,
					new JavaInstallationManager());

		return instances.get(ManagerContext.APPLICATION);
	}

	public static final JavaInstallationManager getInstance(
			ManagerContext contextType) {
		if (!instances.containsKey(contextType))
			throw new RuntimeException("Instance not initialized");

		return instances.get(contextType);
	}

	@Override
	public List<JavaInstallation> enumerateAll() {
		return super.enumerateAll(JavaInstallation.class);
	}

	@Override
	protected JavaInstallationFields[] getFields() {
		return JavaInstallationFields.values();
	}

	@Override
	protected PreferenceFields getNextIdField() {
		return PreferenceFields.JAVA_INSTALLATION_NEXT_ID;
	}

	@Override
	protected PreferenceFields getIdsListField() {
		return PreferenceFields.JAVA_INSTALLATIONS;
	}

	@Override
	protected JavaInstallation createModel() {
		return new JavaInstallation();
	}

	@Override
	public void delete(JavaInstallation object) throws Exception {
		List<LaunchConfiguration> launchConfigurations = LaunchConfigurationManager
				.getInstance(contextType).enumerateAll();
		for (LaunchConfiguration launchConfiguration : launchConfigurations) {
			if (object.equals(launchConfiguration.getJava())) {
				throw new ConstraintViolationException(
						"The Java Installation cannot be delete. It is referenced by min. a Launch Configuration.");
			}
		}

		super.delete(object);
	}
}
