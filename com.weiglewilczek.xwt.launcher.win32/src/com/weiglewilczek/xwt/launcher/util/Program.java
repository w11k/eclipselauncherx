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

	// valid until eclipse 3.3
	// -> C:\Programme\Java\jdk1.6.0_20\bin\javaw -cp
	// C:\Programme\eclipse\eclipse361\startup.jar
	// org.eclipse.core.launcher.Main -os win32 -ws win32 -arch x86
	// -data C:\dev\INA_TRUNK -showsplash
	// C:\Programme\eclipse\eclipse361\eclipse
	// -showsplash 600

	// valid since eclipse 3.4
	// -> C:\Programme\Java\jdk1.6.0_20\bin\javaw -cp
	// C:\Programme\eclipse\eclipse361\plugins\org.eclipse.equinox.launcher_1.1.0.v
	// 20100507.jar org.eclipse.core.launcher.Main -os win32 -ws win32 -arch x86
	// -data C:\dev\INA_TRUNK -showsplash
	// C:\Programme\eclipse\eclipse361\eclipse
	// -showsplash 600

	@Override
	public void execute(LaunchConfiguration configuration) throws Exception {
		if (configuration == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		Runtime.getRuntime().exec(getCommands(configuration));
	}

	// TODO: test java or eclipse or workspace on other volume as "C:\"
	// TODO: surround paths with ""
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
			pathToLauncher = eclipseHomePath + STARTUP;
		} else {
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

		eclipseHomePath.replace('\\', '/');

		String commands = "";
		String javaExecutablePath = configuration.getJava()
				.getPathToExecutable();

		if ((javaExecutablePath == null) || (javaExecutablePath.equals(""))) {
			javaExecutablePath = System.getProperty("java.home");
			javaExecutablePath += File.separator + "bin" + File.separator
					+ JAVA_EXECUTABLE_FILE_NAME;
		}

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
		commands += "-data " + configuration.getWorkspacePath() + SPACE;
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

	// won't do
	// @Override
	// public void execute(String executable, String[] args) throws Exception {
	// if (executable == null || args == null) {
	// SWT.error(SWT.ERROR_NULL_ARGUMENT);
	// }
	//
	// // cmd /nowait /c start ...
	//
	// String[] commands = new String[args.length + 4];
	// commands[0] = "cmd";
	// commands[1] = "/nowait";
	// commands[2] = "/c";
	// commands[3] = "start";
	//
	// System.arraycopy(args, 0, commands, 4, args.length);
	//
	// Runtime.getRuntime().exec(commands);
	// }
}
