 /*
 * (c)2010 2023 PATAPATA Corp. Corp. All Rights Reserved
 *
 * 機能名　　　　：DAOクラス
 * ファイル名　　：UserInfoDao.java
 * クラス名　　　：UserInfoDao
 * 概要　　　　　：user_info ユーザ情報テーブルのDAOを提供する。
 * バージョン　　：
 *
 * 改版履歴　　　：
 * 2018/09/21 <新規>    新規作成
 *
 */
package jp.swell.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DaoPageInfo;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.DbI;
import jp.patasys.common.db.DbO;
import jp.patasys.common.db.DbS;
import jp.patasys.common.db.GetNumber;
import jp.patasys.common.util.Digest;

/**
 * user_info ユーザ情報テーブルのDAOを提供する。
 *
 * @author 2023 PATAPATA Corp. Corp.
 * @version 1.0
 */
public class UserInfoDao implements Serializable
{
     /** Derializable No. */
    private static final long serialVersionUID = 1L;
    /**
     * データアクセス権限のあるユーザリスト
     */
    private ArrayList<String> authorityUserList = null;
    /**
     * 姓名取得する
     *
     * @return fulltName 姓名
     */
    public String getFullName()
    {
        String str = "";
        if (lastName.length() != 0) str = lastName;
        str += !(middleName.length() == 0) ? " " : "";
        str += middleName;
        str += !(firstName.length() == 0) ? " " : "";
        str += firstName;
        return str;
    }
    /**
     * 姓名かなを取得する
     *
     * @return fulltName 姓名
     */
    public String getFullNameKana()
    {
        String str = "";
        if (lastNameKana.length() != 0) str = lastNameKana;
        str += !(middleNameKana.length() == 0) ? " " : "";
        str += middleNameKana;
        str += !(firstNameKana.length() == 0) ? " " : "";
        str += firstNameKana;
        return str;
    }
    /**
     * user_info ユーザ情報テーブルで使用するメンバー変数。
     */
    /**
     * userInfoId  ユーザ情報ID
     */
    private String userInfoId = "";
    /**
     * ユーザ情報IDを取得する。.
     * @return  userInfoId ユーザ情報ID
     */
    public String getUserInfoId()
    {
        return userInfoId;
    }
    /**
     * ユーザ情報IDをセットする。.
     * @param userInfoId ユーザ情報ID
     */
    public void setUserInfoId(String userInfoId)
    {
        this.userInfoId = userInfoId;
    }

    /**
     * password  パスワード
     */
    private String password = "";
    /**
     * パスワードを取得する。.
     * @return  password パスワード
     */
    public String getPassword()
    {
        return password;
    }
    /**
     * パスワードをセットする。.
     * @param password パスワード
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * lastName  姓
     */
    private String lastName = "";
    /**
     * 姓を取得する。.
     * @return  lastName 姓
     */
    public String getLastName()
    {
        return lastName;
    }
    /**
     * 姓をセットする。.
     * @param lastName 姓
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * middleName  ミドルネーム
     */
    private String middleName = "";
    /**
     * ミドルネームを取得する。.
     * @return  middleName ミドルネーム
     */
    public String getMiddleName()
    {
        return middleName;
    }
    /**
     * ミドルネームをセットする。.
     * @param middleName ミドルネーム
     */
    public void setMiddleName(String middleName)
    {
        this.middleName = middleName;
    }

    /**
     * firstName  名
     */
    private String firstName = "";
    /**
     * 名を取得する。.
     * @return  firstName 名
     */
    public String getFirstName()
    {
        return firstName;
    }
    /**
     * 名をセットする。.
     * @param firstName 名
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * maidenName  旧姓
     */
    private String maidenName = "";
    /**
     * 旧姓を取得する。.
     * @return  maidenName 旧姓
     */
    public String getMaidenName()
    {
        return maidenName;
    }
    /**
     * 旧姓をセットする。.
     * @param maidenName 旧姓
     */
    public void setMaidenName(String maidenName)
    {
        this.maidenName = maidenName;
    }

