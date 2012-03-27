/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;
import com.weiglewilczek.xwt.launcher.model.Group;
import com.weiglewilczek.xwt.launcher.model.Installation;
import com.weiglewilczek.xwt.launcher.model.JavaInstallation;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;

public class ImportExportManager {
	private static ImportExportManager instance;

	private ImportExportManager() {
	}

	public static ImportExportManager getInstance() {
		if (instance == null) {
			instance = new ImportExportManager();
		}

		return instance;
	}

	public void exportProperties(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);

		Properties properties = new Properties();
		Properties javas = JavaInstallationManager.getInstance()
				.getProperties();
		properties.putAll(javas);
		Properties eclipses = EclipseInstallationManager.getInstance()
				.getProperties();
		properties.putAll(eclipses);
		Properties launcher = LaunchConfigurationManager.getInstance()
				.getProperties();
		properties.putAll(launcher);
		Properties groups = GroupManager.getInstance().getProperties();
		properties.putAll(groups);

		properties.store(fos, "EclipseLauncherX");

		fos.close();
	}

	public void importProperties(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);

		Properties properties = new Properties();
		properties.load(fis);

		fis.close();

		// Initialize Import Manager:
		new JavaInstallationManager(ManagerContext.IMPORT, properties);
		new EclipseInstallationManager(ManagerContext.IMPORT, properties);
		new LaunchConfigurationManager(ManagerContext.IMPORT, properties);
		new GroupManager(ManagerContext.IMPORT, properties);

		// do import
		JavaInstallationManager javaImportManager = JavaInstallationManager
				.getInstance(ManagerContext.IMPORT);
		Map<Long, JavaInstallation> oldIdToNewJavaMap = importInstallations(
				javaImportManager, JavaInstallationManager.getInstance());

		EclipseInstallationManager eclipseImportManager = EclipseInstallationManager
				.getInstance(ManagerContext.IMPORT);
		Map<Long, EclipseInstallation> oldIdToNewEclipseMap = importInstallations(
				eclipseImportManager, EclipseInstallationManager.getInstance());

		Map<Long, LaunchConfiguration> oldIdToNewLauncherMap = importLauncher(
				oldIdToNewJavaMap, oldIdToNewEclipseMap);

		importGroup(oldIdToNewLauncherMap);
	}

	private <T extends Installation<?>> Map<Long, T> importInstallations(
			BaseManager<T, ?> importManager,
			BaseManager<T, ?> actualManagerInstance) {
		Map<Long, T> oldIdToNewEntryMap = new HashMap<Long, T>();

		List<T> javasToImport = importManager.enumerateAll();
		List<T> actualJavas = actualManagerInstance.enumerateAll();

		// non found
		if (javasToImport.isEmpty()) {
			return oldIdToNewEntryMap;
		}

		// import all
		if (actualJavas.isEmpty()) {
			for (T javaInstallation : javasToImport) {
				Long oldId = javaInstallation.getId();
				actualManagerInstance.create(javaInstallation);
				oldIdToNewEntryMap.put(oldId, javaInstallation);
			}
		}
		// compare and import
		else {
			for (T newJava : javasToImport) {
				Long oldId = newJava.getId();
				T matching = null;
				for (T actualJava : actualJavas) {
					if (equals(newJava, actualJava)) {
						matching = actualJava;
						break;
					}
				}

				if (matching != null) {
					oldIdToNewEntryMap.put(oldId, matching);
				} else {
					actualManagerInstance.create(newJava);
					oldIdToNewEntryMap.put(oldId, newJava);
				}
			}
		}

		return oldIdToNewEntryMap;
	}

	private Map<Long, LaunchConfiguration> importLauncher(
			Map<Long, JavaInstallation> oldIdToNewJavaMap,
			Map<Long, EclipseInstallation> oldIdToNewEclipseMap) {
		Map<Long, LaunchConfiguration> oldIdToNewEntryMap = new HashMap<Long, LaunchConfiguration>();

		LaunchConfigurationManager eclipseManager = LaunchConfigurationManager
				.getInstance(ManagerContext.IMPORT);
		List<LaunchConfiguration> launcherToImport = eclipseManager
				.enumerateAll();
		List<LaunchConfiguration> actualLaunchers = LaunchConfigurationManager
				.getInstance().enumerateAll();

		// non found
		if (launcherToImport.isEmpty()) {
			return oldIdToNewEntryMap;
		}

		// import all
		if (actualLaunchers.isEmpty()) {
			for (LaunchConfiguration launchConfiguration : launcherToImport) {
				Long oldId = launchConfiguration.getId();
				replaceJava(launchConfiguration, oldIdToNewJavaMap);
				replaceEclipse(launchConfiguration, oldIdToNewEclipseMap);
				LaunchConfigurationManager.getInstance().create(
						launchConfiguration);
				oldIdToNewEntryMap.put(oldId, launchConfiguration);
			}
		}
		// compare and import
		else {
			for (LaunchConfiguration newLauncher : launcherToImport) {
				Long oldId = newLauncher.getId();
				LaunchConfiguration matching = null;
				for (LaunchConfiguration actualLauncher : actualLaunchers) {
					if (equals(newLauncher, actualLauncher)) {
						matching = actualLauncher;
						break;
					}
				}

				if (matching != null) {
					oldIdToNewEntryMap.put(oldId, matching);
				} else {
					replaceJava(newLauncher, oldIdToNewJavaMap);
					replaceEclipse(newLauncher, oldIdToNewEclipseMap);
					LaunchConfigurationManager.getInstance()
							.create(newLauncher);
					oldIdToNewEntryMap.put(oldId, newLauncher);
				}
			}
		}

		return oldIdToNewEntryMap;
	}

	private void importGroup(
			Map<Long, LaunchConfiguration> oldIdToNewLauncherMap) {

		GroupManager groupManager = GroupManager
				.getInstance(ManagerContext.IMPORT);
		List<Group> groupsToImport = new ArrayList<Group>(
				groupManager.enumerateAll());
		List<Group> actualGroups = new ArrayList<Group>(GroupManager
				.getInstance().enumerateAll());

		// remove default group
		groupsToImport.remove(groupManager.get(-1l));
		actualGroups.remove(GroupManager.getInstance().get(-1l));

		// non found
		if (groupsToImport.isEmpty()) {
			return;
		}

		// import all
		if (actualGroups.isEmpty()) {
			for (Group group : groupsToImport) {
				replaceLauncher(group, oldIdToNewLauncherMap);
				GroupManager.getInstance().create(group);
			}
		}
		// compare and import
		else {
			for (Group newGroup : groupsToImport) {
				Group matching = null;
				for (Group actualGroup : actualGroups) {
					if (equals(newGroup, actualGroup)) {
						matching = actualGroup;
						break;
					}
				}

				if (matching == null) {
					replaceLauncher(newGroup, oldIdToNewLauncherMap);
					GroupManager.getInstance().create(newGroup);
				}
			}
		}

	}

	private void replaceJava(LaunchConfiguration configuration,
			Map<Long, JavaInstallation> oldIdToNewJavaMap) {
		if (configuration.getJava() != null) {
			JavaInstallation java = oldIdToNewJavaMap.get(configuration
					.getJava().getId());
			configuration.setJava(java);
		}
	}

	private void replaceEclipse(LaunchConfiguration configuration,
			Map<Long, EclipseInstallation> oldIdToNewEclipseMap) {
		if (configuration.getEclipse() != null) {
			EclipseInstallation eclipse = oldIdToNewEclipseMap
					.get(configuration.getEclipse().getId());
			configuration.setEclipse(eclipse);
		}
	}

	private void replaceLauncher(Group group,
			Map<Long, LaunchConfiguration> oldIdToNewLauncherMap) {
		if (group.getConfigurations() != null) {
			List<LaunchConfiguration> newLauncher = new ArrayList<LaunchConfiguration>();
			for (LaunchConfiguration configuration : group.getConfigurations()) {
				LaunchConfiguration launcher = oldIdToNewLauncherMap
						.get(configuration.getId());
				newLauncher.add(launcher);
			}
			group.setConfigurations(newLauncher);
		}
	}

	private boolean equals(Object newProperty, Object actualProperty) {
		if (newProperty == null && actualProperty == null) {
			return true;
		} else if (newProperty != null && newProperty.equals(actualProperty)) {
			return true;
		}

		return false;
	}

	private boolean equals(Installation<?> installation1,
			Installation<?> installation2) {
		if (installation1 == null && installation2 == null) {
			return true;
		} else if (installation1 != null
				&& installation2 != null
				&& equals(installation1.getName(), installation2.getName())
				&& equals(installation1.getPathToExecutable(),
						installation2.getPathToExecutable())) {
		}

		return false;
	}

	private boolean equals(LaunchConfiguration launcher1,
			LaunchConfiguration launcher2) {
		if (equals(launcher1.getName(), launcher2.getName())
				&& equals(launcher1.getWorkspacePath(),
						launcher2.getWorkspacePath())
				&& equals(launcher1.getEclipseArgs(),
						launcher2.getEclipseArgs())
				&& equals(launcher1.getVmArgs(), launcher2.getVmArgs())
				&& equals(launcher1.getJava(), launcher2.getJava())
				&& equals(launcher1.getEclipse(), launcher2.getEclipse())) {
			return true;
		}

		return false;
	}

	private boolean equals(Group group1, Group group2) {
		if (equals(group1.getName(), group2.getName())
				&& group1.getConfigurations().size() == group2
						.getConfigurations().size()) {
			for (LaunchConfiguration configuration1 : group1
					.getConfigurations()) {
				boolean match = false;
				for (LaunchConfiguration configuration2 : group2
						.getConfigurations()) {
					if (equals(configuration1, configuration2)) {
						match = true;
						break;
					}
				}

				if (!match) {
					return false;
				}
			}
			return true;
		}

		return false;
	}
}
