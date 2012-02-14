package com.weiglewilczek.xwt.launcher.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;
import com.weiglewilczek.xwt.launcher.util.Program;

public class Launch extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil
				.getCurrentSelection(event);
		LaunchConfiguration selectedConfiguration = (LaunchConfiguration) selection
				.getFirstElement();
		runLaunchConfiguration(selectedConfiguration);

		return null;
	}

	private void runLaunchConfiguration(LaunchConfiguration launchConfiguration) {
		List<String> commands = new ArrayList<String>();
		if (Platform.getOS().equals(Platform.OS_MACOSX)) {
			String executableName = new File(launchConfiguration.getEclipse()
					.getPathToExecutable()).getName();
			executableName = executableName.substring(0,
					executableName.indexOf("."));
			commands.add(launchConfiguration.getEclipse().getPathToExecutable()
					+ "/Contents/MacOS/" + executableName);

			if (launchConfiguration.getJava() != null) {
				commands.add("-vm");
				commands.add(launchConfiguration.getJava()
						.getPathToExecutable());
			}

			if (launchConfiguration.getWorkspacePath() != null
					&& launchConfiguration.getWorkspacePath().length() > 0) {
				commands.add("-data");
				commands.add(launchConfiguration.getWorkspacePath());
			}

			if (launchConfiguration.getEclipseArgs() != null
					&& launchConfiguration.getEclipseArgs().length() > 0) {
				commands.add(launchConfiguration.getEclipseArgs());
			}

			commands.add("-showlocation");

			if (launchConfiguration.getVmArgs() != null
					&& launchConfiguration.getVmArgs().length() > 0) {
				commands.add("-vmargs " + launchConfiguration.getVmArgs());
			}
		}

		if (commands.size() > 0) {
			new Program().execute(commands.toArray(new String[0]));
		}
	}

}
