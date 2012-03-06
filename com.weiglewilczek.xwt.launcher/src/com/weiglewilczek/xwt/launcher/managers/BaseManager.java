/*
 * Copyright (c) 2012 WeigleWilczek and others.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.weiglewilczek.xwt.launcher.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.weiglewilczek.xwt.launcher.Activator;
import com.weiglewilczek.xwt.launcher.listener.IListener;
import com.weiglewilczek.xwt.launcher.listener.ListenerType;
import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;
import com.weiglewilczek.xwt.launcher.model.JavaInstallation;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;
import com.weiglewilczek.xwt.launcher.model.ModelElement;

public abstract class BaseManager<T extends ModelElement<F>, F extends BaseFields<F>> {

	private final Preferences preferences;
	private final Properties properties;

	private final List<IListener> listenerList = new ArrayList<IListener>();

	private final Map<Long, T> cache = new HashMap<Long, T>();

	protected final ManagerContext contextType;

	public BaseManager() {
		contextType = ManagerContext.APPLICATION;

		properties = new Properties();
		preferences = Preferences.userNodeForPackage(Activator.class);

		try {
			String[] keys = preferences.keys();

			for (String key : keys) {
				String value = preferences.get(key, null);
				properties.put(key, value);
			}
		} catch (BackingStoreException aEx) {
			Activator
					.getDefault()
					.getLog()
					.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
							"error loading preferences", aEx));
		}
	}

	public BaseManager(ManagerContext contextType, Properties properties) {
		this.contextType = contextType;
		preferences = null;
		this.properties = properties;
	}

	public T create(T object) {
		synchronized (properties) {
			Long nextVal = getLong(getNextIdField().getName());

			// always start with 1, 0 is threaten as null
			if (nextVal == 0) {
				nextVal = 1l;
			}

			object.setId(nextVal);

			String ids = (String) properties.get(getIdsListField().getName());
			if (ids != null && ids.length() > 0)
				setProperty(getIdsListField().getName(), ids + "," + nextVal);
			else
				setProperty(getIdsListField().getName(), nextVal.toString());

			setFields(object);

			setProperty(getNextIdField().getName(),
					new Long(nextVal + 1).toString());

		}

		synchronized (cache) {
			cache.put(object.getId(), object);
		}

		handleCreated(object);

		return object;
	}

	public T update(T object) {
		boolean contains = contains(object.getId());

		synchronized (properties) {
			if (object.getId() != null && contains) {
				setFields(object);
			} else {
				object = create(object);
			}
		}

		handleUpdated(object);

		return object;
	}

	public void delete(T object) throws Exception {
		Long id = object.getId();

		synchronized (properties) {
			String ids = (String) properties.get(getIdsListField().getName());
			if (ids.equals(id.toString())) {
				setProperty(getIdsListField().getName(), "");
			} else if (ids.startsWith(id.toString() + ",")) {
				ids = ids.replaceFirst(id.toString() + ",", "");
				setProperty(getIdsListField().getName(), ids);
			} else if (ids.indexOf("," + id.toString() + ",") > -1) {
				ids = ids.replaceAll("," + id.toString() + ",", ",");
				setProperty(getIdsListField().getName(), ids);
			} else if (ids.endsWith("," + id.toString())) {
				ids = ids.substring(0,
						ids.length() - ("," + id.toString()).length());
				setProperty(getIdsListField().getName(), ids);
			} else {
				return;
			}

			for (BaseFields<?> field : getFields()) {
				removeEntry(id + field.getName());
			}
		}

		synchronized (cache) {
			cache.remove(id);
		}

		handleDeleted(object);
	}

	public T get(Long id) {

		final T object;
		synchronized (cache) {
			if (cache.containsKey(id)) {
				object = cache.get(id);
			} else if (contains(id)) {
				object = createModel();
				object.setId(id);
				cache.put(id, object);
			} else {
				return null;
			}
		}

		synchronized (properties) {
			for (F field : getFields()) {
				if (field.getTypeInstance().getClass().equals(String.class)) {
					String value = (String) properties.get(id.toString()
							+ field.getName());
					object.setProperty(field, value);
				} else if (field.getTypeInstance().getClass()
						.equals(Long.class)) {
					long value = getLong(id.toString() + field.getName());
					object.setProperty(field, value);
				} else if (field.getTypeInstance().getClass()
						.equals(JavaInstallation.class)) {
					if (properties.containsKey(id.toString() + field.getName())) {
						// null-value is returned as 0
						long value = getLong(id.toString() + field.getName());
						object.setProperty(field, JavaInstallationManager
								.getInstance(contextType).get(value));
					}
				} else if (field.getTypeInstance().getClass()
						.equals(EclipseInstallation.class)) {
					if (properties.containsKey(id.toString() + field.getName())) {
						long value = getLong(id.toString() + field.getName());
						object.setProperty(field, EclipseInstallationManager
								.getInstance(contextType).get(value));
					}
				} else if (field.getTypeInstance().getClass()
						.equals(LaunchConfiguration[].class)) {
					if (properties.containsKey(id.toString() + field.getName())) {
						String value = (String) properties.get(id.toString()
								+ field.getName());
						String[] values = value.split(",");

						List<LaunchConfiguration> configurations = new ArrayList<LaunchConfiguration>();
						for (String configurationId : values) {
							LaunchConfiguration configuration = LaunchConfigurationManager
									.getInstance(contextType).get(
											new Long(configurationId));
							if (configuration != null) {
								configurations.add(configuration);
							}
						}

						object.setProperty(field, configurations);
					}
				} else
					System.out.println("warn: unknown type: "
							+ field.getTypeInstance().getClass());
			}

			return object;
		}
	}

	protected List<T> enumerateAll(Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		String ids = (String) properties.get(getIdsListField().getName());
		if (ids != null && ids.length() > 0) {
			String[] idList = ids.split(",");

			for (int i = 0; i < idList.length; i++) {
				String idString = idList[i];
				Long id = Long.valueOf(idString);
				if (id != null) {
					T loaded = get(id);
					if (loaded != null) {
						result.add(loaded);
					}
				}
			}
		}

		return result;
	}

	public abstract List<T> enumerateAll();

	protected abstract F[] getFields();

	protected abstract PreferenceFields getNextIdField();

	protected abstract PreferenceFields getIdsListField();

	protected abstract T createModel();

	@SuppressWarnings("unchecked")
	private void setFields(T object) {
		Long id = object.getId();

		for (F field : getFields()) {
			Object value = object.getProperty(field);
			if (value != null) {
				if (value instanceof String)
					setProperty(id + field.getName(), (String) value);
				else if (value instanceof Long)
					setProperty(id + field.getName(), ((Long) value).toString());
				else if (value instanceof ModelElement)
					setProperty(id + field.getName(), ((ModelElement<?>) value)
							.getId().toString());
				else if (field.getTypeInstance().getClass()
						.equals(LaunchConfiguration[].class)) {
					List<LaunchConfiguration> configurations = (List<LaunchConfiguration>) value;
					if (configurations.size() > 0) {
						String configurationIds = "";
						for (LaunchConfiguration configuration : (List<LaunchConfiguration>) value) {
							configurationIds = configurationIds
									+ configuration.getId() + ",";
						}
						configurationIds.substring(0,
								configurationIds.length() - 1);

						setProperty(id + field.getName(), configurationIds);
					} else {
						removeEntry(id + field.getName());
					}
				} else
					System.out.println("warn: unknown type: "
							+ value.getClass());
			} else {
				removeEntry(id + field.getName());
			}
		}
	}

	public boolean contains(Long id) {
		synchronized (properties) {
			String ids = (String) properties.get(getIdsListField().getName());
			if (ids != null
					&& ids.length() > 0
					&& (ids.equals(id.toString())
							|| ids.startsWith(id.toString() + ",")
							|| ids.indexOf("," + id.toString() + ",") > -1 || ids
								.endsWith("," + id.toString()))) {
				return true;
			}
		}

		return false;
	}

	private void removeEntry(String key) {
		properties.remove(key);

		if (preferences != null) {
			preferences.remove(key);
		}
	}

	private void setProperty(String key, String value) {
		properties.put(key, value);

		if (preferences != null) {
			preferences.put(key, value);
		}
	}

	private long getLong(String key) {
		Object value = properties.get(key);
		if (value instanceof String && ((String) value).length() > 0) {
			return Long.parseLong((String) value);
		}

		return 0;
	}

	public void addListener(IListener listener) {
		synchronized (listenerList) {
			listenerList.add(listener);
		}
	}

	public void removeListener(IListener listener) {
		synchronized (listenerList) {
			listenerList.remove(listener);
		}
	}

	protected void handleCreated(T object) {
		synchronized (listenerList) {
			for (IListener listener : listenerList) {
				listener.handle(ListenerType.CREATE, object);
			}
		}
	}

	protected void handleUpdated(T object) {
		synchronized (listenerList) {
			for (IListener listener : listenerList) {
				listener.handle(ListenerType.UPDATE, object);
			}
		}
	}

	protected void handleDeleted(T object) {
		synchronized (listenerList) {
			for (IListener listener : listenerList) {
				listener.handle(ListenerType.DELETE, object);
			}
		}
	}

	Properties getProperties() {
		return properties;
	}
}
