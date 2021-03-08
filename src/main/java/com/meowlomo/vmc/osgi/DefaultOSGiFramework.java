/**
 * 
 */
package com.meowlomo.vmc.osgi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meowlomo.vmc.util.MOCommonUtil;

/**
 * @author 陈琪
 *
 */
public class DefaultOSGiFramework implements OSGiFramework {
	
	private static BundleContext _context;
	private static HashSet<String> _bundleNames = new HashSet<String>();
	private static Map<String, Long> _bundleNameId = new HashMap<String, Long>(100);
	private static final Logger logger = LoggerFactory.getLogger(DefaultOSGiFramework.class);
	/**
	 * 
	 */
	public DefaultOSGiFramework() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public BundleContext initFramework(String frameworkConfig) {
		String[] osgiArgs;
		if (null != frameworkConfig && !frameworkConfig.isEmpty()){
			osgiArgs = new String[]{"-console", "true", "-noExit", "-noShutdown", "-configuration", frameworkConfig};
		}else{
			osgiArgs = new String[]{"-console", "true", "-noExit", "-noShutdown"};
		}
		BundleContext bc = null;
		try {
			EclipseStarter.run(osgiArgs, null);
			bc = EclipseStarter.getSystemBundleContext();
		} catch (Exception e) {
			bc = null;
			e.printStackTrace();
		}

		if (null == bc)
			logger.info("OSGi Framework init failed.");
		else{
			_context = EclipseStarter.getSystemBundleContext();
		}
		
		MOCommonUtil.sleepSeconds(3);
		
		logger.info(null != bc ? "[OK] OSGi init OK\n" + getBundleNames(false, true) : "[ERROR] OSGi init failed.");
		return bc;
	}
	
	public static BundleContext getContext(){
		return _context;
	}
	
	public void closeFramework(){
		try {
			EclipseStarter.shutdown();
		} catch (Exception e) {
			logger.info("Exception occured where closing framework.");
		}
		_context = null;
	}
	
	public Bundle addBundle(String bundleSrc){
		Bundle bd = null;
		if (!isValideBundleUrl(bundleSrc))
			return bd;
		
		try {
			bd = _context.installBundle(bundleSrc);
			if (null != bd){
				int bdState = bd.getState();
				Integer[] sts = {Bundle.INSTALLED, Bundle.RESOLVED, Bundle.STARTING, Bundle.STOPPING};
				List<Integer> states = Arrays.asList(sts);
				if (states.contains(bdState)){
					bd.start();
					bdState = bd.getState();
					if (Bundle.ACTIVE == bdState){
						_bundleNameId.put(bd.getSymbolicName(), bd.getBundleId());
						_bundleNames.add(bd.getSymbolicName());
					}else{
						if (removeBundle(bd.getSymbolicName()))
							bd = null;
					}
				}else{
					if (removeBundle(bd.getSymbolicName()))
						bd = null;
				}
			}
		} catch (Exception e) {
			System.err.println(e.getClass());
			bd = null;
		}
		return bd;
	}
	
	public boolean removeBundle(String bundleName){
		//remove an non exist bundle
		if (!isInstalledBundleName(bundleName)) return true;
		
		//remove an non-exist bundle
		Long bundleId = _bundleNameId.get(bundleName);
		Bundle targetBundle = _context.getBundle(bundleId);
		if (null == targetBundle) return true;
		
		boolean removeResult = true;
		try {
			targetBundle.uninstall();
		} catch (Exception e) {
			removeResult = false;
		}
		
		if (removeResult){
			_bundleNames.remove(bundleName);
			_bundleNameId.remove(bundleName);
		}
		return removeResult;
	}
	
	public Bundle updateBundle(String bundleName, String bundleSrc){
		return !removeBundle(bundleName) ? null : addBundle(bundleSrc);
	}
	
	public boolean stopBundle(String bundleName){
		boolean bResult = true;
		if (_bundleNames.contains(bundleName) && _bundleNameId.containsKey(bundleName)){
			long bundleId = _bundleNameId.get(bundleName);
			Bundle bd = _context.getBundle(bundleId);
			if (null != bd){
				try {
					bd.stop(0);
				} catch (BundleException e) {
					// TODO Auto-generated catch block
					bResult = false;
				}
			}
		}
		return bResult;
	}
	
	public String doTest(String bundleNames, String jsonParam){
		return "Test OK ... Should be transfer into bundle";
	}

	public Object getBundleNames(boolean checkBundleVersion, boolean inJsonFormat){
		
		//1.获取bundle SymbolicName 与 Version
		
		//2.针对SymbolicName比较version
		
		//3.第2条不匹配的不通过.最终未匹配到bundle的也不通过
		
		Bundle[] bundles = _context.getBundles();
		_bundleNames.clear();
		_bundleNameId.clear();
		
		if (inJsonFormat) {
			ArrayNode jsonResult = JsonNodeFactory.instance.arrayNode();
			for(Bundle bundle : bundles) {
				ObjectNode bd = JsonNodeFactory.instance.objectNode();
				bd.put("id", bundle.getBundleId());
				bd.put("state", OSGiUtil.getStateName(bundle.getState()));
				bd.put("version", bundle.getVersion().toString());
				bd.put("symbolicName", bundle.getSymbolicName());
				jsonResult.add(bd);
				_bundleNames.add(bundle.getSymbolicName());
				_bundleNameId.put(bundle.getSymbolicName(), bundle.getBundleId());
			}
			return jsonResult;
		} else {
			String bdNames = "";
			for(Bundle bundle : bundles){
				bdNames += String.format("\t%d\t|     %s\t|\t%s   \t\t|  %s\r\n"
						, bundle.getBundleId()
						, OSGiUtil.getStateName(bundle.getState())
						, (bundle.getVersion().toString().length()  > 16 ? bundle.getVersion() : (bundle.getVersion() + "\t"))
						, bundle.getSymbolicName());
				_bundleNames.add(bundle.getSymbolicName());
				_bundleNameId.put(bundle.getSymbolicName(), bundle.getBundleId());
			}
			return bdNames;
		}
	}
	
	private boolean isValideBundleUrl(String bundleSrc){
		return true;
	}
	
	private boolean isInstalledBundleName(String bundleName) {
		return _bundleNames.contains(bundleName) && _bundleNameId.containsKey(bundleName);
	}
}
