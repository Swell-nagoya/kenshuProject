
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
            bean.trimAllItem();
            if ("search".equals(bean.value("action_cmd")))
            {
                bean.setValue("pageNo", "1");
                searchList();
            }
            else if ("next".equals(bean.value("action_cmd")))
            {
                bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), 1));
                searchList();
            }
            else if ("jump".equals(bean.value("action_cmd")))
            {
                searchList();
            }
            else if ("prior".equals(bean.value("action_cmd")))
            {
                bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), -1));
                searchList();
            }
            else if ("sort".equals(bean.value("action_cmd")))
            {
                searchList();
            }
            else if ("clear".equals(bean.value("action_cmd")))
            {
                formClear();
                searchList();
            }
            else if ("return".equals(bean.value("action_cmd")))
            {
                redirect("MenuAdmin.do");
                return;
            }
            else if ("bulk_update".equals(bean.value("action_cmd")))
            {
                if (getSelectedUserInfoIds().length == 0)
                {
                    bean.setError("対象ユーザーを選択してください。");
                    searchList();
                }
                else
                {
                    bulkUpdateAdmin();
                    return;
                }
            }
            else if ("bulk_delete".equals(bean.value("action_cmd")))
            {
                if (getSelectedUserInfoIds().length == 0)
                {
                    bean.setError("対象ユーザーを選択してください。");
                    searchList();
                }
                else
                {
                    bulkDelete();
                    return;
                }
            }
            else
            {
                searchList();
            }
            forward("ViewUserList.jsp");
        }
        else if ("UserInfoDetail_1".equals(bean.value("form_name")) || "UserInfoDetail_2".equals(bean.value("form_name")) || "UserInfoDetail_3".equals(bean.value("form_name")))
        {
            setWebBeanFromSerialize(bean.value("search_info"));
            bean = getWebBean();
            searchList();
            forward("ViewUserList.jsp");
        }
        else
        {
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
    private void formInit() throws AtareSysException
    {
        WebBean bean = getWebBean();
        bean.setValue("sort_key", "full_name_kana"); /* 初回のソートキーを入れる */
        bean.setValue("sort_order", "asc");
        bean.setValue("search_mode", "and"); /* 検索条件の結合方法の初期値 */
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
        bean.setValue("list_search_admin", "");
        bean.setValue("list_search_status", "");
        bean.setValue("sort_key", "full_name_kana"); /* 初回のソートキーを入れる */
        bean.setValue("sort_order", "asc");
        bean.setValue("search_mode", "and");
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
        CommonDoActionProcess.checkMaxLength(errors, "list_search_full_name", bean.value("list_search_full_name"), 100, "氏名");
        CommonDoActionProcess.checkMaxLength(errors, "list_search_full_name_kana", bean.value("list_search_full_name_kana"), 100, "氏名よみ");
        CommonDoActionProcess.checkMaxLength(errors, "list_search_memail", bean.value("list_search_memail"), 100, "メールアドレス");
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
        dao.setSearchMemail(bean.value("list_search_memail"));
        dao.setSearchAdmin(bean.value("list_search_admin"));
        dao.setSearchStatus(bean.value("list_search_status"));
        dao.setSearchMode(bean.value("search_mode"));

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
        bean.setValue("lineCount", daoPageInfo.getLineCount());
        bean.setValue("pageNo", daoPageInfo.getPageNo());
        bean.setValue("recordCount", daoPageInfo.getRecordCount());
        bean.setValue("maxPageNo", daoPageInfo.getMaxPageNo());

        bean.getWebValues().remove("search_info");
        String search_info = Sup.serialize(bean);
        bean.setValue("search_info", search_info);
        bean.setValue("list", listData);
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

    /**
     * 選択されたユーザーID配列を取得する。
     */
    private String[] getSelectedUserInfoIds()
    {
        WebBean bean = getWebBean();
        String selectedIds = bean.value("select_user_info_ids");

        if (selectedIds == null || selectedIds.trim().length() == 0)
        {
            return new String[0];
        }

        String[] rawIds = selectedIds.split(",");
        ArrayList<String> idList = new ArrayList<String>();

        for (int i = 0; i < rawIds.length; i++)
        {
            String id = rawIds[i].trim();
            if (id.length() > 0 && !idList.contains(id))
            {
                idList.add(id);
            }
        }

        return idList.toArray(new String[idList.size()]);
    }

    /**
     * 一括修正の場合。
     * 選択したユーザーの区分(管理者/一般)をまとめて変更する。
     * @throws AtareSysException
     */
    private void bulkUpdateAdmin() throws AtareSysException
    {
        WebBean bean = getWebBean();
        String[] userInfoIds = getSelectedUserInfoIds();
        String admin = bean.value("bulk_admin_value");

        if (!"1".equals(admin) && !"0".equals(admin))
        {
            bean.setError("区分を選択してください。");
            searchList();
            forward("ViewUserList.jsp");
            return;
        }

        try
        {
            DbBase.dbBeginTran();

            UserInfoDao dao = new UserInfoDao();
            for (int i = 0; i < userInfoIds.length; i++)
            {
                String userInfoId = userInfoIds[i].trim();
                if (userInfoId.length() == 0)
                {
                    continue;
                }

                dao.dbUpdateAdmin(userInfoId, admin);
            }

            DbBase.dbCommitTran();
            redirect("ViewUserList.do");
        }
        catch (Exception e)
        {
            DbBase.dbRollbackTran();
            bean.setError("ユーザーデータの一括修正に失敗しました。");
            searchList();
            forward("ViewUserList.jsp");
        }
    }

    /**
     * 一括削除の場合。
     * 選択したユーザーのステータスをまとめて退職(9)にする。
     * @throws AtareSysException
     */
    private void bulkDelete() throws AtareSysException
    {
        WebBean bean = getWebBean();
        String[] userInfoIds = getSelectedUserInfoIds();

        try
        {
            DbBase.dbBeginTran();

            UserInfoDao dao = new UserInfoDao();
            for (int i = 0; i < userInfoIds.length; i++)
            {
                String userInfoId = userInfoIds[i].trim();
                if (userInfoId.length() == 0)
                {
                    continue;
                }

                dao.dbDelete(userInfoId);
            }

            DbBase.dbCommitTran();
            redirect("ViewUserList.do");
        }
        catch (Exception e)
        {
            DbBase.dbRollbackTran();
            bean.setError("ユーザーデータの一括削除に失敗しました。");
            searchList();
            forward("ViewUserList.jsp");
        }
    }
}
