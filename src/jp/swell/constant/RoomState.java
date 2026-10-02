package jp.swell.constant;

import java.util.ArrayList;

public class RoomState
{
    public static final String Available = "1";    //利用可
    public static final String InUse = "2";        //使用中
    public static final String Maintenance = "3";   //メンテナンス中
    public static final String Deleted = "9";      //削除

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

    static ArrayList<RoomState> RoomState = null;

    /**
     * コンストラクタ。.
     */
    static
    {
        RoomState = new ArrayList<RoomState>();
        RoomState cls = new RoomState();
        cls.state = Available;
        cls.stateClass = "Available";
        cls.stateName = "利用可";
        RoomState.add(cls);
        cls = new RoomState();
        cls.state = InUse;
        cls.stateClass = "InUse";
        cls.stateName = "使用中";
        RoomState.add(cls);
        cls = new RoomState();
        cls.state = Maintenance;
        cls.stateClass = "Maintenance";
        cls.stateName = "メンテナンス中";
        RoomState.add(cls);
        cls = new RoomState();
        cls.state = Deleted;
        cls.stateClass = "Deleted";
        cls.stateName = "削除";
        RoomState.add(cls);
    }

    /**
     * 状態フラグから状態名を取得する。.
     *
     * @param pStateFlg 状態フラグ
     * @return 状態名
     */
    public String getStateName(String pStateFlg)
    {
        for (int i = 0; i < RoomState.size(); i++)
        {
            if (RoomState.get(i).state.equals(pStateFlg))
            {
                return RoomState.get(i).stateName;
            }
        }
        return "";
    }

    /**
     * 選択肢一覧を取得する。.
     *
     * @return 状態の一覧
     */
    public ArrayList<RoomState> getList()
    {
        return RoomState;
    }

    /**
     * @return state 状態フラグ
     */
    public String getState() {
        return state;
    }

    /**
     * @return stateName 状態名
     */
    public String getStateName() {
        return stateName;
    }
}
