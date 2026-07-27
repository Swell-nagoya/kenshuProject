
/*
 * (c)2023 PATAPATA Corp. Corp. All Rights Reserved
 *
 * システム名　　：PATAPATA System
 * サブシステム名：コントローラ
 * 機能名　　　　：user_info ユーザ情報テーブルデータをLIST表示するためのコントローラクラス
 * ファイル名　　：UserInfoList.java
 * クラス名　　　：UserInfoList
 * 概要　　　　　：user_info ユーザ情報テーブルデータをLIST表示するためのコントローラクラス
 * バージョン　　：
 *
 * 改版履歴　　　：
 * 2013/03/29 <新規>    新規作成
 *
 */
package jp.swell.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.SystemUserInfoValue;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.patasys.common.util.Validate;
import jp.swell.common.ControllerBase;
import jp.swell.dao.UserInfoDao;
import jp.swell.user.UserLoginInfo;

/**
 * ：user_info ユーザ情報テーブルデータをLIST表示するためのコントローラクラス
 *
 * @author PATAPATA
 * @version 1.0
 */
public class ViewUserList extends ControllerBase
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
    public void doInit()
    {
        setLoginNeeds(true); // この処理にはログインが必要かどうか
        setHttpNeeds(false); // この処理はhttpでなければならないか
        setHttpsNeeds(false); // この処理はhttps でなければならないか。公開時にはtrueにする
        setUsecache(false); // この処理はクライアントのキャッシュを認めるか
    }
    /**
     * jp.patasys.cloudbiz.common.ControllerBase のメソッドをオーバライドする。
     * ここで、コントローラの処理を記述する.
     *
     * @throws Exception エラー
     */
    @Override
    public void doActionProcess() throws AtareSysException
    {
        WebBean bean = getWebBean();
        if ("ViewUserList".equals(bean.value("form_name")))
        {
            bean.setValue("user_info_id", getLoginUserId());
            bean.trimAllItem();
            adminSet();
            if ("search".equals(bean.value("action_cmd")))
            {
            	
                bean.setValue("pageNo", "1");
                searchList();
                forward("ViewUserList.jsp");
            }
            else if ("next".equals(bean.value("action_cmd")))
            {
                bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), 1));
                searchList();
                forward("ViewUserList.jsp");
            }
            else if ("jump".equals(bean.value("action_cmd")))
            {
                searchList();
                forward("ViewUserList.jsp");
            }
            else if ("prior".equals(bean.value("action_cmd")))
            {
                bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), -1));
                searchList();
                forward("ViewUserList.jsp");
            }
            else if ("sort".equals(bean.value("action_cmd")))
            {
             searchList();
             forward("ViewUserList.jsp");
            }
            else if ("clear".equals(bean.value("action_cmd")))
            {
                formClear();
                searchList();
                forward("ViewUserList.jsp");
            }
            else if ("return".equals(bean.value("action_cmd")))
            {
                redirect("MenuAdmin.do");
                return;
            }
            else
            {
                searchList();
                forward("ViewUserList.jsp");
            }
        }
        else if ("UserInfoDetail".equals(bean.value("form_name")))
        {
            adminSet();
            searchList();
            forward("ViewUserList.jsp");
        }
        else
        {
           	adminSet();
            formInit();
            searchList();
            forward("ViewUserList.jsp");
        }
    }


    /**
     * 最初の画面を表示する。.
     *
     * @throws AtareSysException
     */
    private void adminSet() throws AtareSysException
    {
        WebBean bean = getWebBean();
        UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();

        if (userLoginInfo != null && userLoginInfo.isSystemManager()) {
            bean.setValue("admin", "1");
        } else {
            bean.setValue("admin", "0");
        }
        
    }
    /**
     * 最初の画面を表示する。.
     *
     * @throws AtareSysException
     */
    private void formInit() throws AtareSysException
    {
        WebBean bean = getWebBean();
        bean.setValue("sort_key", "full_name_kana"); /* 初回のソートキーを入れる */
        bean.setValue("sort_order", "asc");
        bean.setValue("lineCount", SystemUserInfoValue.getUserInfoValue(getLoginUserId(), "ViewUserList", "lineCount", "100"));
    }

    /**
     * フィールドをクリアする。.
     */
    private void formClear() throws AtareSysException
    {
        WebBean bean = getWebBean();
        bean.setValue("list_search_full_name", "");
        bean.setValue("list_search_full_name_kana", "");
        bean.setValue("list_search_memail", "");
        bean.setValue("list_search_state", "");
        bean.setValue("list_search_conditions", "");
        bean.setValue("lineCount", "");
        String search_info = Sup.serialize(bean);
        bean.setValue("search_info", search_info);
    }

    /**
     * 入力チェックを行う。.
     *
     * @return errors HashMapにエラーフィールドをキーとしてエラーメッセージを返す
     */
    private HashMap<String, String> inputCheck()
    {
        WebBean bean = getWebBean();
        HashMap<String, String> errors = bean.getItemErrors();
        if (bean.value("list_search_full_name").length() > 0)
        {
            if (100 < bean.value("list_search_full_name").length())
            {
                errors.put("list_search_full_name", "氏名の入力内容が長すぎます。");
            }
        }
        if (bean.value("list_search_full_name_kana").length() > 0)
        {
            if (100 < bean.value("list_search_full_name_kana").length())
            {
                errors.put("list_search_full_name_kana", "氏名よみの入力内容が長すぎます。");
            }
        }
        return errors;
    }

    /**
     * 検索を行いbeanに格納する。.
     */
    private void searchList() throws AtareSysException
    {
        WebBean bean = getWebBean();
        HashMap<String, String> errors;

        errors = inputCheck();
        if (errors.size() > 0)
        {
            bean.setValue("errors", errors);
            return;
        }
        LinkedHashMap<String, String> sortKey = sortKey();
        UserInfoDao dao = new UserInfoDao();
        dao.setSearchName(bean.value("list_search_full_name"));
        dao.setMemail(bean.value("list_search_memail"));
        dao.setSearchConditions(bean.value("list_search_conditions"));
        
        String stateStr = bean.value("list_search_state");
        dao.setStateFlg((stateStr == null || stateStr.isEmpty()) ? 1 : Integer.parseInt(stateStr));
        
        DaoPageInfo daoPageInfo = new DaoPageInfo();
        if (!Validate.isInteger(bean.value("lineCount")))
        {
            bean.setValue("lineCount", "20");
        }
        daoPageInfo.setLineCount(Integer.parseInt(bean.value("lineCount")));
        SystemUserInfoValue.setUserInfoValue(getLoginUserId(), "ViewUserList", "lineCount", bean.value("lineCount"));
        if (!Validate.isInteger(bean.value("pageNo")))
        {
            daoPageInfo.setPageNo(1);
        }
        else
        {
            daoPageInfo.setPageNo(Integer.parseInt(bean.value("pageNo")));
        }
        
        ArrayList<UserInfoDao> listData = UserInfoDao.dbSelectList(dao, sortKey, daoPageInfo);

        ArrayList<String> hitUserIds = new ArrayList<String>();
        if (listData != null) {
	
         for (UserInfoDao rowDao : listData) {
            if (rowDao.getUserInfoId() != null) {
              hitUserIds.add(rowDao.getUserInfoId());
            }
          }
        }

        String joinedIds = String.join(",", hitUserIds);
        String serializedData  = Sup.serialize(joinedIds);
        
     System.out.println(listData);
     dbStateEdit();

        //bean.setValue("state_flg_all",joinedIds);
        bean.setValue("state_flg_all", serializedData);



        bean.setValue("lineCount", daoPageInfo.getLineCount());
        bean.setValue("pageNo", daoPageInfo.getPageNo());
        bean.setValue("recordCount", daoPageInfo.getRecordCount());
        bean.setValue("maxPageNo", daoPageInfo.getMaxPageNo());

        bean.getWebValues().remove("search_info");
        String search_info = Sup.serialize(bean);
        bean.setValue("search_info", search_info);
        bean.setValue("list", listData);
    }


    public void dbStateEdit() throws AtareSysException
    {

    	WebBean bean = getWebBean();
    	UserInfoDao dao = new UserInfoDao();

    	String[] listStateFlgs = getRequest().getParameterValues("list_state_flg");
    	getRequest().setAttribute("checkedFlgs", listStateFlgs);

    	String state_flg_all_text = bean.value("state_flg_all");
    	state_flg_all_text = (String) Sup.deserialize(state_flg_all_text);

//    	 【修正点】ここで null で初期化しておきます
    	String[] state_flg_all_array = null; 

    	if (state_flg_all_text != null && !state_flg_all_text.equals("")) {
    	    state_flg_all_array = state_flg_all_text.split(",");
    	}

    	System.out.println(state_flg_all_text);
    	System.out.println(state_flg_all_array);
    	try {
    	    DbBase.dbBeginTran();
    	    
    	    // 画面表示されている利用停止の値をすべてリセット「1」.
    	    if (state_flg_all_array != null) {
    	       for (int z = 0; z < state_flg_all_array.length; z++) {
    	        String userInfoId = state_flg_all_array[z];
    	        
    	        System.out.println(userInfoId);
    	        dao.dbUpdateStateFlg(userInfoId,"1");
    	       }
    	    }
    	    // 画面表示されている利用停止の値でチェックが入っているものは「8」.
    	    if (listStateFlgs != null) {
    	       for (int i = 0; i < listStateFlgs.length; i++) {
    	         String userInfoId = listStateFlgs[i];
    	         dao.dbUpdateStateFlg(userInfoId,"8");
    	       }
    	     }
    	    DbBase.dbCommitTran();
    	   // redirect("ViewUserList.do");
    	} catch (Exception e) {
    	    DbBase.dbRollbackTran();
    	   // forward("ViewUserList.jsp");
    	}
        
    }
    
    /**
     * ソート順番を求める
     *
     * @return ソート順を格納した配列を返す
     */
    private LinkedHashMap<String, String> sortKey()
    {
        WebBean bean = getWebBean();
        String key = "";
        LinkedHashMap<String, String> sort_key = new LinkedHashMap<String, String>(); /* この配列にソートキーとソートオーダーを入れる */
        if (bean.value("sort_key").length() == 0 && bean.value("sort_key_old").length() == 0) return null;
        
        if (bean.value("sort_key_old").length() > 0)
        {
            if (bean.value("sort_key").length() > 0)
            {
                if (bean.value("sort_key").equals(bean.value("sort_key_old")))
                {
                    // 同一ソートキー（フリップフロップ）
                    key = bean.value("sort_key_old");
                    if ("desc".equals(bean.value("sort_order")))
                    {
                        sort_key.put(key, "asc");
                    }
                    else
                    {
                        sort_key.put(key, "desc");
                    }
                }
                else
                {
                    // 新たなソートキー
                    key = bean.value("sort_key");
                    sort_key.put(key, "asc");
                }
            }
            else
            {
                // 引き継ぎ
                key = bean.value("sort_key_old");
                if ("asc".equals(bean.value("sort_order")))
                {
                    sort_key.put(key, "asc");
                }
                else
                {
                    sort_key.put(key, "desc");
                }
            }
        }
        else
        {
            // 初期値
            key = bean.value("sort_key");
            if ("asc".equals(bean.value("sort_order")))
            {
                sort_key.put(key, "asc");
            }
            else
            {
                sort_key.put(key, "desc");
            }
        }
        bean.setValue("sort_key", "");
        bean.setValue("sort_key_old", key);
        bean.setValue("sort_order", sort_key.get(key));
        return sort_key;
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
    private String calcPageNo(String pageNo, int add)
    {
        int ret;
        if (null == pageNo)
        {
            pageNo = "1";
        }
        else if ("".equals(pageNo))
        {
            pageNo = "1";
        }
        else if (!Validate.isInteger(pageNo))
        {
            pageNo = "1";
        }
        ret = Integer.parseInt(pageNo);
        ret += add;
        return String.valueOf(ret);
    }
}
