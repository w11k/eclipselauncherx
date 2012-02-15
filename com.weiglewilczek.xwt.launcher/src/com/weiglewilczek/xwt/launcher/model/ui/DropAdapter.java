package com.weiglewilczek.xwt.launcher.model.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;

import com.weiglewilczek.xwt.launcher.managers.GroupManager;
import com.weiglewilczek.xwt.launcher.managers.LaunchConfigurationManager;
import com.weiglewilczek.xwt.launcher.model.Group;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;
import com.weiglewilczek.xwt.launcher.model.ObservableGroup;

public class DropAdapter extends ViewerDropAdapter {

	public DropAdapter(TreeViewer viewer) {
		super(viewer);

		setFeedbackEnabled(true);
		setScrollExpandEnabled(true);
	}

	@Override
	public boolean performDrop(Object data) {
		if (data instanceof String) {
			String drop = (String) data;
			String[] launchConfigIds = drop.split(",");
			List<LaunchConfiguration> dragSource = new ArrayList<LaunchConfiguration>();
			for (String string : launchConfigIds) {
				if (string.length() > 0) {
					Long id = Long.parseLong(string);
					LaunchConfiguration configuration = LaunchConfigurationManager
							.getInstance().get(id);
					dragSource.add(configuration);
				}
			}

			try {
				Group targetGroup = null;
				if (getCurrentTarget() instanceof ObservableGroup) {
					targetGroup = ((ObservableGroup) getCurrentTarget())
							.getGroup();
				} else if (getCurrentTarget() instanceof LaunchConfiguration) {
					LaunchConfiguration targetConfiguration = (LaunchConfiguration) getCurrentTarget();
					targetGroup = GroupManager.getInstance()
							.getGroupForConfiguration(targetConfiguration);
				}

				if (targetGroup != null) {
					for (LaunchConfiguration configuration : dragSource) {
						if (!targetGroup.equals(GroupManager.getInstance()
								.getGroupForConfiguration(configuration))) {
							Group group = GroupManager.getInstance()
									.getGroupForConfiguration(configuration);
							group.removeConfiguration(configuration);
							GroupManager.getInstance().update(group);

							targetGroup.addConfiguration(configuration);
							GroupManager.getInstance().update(targetGroup);

							// refresh second time at last, so the draged
							// config, that is not stored but calculated!, is
							// removed from group in ui
							if (group.getId() == -1) {
								GroupManager.getInstance().update(group);
							}
						}
					}

					return true;
				}
			} catch (Exception e) {
				// TODO handle
				e.printStackTrace();
			}
		}

		return false;
	}

	@Override
	public boolean validateDrop(Object target, int operation, TransferData type) {
		return true;
	}
}
