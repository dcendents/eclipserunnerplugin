package com.eclipserunner.ui.dnd;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;

import com.eclipserunner.views.RunnerView;

/**
 * Listener for handling drag events.
 * 
 * @author bary
 */
public class RunnerViewDragListener implements DragSourceListener {

	private final RunnerView runnerView;

	public RunnerViewDragListener(RunnerView runnerView) {
		this.runnerView = runnerView;
	}

	public void dragStart(DragSourceEvent event) {
		if (!runnerView.isLaunchConfigurationSelected()) {
			event.doit = false;
		}
	}

	public void dragSetData(DragSourceEvent event) {
		if (runnerView.isLaunchConfigurationSelected()) {
			ILaunchConfiguration launchConfiguration = runnerView.getSelectedLaunchConfiguration();
			String launchConfigurationName = launchConfiguration.getName();

			if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
				event.data = launchConfigurationName;
			}
		}
	}

	public void dragFinished(DragSourceEvent event) {
		runnerView.modelChanged();
	}

}
