package com.kk.rainbow.core.listener;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class ScheduleServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  Timer timer = null;

  public void destroy()
  {
    super.destroy();
    this.timer.cancel();
  }

  public void init()
    throws ServletException
  {
    String str1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String str2 = getInitParameter("start");
    String str3 = str1 + str2;
    String str4 = getInitParameter("period");
    System.out.println(">>>>>>>>>>>>>>" + str3 + ":" + str4 + "<<<<<<<<<<<<<<<<");
    long l1 = System.currentTimeMillis();
    long l2 = 0L;
    try
    {
      l2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").parse(str3).getTime();
    }
    catch (ParseException localParseException)
    {
      localParseException.printStackTrace();
    }
    long l3 = l2 - l1;
    System.out.println(">>>>>>>>>>>>>>" + l2 + "--" + l1 + "==" + l3 + "<<<<<<<<<<<<<<<<");
    long l4 = Long.parseLong(str4);
    Timer localTimer = new Timer();
  }

  public static void main(String[] paramArrayOfString)
  {
    try
    {
      String str1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
      String str2 = "15:03:00";
      String str3 = str1 + " " + str2;
      String str4 = "5000";
      System.out.println(">>>>>>>>>>>>>>" + str3 + ":" + str4 + "<<<<<<<<<<<<<<<<");
      long l1 = System.currentTimeMillis();
      long l2 = 0L;
      l2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").parse(str3).getTime();
      long l3 = l2 - l1;
      System.out.println(">>>>>>>>>>>>>>" + l2 + ":--" + l1 + "==" + l3 + "<<<<<<<<<<<<<<<<");
      long l4 = Long.parseLong(str4);
      Timer localTimer = new Timer();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
}