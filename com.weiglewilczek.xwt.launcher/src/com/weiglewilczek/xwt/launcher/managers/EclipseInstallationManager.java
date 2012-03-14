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

import com.weiglewilczek.xwt.launcher.Messages;
import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;

public class EclipseInstallationManager extends
		BaseManager<EclipseInstallation, EclipseInstallationFields> {

	private static Map<ManagerContext, EclipseInstallationManager> instances = new HashMap<ManagerContext, EclipseInstallationManager>();

	private EclipseInstallationManager() {
		super();
	}

	public EclipseInstallationManager(ManagerContext contextType,
			Properties properties) {
		super(contextType, properties);
		instances.put(contextType, this);
	}

	public static final EclipseInstallationManager getInstance() {
		if (instances.isEmpty())
			instances.put(ManagerContext.APPLICATION,
					new EclipseInstallationManager());

		return instances.get(ManagerContext.APPLICATION);
	}

	public static final EclipseInstallationManager getInstance(
			ManagerContext contextType) {
		if (!instances.containsKey(contextType)) {
			throw new RuntimeException("Instance not initialized");
		}

		return instances.get(contextType);
	}

	@Override
	public List<EclipseInstallation> enumerateAll() {
		return super.enumerateAll(EclipseInstallation.class);
	}

	@Override
	protected EclipseInstallationFields[] getFields() {
		return EclipseInstallationFields.values();
	}

	@Override
	protected PreferenceFields getNextIdField() {
		return PreferenceFields.ECLIPSE_INSTALLATION_NEXT_ID;
	}

	@Override
	protected PreferenceFields getIdsListField() {
		return PreferenceFields.ECLIPSE_INSTALLATIONS;
	}

	@Override
	protected EclipseInstallation createModel() {
		return new EclipseInstallation();
	}

	@Override
	public void delete(EclipseInstallation object) throws Exception {
		List<LaunchConfiguration> launchConfigurations = LaunchConfigurationManager
				.getInstance(contextType).enumerateAll();
		for (LaunchConfiguration launchConfiguration : launchConfigurations) {
			if (object.equals(launchConfiguration.getEclipse())) {
				throw new ConstraintViolationException(
						Messages.EclipseInstallationManager_ConstraintViolationException);
			}
		}

		super.delete(object);
	}
}
