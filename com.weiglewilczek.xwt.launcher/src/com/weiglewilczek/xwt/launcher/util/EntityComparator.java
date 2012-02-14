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
