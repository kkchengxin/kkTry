package com.kk.rainbow.core.manager;

import com.kk.rainbow.core.manager.module.LogModule;
import com.kk.rainbow.core.manager.module.ProcessModule;

public class ModulesManager
{
  private static ModulesManager configManager = new ModulesManager();

  public static ModulesManager getInstance()
  {
    return configManager == null ? new ModulesManager() : configManager;
  }

  public boolean isOpenLog()
  {
    return Boolean.valueOf(LogModule.getInstance().getIsOpen()).booleanValue();
  }

  public boolean isOpenProcess()
  {
    return Boolean.valueOf(ProcessModule.getInstance().getIsOpen()).booleanValue();
  }
}