/*
 * (c)2023 PATAPATA Corp. Corp. All Rights Reserved
 *
 * システム名　　：PATAPATA System
 * サブシステム名：コントローラ
 * 機能名　　　　：user_info ユーザ情報テーブルデータを登録・更新・削除するためのコントローラクラス
 * ファイル名　　：RoomDetail.java
 * クラス名　　　：RoomDetail
 * 概要　　　　　：room 部屋情報テーブルデータを登録・更新・削除するためのコントローラクラス
 * バージョン　　：
 *
 * 改版履歴　　　：
 * 2013/03/29 <新規>    新規作成
 *
 */
package jp.swell.controller;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.SystemUserInfoValue;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.patasys.common.util.Validate;
import jp.swell.common.ControllerBase;

/**
 * ：user_info ユーザ情報テーブルデータを登録・更新・削除するためのコントローラクラス
 *
 * @author PATAPATA
 * @version 1.0
 */
public class AnnouncementList extends ControllerBase {
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
        setLoginNeeds(true); // この処理にはログインが必要かどうか
        setHttpNeeds(false); // この処理はhttpでなければならないか
        setHttpsNeeds(false); // この処理はhttps でなければならないか。公開時にはtrueにする
        setUsecache(false); // この処理はクライアントのキャッシュを認めるか
    }

    /**
     * jp.swell.cloudbiz.common.ControllerBase のメソッドをオーバライドする。 ここで、コントローラの処理を記述する.
     * ここで、コントローラの処理を記述する.
     * @throws Exception エラー
     */
    @Override
    public void doActionProcess() throws AtareSysException {
        WebBean bean = getWebBean();

        if ("AnnouncementList".equals(bean.value("form_name"))) {
            bean.trimAllItem();
            if ("search".equals(bean.value("action_cmd"))) {
                bean.setValue("pageNo", "1");
            
            } else if ("next".equals(bean.value("action_cmd"))) {
                bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), 1));
            
            } else if ("jump".equals(bean.value("action_cmd"))) {
       
            } else if ("prior".equals(bean.value("action_cmd"))) {
                bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), -1));
           
            } else if ("sort".equals(bean.value("action_cmd"))) {
             
            } else if ("clear".equals(bean.value("action_cmd"))) {
                formClear();
               
            } else if ("return".equals(bean.value("action_cmd"))) {
                redirect("MenuAdmin.do");
            } else {
                      }
            forward("Announcement.jsp");
        } else if ("AnnouncementDetail".equals(bean.value("form_name")) || "AnnouncementDetail_2".equals(bean.value("form_name"))) {
            setWebBeanFromSerialize(bean.value("search_info"));
            bean = getWebBean();
      
            forward("Announcement.jsp");
        } else {
        	if(bean.value("sort_key").equals("1")) {
        		bean.setValue("sort_key","0");
        	}else {
        		bean.setValue("sort_key","1");
        	}
        	formInit();
         
            forward("Announcement.jsp");
        }
    }

    /**
     * 最初の画面を表示する。.
     *
     * @throws AtareSysException
     */
    private void formInit() throws AtareSysException {
        WebBean bean = getWebBean();
        bean.setValue("lineCount",
                SystemUserInfoValue.getUserInfoValue(getLoginUserId(), "RoomList", "lineCount", "100"));
    }

    /**
     * フィールドをクリアする。.
     */
    private void formClear() throws AtareSysException {
        WebBean bean = getWebBean();
        bean.setValue("list_search_anno_id", "");
        bean.setValue("lineCount", "");
        String search_info = Sup.serialize(bean);
        bean.setValue("search_info", search_info);
    }



    /**
     * ページ番号を加算減算する
     *
     * @param $page_no
     *        現在のページ番号
     * @param $add
     *        加算減算する値
     * @return 結果のページを返す
     */
    private String calcPageNo(String pageNo, int add) {
        int ret;
        if (null == pageNo) {
            pageNo = "1";
        } else if ("".equals(pageNo)) {
            pageNo = "1";
        } else if (!Validate.isInteger(pageNo)) {
            pageNo = "1";
        }
        ret = Integer.parseInt(pageNo);
        ret += add;
        return String.valueOf(ret);
    }
}
