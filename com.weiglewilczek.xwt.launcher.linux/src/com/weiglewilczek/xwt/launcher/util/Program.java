/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.util;

import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;

public class Program extends AbstractProgram {

	@Override
	protected String[] getExecutionCommand(EclipseInstallation eclipse) {
		String[] commands = new String[3];
		commands[0] = "sh";
		commands[1] = "-c";
		commands[2] = "\"" + eclipse.getPathToExecutable() + "\"";
		return commands;
	}
}
