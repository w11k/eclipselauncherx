/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.util;

/**
 * Factory class creating an instance of and OS dependent implementation of
 * IProgram and calls the execute method on it.
 * 
 * @author danielablank
 */
public class ProgramFactory {
	public static void execute(String executable, String[] args)
			throws Exception {
		IProgram program = (IProgram) ProgramFactory.class.getClassLoader()
				.loadClass("com.weiglewilczek.xwt.launcher.util.Program")
				.newInstance();
		program.execute(executable, args);
	}
}