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
 * Compares two entities of a certain type.
 * 
 * @author Ilya Shinkarenko
 * @param <T>
 *            type to compare
 * 
 */
public interface EntityComparator<T> {

	/**
	 * Compares the given entities and returns the comparison result.
	 * 
	 * @param t1
	 * @param t2
	 * @return comparison result
	 * @see Comparable
	 */
	public abstract int doCompare(T t1, T t2);
}
