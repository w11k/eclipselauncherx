package com.weiglewilczek.xwt.launcher.util;

/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

import java.io.File;

import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;

public class Program extends AbstractProgram {

	@Override
	protected String[] getExecutionCommand(EclipseInstallation eclipse) {
		String[] commands = new String[1];

		String executableName = new File(eclipse.getPathToExecutable())
				.getName().toLowerCase();
		executableName = executableName.substring(0,
				executableName.indexOf("."));
		commands[0] = (eclipse.getPathToExecutable() + "/Contents/MacOS/" + executableName);
		return commands;
	}
}
