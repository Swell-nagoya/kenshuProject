package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import jp.swell.controller.RoomDetail;

import jp.swell.dao.RoomDao;

class RoomDetailTest {

	@Test
	void 部屋名が空欄ならエラーになる() throws Exception {
		
		RoomDetail roomDetail = new RoomDetail();
		RoomDao roomDao = new RoomDao();
		HashMap<String,String> errors = new HashMap<>();
		
		roomDetail.checkRoomName( "","", roomDao, errors);
		assertEquals( "部屋名を入力してください。", errors.get("room_name_empty"));
	}
	
	@Test
	void 部屋名が重複ならエラーになる() throws Exception {
		
		RoomDetail roomDetail = new RoomDetail();
		RoomDao roomDao = new RoomDao();
		HashMap<String, String> errors = new HashMap<>();
		
		roomDetail.checkRoomName("応接室01", "" , roomDao, errors);
		
		assertEquals("この部屋名は既に登録されています。",errors.get("room_name_duplicate"));
	}
	
	@Test
	void 部屋名が正常ならエラーにならない() throws Exception {
		
		RoomDetail roomDetail = new RoomDetail();
		RoomDao roomDao = new RoomDao();
		HashMap<String, String> errors = new HashMap<>();
		
		roomDetail.checkRoomName( "JUnitテスト用","",  roomDao, errors);
		assertTrue(errors.isEmpty());
	}

}