    /**
     * lastNameKana  姓よみ
     */
    private String lastNameKana = "";
    /**
     * 姓よみを取得する。.
     * @return  lastNameKana 姓よみ
     */
    public String getLastNameKana()
    {
        return lastNameKana;
    }
    /**
     * 姓よみをセットする。.
     * @param lastNameKana 姓よみ
     */
    public void setLastNameKana(String lastNameKana)
    {
        this.lastNameKana = lastNameKana;
    }

    /**
     * middleNameKana  ミドルネームよみ
     */
    private String middleNameKana = "";
    /**
     * ミドルネームよみを取得する。.
     * @return  middleNameKana ミドルネームよみ
     */
    public String getMiddleNameKana()
    {
        return middleNameKana;
    }
    /**
     * ミドルネームよみをセットする。.
     * @param middleNameKana ミドルネームよみ
     */
    public void setMiddleNameKana(String middleNameKana)
    {
        this.middleNameKana = middleNameKana;
    }

    /**
     * firstNameKana  名よみ
     */
    private String firstNameKana = "";
    /**
     * 名よみを取得する。.
     * @return  firstNameKana 名よみ
     */
    public String getFirstNameKana()
    {
        return firstNameKana;
    }
    /**
     * 名よみをセットする。.
     * @param firstNameKana 名よみ
     */
    public void setFirstNameKana(String firstNameKana)
    {
        this.firstNameKana = firstNameKana;
    }

    /**
     * maidenNameKana  旧姓よみ
     */
    private String maidenNameKana = "";
    /**
     * 旧姓よみを取得する。.
     * @return  maidenNameKana 旧姓よみ
     */
    public String getMaidenNameKana()
    {
        return maidenNameKana;
    }
    /**
     * 旧姓よみをセットする。.
     * @param maidenNameKana 旧姓よみ
     */
    public void setMaidenNameKana(String maidenNameKana)
    {
        this.maidenNameKana = maidenNameKana;
    }

    /**
     * zipcode  郵便番号
     */
    private String zipcode = "";
    /**
     * 郵便番号を取得する。.
     * @return  zipcode 郵便番号
     */
    public String getZipcode()
    {
        return zipcode;
    }
    /**
     * 郵便番号をセットする。.
     * @param zipcode 郵便番号
     */
    public void setZipcode(String zipcode)
    {
        this.zipcode = zipcode;
    }

    /**
     * address  住所
     */
    private String address = "";
    /**
     * 住所を取得する。.
     * @return  address 住所
     */
    public String getAddress()
    {
        return address;
    }
    /**
     * 住所をセットする。.
     * @param address 住所
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * station  最寄りの駅
     */
    private String station = "";
    /**
     * 最寄りの駅を取得する。.
     * @return  station 最寄りの駅
     */
    public String getStation()
    {
        return station;
    }
    /**
     * 最寄りの駅をセットする。.
     * @param station 最寄りの駅
     */
    public void setStation(String station)
    {
        this.station = station;
    }

    /**
     * tel  電話番号
     */
    private String tel = "";
    /**
     * 電話番号を取得する。.
     * @return  tel 電話番号
     */
    public String getTel()
    {
        return tel;
    }
    /**
     * 電話番号をセットする。.
     * @param tel 電話番号
     */
    public void setTel(String tel)
    {
        this.tel = tel;
    }

    /**
     * fax  ＦＡＸ
     */
    private String fax = "";
    /**
     * ＦＡＸを取得する。.
     * @return  fax ＦＡＸ
     */
    public String getFax()
    {
        return fax;
    }
    /**
     * ＦＡＸをセットする。.
     * @param fax ＦＡＸ
     */
    public void setFax(String fax)
    {
        this.fax = fax;
    }

    /**
     * mtel  携帯電話番号
     */
    private String mtel = "";
    /**
     * 携帯電話番号を取得する。.
     * @return  mtel 携帯電話番号
     */
    public String getMtel()
    {
        return mtel;
    }
    /**
     * 携帯電話番号をセットする。.
     * @param mtel 携帯電話番号
     */
    public void setMtel(String mtel)
    {
        this.mtel = mtel;
    }

