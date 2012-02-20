package com.weiglewilczek.xwt.launcher.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

	private static Preferences preferences;
	private static Properties properties;

	private final List<IListener> listenerList = new ArrayList<IListener>();

	private final Map<Long, T> cache;

	static {
		preferences = Preferences.userNodeForPackage(Activator.class);

		properties = new Properties();

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

	public BaseManager() {
		cache = new HashMap<Long, T>();
	}

	public T create(T object) throws BackingStoreException {
		synchronized (properties) {
			Long nextVal = getLong(getNextIdField().getName());
			object.setId(nextVal);

			String ids = (String) properties.get(getIdsListField().getName());
			if (ids != null && ids.length() > 0)
				properties.setProperty(getIdsListField().getName(), ids + ","
						+ nextVal);
			else
				properties.setProperty(getIdsListField().getName(),
						nextVal.toString());

			setFields(object);

			properties.setProperty(getNextIdField().getName(), new Long(
					nextVal + 1).toString());

			commit();
		}

		synchronized (cache) {
			cache.put(object.getId(), object);
		}

		handleCreated(object);

		return object;
	}

	public T update(T object) throws BackingStoreException {
		synchronized (properties) {
			if (object.getId() != null) {
				String ids = (String) properties.get(getIdsListField()
						.getName());
				if (ids != null
						&& ids.length() > 0
						&& (ids.equals(object.getId().toString())
								|| ids.startsWith(object.getId().toString()
										+ ",")
								|| ids.indexOf("," + object.getId().toString()
										+ ",") > -1 || ids.endsWith(","
								+ object.getId().toString()))) {
					setFields(object);
				} else {
					object = create(object);
				}
			} else {
				object = create(object);
			}

			commit();
		}

		handleUpdated(object);

		return object;
	}

	public void delete(T object) throws Exception {
		Long id = object.getId();

		synchronized (properties) {
			String ids = (String) properties.get(getIdsListField().getName());
			if (ids.equals(id.toString())) {
				properties.setProperty(getIdsListField().getName(), "");
			} else if (ids.startsWith(id.toString() + ",")) {
				ids = ids.replaceFirst(id.toString() + ",", "");
				properties.setProperty(getIdsListField().getName(), ids);
			} else if (ids.indexOf("," + id.toString() + ",") > -1) {
				ids = ids.replaceAll("," + id.toString() + ",", ",");
				properties.setProperty(getIdsListField().getName(), ids);
			} else if (ids.endsWith("," + id.toString())) {
				ids = ids.substring(0,
						ids.length() - ("," + id.toString()).length());
				properties.setProperty(getIdsListField().getName(), ids);
			} else {
				return;
			}

			for (BaseFields<?> field : getFields()) {
				removeEntry(id + field.getName());
			}

			commit();
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
			} else {
				object = createModel();
				object.setId(id);
				cache.put(id, object);
			}
		}

		synchronized (properties) {
			String ids = (String) properties.get(getIdsListField().getName());
			if (ids != null
					&& ids.length() > 0
					&& (ids.equals(id.toString())
							|| ids.startsWith(id.toString() + ",")
							|| ids.indexOf("," + id.toString() + ",") > -1 || ids
								.endsWith("," + id.toString()))) {

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
						if (properties
								.contains(id.toString() + field.getName())) {
							// null-value is returned as 0
							long value = getLong(id.toString()
									+ field.getName());
							object.setProperty(field, JavaInstallationManager
									.getInstance().get(value));
						}
					} else if (field.getTypeInstance().getClass()
							.equals(EclipseInstallation.class)) {
						if (properties
								.contains(id.toString() + field.getName())) {
							long value = getLong(id.toString()
									+ field.getName());
							object.setProperty(field,
									EclipseInstallationManager.getInstance()
											.get(value));
						}
					} else if (field.getTypeInstance().getClass()
							.equals(LaunchConfiguration[].class)) {
						if (properties
								.contains(id.toString() + field.getName())) {
							String value = (String) properties.get(id
									.toString() + field.getName());
							String[] values = value.split(",");

							List<LaunchConfiguration> configurations = new ArrayList<LaunchConfiguration>();
							for (String configurationId : values) {
								LaunchConfiguration configuration = LaunchConfigurationManager
										.getInstance().get(
												new Long(configurationId));
								configurations.add(configuration);
							}

							object.setProperty(field, configurations);
						}
					} else
						System.out.println("warn: unknown type: "
								+ field.getTypeInstance().getClass());
				}

				return object;
			}

			return null;
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
					result.add(get(id));
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
					properties
							.setProperty(id + field.getName(), (String) value);
				else if (value instanceof Long)
					properties.setProperty(id + field.getName(),
							((Long) value).toString());
				else if (value instanceof ModelElement)
					properties.setProperty(id + field.getName(),
							((ModelElement<?>) value).getId().toString());
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

						properties.setProperty(id + field.getName(),
								configurationIds);
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

	private void commit() throws BackingStoreException {
		try {
			for (Entry<Object, Object> entry : properties.entrySet()) {
				preferences.put((String) entry.getKey(),
						(String) entry.getValue());
			}
		} catch (Exception e) {
			throw new BackingStoreException(e);
		}
	}

	private void removeEntry(String key) {
		properties.remove(key);
	}

	private long getLong(String key) {
		Object value = properties.get(key);
		if (value instanceof String && ((String) value).length() > 0) {
			return Long.parseLong(key);
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
}
