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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.SystemUserInfoValue;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.patasys.common.util.Validate;
import jp.swell.common.ControllerBase;
import jp.swell.dao.FileDao;
import jp.swell.user.UserLoginInfo;

/**
 * ：user_info ユーザ情報テーブルデータを登録・更新・削除するためのコントローラクラス
 *
 * @author PATAPATA
 * @version 1.0
 */
public class FileList extends ControllerBase {
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

        if ("FileList".equals(bean.value("form_name"))) {
            bean.trimAllItem();
            if ("search".equals(bean.value("action_cmd"))) {
                bean.setValue("pageNo", "1");
                searchList();
            } else if ("next".equals(bean.value("action_cmd"))) {
                bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), 1));
                searchList();
            } else if ("jump".equals(bean.value("action_cmd"))) {
                searchList();
            } else if ("prior".equals(bean.value("action_cmd"))) {
                bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), -1));
                searchList();
            } else if ("sort".equals(bean.value("action_cmd"))) {
                searchList();
            } else if ("clear".equals(bean.value("action_cmd"))) {
                formClear();
                searchList();
            } else if ("return".equals(bean.value("action_cmd"))) {
                redirect("MenuAdmin.do");
            } else {
                searchList();
            }
            forward("FileList.jsp");
        } else if ("FileDetail".equals(bean.value("form_name")) || "FileDetail_2".equals(bean.value("form_name"))) {
            setWebBeanFromSerialize(bean.value("search_info"));
            bean = getWebBean();
            searchList();
            forward("FileList.jsp");
        } else {
            formInit();
            searchList();
            forward("FileList.jsp");
        }
    }

    /**
     * 最初の画面を表示する。.
     *
     * @throws AtareSysException
     */
    private void formInit() throws AtareSysException {
        WebBean bean = getWebBean();
        bean.setValue("sort_key", "file_name"); /* 初回のソートキーを入れる */
        bean.setValue("sort_order", "asc");
        bean.setValue("lineCount",
                SystemUserInfoValue.getUserInfoValue(getLoginUserId(), "RoomList", "lineCount", "100"));
    }

    /**
     * フィールドをクリアする。.
     */
    private void formClear() throws AtareSysException {
        WebBean bean = getWebBean();
        bean.setValue("list_search_file_name", "");
        bean.setValue("lineCount", "");
        String search_info = Sup.serialize(bean);
        bean.setValue("search_info", search_info);
    }

    /**
     * 入力チェックを行う。.
     *
     * @return errors HashMapにエラーフィールドをキーとしてエラーメッセージを返す
     */
    private HashMap<String, String> inputCheck() {
        WebBean bean = getWebBean();
        HashMap<String, String> errors = bean.getItemErrors();
        if (bean.value("list_search_file_name").length() > 0) {
            if (100 < bean.value("list_search_file_name").length()) {
                errors.put("list_search_file_name", "氏名の入力内容が長すぎます。");
            }
        }
        return errors;
    }

    /**
     * 検索を行いbeanに格納する。.
     */
    private void searchList() throws AtareSysException {
        WebBean bean = getWebBean();
        UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();
        HashMap<String, String> errors = inputCheck();
        if (errors.size() > 0) {
            bean.setValue("errors", errors);
            return;
        }

        LinkedHashMap<String, String> sortKey = sortKey();
        DaoPageInfo daoPageInfo = new DaoPageInfo();
        if (!Validate.isInteger(bean.value("lineCount"))) {
            bean.setValue("lineCount", "20");
        }
        daoPageInfo.setLineCount(Integer.parseInt(bean.value("lineCount")));
        if (!Validate.isInteger(bean.value("pageNo"))) {
            daoPageInfo.setPageNo(1);
        } else {
            daoPageInfo.setPageNo(Integer.parseInt(bean.value("pageNo")));
        }

        // 自分がアップロードしたファイル（送信）
        FileDao sentDao = new FileDao();
        sentDao.setUploadUserId(userLoginInfo.getUserInfoId());
        sentDao.setSearchFileName(bean.value("list_search_file_name"));
        List<FileDao> sentFiles = FileDao.dbSelectList(sentDao, sortKey, daoPageInfo);
        for (FileDao file : sentFiles) {
            file.setFileType("sent");
        }

        // 自分宛てのファイル（受信）
        FileDao receivedDao = new FileDao();
        receivedDao.setUserInfoId(userLoginInfo.getUserInfoId());
        receivedDao.setSearchFileName(bean.value("list_search_file_name"));
        List<FileDao> receivedFiles = FileDao.dbSelectList(receivedDao, sortKey, daoPageInfo);
        for (FileDao file : receivedFiles) {
            file.setFileType("received");
        }

        // マージしてセット
        ArrayList<FileDao> fileList = new ArrayList<>();
        fileList.addAll(receivedFiles);
        fileList.addAll(sentFiles);

        bean.setValue("list", fileList);
        bean.setValue("lineCount", daoPageInfo.getLineCount());
        bean.setValue("pageNo", daoPageInfo.getPageNo());
        // 受信件数だけでは recordCount が正確に反映されない可能性があるため、明示的に再セット
        bean.setValue("recordCount", fileList.size());
        bean.setValue("maxPageNo", Math.max(1, (int) Math.ceil((double) fileList.size() / daoPageInfo.getLineCount())));

        SystemUserInfoValue.setUserInfoValue(getLoginUserId(), "FileList", "lineCount", bean.value("lineCount"));

        if (!Validate.isInteger(bean.value("lineCount"))) {
            bean.setValue("lineCount", "20");
        }
        daoPageInfo.setLineCount(Integer.parseInt(bean.value("lineCount")));
        SystemUserInfoValue.setUserInfoValue(getLoginUserId(), "FileList", "lineCount", bean.value("lineCount"));
        if (!Validate.isInteger(bean.value("pageNo"))) {
            daoPageInfo.setPageNo(1);
        } else {
            daoPageInfo.setPageNo(Integer.parseInt(bean.value("pageNo")));
        }

    }

    /**
     * ソート順番を求める
     *
     * @return ソート順を格納した配列を返す
     */
    private LinkedHashMap<String, String> sortKey() {
        WebBean bean = getWebBean();
        String key = "upload_date"; // アップロード日時をキーに設定
        LinkedHashMap<String, String> sort_key = new LinkedHashMap<>();

        // 既存のソートロジックを調整
        if (bean.value("sort_key").length() == 0 && bean.value("sort_key_old").length() == 0)
            return null;

        if (bean.value("sort_key_old").length() > 0) {
            if (bean.value("sort_key").length() > 0) {
                if (bean.value("sort_key").equals(bean.value("sort_key_old"))) {
                    // 同一ソートキー（フリップフロップ）
                    key = bean.value("sort_key_old");
                    if ("desc".equals(bean.value("sort_order"))) {
                        sort_key.put(key, "asc");
                    } else {
                        sort_key.put(key, "desc");
                    }
                } else {
                    // 新たなソートキー
                    key = bean.value("sort_key");
                    sort_key.put(key, "asc");
                }
            } else {
                // 引き継ぎ
                key = bean.value("sort_key_old");
                sort_key.put(key, "asc".equals(bean.value("sort_order")) ? "asc" : "desc");
            }
        } else {
            // 初期値
            sort_key.put(key, "asc"); // 初期ソート順を指定
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
