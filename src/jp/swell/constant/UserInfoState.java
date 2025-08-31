package jp.swell.constant;

import java.util.ArrayList;

import jp.patasys.common.AtareSysException;

public class UserInfoState
{
    public static final String InterimRegistration = "0"; //仮登録
    public static final String Registration = "1";        //登録中
    public static final String LimitRegistration = "2";   //制限中
    public static final String UnderSuspension = "8";     //停止中
    public static final String Delete = "9" ;             //削除
    /**
     * status  状態
     */
    private String state = "";
    /**
     * statusName  状態名
     */
    private String stateName = "";
    /**
     * statusClass  クラス名
     */
    private String stateClass = "";

    static ArrayList<UserInfoState> UserInfoState = null;

    /**
     * コンストラクタ。.
     */
    static
    {
        UserInfoState = new ArrayList<UserInfoState>();
    	UserInfoState cls = new UserInfoState();
    	cls.state = InterimRegistration;
    	cls.stateClass = "InterimRegistration";
    	cls.stateName = "仮登録";
    	UserInfoState.add(cls);
        cls = new UserInfoState();
    	cls.state = Registration ;
    	cls.stateClass = "Registration";
    	cls.stateName = "登録";
    	UserInfoState.add(cls);
        cls = new UserInfoState();
    	cls.state = LimitRegistration ;
    	cls.stateClass = "LimitRegistration";
    	cls.stateName = "制限中";
    	UserInfoState.add(cls);
        cls = new UserInfoState();
    	cls.state = UnderSuspension;
    	cls.stateClass = "UnderSuspension";
    	cls.stateName = "停止";
    	UserInfoState.add(cls);
        cls = new UserInfoState();
    	cls.state = Delete ;
    	cls.stateClass = "Delete";
    	cls.stateName = "削除";
    	UserInfoState.add(cls);
    }

    /**
     * user_employment_kind 契約雇用形態テーブルを検索し指定されたレコードのリストを返す。.
     *
     * @return 取得したUserInfoStatusDaoの配列
     * @throws AtareSysException フレームワーク共通例外
     */
    public String getStateName(String pStateFlg)
    {
    	for(int i=0;i<UserInfoState.size();i++)
    	{
    		if(pStateFlg.equals(UserInfoState.get(i).state))
    		{
    			return UserInfoState.get(i).stateName;
    		}
    	}
    	return "";
    }

    /**
     * user_employment_kind 契約雇用形態テーブルを検索し指定されたレコードのリストを返す。.
     *
     * @return 取得したUserInfoStatusDaoの配列
     * @throws AtareSysException フレームワーク共通例外
     */
    public ArrayList<UserInfoState> getList()
    {
    	return UserInfoState;
    }

    /**
     * user_employment_kind 契約雇用形態テーブルを検索し指定されたレコードのリストを返す。.
     *
     * @return 取得したUserInfoStatusDaoの配列
     * @throws AtareSysException フレームワーク共通例外
     */
    public ArrayList<Object> getListHtml()
    {
        ArrayList<Object> array = new ArrayList<Object>();
        for(int i=0;i<UserInfoState.size();i++)
        {
        	array.add(UserInfoState.get(i));
        }
        return array;
    }

	/**
	 * userEmploymentKindId  契約雇用形態IDを取得します。
	 * @return userEmploymentKindId  契約雇用形態ID
	 */
	public String getState() {
	    return state;
	}

	/**
	 * kindName  契約雇用形態名を取得します。
	 * @return kindName  契約雇用形態名
	 */
	public String getStateName() {
	    return stateName;
	}

	/**
	 * 0:仮登録 1:通常 2:制限利用 8:停止 9:削除を取得する
	 *
	 * @return stateFlg 0:通常 1:停止 9:削除
	 */
	public String getStateClass(String pStateFlg)
	{
    	for(int i=0;i<UserInfoState.size();i++)
    	{
    		if(pStateFlg.equals(UserInfoState.get(i).state))
    		{
    			return UserInfoState.get(i).stateClass;
    		}
    	}
		return "unknown";
	}

}
