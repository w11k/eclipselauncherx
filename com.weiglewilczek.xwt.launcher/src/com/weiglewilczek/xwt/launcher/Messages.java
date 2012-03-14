/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.weiglewilczek.xwt.launcher.messages"; //$NON-NLS-1$
	public static String About_About;
	public static String About_Error;
	public static String About_Version;
	public static String About_Author;
	public static String About_Copyright;
	public static String BaseManager_Error;
	public static String BaseManager_WarningUnknownType;
	public static String DeleteEclipseInstallation_Error;
	public static String DeleteGroup_Error;
	public static String DeleteJavaInstallation_Error;
	public static String DeleteLaunchConfiguration_Error;
	public static String EclipseInstallationDialog_NewEclipseInstallation;
	public static String EclipseInstallationListView_Error;
	public static String EclipseInstallationManager_ConstraintViolationException;
	public static String EditEclipseInstallation_Error;
	public static String EditGroup_EditGroup;
	public static String EditGroup_Error;
	public static String EditJavaInstallation_Error;
	public static String EditLaunchConfiguration_EditLaunchConfiguration;
	public static String EditLaunchConfiguration_Error;
	public static String Export_Error;
	public static String Export_Export;
	public static String GroupDialog_NewGroup;
	public static String GroupManager_Others;
	public static String GroupsView_EditError;
	public static String GroupsView_LaunchingError;
	public static String Import_Error;
	public static String Import_Import;
	public static String JavaInstallationDialog_NewJavaInstallation;
	public static String JavaInstallationListView_Error;
	public static String JavaInstallationManager_ConstraintViolationException;
	public static String Launch_Error;
	public static String Launch_Launch;
	public static String Launch_PlatformError;
	public static String LaunchConfigurationDialog_NewLaunchConfiguration;
	public static String NewEclipseInstallation_Error;
	public static String NewGroup_Error;
	public static String NewGroup_NewGroup;
	public static String NewJavaInstallation_Error;
	public static String NewLaunchConfiguration_Error;
	public static String NewLaunchConfiguration_ErrorOpeningGroupsView;
	public static String NewLaunchConfiguration_NewLaunchConfiguration;
	public static String OpenEclipseInstallationListView_Error;
	public static String OpenGroupsView_Error;
	public static String OpenJavaInstallationListView_Error;
	public static String OpenLaunchConfigurationListView_Error;
	public static String WorkspaceListView_Error;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
