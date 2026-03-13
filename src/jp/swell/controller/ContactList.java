/*
 * (c)2023 PATAPATA Corp. Corp. All Rights Reserved
 *
 * システム名　　：PATAPATA System
 * サブシステム名：コントローラ
 * 機能名　　　　：user_contact_info 連絡先情報テーブルデータをLIST表示するためのコントローラクラス
 * ファイル名　　：ContactList.java
 * クラス名　　　：ContactList
 * 概要　　　　　：user_contact_info 連絡先情報テーブルデータをLIST表示するためのコントローラクラス
 * バージョン　　：
 *
 * 改版履歴　　　：
 * 2013/03/29 <新規>    新規作成
 * 2025/08/13 <改修>    UserInfo流の画面遷移/ページング/ソート/検索に合わせて全面改修
 */

package jp.swell.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.patasys.common.util.Validate;
import jp.swell.common.ControllerBase;
import jp.swell.dao.ContactDao;

public class ContactList extends ControllerBase {

    @Override
    public void doInit() {
        setLoginNeeds(false); // この処理にはログインが必要かどうか
        setHttpNeeds(false); // この処理はhttpでなければならないか
        setHttpsNeeds(false); // この処理はhttps でなければならないか。公開時にはtrueにする
        setUsecache(false); // この処理はクライアントのキャッシュを認めるか
    }