    /**
     * memail  携帯Eメール
     */
    private String memail = "";
    /**
     * 携帯Eメールを取得する。.
     * @return  memail 携帯Eメール
     */
    public String getMemail()
    {
        return memail;
    }
    /**
     * 携帯Eメールをセットする。.
     * @param memail 携帯Eメール
     */
    public void setMemail(String memail)
    {
        this.memail = memail;
    }

    /**
     * passwordModifyDate  パスワード変更日時
     */
    private String passwordModifyDate = "";
    /**
     * パスワード変更日時を取得する。.
     * @return  passwordModifyDate パスワード変更日時
     */
    public String getPasswordModifyDate()
    {
        return passwordModifyDate;
    }
    /**
     * パスワード変更日時をセットする。.
     * @param passwordModifyDate パスワード変更日時
     */
    public void setPasswordModifyDate(String passwordModifyDate)
    {
        this.passwordModifyDate = passwordModifyDate;
    }

    /**
     * loginEnableFrom  ログイン可能期間FROM
     */
    private String loginEnableFrom = "";
    /**
     * ログイン可能期間FROMを取得する。.
     * @return  loginEnableFrom ログイン可能期間FROM
     */
    public String getLoginEnableFrom()
    {
        return loginEnableFrom;
    }
    /**
     * ログイン可能期間FROMをセットする。.
     * @param loginEnableFrom ログイン可能期間FROM
     */
    public void setLoginEnableFrom(String loginEnableFrom)
    {
        this.loginEnableFrom = loginEnableFrom;
    }

    /**
     * loginEnableTo  ログイン可能期間TO
     */
    private String loginEnableTo = "";
    /**
     * ログイン可能期間TOを取得する。.
     * @return  loginEnableTo ログイン可能期間TO
     */
    public String getLoginEnableTo()
    {
        return loginEnableTo;
    }
    /**
     * ログイン可能期間TOをセットする。.
     * @param loginEnableTo ログイン可能期間TO
     */
    public void setLoginEnableTo(String loginEnableTo)
    {
        this.loginEnableTo = loginEnableTo;
    }

    /**
     * searchFullName 検索用氏名
     */
    private String searchFullName = "";
    /**
     * searchFullName 検索用氏名かな
     */
    private String searchFullNameKana = "";

    /**
     * searchFullName 検索用氏名を取得します。
     *
     * @return searchFullName 検索用氏名
     */
    public String getSearchFullName()
    {
        return searchFullName;
    }

    /**
     * searchFullName 検索用氏名を設定します。
     *
     * @param searchFullName
     *        searchFullName 検索用氏名
     */
    public void setSearchFullName(String searchFullName)
    {
        this.searchFullName = searchFullName;
    }

    /**
     * searchFullName 検索用氏名かなを取得します。
     *
     * @return searchFullName 検索用氏名かな
     */
    public String getSearchFullNameKana()
    {
        return searchFullNameKana;
    }

    /**
     * searchFullName 検索用氏名かなを設定します。
     *
     * @param searchFullNameKana
     *        searchFullName 検索用氏名かな
     */
    public void setSearchFullNameKana(String searchFullNameKana)
    {
        this.searchFullNameKana = searchFullNameKana;
    }

    /**
     *  データアクセス権限のあるユーザリストを取得する。.
     */
    public ArrayList<String> getAuthorityUserList()
    {
        return authorityUserList;
    }
    /**
     * ソートフィールドのチェック時に使う。SQLインジェクション対策用。.
     */
    private HashMap<String,String> fieldsArray = new HashMap<String,String>();

