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
	// ContextListenerServlet.java (修正案)
    public void contextInitialized(ServletContextEvent arg0)
    {
        try
        {
            PaSystemProperties.initialize(arg0.getServletContext());
            
            // 例外捕捉のためfreeConnectionをtry-catchで囲む
            try {
                // 問題の処理
                DbBase.freeConnection();
            } catch (Exception e) {
                // DbBaseのエラーを詳細にログ出力し、Tomcat起動を失敗させる
                System.err.println("DbBase.freeConnection()で例外が発生しました:");
                e.printStackTrace();
                throw e; // ランタイム例外として再スローし、起動失敗を確実にする
            }
            
        } catch (AtareSysException e)
        {
            e.printStackTrace();
            // AtareSysExceptionも起動失敗として扱う
            throw new RuntimeException("システムプロパティ初期化失敗", e); 
        } catch (RuntimeException e) {
            // freeConnectionから再スローされたRuntimeExceptionを再度スロー
            throw e; 
        } catch (Throwable t) {
            // 予期せぬエラーもキャッチ
            throw new RuntimeException("リスナー初期化中に予期せぬエラー", t);
        }
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
