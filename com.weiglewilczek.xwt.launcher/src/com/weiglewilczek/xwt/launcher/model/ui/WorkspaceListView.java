package com.weiglewilczek.xwt.launcher.model.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.e4.xwt.DefaultLoadingContext;
import org.eclipse.e4.xwt.XWT;
import org.eclipse.e4.xwt.XWTLoader;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

import com.weiglewilczek.eclipse.utils.ui.jface.viewers.AbstractColumnViewerSorter;
import com.weiglewilczek.xwt.launcher.listener.IListener;
import com.weiglewilczek.xwt.launcher.listener.ListenerType;
import com.weiglewilczek.xwt.launcher.managers.EclipseInstallationManager;
import com.weiglewilczek.xwt.launcher.managers.JavaInstallationManager;
import com.weiglewilczek.xwt.launcher.managers.LaunchConfigurationManager;
import com.weiglewilczek.xwt.launcher.model.EclipseInstallation;
import com.weiglewilczek.xwt.launcher.model.JavaInstallation;
import com.weiglewilczek.xwt.launcher.model.LaunchConfiguration;
import com.weiglewilczek.xwt.launcher.model.ModelElement;
import com.weiglewilczek.xwt.launcher.model.WorkspaceListDataContext;

public class WorkspaceListView extends ViewPart implements IListener {

	private WorkspaceListDataContext dataContext;

	private Composite parent;
	private TableViewer workspaceList;

	public void buildUI() {
		try {
			// set classloader, or ctrl-namespace-classes can't be loaded!!!
			DefaultLoadingContext loadingContext = new DefaultLoadingContext();
			loadingContext.setClassLoader(this.getClass().getClassLoader());
			XWT.setLoadingContext(loadingContext);

			dataContext = new WorkspaceListDataContext();
			Map<String, Object> newOptions = new HashMap<String, Object>();
			List<LaunchConfiguration> launchConfigurations = LaunchConfigurationManager
					.getInstance().enumerateAll();
			WritableList input = new WritableList();
			input.addAll(launchConfigurations);
			dataContext.setLaunchConfigurations(input);
			newOptions.put(XWTLoader.DATACONTEXT_PROPERTY, dataContext);
			newOptions.put(XWTLoader.CONTAINER_PROPERTY, parent);

			Composite area = (Composite) XWT.loadWithOptions(this.getClass()
					.getResource("WorkspaceList.xwt"), newOptions);
			area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			Object table = XWT.findElementByName(area, "workspaceList");
			Object configurationColumn = XWT.findElementByName(area,
					"configurationColumn");
			if (table != null && table instanceof TableViewer) {
				workspaceList = (TableViewer) table;
				getSite().setSelectionProvider(workspaceList);

				workspaceList
						.addDoubleClickListener(new IDoubleClickListener() {

							@Override
							public void doubleClick(DoubleClickEvent event) {
								IHandlerService hs = (IHandlerService) getSite()
										.getService(IHandlerService.class);
								try {
									hs.executeCommand(
											"com.weiglewilczek.xwt.launcher.commands.Launch",
											null);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});

				if (configurationColumn instanceof TableViewerColumn) {
					AbstractColumnViewerSorter<LaunchConfiguration> sorter = new AbstractColumnViewerSorter<LaunchConfiguration>(
							workspaceList,
							(TableViewerColumn) configurationColumn) {

						@Override
						public int doCompare(
								LaunchConfiguration launchConfiguration1,
								LaunchConfiguration launchConfiguration2) {
							return launchConfiguration1.getName().compareTo(
									launchConfiguration2.getName());
						}
					};
					sorter.setSorter(sorter, AbstractColumnViewerSorter.ASC);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		LaunchConfigurationManager.getInstance().addListener(this);
		EclipseInstallationManager.getInstance().addListener(this);
		JavaInstallationManager.getInstance().addListener(this);
		buildUI();
	}

	@Override
	public void setFocus() {
		if (workspaceList != null) {
			workspaceList.getTable().setFocus();
		}
	}

	@Override
	public void handle(ListenerType type, ModelElement<?> object) {
		if (object instanceof LaunchConfiguration) {
			switch (type) {
			case CREATE:
				dataContext.getLaunchConfigurations().add(object);
				workspaceList.refresh();
				break;
			case UPDATE:
				workspaceList.refresh(true);
				break;
			case DELETE:
				dataContext.getLaunchConfigurations().remove(object);
				workspaceList.refresh();
				break;

			default:
				break;
			}
		} else if (object instanceof EclipseInstallation
				&& type.equals(ListenerType.UPDATE)) {

			EclipseInstallation eclipse = (EclipseInstallation) object;

			for (Object writableObject : dataContext.getLaunchConfigurations()) {
				LaunchConfiguration launchConfiguration = (LaunchConfiguration) writableObject;
				if (launchConfiguration.getEclipse().equals(eclipse)) {
					launchConfiguration.setEclipse(eclipse);
				}
			}

			workspaceList.refresh(true);
		} else if (object instanceof JavaInstallation
				&& type.equals(ListenerType.UPDATE)) {
			JavaInstallation java = (JavaInstallation) object;

			for (Object writableObject : dataContext.getLaunchConfigurations()) {
				LaunchConfiguration launchConfiguration = (LaunchConfiguration) writableObject;
				if (launchConfiguration.getJava().equals(java)) {
					launchConfiguration.setJava(java);
				}
			}

			workspaceList.refresh(true);
		}
	}

}
