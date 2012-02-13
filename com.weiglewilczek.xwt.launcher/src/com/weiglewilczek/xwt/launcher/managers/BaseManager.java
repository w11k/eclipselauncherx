package com.weiglewilczek.xwt.launcher.managers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;

import com.weiglewilczek.xwt.launcher.Activator;
import com.weiglewilczek.xwt.launcher.listener.IListener;
import com.weiglewilczek.xwt.launcher.listener.ListenerType;
import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;
import com.weiglewilczek.xwt.launcher.model.JavaInstallation;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;
import com.weiglewilczek.xwt.launcher.model.ModelElement;

public abstract class BaseManager<T extends ModelElement<F>, F extends BaseFields<F>> {

	// prefs are stored in
	// .metadata/.plugins/org.eclipse.core.runtime/.settings/com.weiglewilczek.eclipse.launcher.prefs
	private static final IPreferenceStore store = Activator.getDefault()
			.getPreferenceStore();

	private final List<IListener> listenerList = new ArrayList<IListener>();

	public BaseManager() {

	}

	public T create(T object) throws BackingStoreException {
		synchronized (store) {
			Long nextVal = store.getLong(getNextIdField().getName());
			object.setId(nextVal);

			String ids = store.getString(getIdsListField().getName());
			if (ids != null && ids.length() > 0)
				store.setValue(getIdsListField().getName(), ids + "," + nextVal);
			else
				store.setValue(getIdsListField().getName(), nextVal.toString());

			setFields(object);

			store.setValue(getNextIdField().getName(), nextVal + 1);

			commit();
		}

		handleCreated(object);

		return object;
	}

	public T update(T object) throws BackingStoreException {
		synchronized (store) {
			if (object.getId() != null) {
				String ids = store.getString(getIdsListField().getName());
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
		synchronized (store) {
			Long id = object.getId();

			String ids = store.getString(getIdsListField().getName());
			if (ids.equals(id.toString())) {
				store.setValue(getIdsListField().getName(), null);
			} else if (ids.startsWith(id.toString() + ",")) {
				ids = ids.replaceFirst(id.toString() + ",", "");
				store.setValue(getIdsListField().getName(), ids);
			} else if (ids.indexOf("," + id.toString() + ",") > -1) {
				ids = ids.replaceAll("," + id.toString() + ",", ",");
				store.setValue(getIdsListField().getName(), ids);
			} else if (ids.endsWith("," + id.toString())) {
				ids = ids.substring(0,
						ids.length() - ("," + id.toString()).length());
				store.setValue(getIdsListField().getName(), ids);
			} else {
				return;
			}

			for (BaseFields<?> field : getFields()) {
				removeEntry(id + field.getName());
			}

			commit();
		}

		handleDeleted(object);
	}

	public T get(Long id) {
		T object = createModel();
		object.setId(id);

		synchronized (store) {

			String ids = store.getString(getIdsListField().getName());
			if (ids != null
					&& ids.length() > 0
					&& (ids.equals(id.toString())
							|| ids.startsWith(id.toString() + ",")
							|| ids.indexOf("," + id.toString() + ",") > -1 || ids
								.endsWith("," + id.toString()))) {

				for (F field : getFields()) {
					if (field.getTypeInstance().getClass().equals(String.class)) {
						String value = store.getString(id.toString()
								+ field.getName());
						object.setProperty(field, value);
					} else if (field.getTypeInstance().getClass()
							.equals(Long.class)) {
						long value = store.getLong(id.toString()
								+ field.getName());
						object.setProperty(field, value);
					} else if (field.getTypeInstance().getClass()
							.equals(JavaInstallation.class)) {
						if (store.contains(id.toString() + field.getName())) {
							// null-value is returned as 0
							long value = store.getLong(id.toString()
									+ field.getName());
							object.setProperty(field, JavaInstallationManager
									.getInstance().get(value));
						}
					} else if (field.getTypeInstance().getClass()
							.equals(EclipseInstallation.class)) {
						if (store.contains(id.toString() + field.getName())) {
							long value = store.getLong(id.toString()
									+ field.getName());
							object.setProperty(field,
									EclipseInstallationManager.getInstance()
											.get(value));
						}
					} else if (field.getTypeInstance().getClass()
							.equals(LaunchConfiguration[].class)) {
						if (store.contains(id.toString() + field.getName())) {
							String value = store.getString(id.toString()
									+ field.getName());
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
		String ids = store.getString(getIdsListField().getName());
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
					store.setValue(id + field.getName(), (String) value);
				else if (value instanceof Long)
					store.setValue(id + field.getName(),
							((Long) value).longValue());
				else if (value instanceof ModelElement)
					store.setValue(id + field.getName(),
							((ModelElement<?>) value).getId());
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

						store.setValue(id + field.getName(), configurationIds);
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
		IEclipsePreferences[] nodes = ((ScopedPreferenceStore) store)
				.getPreferenceNodes(false);

		for (int i = 0; i < nodes.length; i++) {
			IEclipsePreferences eclipsePreferences = nodes[i];
			if (eclipsePreferences.name().equals(
					Activator.getDefault().getBundle().getSymbolicName())) {
				try {
					eclipsePreferences.flush();
				} catch (BackingStoreException e) {
					// TODO: rollback?
					throw e;
				}
			}
		}
	}

	private void removeEntry(String key) {
		IEclipsePreferences[] nodes = ((ScopedPreferenceStore) store)
				.getPreferenceNodes(false);

		for (int i = 0; i < nodes.length; i++) {
			IEclipsePreferences eclipsePreferences = nodes[i];
			if (eclipsePreferences.name().equals(
					Activator.getDefault().getBundle().getSymbolicName())) {
				eclipsePreferences.remove(key);
			}
		}
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
