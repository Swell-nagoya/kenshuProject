package jp.swell.controller;

import java.util.ArrayList;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.http.WebBean;
import jp.swell.common.ControllerBase;
import jp.swell.dao.ReserveDao;

public class Calendar extends ControllerBase
{
    /**
     * jp.patasys.alumni.controller.HttpServlet のメソッドをオーバライドする。
     * オーバライドしない場合は、デフォルトが設定される。.
     * この処理にはログインが必要かどうか デフォルト true.
     * この処理はhttpでなければならないか デフォルト false.
     * この処理はhttps でなければならないか デフォルト false.
     * この処理はクライアントのキャッシュを認めるか デフォルト false. 等を設定する。
     * doActionの前に呼ばれる。
     */
    @Override
    public void doInit() {
        setLoginNeeds(true); // ログインが必要
        setHttpNeeds(false); 
        setHttpsNeeds(false); 
        setUsecache(false);

        // ログインユーザーIDを取得してログ出力
        String loggedInUserId = getLoginUserId();
        System.out.println("ログイン中のユーザー: " + loggedInUserId);
    }
    
    @Override
    public void doActionProcess() throws AtareSysException
    {
        WebBean bean = getWebBean();

        if ("Calendar".equals(bean.value("form_name")))
        {
            if ("return".equals(bean.value("action_cmd"))) 
            {
                forward("MenuAdmin.do");
                return;
            }
        }
        else
        {
            ReserveDao reserveDao = new ReserveDao();
            ArrayList<ReserveDao> reserves = reserveDao.getCalendarReserves();
            bean.setValue("reserves", reserves);
            forward("Calendar.jsp");
        }
    }

}