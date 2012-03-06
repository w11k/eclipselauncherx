/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.util;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;

public class Program {
	/**
	 * Executes the program with the file as the single argument in the
	 * operating system.
	 * 
	 * @param fileName
	 *            the file or program name
	 * @return <code>true</code> if the file is launched, otherwise
	 *         <code>false</code>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT when fileName is null</li>
	 *                </ul>
	 */
	public boolean execute(String fileName) {
		if (fileName == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		String fileURI = new File(fileName).toURI().toString();

		String[] command = new String[] { "open", fileURI };

		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	 * Executes a command.
	 * 
	 * @param command
	 *            the file or program name
	 * @return <code>true</code> if the file is launched, otherwise
	 *         <code>false</code>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT when fileName is null</li>
	 *                </ul>
	 */
	public boolean execute(String[] command) {
		if (command == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			return false;
		}

		return true;
	}
}

/*******************************************************************************
 * Copyright (c) 2009 Weigle Wilczek GmbH, Martinstr. 42-44, 73728 Esslingen,
 * Germany. All rights reserved.
 * 
 * WeigleWilczek has intellectual property rights relating to technology
 * embodied in this artefact. This artefact and the product to which it pertains
 * are distributed under licenses restricting their use, copying, distribution,
 * and decompilation. No part of the product or of this document may be
 * reproduced in any form by any means without prior written authorization of
 * WeigleWilczek and its licensors, if any.
 * 
 * DOCUMENTATION IS PROVIDED "AS IS" AND ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESEN- TATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * DISCLAIMED, EXCEPT TO THE EXTENT THAT SUCH DISCLAIMERS ARE HELD TO BE LEGALLY
 * INVALID.
 ******************************************************************************/
