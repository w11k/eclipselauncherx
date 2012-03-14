/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.util;

import org.eclipse.swt.SWT;

public class Program implements IProgram {

	@Override
	public void execute(String executable, String[] args) throws Exception {
		if (executable == null || args == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		// cmd /nowait /c start ...

		String[] commands = new String[args.length + 4];
		commands[0] = "cmd";
		commands[1] = "/nowait";
		commands[2] = "/c";
		commands[3] = "start";

		System.arraycopy(args, 0, commands, 4, args.length);

		Runtime.getRuntime().exec(commands);
	}
}
