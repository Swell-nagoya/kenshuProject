package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import jp.swell.dao.RoomDao;

class RoomDaoTest {

	@Test
	void 同じ部屋名が存在する場合trueになる() throws Exception{
		RoomDao dao = new RoomDao();
		boolean result = dao.isRoomNameExists("mtgroom1",null);
		assertTrue(result);
	}
	
	@Test
	void 更新時は自分自身の部屋を重複扱いしない() throws Exception { 
		RoomDao dao = new RoomDao();
		boolean result = dao.isRoomNameExists("mtgroom1","1");
		assertFalse(result);
	}
	
	@Test
	void 更新時に他の部屋と同じ名前なら重複になる() throws Exception { 
		RoomDao dao = new RoomDao();
		boolean result = dao.isRoomNameExists("部屋", "1");
		assertTrue(result);
	}
	
	@Test
	void 新規登録時に存在しない部屋名なら重複しない() throws Exception {
		RoomDao dao = new RoomDao();
		boolean result = dao.isRoomNameExists("JUnitテスト用_存在しない部屋_99999",null);
		assertFalse(result);
	}

}
