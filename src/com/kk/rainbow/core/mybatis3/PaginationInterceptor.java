package com.kk.rainbow.core.mybatis3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import com.kk.rainbow.core.manager.Log;

@Intercepts({@org.apache.ibatis.plugin.Signature(type=org.apache.ibatis.executor.statement.StatementHandler.class, method="prepare", args={Connection.class})})
public class PaginationInterceptor
  implements Interceptor
{
  public Object intercept(Invocation paramInvocation)
    throws Throwable
  {
    Log.debug("====>Execute PaginationInterceptor's intercept");
    RoutingStatementHandler localRoutingStatementHandler = (RoutingStatementHandler)paramInvocation.getTarget();
    BoundSql localBoundSql = localRoutingStatementHandler.getBoundSql();
    Page localPage = getParameterPage(localBoundSql);
    if (localPage == null)
    {
      Log.debug("====>query function has not parameter " + Page.class.getName());
      return paramInvocation.proceed();
    }
    int i = getCount((Connection)paramInvocation.getArgs()[0], localBoundSql.getSql().trim());
    if (i == 0)
    {
      Log.debug("====>query function resultset 0");
      PageHelper.remove();
      return paramInvocation.proceed();
    }
    localPage.setTotalCount(i);
    PageHelper.setPage(Page.doPage(localPage));
    MetaObject localMetaObject = MetaObject.forObject(localRoutingStatementHandler);
    Configuration localConfiguration = (Configuration)localMetaObject.getValue("delegate.configuration");
    Dialect.Type localType = null;
    try
    {
      localType = Dialect.Type.valueOf(localConfiguration.getVariables().getProperty("dialect").toUpperCase());
      Log.debug("====>databaseType :" + localType);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      Log.error(localException.getLocalizedMessage());
    }
    if (localType == null)
      throw new RuntimeException("the value of the dialect property in configuration.xml is not defined : " + localConfiguration.getVariables().getProperty("dialect"));
    Object localObject = null;
//    switch (1.$SwitchMap$rainbow$core$mybatis3$Dialect$Type[localType.ordinal()])
    switch (localType.ordinal())
    {
    case 1:
      localObject = new MySql5Dialect();
      break;
    case 2:
      localObject = new OracleDialect();
    }
    String str = (String)localMetaObject.getValue("delegate.boundSql.sql");
    localMetaObject.setValue("delegate.boundSql.sql", ((Dialect)localObject).getLimitString(str, localPage.getOffset(), localPage.getLimit()));
    localMetaObject.setValue("delegate.rowBounds.offset", Integer.valueOf(0));
    localMetaObject.setValue("delegate.rowBounds.limit", Integer.valueOf(2147483647));
    Log.debug("====>PaginationIntercept SQL : " + localBoundSql.getSql());
    return paramInvocation.proceed();
  }

  private Page getParameterPage(BoundSql paramBoundSql)
  {
    Object localObject1 = paramBoundSql.getParameterObject();
    if (localObject1 != null)
      if ((localObject1 instanceof Map))
      {
        Map localMap = (Map)localObject1;
        Iterator localIterator = localMap.values().iterator();
        while (localIterator.hasNext())
        {
          Object localObject2 = localIterator.next();
          if ((localObject2 instanceof Page))
            return (Page)localObject2;
        }
      }
      else if ((paramBoundSql.getParameterObject() instanceof Page))
      {
        return (Page)paramBoundSql.getParameterObject();
      }
    return null;
  }

  private int getCount(Connection paramConnection, String paramString)
    throws SQLException
  {
    Log.debug("====>source sql = " + paramString);
    paramString = MySql5PageHepler.getCountString(paramString);
    Log.debug("====>count sql = " + paramString);
    PreparedStatement localPreparedStatement = paramConnection.prepareStatement(paramString);
    ResultSet localResultSet = localPreparedStatement.executeQuery();
    if (localResultSet.next())
      return localResultSet.getInt(1);
    localResultSet.close();
    paramConnection.close();
    return 0;
  }

  public Object plugin(Object paramObject)
  {
    return Plugin.wrap(paramObject, this);
  }

  public void setProperties(Properties paramProperties)
  {
  }
}