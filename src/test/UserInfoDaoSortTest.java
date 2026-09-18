package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;

import jp.swell.dao.UserInfoDao;  

class UserInfoDaoSortTest {

	@Test
	void ソート指定なしの場合空文字になる() {
		UserInfoDao dao = new UserInfoDao();
		
		String order = dao.dbOrder(null);
		
		assertEquals("",order);
		
	}
	
	@Test
	void 氏名を昇順ソートできる() {
		UserInfoDao dao = new UserInfoDao();
		
		LinkedHashMap<String,String> sortKey = new LinkedHashMap<>();
		sortKey.put("last_name_kana", "asc");
		
		String order = dao.dbOrder(sortKey);
		
		assertTrue(order.toLowerCase().contains("order by"));
		assertTrue(order.contains("last_name_kana"));
		assertTrue(order.toLowerCase().contains("asc"));
		
	}
	
	@Test
	void 氏名を降順でソートできる() {
        UserInfoDao dao = new UserInfoDao();
		
		LinkedHashMap<String,String> sortKey = new LinkedHashMap<>();
		sortKey.put("last_name_kana", "desc");
		
		String order = dao.dbOrder(sortKey);
		
		assertTrue(order.toLowerCase().contains("order by"));
		assertTrue(order.contains("last_name_kana"));
		assertTrue(order.toLowerCase().contains("desc"));
		
	}

}
