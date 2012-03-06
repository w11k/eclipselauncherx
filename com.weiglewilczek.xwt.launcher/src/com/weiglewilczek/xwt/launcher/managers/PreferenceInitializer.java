/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.managers;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.weiglewilczek.xwt.launcher.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		// 0-Values as long values are interpreted as NULL and are not stored!
		// So the ids must begin at 1
		store.setDefault(
				PreferenceFields.LAUNCH_CONFIGURATION_NEXT_ID.getName(), 1l);
		store.setDefault(
				PreferenceFields.ECLIPSE_INSTALLATION_NEXT_ID.getName(), 1l);
		store.setDefault(PreferenceFields.JAVA_INSTALLATION_NEXT_ID.getName(),
				1l);
		store.setDefault(PreferenceFields.GROUP_NEXT_ID.getName(), 1l);
	}

}
