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

import org.eclipse.swt.SWT;

public class Program implements IProgram {

	@Override
	public void execute(String executable, String[] args) throws Exception {
		if (executable == null || args == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		String[] commands = new String[args.length + 1];

		String executableName = new File(executable).getName();
		executableName = executableName.substring(0,
				executableName.indexOf("."));
		commands[0] = ("\"" + executable + "\"" + "/Contents/MacOS/" + executableName);

		System.arraycopy(args, 0, commands, 1, args.length);

		Runtime.getRuntime().exec(commands);
	}
}
