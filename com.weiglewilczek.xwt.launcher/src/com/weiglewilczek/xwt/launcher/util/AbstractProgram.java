/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;

import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;

public abstract class AbstractProgram implements IProgram {

	@Override
	public void execute(LaunchConfiguration configuration) throws Exception {
		if (configuration == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		String[] executionCommand = getExecutionCommand(configuration
				.getEclipse());
		String[] args = getCommands(configuration);

		String[] command = new String[executionCommand.length + args.length];

		System.arraycopy(executionCommand, 0, command, 0,
				executionCommand.length);
		System.arraycopy(args, 0, command, executionCommand.length, args.length);

		Runtime.getRuntime().exec(command);
	}

	protected abstract String[] getExecutionCommand(EclipseInstallation eclipse);

	protected String[] getCommands(LaunchConfiguration configuration) {
		List<String> commands = new ArrayList<String>();

		if (configuration.getJava() != null) {
			commands.add("-vm");
			commands.add(configuration.getJava().getPathToExecutable());
			// TODO: don't escape paths without whitespaces for macos: path is
			// not recognized, test paths with whitespaces
			// commands.add("\"" + configuration.getJava().getPathToExecutable()
			// + "\"");
		}

		if (configuration.getWorkspacePath() != null
				&& configuration.getWorkspacePath().length() > 0) {
			commands.add("-data");
			// TODO: don't escape paths without whitespaces for macos: path is
			// treaten as relative to eclipse, test paths with whitespaces
			// commands.add("\"" + configuration.getWorkspacePath() + "\"");
			commands.add(configuration.getWorkspacePath());
		}

		if (configuration.getEclipseArgs() != null
				&& configuration.getEclipseArgs().length() > 0) {
			commands.add(configuration.getEclipseArgs());
		}

		commands.add("-showlocation");

		if (configuration.getVmArgs() != null
				&& configuration.getVmArgs().length() > 0) {
			commands.add("-vmargs " + configuration.getVmArgs());
		}

		String[] command = commands.toArray(new String[0]);

		return command;
	}

}