    /**
     * コンストラクタ。.
     */
    public UserInfoDao()
    {
        fieldsArray.put("user_info_id","user_info.user_info_id");
        fieldsArray.put("password","user_info.password");
        fieldsArray.put("last_name","user_info.last_name");
        fieldsArray.put("middle_name","user_info.middle_name");
        fieldsArray.put("first_name","user_info.first_name");
        fieldsArray.put("maiden_name","user_info.maiden_name");
        fieldsArray.put("last_name_kana","user_info.last_name_kana");
        fieldsArray.put("middle_name_kana","user_info.middle_name_kana");
        fieldsArray.put("first_name_kana","user_info.first_name_kana");
        fieldsArray.put("maiden_name_kana","user_info.maiden_name_kana");
    }
    /**
     * user_info ユーザ情報テーブルを検索しuser_info ユーザ情報テーブルの１行を取得します。.
     *
     * @param pUserInfoId   ユーザ情報ID
     * @return true:読み込み成功 false:存在しない
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbSelect(String pUserInfoId) throws AtareSysException
    {
        String sql =  "select "
                + " user_info.user_info_id as user_info___user_info_id"
                + ",user_info.password as user_info___password"
                + ",user_info.last_name as user_info___last_name"
                + ",user_info.middle_name as user_info___middle_name"
                + ",user_info.first_name as user_info___first_name"
                + ",user_info.maiden_name as user_info___maiden_name"
                + ",user_info.last_name_kana as user_info___last_name_kana"
                + ",user_info.middle_name_kana as user_info___middle_name_kana"
                + ",user_info.first_name_kana as user_info___first_name_kana"
                + ",user_info.maiden_name_kana as user_info___maiden_name_kana"
        + " from user_info ";
        sql += ""
        + " where user_info_id = " + DbS.chara(pUserInfoId);
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if(0==rs.size())   return false;
        HashMap<String, String> map = rs.get(0);
        setUserInfoDaoForJoin(map,this);
        return true;
    }
    /**
     * user_info ユーザ情報テーブルを検索しuser_info ユーザ情報テーブルの１行を取得します。.
     *
     * @param pUserInfoId   ユーザ情報ID
     * @return true:読み込み成功 false:存在しない
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbSelect(String pUserInfoId,String pas) throws AtareSysException
    {
        String sql =  "select "
                + " user_info.user_info_id as user_info___user_info_id"
                + ",user_info.password as user_info___password"
                + ",user_info.last_name as user_info___last_name"
                + ",user_info.middle_name as user_info___middle_name"
                + ",user_info.first_name as user_info___first_name"
                + ",user_info.maiden_name as user_info___maiden_name"
                + ",user_info.last_name_kana as user_info___last_name_kana"
                + ",user_info.middle_name_kana as user_info___middle_name_kana"
                + ",user_info.first_name_kana as user_info___first_name_kana"
                + ",user_info.maiden_name_kana as user_info___maiden_name_kana"
        + " from user_info ";
        sql += ""
        + " where user_info_id = " + DbS.chara(pUserInfoId)
        + " and password = " + DbS.chara(pas);
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if(0==rs.size())   return false;
        HashMap<String, String> map = rs.get(0);
        setUserInfoDaoForJoin(map,this);
        return true;
    }

    /**
     * UserInfoDao にuser_info ユーザ情報テーブルから読み込んだデータを設定する。.
     *
     * @param map  読み込んだテーブルの１レコードが入っているHashMap
     * @param dao  UserInfoDaoこのテーブルのインスタンス
     */
    public void setUserInfoDao(HashMap<String, String> map,UserInfoDao dao)  throws AtareSysException
    {
        dao.setUserInfoId(DbI.chara(map.get("user_info_id")));
        dao.setPassword(DbI.chara(map.get("password")));
        dao.setLastName(DbI.chara(map.get("last_name")));
        dao.setMiddleName(DbI.chara(map.get("middle_name")));
        dao.setFirstName(DbI.chara(map.get("first_name")));
        dao.setMaidenName(DbI.chara(map.get("maiden_name")));
        dao.setLastNameKana(DbI.chara(map.get("last_name_kana")));
        dao.setMiddleNameKana(DbI.chara(map.get("middle_name_kana")));
        dao.setFirstNameKana(DbI.chara(map.get("first_name_kana")));
        dao.setMaidenNameKana(DbI.chara(map.get("maiden_name_kana")));
    }

