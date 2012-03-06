/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.model;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;

public class GroupsObservableFactory implements IObservableFactory {

	@Override
	public IObservable createObservable(Object object) {
		if (object instanceof GroupsDataContext) {
			return ((GroupsDataContext) object).getGroups();
		} else if (object instanceof ObservableGroup) {
			return ((ObservableGroup) object).getConfigurations();
		}

		return new WritableList();
	}

}
