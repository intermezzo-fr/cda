/*!
* Copyright 2002 - 2013 Webdetails, a Pentaho company.  All rights reserved.
* 
* This software was developed by Webdetails and is provided under the terms
* of the Mozilla Public License, Version 2.0, or any later version. You may not use
* this file except in compliance with the license. If you need a copy of the license,
* please go to  http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
*
* Software distributed under the Mozilla Public License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
* the license for the specific language governing your rights and limitations.
*/

package pt.webdetails.cda;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.platform.api.engine.IPluginLifecycleListener;
import org.pentaho.platform.api.engine.PluginLifecycleException;

import pt.webdetails.cda.cache.scheduler.CacheScheduleManager;
import pt.webdetails.cda.utils.PluginHibernateUtil;
import pt.webdetails.cda.utils.Util;
import pt.webdetails.cpf.PentahoPluginEnvironment;
import pt.webdetails.cpf.PluginEnvironment;
import pt.webdetails.cpf.SimpleLifeCycleListener;

/**
 * This class inits Cda plugin within the bi-platform
 * @author gorman
 *
 */
public class CdaLifecycleListener extends SimpleLifeCycleListener implements IPluginLifecycleListener
{

  static Log logger = LogFactory.getLog(CacheScheduleManager.class);


  public void init() throws PluginLifecycleException
  {
    super.init();
//    // boot cda
//    CdaBoot.getInstance().start();
//    
    CdaEngine.init(new PentahoCdaEnvironment());
    PluginHibernateUtil.initialize();
  }


  public void loaded() throws PluginLifecycleException
  {
    super.loaded();
    // TODO: why?
    ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
    try
    {
      Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
      
//      try {
//        CdaEngine.init(new PentahoCdaEnvironment());
//      } catch (InitializationException ie) {
//        throw new PluginLifecycleException("Error initializing CDA Engine", ie);
//      }
      
      // TODO: why?
      CacheScheduleManager.getInstance().coldInit();
    }
    catch (Exception e)
    {
      logger.error(Util.getExceptionDescription(e));
    }
    finally
    {
      Thread.currentThread().setContextClassLoader(contextCL);
    }
  }


  public void unLoaded() throws PluginLifecycleException
  {
  }


  @Override
  public PluginEnvironment getEnvironment() {
    return PentahoPluginEnvironment.getInstance();
  }
}
