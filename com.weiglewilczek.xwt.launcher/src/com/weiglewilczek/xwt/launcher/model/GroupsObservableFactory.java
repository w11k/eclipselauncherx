package com.weiglewilczek.xwt.launcher.model;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;

public class GroupsObservableFactory implements IObservableFactory {

	@Override
	public IObservable createObservable(Object object) {
		if (object instanceof GroupsDataContext) {
			return ((GroupsDataContext)object).getGroups();
		}
		else if (object instanceof ObservableGroup) {
			return ((ObservableGroup)object).getConfigurations();
		}

		return new WritableList();
	}

}
