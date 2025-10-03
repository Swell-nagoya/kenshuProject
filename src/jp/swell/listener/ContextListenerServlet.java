package jp.swell.listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.DbIsolate;
import jp.swell.common.PaSystemProperties;
/**
 * WebアプリケーションのServletコンテキストに変更があったことを通知するためのイベントクラス
 *
 * @author 2023 PATAPATA Corp. Corp.
 * @version 1.0
 */
public class ContextListenerServlet implements ServletContextListener
{

    /**
     * コンテキストの初期化
     * @param arg0
     */
    public void contextInitialized(ServletContextEvent arg0)
    {
        try
        {
            PaSystemProperties.initialize(arg0.getServletContext());
        } catch (AtareSysException e)
        {
            e.printStackTrace();
            return;
        }
        DbBase.freeConnection();
    }

    /**
     * コンテキストの削除
     * @param arg0
     */
    public void contextDestroyed(ServletContextEvent arg0)
    {
        DbBase.closeAllConnections();
        DbIsolate.closeAllConnections();
        // Driverのメモリ開放
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        try {
            while (drivers.hasMoreElements()) {
                DriverManager.deregisterDriver(drivers.nextElement());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
