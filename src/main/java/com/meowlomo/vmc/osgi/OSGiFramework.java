/**
 * 
 */
package com.meowlomo.vmc.osgi;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * @author 陈琪
 *
 */
public interface OSGiFramework {
	BundleContext initFramework(String frameworkConfig);

	void closeFramework();

	Bundle addBundle(String bundleSrc);

	boolean removeBundle(String bundleName);

	Bundle updateBundle(String bundleName, String bundleSrc);

	boolean stopBundle(String bundleName);

	String doTest(String bundleNames, String jsonParam);
}
