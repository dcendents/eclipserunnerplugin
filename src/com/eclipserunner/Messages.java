package com.eclipserunner;

import org.eclipse.osgi.util.NLS;

/**
 * Eclipse runner property constants.
 * 
 * @author vachacz
 */
public class Messages extends NLS {

	private static final String BUNDLE_NAME = "com.eclipserunner.messages";

	static {
		reloadMessages();
	}

	public static void reloadMessages() {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String Message_runConfiguration;
	public static String Message_runConfigurationTooltip;
	public static String Message_openRunConfigurationsDialog;
	public static String Message_openRunConfigurationsDialogTooltip;

	public static String Message_debugConfiguration;
	public static String Message_debugConfigurationTooltip;
	public static String Message_openDebugConfigurationsDialog;
	public static String Message_openDebugConfigurationsDialogTooltip;
	
}
