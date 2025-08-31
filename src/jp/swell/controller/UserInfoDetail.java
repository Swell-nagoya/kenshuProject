/*
 * (c)2023 PATAPATA Corp. Corp. All Rights Reserved
 *
 * システム名　　：PATAPATA System
 * サブシステム名：コントローラ
 * 機能名　　　　：user_info ユーザ情報テーブルデータを登録・更新・削除するためのコントローラクラス
 * ファイル名　　：UserInfoDetail.java
 * クラス名　　　：UserInfoDetail
 * 概要　　　　　：user_info ユーザ情報テーブルデータを登録・更新・削除するためのコントローラクラス
 * バージョン　　：
 *
 * 改版履歴　　　：
 * 2013/03/29 <新規>    新規作成
 *
 */
package jp.swell.controller;

import java.util.HashMap;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.swell.common.ControllerBase;
import jp.swell.dao.UserInfoDao;
import jp.swell.user.UserLoginInfo;

/**
 * ：user_info ユーザ情報テーブルデータを登録・更新・削除するためのコントローラクラス
 *
 * @author PATAPATA
 * @version 1.0
 */
public class UserInfoDetail extends ControllerBase
{

    /**
     * jp.swell.cloudbiz.common.ControllerBase のメソッドをオーバライドする。 ここで、コントローラの処理を記述する.
     * ここで、コントローラの処理を記述する.
     *
     * @throws Exception エラー
     */
    @Override
    public void doActionProcess() throws AtareSysException
    {
        WebBean bean = getWebBean();
        if ("UserInfoDetail_1".equals(bean.value("form_name")))
        {
            if ("go_next".equals(bean.value("action_cmd")))
            {
                bean.rtrimAllItem();
                UserInfoDao dao = setWeb2Dao2InputInfo();
                if ("update".equals(bean.value("request_cmd")))
                {
                    if (inputCheck(dao))
                    {
                        bean.setMessage("この内容で修正します。よろしいですか？");
                        forward("UserInfoDetail_2.jsp");
                    }
                    else
                    {
                        bean.setError("入力内容に誤りがあります");
                        forward("UserInfoDetail_1.jsp");
                    }
                }
            }
            else
            {
                forward("UserInfoDetail_1.jsp");
            }
        }
        else
        {
            bean.setValue("request_name", "修正する");
            bean.setValue("request_cmd", "update");
            setDb2Web();
            bean.setMessage("以下の項目を修正してください。");
            forward("UserInfoDetail_1.jsp");
        }
    }

    /**
     * データベースの内容を表示エリアに編集する。.
     *
     * @return boolean
     * @throws AtareSysException エラー
     */
    private boolean setDb2Web() throws AtareSysException
    {
        WebBean bean = getWebBean();
		UserLoginInfo userLoginInfo = (UserLoginInfo) getLoginInfo();
        UserInfoDao dao = new UserInfoDao();
        if (!dao.dbSelect(userLoginInfo.getUserInfoId()))
        {
            return false;
        }
        bean.setValue("last_name", dao.getLastName());
        bean.setValue("middle_name", dao.getMiddleName());
        bean.setValue("first_name", dao.getFirstName());
        bean.setValue("maiden_name", dao.getMaidenName());
        bean.setValue("last_name_kana", dao.getLastNameKana());
        bean.setValue("middle_name_kana", dao.getMiddleNameKana());
        bean.setValue("first_name_kana", dao.getFirstNameKana());
        bean.setValue("maiden_name_kana", dao.getMaidenNameKana());

        bean.setValue("select_info", Sup.serialize(dao)); // 編集前に読み込んだデータを格納しておく
        bean.setValue("input_info", Sup.serialize(dao));
        return true;
    }

