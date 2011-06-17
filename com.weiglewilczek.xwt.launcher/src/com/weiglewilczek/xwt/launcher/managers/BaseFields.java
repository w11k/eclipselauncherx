package com.weiglewilczek.xwt.launcher.managers;

public interface BaseFields<T> {
	String getName();
	Object getTypeInstance();
	T findForName(String name);
}