    @Override
    public void doActionProcess() throws AtareSysException {
        WebBean bean = getWebBean();

        if ("ContactList".equals(bean.value("form_name"))) {
            // ★一覧フォームからの操作
            bean.trimAllItem();

            String action = bean.value("action_cmd");
            if ("search".equals(action)) {
                // 検索条件が更新されたら1ページ目から
                bean.setValue("pageNo", "1");
                searchList();
            } else if ("next".equals(action)) {
                bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), 1));
                searchList();
            } else if ("jump".equals(action)) {
                searchList();
            } else if ("prior".equals(action)) {
                bean.setValue("pageNo", calcPageNo(bean.value("pageNo"), -1));
                searchList();
            } else if ("sort".equals(action)) {
                // ★ソート要求：キーはJSPから sort_key に入ってくる
                searchList();
            } else if ("clear".equals(action)) {
                formClear();
                searchList();
            } else if ("return".equals(action)) {
                redirect("MenuAdmin.do");
                return;
            } else {
                // 初期表示または未知のactionはそのまま検索
                searchList();
            }
            forward("ContactList.jsp");
            return;
        }
        // ★戻り遷移（ContactListDetail_* から戻ってきた時に、検索条件を復元して一覧を再表示）
        else if ("ContactListDetail_1".equals(bean.value("form_name"))
                || "ContactListDetail_3".equals(bean.value("form_name"))) {
            setWebBeanFromSerialize(bean.value("search_info")); // 保存しておいた検索条件を復元
            bean = getWebBean();
            searchList();
            forward("ContactList.jsp");
            return;
        } else {
            // ★Menuなどから最初に来た時の初期化
            formInit();
            searchList();
            forward("ContactList.jsp");
            return;
        }
    }

    // ===== 初期化／入力チェック ===================================================

    /** 最初の画面を表示する(初期値をセット) */
    private void formInit() throws AtareSysException {
        WebBean bean = getWebBean();
        bean.setValue("form_name", "ContactList");

        // ★UserInfo流に合わせる：初期のソートは「氏名よみ（かな）」相当
        //   Contactでは last_name_kana / first_name_kana を使うためキー名は last_name_kana に統一
        bean.setValue("sort_key", "last_name_kana");
        bean.setValue("sort_order", "asc");

        // ★表示件数・ページ初期値
        bean.setValue("lineCount", "100");
        bean.setValue("pageNo", "1");

        // ★検索条件初期化
        bean.setValue("list_search_full_name", "");
        bean.setValue("list_search_full_name_kana", "");
    }

    /** 「クリア」押下時に検索条件を空にする */
    private void formClear() throws AtareSysException {
        WebBean bean = getWebBean();
        bean.setValue("list_search_full_name", "");
        bean.setValue("list_search_full_name_kana", "");
        // 表示件数は既定に戻す
        bean.setValue("lineCount", "100");

        // ★検索条件を保存（戻り遷移で復元できるように）
        String search_info = Sup.serialize(bean);
        bean.setValue("search_info", search_info);
    }

    /** 検索条件の入力チェック（長さのみ） */
    private HashMap<String, String> inputCheck() {
        WebBean bean = getWebBean();
        HashMap<String, String> errors = bean.getItemErrors();

        if (bean.value("list_search_full_name").length() > 100) {
            errors.put("list_search_full_name", "氏名の入力内容が長すぎます。");
        }
        if (bean.value("list_search_full_name_kana").length() > 100) {
            errors.put("list_search_full_name_kana", "氏名よみの入力内容が長すぎます。");
        }
        return errors;
    }

    // ===== 検索本体（UserInfo流に合わせて Bean 項目を整える） ====================

    /** 一覧データを検索して WebBean に格納 */
    private void searchList() throws AtareSysException {
        WebBean bean = getWebBean();

        // 入力チェック
        HashMap<String, String> errors = inputCheck();
        if (!errors.isEmpty()) {
            bean.setValue("errors", errors);
            return;
        }

        // ★全件取得（ContactDao 側のメソッドを想定。なければ適宜修正）
        ArrayList<ContactDao> allData;
        try {
            ContactDao dao = new ContactDao();
            allData = dao.getAllContacts();
        } catch (Exception ex) {
            // 取得失敗時は空で返す
            allData = new ArrayList<ContactDao>();
            errors = new HashMap<>();
            errors.put("system_error", "連絡先情報の取得に失敗しました。");
            bean.setValue("errors", errors);
        }

        // ★検索条件の適用（氏名・氏名かな）
        final String qName = bean.value("list_search_full_name");
        final String qNameKana = bean.value("list_search_full_name_kana");

        ArrayList<ContactDao> filtered = new ArrayList<>();
        for (ContactDao c : allData) {
            boolean ok = true;
            if (qName != null && qName.length() > 0) {
                // 姓 + 名 + ミドル で部分一致（大文字小文字はそのまま）
                String full = n(c.getLastName()) + n(c.getFirstName()) + n(c.getMiddleName());
                ok &= full.contains(qName);
            }
            if (ok && qNameKana != null && qNameKana.length() > 0) {
                String fullKana = n(c.getLastNameKana()) + n(c.getFirstNameKana()) + n(c.getMiddleNameKana());
                ok &= fullKana.contains(qNameKana);
            }
            if (ok)
                filtered.add(c);
        }

        // ★ソートの適用（JSPからの sort_key / sort_order を使用）
        //   対応キー：last_name_kana / last_name / email / phone_number など最低限を用意
        String sortKey = bean.value("sort_key");
        String sortKeyOld = bean.value("sort_key_old");
        String sortOrder = bean.value("sort_order");

        // ▼UserInfo流：フリップフロップ（同じキーをクリックしたら昇⇄降を反転）
        if (sortKeyOld != null && sortKey != null && sortKey.length() > 0) {
            if (sortKey.equals(sortKeyOld)) {
                sortOrder = "desc".equals(sortOrder) ? "asc" : "desc";
            } else {
                // 新しいキーを選んだら asc から
                sortOrder = "asc";
            }
            bean.setValue("sort_key_old", sortKey);
            bean.setValue("sort_order", sortOrder);
            // 次の呼び出しまでは空に（UserInfo流の運用に合わせる）
            bean.setValue("sort_key", "");
        } else if (sortKeyOld == null || sortKeyOld.length() == 0) {
            // 初回
            bean.setValue("sort_key_old", (sortKey != null && sortKey.length() > 0) ? sortKey : "last_name_kana");
            if (sortOrder == null || sortOrder.length() == 0)
                bean.setValue("sort_order", "asc");
        }
        final String effectiveKey = bean.value("sort_key_old"); // 実際に使うキー
        final boolean asc = !"desc".equals(bean.value("sort_order"));

        Comparator<ContactDao> comp = buildComparator(effectiveKey, asc);
        Collections.sort(filtered, comp);

        // ★ページング
        int pageNo = parseOrDefault(bean.value("pageNo"), 1);
        int lineCount = parseOrDefault(bean.value("lineCount"), 100);
        if (pageNo < 1)
            pageNo = 1;
        if (lineCount < 1)
            lineCount = 100;

        int totalCount = filtered.size();
        int maxPageNo = (int) Math.ceil((double) totalCount / (double) lineCount);
        if (maxPageNo < 1)
            maxPageNo = 1;
        if (pageNo > maxPageNo)
            pageNo = maxPageNo;

        int startIndex = (pageNo - 1) * lineCount;
        int endIndex = Math.min(startIndex + lineCount, totalCount);

        ArrayList<ContactDao> page = new ArrayList<>();
        if (startIndex < totalCount) {
            for (int i = startIndex; i < endIndex; i++) {
                page.add(filtered.get(i));
            }
        }

        // ★JSPが参照する項目名をUserInfo流に合わせてセット
        bean.setValue("lineCount", lineCount);
        bean.setValue("pageNo", pageNo);
        bean.setValue("recordCount", totalCount); // 総件数
        bean.setValue("maxPageNo", maxPageNo);
        bean.setValue("list", page);

        // ★検索条件を保存（詳細→戻る で復元するため）
        bean.getWebValues().remove("search_info");
        String search_info = Sup.serialize(bean);
        bean.setValue("search_info", search_info);
    }

    // ===== ユーティリティ =======================================================

    /** null安全な文字列化 */
    private static String n(String s) {
        return (s == null) ? "" : s;
    }

    /** 並び替え用コンパレータを生成 */
    private static Comparator<ContactDao> buildComparator(final String key, final boolean asc) {
        Comparator<ContactDao> c;

        if ("last_name".equals(key)) {
            c = Comparator.comparing((ContactDao o) -> n(o.getLastName()) + n(o.getFirstName()) + n(o.getMiddleName()));
        } else if ("email".equals(key) || "memail".equals(key)) { // JSP側のキー揺れ対策で memail も拾う
            c = Comparator.comparing(o -> n(o.getEmail()));
        } else if ("phone_number".equals(key)) {
            c = Comparator.comparing(o -> n(o.getPhoneNumber()));
        } else {
            // 既定：氏名よみ（かな）
            c = Comparator.comparing(
                    (ContactDao o) -> n(o.getLastNameKana()) + n(o.getFirstNameKana()) + n(o.getMiddleNameKana()));
        }

        if (!asc)
            c = c.reversed();
        return c;
    }

    /** ページ番号を加算減算する(UserInfo流と同じ仕様) */
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

    /** 文字列→int (失敗時は既定値) */
    private static int parseOrDefault(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }
}