    /**
     * 入力チェックを行う。.
     *
     * @return errors HashMapにエラーフィールドをキーとしてエラーメッセージを返す
     * @throws AtareSysException
     */
    private boolean inputCheck(UserInfoDao pUserInfoDao) throws AtareSysException
    {
        WebBean bean = getWebBean();
        HashMap<String, String> errors = bean.getItemErrors();
        if (bean.value("last_name").length() == 0)
        {
            errors.put("last_name", "姓を入力してください。");
        }
        else if (bean.value("last_name").length() > 0)
        {
            if (100 < bean.value("last_name").length())
            {
                errors.put("last_name", "姓の入力内容が長すぎます。");
            }
        }
        if (bean.value("middle_name").length() > 0)
        {
            if (100 < bean.value("middle_name").length())
            {
                errors.put("middle_name", "ミドルネームの入力内容が長すぎます。");
            }
        }
        if (bean.value("first_name").length() == 0)
        {
            errors.put("first_name", "名を入力してください。");
        }
        else if (bean.value("first_name").length() > 0)
        {
            if (100 < bean.value("first_name").length())
            {
                errors.put("first_name", "名の入力内容が長すぎます。");
            }
        }
        if (bean.value("maiden_name").length() > 0)
        {
            if (100 < bean.value("maiden_name").length())
            {
                errors.put("maiden_name", "旧姓の入力内容が長すぎます。");
            }
        }
        if (bean.value("last_name_kana").length() == 0)
        {
            errors.put("last_name_kana", "姓よみを入力してください。");
        }
        else if (bean.value("last_name_kana").length() > 0)
        {
            if (100 < bean.value("last_name_kana").length())
            {
                errors.put("last_name_kana", "姓よみの入力内容が長すぎます。");
            }
        }
        if (bean.value("middle_name_kana").length() > 0)
        {
            if (100 < bean.value("middle_name_kana").length())
            {
                errors.put("middle_name_kana", "ミドルネームよみの入力内容が長すぎます。");
            }
        }
        if (bean.value("first_name_kana").length() == 0)
        {
            errors.put("first_name_kana", "名よみを入力してください。");
        }
        else if (bean.value("first_name_kana").length() > 0)
        {
            if (100 < bean.value("first_name_kana").length())
            {
                errors.put("first_name_kana", "名よみの入力内容が長すぎます。");
            }
        }
        if (bean.value("maiden_name_kana").length() > 0)
        {
            if (100 < bean.value("maiden_name_kana").length())
            {
                errors.put("maiden_name_kana", "旧姓よみの入力内容が長すぎます。");
            }
        }
        if (errors.size() > 0)
        {
            return false;
        }
        return true;
    }

    /**
     * データベース処理を行う。.
     *
     * @return boolean
     * @throws AtareSysException エラー
     */
    private void processDb() throws AtareSysException
    {
        WebBean bean = getWebBean();
        try
        {
            DbBase.dbBeginTran();
            if ("update".equals(bean.value("request_cmd")))
            {
                UserLoginInfo userInfo = (UserLoginInfo) getLoginInfo();
                userInfo.resetUserInfo();
                setLoginInfo(userInfo);
                bean.setMessage("修正を完了しました。");
            }
            DbBase.dbCommitTran();
        } catch (AtareSysException e)
        {
            DbBase.dbRollbackTran();
            e.printStackTrace();
            throw new AtareSysException(e.getMessage());
        }
    }

    /**
     * 画面の項目をDAOクラスに格納しそれをシリアライズして、input_infoフィールドに格納する
     *
     * @return なし
     * @throws AtareSysException エラー
     */
    private UserInfoDao setWeb2Dao2InputInfo() throws AtareSysException
    {
        WebBean bean = getWebBean();
        UserInfoDao dao = (UserInfoDao) Sup.deserialize(bean.value("select_info"));
        dao.setLastName(bean.value("last_name"));
        dao.setMiddleName(bean.value("middle_name"));
        dao.setFirstName(bean.value("first_name"));
        dao.setMaidenName(bean.value("maiden_name"));
        dao.setLastNameKana(bean.value("last_name_kana"));
        dao.setMiddleNameKana(bean.value("middle_name_kana"));
        dao.setFirstNameKana(bean.value("first_name_kana"));
        dao.setMaidenNameKana(bean.value("maiden_name_kana"));

        bean.setValue("input_info", Sup.serialize(dao));
        return dao;
    }

    /**
     * input_infoフィールドからクラスを取り出し、画面の項目に値を設定する
     *
     * @return なし
     * @throws AtareSysException
     */
    private void setInputInfo2Dao2Web() throws AtareSysException
    {
        WebBean bean = getWebBean();
        UserInfoDao dao = (UserInfoDao) Sup.deserialize(bean.value("input_info"));
        bean.setValue("last_name", dao.getLastName());
        bean.setValue("middle_name", dao.getMiddleName());
        bean.setValue("first_name", dao.getFirstName());
        bean.setValue("maiden_name", dao.getMaidenName());
        bean.setValue("last_name_kana", dao.getLastNameKana());
        bean.setValue("middle_name_kana", dao.getMiddleNameKana());
        bean.setValue("first_name_kana", dao.getFirstNameKana());
        bean.setValue("maiden_name_kana", dao.getMaidenNameKana());
    }
}
