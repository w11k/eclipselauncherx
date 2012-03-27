/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;

import com.weiglewilczek.xwt.launcher.Activator;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;

public class Program implements IProgram {

	private static final String JAVA_EXECUTABLE_FILE_NAME = "javaw";

	private static final String SPACE = " ";

	private static final String STARTUP = "startup.jar";
	private static final String STARTUP_INI_ARG = "-startup";

	@Override
	public void execute(LaunchConfiguration configuration) throws Exception {
		if (configuration == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		Runtime.getRuntime().exec(getCommands(configuration));
	}

	private String getCommands(LaunchConfiguration configuration)
			throws Exception {
		String eclipseHomePath = configuration.getEclipse()
				.getPathToExecutable();
		eclipseHomePath = new File(eclipseHomePath).getParent();
		if (!eclipseHomePath.endsWith(File.separator))
			eclipseHomePath += File.separator;

		String pathToLauncher = "";
		File testStartup = new File(eclipseHomePath + STARTUP);
		if (testStartup.exists()) {
			// valid until eclipse 3.3
			pathToLauncher = eclipseHomePath + STARTUP;
		} else {
			// valid since eclipse 3.4
			// read ini file
			String pathToIni = configuration.getEclipse().getPathToExecutable()
					.replaceAll(".exe", ".ini");
			File ini = new File(pathToIni);

			FileReader fr = new FileReader(ini);
			BufferedReader in = new BufferedReader(fr);
			try {
				boolean startupArgFound = false;
				String inputLine = null;

				inputLine = in.readLine();
				while (inputLine != null) {
					if (startupArgFound) {
						pathToLauncher = eclipseHomePath + inputLine;
						break;
					}

					if (inputLine.equals(STARTUP_INI_ARG)) {
						startupArgFound = true;
					}

					inputLine = in.readLine();
				}
			} finally {
				in.close();
				fr.close();
			}
		}

		pathToLauncher = "\"" + pathToLauncher + "\"";

		eclipseHomePath.replace('\\', '/');
		eclipseHomePath = "\"" + eclipseHomePath + "\"";

		String commands = "";
		String javaExecutablePath = configuration.getJava()
				.getPathToExecutable();

		if ((javaExecutablePath == null) || (javaExecutablePath.equals(""))) {
			javaExecutablePath = System.getProperty("java.home");
			javaExecutablePath += File.separator + "bin" + File.separator
					+ JAVA_EXECUTABLE_FILE_NAME;
		}

		javaExecutablePath = "\"" + javaExecutablePath + "\"";

		if (configuration.getVmArgs() != null
				&& configuration.getVmArgs().length() > 0) {
			javaExecutablePath += SPACE + configuration.getVmArgs();
		}

		commands = javaExecutablePath + " -cp " + pathToLauncher
				+ " org.eclipse.core.launcher.Main ";
		commands += "-os " + Platform.getOS() + SPACE;
		commands += "-ws " + Platform.getWS() + SPACE;
		commands += "-arch ";
		commands += Platform.getOSArch() + SPACE;

		String data = "\"" + configuration.getWorkspacePath() + "\"";

		commands += "-data " + data + SPACE;
		commands += "-showsplash ";
		commands += eclipseHomePath + "eclipse -showsplash 600";
		commands += " -showlocation";

		if (configuration.getEclipseArgs() != null
				&& configuration.getEclipseArgs().length() > 0) {
			commands += SPACE + configuration.getEclipseArgs();
		}

		Activator.getDefault().getLog()
				.log(new Status(IStatus.INFO, Activator.PLUGIN_ID, commands));

		return commands;
	}
}
