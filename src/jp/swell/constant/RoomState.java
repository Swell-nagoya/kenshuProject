package jp.swell.constant;

import java.util.ArrayList;

public class RoomState
{
	public static final String AVAILABLE = "1";             //利用可能
	public static final String UNAVAILABLE = "2";           //利用不可
	public static final String UNDER_MAINTENANCE = "8";     //メンテナンス中
	//	public static final String DELETE = "9";               //削除
	/**
	 * status  状態
	 */
	private String state;
	/**
	 * statusName  状態名
	 */
	private String stateName;
	/**
	 * statusClass  クラス名
	 */
	private String stateClass;

	static ArrayList<RoomState> stateList;

	/**
	 * 静的初期化ブロック
	 */
	static
	{
		stateList = new ArrayList<RoomState>();
		RoomState cls = new RoomState();
		cls.state = AVAILABLE;
		cls.stateClass = "Available";
		cls.stateName = "利用可能";
		stateList.add(cls);
		cls = new RoomState();
		cls.state = UNAVAILABLE;
		cls.stateClass = "Unavailable";
		cls.stateName = "利用不可";
		stateList.add(cls);
		cls = new RoomState();
		cls.state = UNDER_MAINTENANCE;
		cls.stateClass = "UnderMaintenance";
		cls.stateName = "メンテナンス中";
		stateList.add(cls);
		cls = new RoomState();
		//		cls.state = DELETE;
		//		cls.stateClass = "Delete";
		//		cls.stateName = "削除";
		//		stateList.add(cls);
	}

	public static String getStateName(String state)
	{
		return stateList.stream()
				.filter(s -> s.state.equals(state))
				.findFirst()
				.map(s -> s.stateName)
				.orElse("");
	}

	public static String getStateClass(String state)
	{
		return stateList.stream()
				.filter(s -> s.state.equals(state))
				.findFirst()
				.map(s -> s.stateClass)
				.orElse("unknown");   			
	}

	public static ArrayList<RoomState> getList()
	{
		return stateList;
	}

	public String getState() {
		return state;
	}

	public String getStateName() {
		return stateName;
	}

}
