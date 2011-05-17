package com.eclipserunner.model;

import java.util.List;

/**
 * Interface for tree elements supporting droppable.
 *
 * @author tonyq
 */
public interface IDroppable {

	public boolean validateDrop(int currentLocation) ;

	public boolean performDrop(List<ILaunchNode> launchNodesToMove);
}
