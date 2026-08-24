package jp.swell.controller;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.http.WebBean;
import jp.swell.common.ControllerBase;

public class ApiRoomList extends ControllerBase {

    @Override
    public void doInit() {
        setLoginNeeds(false); // APIの動作確認を簡単にするためfalseに設定
        setHttpNeeds(false);
        setHttpsNeeds(false);
        setUsecache(false);
    }

    @Override
    public void doActionProcess() throws AtareSysException {
     WebBean bean = getWebBean();
     System.out.println("hit");
     forward("ApiRoomList.jsp");
     return;
/*
     if ("ApiRoomList".equals(bean.value("form_name"))) {
         // ★一覧フォームからの操作
         bean.trimAllItem();

         String action = bean.value("action_cmd");
         if ("search".equals(action)) {
         } else {
         }
         forward("ApiRoomList.jsp");
         return;
     }
     */
     
    }
}
