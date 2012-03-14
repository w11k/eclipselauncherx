/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.util;

import org.eclipse.swt.SWTError;

public interface IProgram {

	/**
	 * Executes a command.
	 * 
	 * @param executable
	 *            the path and program name
	 * @param args
	 *            arguments for the executable
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT when executable or args is null</li>
	 *                </ul>
	 */
	public void execute(String executable, String[] args) throws Exception;
}