    /**
     * UserInfoDao にuser_info ユーザ情報テーブルから読み込んだデータを設定する。.
     *
     * @param map  読み込んだテーブルの１レコードが入っているHashMap
     * @param dao  UserInfoDaoこのテーブルのインスタンス
     */
    public void setUserInfoDaoForJoin(HashMap<String, String> map,UserInfoDao dao)  throws AtareSysException
    {
        dao.setUserInfoId(DbI.chara(map.get("user_info___user_info_id")));
        dao.setPassword(DbI.chara(map.get("user_info___password")));
        dao.setLastName(DbI.chara(map.get("user_info___last_name")));
        dao.setMiddleName(DbI.chara(map.get("user_info___middle_name")));
        dao.setFirstName(DbI.chara(map.get("user_info___first_name")));
        dao.setMaidenName(DbI.chara(map.get("user_info___maiden_name")));
        dao.setLastNameKana(DbI.chara(map.get("user_info___last_name_kana")));
        dao.setMiddleNameKana(DbI.chara(map.get("user_info___middle_name_kana")));
        dao.setFirstNameKana(DbI.chara(map.get("user_info___first_name_kana")));
        dao.setMaidenNameKana(DbI.chara(map.get("user_info___maiden_name_kana")));

    }
    /**
     * user_info ユーザ情報テーブルにデータを挿入する
     *
     * @return true:成功 false:失敗
     * @throws AtareSysException エラー
     */
    public boolean dbInsert() throws AtareSysException
    {
        setUserInfoId(GetNumber.getNumberChar("user_info"));
        String sql="insert into user_info ("
        + " user_info_id"
        + ",password"
        + ",last_name"
        + ",middle_name"
        + ",first_name"
        + ",maiden_name"
        + ",last_name_kana"
        + ",middle_name_kana"
        + ",first_name_kana"
        + ",maiden_name_kana"
        + " ) values ( "
        + DbO.chara(getUserInfoId())
        + "," + DbO.chara(getPassword())
        + "," + DbO.chara(getLastName())
        + "," + DbO.chara(getMiddleName())
        + "," + DbO.chara(getFirstName())
        + "," + DbO.chara(getMaidenName())
        + "," + DbO.chara(getLastNameKana())
        + "," + DbO.chara(getMiddleNameKana())
        + "," + DbO.chara(getFirstNameKana())
        + "," + DbO.chara(getMaidenNameKana())
        + " )";
        int ret = DbBase.dbExec(sql);
        if(ret!=1) throw new AtareSysException("dbInsert number or record exception.") ;
        return true;
    }


    /**
     * user_info ユーザ情報テーブルのデータを更新する。.
     *
     * @return true:成功 false:失敗
     * @throws AtareSysException フレームワーク共通例外
     */
    public boolean dbUpdate() throws AtareSysException
    {
        String sql="update user_info set "
        + " last_name = " +  DbO.chara(getLastName())
        + "," + " middle_name = " +    DbO.chara(getMiddleName())
        + "," + " first_name = " +    DbO.chara(getFirstName())
        + "," + " maiden_name = " +    DbO.chara(getMaidenName())
        + "," + " last_name_kana = " +    DbO.chara(getLastNameKana())
        + "," + " middle_name_kana = " +    DbO.chara(getMiddleNameKana())
        + "," + " first_name_kana = " +    DbO.chara(getFirstNameKana())
        + "," + " maiden_name_kana = " +    DbO.chara(getMaidenNameKana())
        + " where user_info_id = " + DbS.chara(userInfoId)
        + "";
        int ret = DbBase.dbExec(sql);
        if(ret!=1) throw new AtareSysException("dbUpdate number or record exception.") ;
        return true;
    }



