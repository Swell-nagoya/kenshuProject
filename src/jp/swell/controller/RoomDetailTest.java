package jp.swell.controller; // ※テスト対象と同じパッケージ（jp.swell.controller）にするとprivateでもアクセスしやすくなります

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jp.patasys.common.http.WebBean;
import jp.swell.dao.RoomDao;

class RoomDetailTest {
 private WebBean bean;
 private RoomDao pRoomDao;
 private RoomDetail roomDetail;
 
 @BeforeEach
 void setUp() {
     bean = new WebBean();
     roomDetail = new RoomDetail();
     roomDetail.setWebBean(bean);
     pRoomDao = new RoomDao();
 }

 /* 
  *部屋名が空の場合.
  *（新規登録画面）
 */
	@Test
	void testInputCheck1() throws Exception {

		bean.setValue("room_name", ""); // 部屋名
  bean.setValue("request_cmd", "insert"); // 新規登録としてテスト
		
		boolean isValid = roomDetail.inputCheck(pRoomDao);
		assertFalse(isValid);

		HashMap<String, String> errorSet = bean.getItemErrors();

		assertEquals("部屋名を入力してください。", errorSet.get("room_name_empty"));

	}

 /* 
  *部屋名が空の場合.
  *（更新画面）
 */
	@Test
	void testInputCheck2() throws Exception {

  bean.setValue("before_name", "TESTB"); // 更新前の自身の部屋名
		bean.setValue("room_name", ""); // 部屋名
  bean.setValue("request_cmd", "update"); // 更新登録としてテスト
		
		boolean isValid = roomDetail.inputCheck(pRoomDao);
		assertFalse(isValid);

		HashMap<String, String> errorSet = bean.getItemErrors();

		assertEquals("部屋名を入力してください。", errorSet.get("room_name_empty"));

	}

 /* 
  * DBに登録済みの部屋名と新規登録の部屋名が一致する場合.
  *（新規登録画面）
  * 事前準備: room.room_name DBに部屋名を登録する
 */
	@Test
	void testInputCheck3() throws Exception {

     bean.setValue("room_name", "牢屋"); // DBに登録済みの部屋名
	    bean.setValue("request_cmd", "insert"); // 新規登録としてテスト
	    
	    boolean isValid = roomDetail.inputCheck(pRoomDao);
	    
	    assertFalse(isValid); 

	    HashMap<String, String> errorSet = bean.getItemErrors();
	    
	    assertEquals("同一の部屋名が登録済みです。別の名前を入力してください。", errorSet.get("room_name_duplicate"));
	}

 /* 
  * DBに登録済みの部屋名と新規登録の部屋名が一致する場合.
  *（更新画面）
  * 事前準備: room.room_name DBに部屋名を登録する
 */
	@Test
	void testInputCheck4() throws Exception {

     bean.setValue("before_name", "TESTB"); 
     bean.setValue("room_name", "牢屋"); // DBに登録済みの部屋名
	    bean.setValue("request_cmd", "update"); // 新規登録としてテスト
	    
	    boolean isValid = roomDetail.inputCheck(pRoomDao);
	    
	    assertFalse(isValid); 

	    HashMap<String, String> errorSet = bean.getItemErrors();
	    
	    assertEquals("同一の部屋名が登録済みです。別の名前を入力してください。", errorSet.get("room_name_duplicate"));
	}
	
 /* 
  *変更前の部屋名と更新する部屋名が一致する場合.
  *（更新画面）
 */
	@Test
	void testInputCheck5() throws Exception {

	    bean.setValue("before_name", "TESTA"); // 更新前の自身の部屋名
	    bean.setValue("room_name", "TESTA");  // 部屋名
	    bean.setValue("request_cmd", "update"); // 更新としてテスト
	    
	    boolean isValid = roomDetail.inputCheck(pRoomDao);
	    
	    assertFalse(isValid); 

	    HashMap<String, String> errorSet = bean.getItemErrors();
	    
	    assertEquals("部屋名が以前と同じです。別の名前を入力してください。", errorSet.get("room_name_duplicate"));
	}

 /* 
  *部屋名を新規登録する場合.
  *（新規登録画面）
 */
	@Test
	void testInputCheck6() throws Exception {
		
	    bean.setValue("room_name", "TESTA"); // 部屋名
	    bean.setValue("request_cmd", "insert"); // 新規登録としてテスト
	    
	    boolean isValid = roomDetail.inputCheck(pRoomDao);
	    
	    assertTrue(isValid); 

	    HashMap<String, String> errorSet = bean.getItemErrors();
	    
	    assertEquals( null, errorSet.get("room_name_duplicate"));
	}

 /* 
  *部屋名を更新する場合.
  *（更新画面）
 */
	@Test
	void testInputCheck7() throws Exception {

	    bean.setValue("before_name", "TESTB"); 
	    bean.setValue("room_name", "TESTA"); // 部屋名
	    bean.setValue("request_cmd", "update"); // 更新としてテスト
	    
	    boolean isValid = roomDetail.inputCheck(pRoomDao);
	    
	    assertTrue(isValid); 

	    HashMap<String, String> errorSet = bean.getItemErrors();
	    
	    assertEquals( null, errorSet.get("room_name_duplicate"));
	}
}