    /**
     * user_info ユーザ情報テーブルからデータを削除する
     *
     * @param pUserInfoId   ユーザ情報ID
     * @return true:成功 false:失敗
     * @throws AtareSysException エラー
     */
    public boolean dbDelete(String pUserInfoId) throws AtareSysException
    {
        String sql="update user_info set "
        + " state_flg = 9 "
        + " where user_info_id = " + DbS.chara(userInfoId);
        int ret = DbBase.dbExec(sql);
        if(ret!=1) throw new AtareSysException("dbDelete number or record exception.") ;
        return true;
    }


    /**
     * user_info ユーザ情報テーブルを検索し指定されたレコードのリストを返す
     * @param myclass        検索条件をUserInfoDaoのインスタンスに入れて渡す
     * @param sortKey     ソート順を配列で渡す　キー値は項目名　値はソート順 "ASC" "DESC"
     * @return 取得したUserInfoDaoの配列
     * @throws AtareSysException エラー
     */
    static public ArrayList<UserInfoDao> dbSelectListAll() throws AtareSysException
    {
        ArrayList<UserInfoDao> array = new ArrayList<UserInfoDao>();

        List<HashMap<String, String>> rs;
        HashMap<String, String> map;
        String sql =  "select * "
                + " from user_info ";
        rs  =  DbBase.dbSelect(sql);
        int cnt = rs.size();
        if(cnt < 1)    return array;
        for(int i=0;i<cnt;i++)
        {
            UserInfoDao dao  = new UserInfoDao();
            map = rs.get(i);
            dao.setUserInfoDao(map,dao);
            array.add(dao);
        }
        return array;
    }

    /**
     * user_info ユーザ情報テーブルを検索し指定されたレコードのリストを返す
     * @param myclass        検索条件をUserInfoDaoのインスタンスに入れて渡す
     * @param sortKey     ソート順を配列で渡す　キー値は項目名　値はソート順 "ASC" "DESC"
     * @param daoPageInfo   取得したいページの番やライン数を入れる。結果がここに帰ってくる
     *                       ライン数に-1を入れると全件取得になる
     * @return 取得したUserInfoDaoの配列
     * @throws AtareSysException エラー
     */
    static public ArrayList<UserInfoDao> dbSelectList(UserInfoDao myclass,LinkedHashMap<String,String> sortKey,DaoPageInfo daoPageInfo) throws AtareSysException
    {
        ArrayList<UserInfoDao> array = new ArrayList<UserInfoDao>();

        /* レコードの総件数を求める */
        String sql =  "select count(*) as count"
        + " from user_info "
        + myclass.dbWhere();
        List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
        if(0==rs.size())   return array;
        HashMap<String, String> map = rs.get(0);
        int len = Integer.parseInt(map.get("count"));
        daoPageInfo.setRecordCount(len);
        if(len == 0)   return array;
        if(-1==daoPageInfo.getLineCount()) daoPageInfo.setLineCount(len);
        daoPageInfo.setMaxPageNo((int) Math.ceil((double)len/(double)(daoPageInfo.getLineCount())));
        if(daoPageInfo.getPageNo() < 1) daoPageInfo.setPageNo(1);
        if(daoPageInfo.getPageNo() > daoPageInfo.getMaxPageNo()) daoPageInfo.setPageNo(daoPageInfo.getMaxPageNo());
        int start  =   (daoPageInfo.getPageNo() - 1) * daoPageInfo.getLineCount();
        sql =  "select "
                + " user_info.user_info_id as user_info___user_info_id"
                + ",user_info.password as user_info___password"
                + ",user_info.last_name as user_info___last_name"
                + ",user_info.middle_name as user_info___middle_name"
                + ",user_info.first_name as user_info___first_name"
                + ",user_info.maiden_name as user_info___maiden_name"
                + ",user_info.last_name_kana as user_info___last_name_kana"
                + ",user_info.middle_name_kana as user_info___middle_name_kana"
                + ",user_info.first_name_kana as user_info___first_name_kana"
                + ",user_info.maiden_name_kana as user_info___maiden_name_kana"
        + " from user_info ";
        String where = myclass.dbWhere();
        String order = myclass.dbOrder(sortKey);
        sql += where;
        sql += order;
        sql += " limit " + daoPageInfo.getLineCount() + " offset " + start + ";";
        rs  =  DbBase.dbSelect(sql);
        int cnt = rs.size();
        if(cnt < 1)    return array;
        for(int i=0;i<cnt;i++)
        {
            UserInfoDao dao  = new UserInfoDao();
            map = rs.get(i);
            dao.setUserInfoDaoForJoin(map,dao);
            array.add(dao);
        }
        return array;
    }

    /**
     * user_info ユーザ情報テーブルの検索条件を設定する。.
     *
     * @return String where句の文字列
     * @throws AtareSysException フレームワーク共通例外
     */
    private String dbWhere() throws AtareSysException
    {
        StringBuffer where = new StringBuffer(1024);

        if(getUserInfoId().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("user_info.user_info_id = " + DbS.chara(getUserInfoId()));
        }

        if(getLastName().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("user_info.last_name = " + DbS.chara(getLastName()));
        }

        if(getMiddleName().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("user_info.middle_name = " + DbS.chara(getMiddleName()));
        }

        if(getFirstName().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("user_info.first_name = " + DbS.chara(getFirstName()));
        }

        if(getMaidenName().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("user_info.maiden_name = " + DbS.chara(getMaidenName()));
        }

        if(getLastNameKana().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("user_info.last_name_kana = " + DbS.chara(getLastNameKana()));
        }

        if(getMiddleNameKana().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("user_info.middle_name_kana = " + DbS.chara(getMiddleNameKana()));
        }

        if(getFirstNameKana().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("user_info.first_name_kana = " + DbS.chara(getFirstNameKana()));
        }

        if(getMaidenNameKana().length()>0)
        {
            where.append(where.length()>0 ? " AND " : "");
            where.append("user_info.maiden_name_kana = " + DbS.chara(getMaidenNameKana()));
        }

        if(where.length()>0)
        {
            return "where " + where.toString();
        }
        return "";
    }

    /**
     * user_info ユーザ情報テーブルの並べ替え順序を設定する。.
     *
     * @param sortKey
     * @return Stringソート句の文字列
     */
    private String dbOrder(LinkedHashMap<String, String> sortKey)
    {
        String str = "";
        if (sortKey == null) return "";
        Set<String> keySet = sortKey.keySet();
        for (Iterator<String> i = keySet.iterator(); i.hasNext();)
        {
            String key = i.next();
            if (null == fieldsArray.get(key)) continue;
            str += !"".equals(str) ? " , " : "";
            String ss[] = fieldsArray.get(key).split(",");
            for (int j = 0; j < ss.length; j++)
            {
                if (j != 0) str += ",";
                str += ss[j] + ' ' + sortKey.get(key);
            }
        }
        str = "".equals(str) ? "" : (" order by " + str);
        return str;
    }
    /**
	 * ログイン処理のチェックを行う。
	 *
	 * @param pAccount アカウントまたはメールアドレス
	 * @param pPassword パスワード
	 * @return 0::失敗 1:成功 2:管理者ログイン
	 * @throws AtareSysException
	 *         エラー
	 */
	public boolean login(String pAccount, String pPassword) throws AtareSysException {
		String sql = "";
		sql = " SELECT user_info.*"
				+ " FROM user_info "
				+ " WHERE "
				+ " ( user_info_id  = " + DbS.chara(pAccount)
				+ " or memail = " + DbS.chara(pAccount) + " ) ";
		List<HashMap<String, String>> rs = DbBase.dbSelect(sql);
		if (1 != rs.size())
			return false;
		HashMap<String, String> map = rs.get(0);
		setUserInfoDao(map, this);
		String password = Digest.hex(Digest.SHA512, pPassword);
		if (!password.equals(DbI.chara(map.get("password")))) {
			return false;
		}
		return true;
	}

}