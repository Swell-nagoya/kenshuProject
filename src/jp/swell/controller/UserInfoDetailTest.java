package jp.swell.controller; // ※テスト対象と同じパッケージ（jp.swell.controller）にするとprivateでもアクセスしやすくなります

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jp.patasys.common.http.WebBean;
import jp.swell.dao.UserInfoDao;

class UserInfoDetailTest {
 private WebBean bean;
 private UserInfoDetail detail;
 private UserInfoDao pUserInfoDao;
 @BeforeEach
 void setUp() {
     bean = new WebBean();
     detail = new UserInfoDetail();
     pUserInfoDao = new UserInfoDao();
 }
 
	void testInputCheckName1() throws Exception {

  bean.setValue("last_name", ""); 
  bean.setValue("first_name", "");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.nameCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("氏名を入力してください。", errorSet.get("last_name"));
  assertEquals("", errorSet.get("first_name"));
  System.out.println("エラーメッセージ（氏名：名字）: " + errorSet.get("last_name"));
  System.out.println("エラーメッセージ（氏名：名前）: " + errorSet.get("first_name"));
	}
	
	@Test
	void testInputCheckName2() throws Exception {

  bean.setValue("last_name", ""); 
  bean.setValue("first_name", "テスト");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.nameCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("名字を入力してください。", errorSet.get("last_name"));
  System.out.println("エラーメッセージ（氏名：名字）: " + errorSet.get("last_name"));
	}
	
	@Test
	void testInputCheckName3() throws Exception {

  bean.setValue("last_name", "テスト"); 
  bean.setValue("first_name", "");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.nameCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("名前を入力してください。", errorSet.get("first_name"));
  System.out.println("エラーメッセージ（氏名：名前）: " + errorSet.get("first_name"));
	}

	@Test
	void testInputCheckNameKana1() throws Exception {

  bean.setValue("last_name_kana", ""); 
  bean.setValue("first_name_kana", "");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.nameKanaCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("氏名のよみを入力してください。", errorSet.get("last_name_kana"));
  assertEquals("", errorSet.get("first_name_kana"));
  System.out.println("エラーメッセージ（氏名よみ：名字）: " + errorSet.get("last_name_kana"));
  System.out.println("エラーメッセージ（氏名よみ：名前）: " + errorSet.get("first_name_kana"));
	}
	
	@Test
	void testInputCheckNameKana2() throws Exception {

  bean.setValue("last_name_kana", ""); 
  bean.setValue("first_name_kana", "てすと");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.nameKanaCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("名字のよみを入力してください。", errorSet.get("last_name_kana"));
  System.out.println("エラーメッセージ（氏名よみ：名字）: " + errorSet.get("last_name_kana"));
	}
	
	@Test
	void testInputCheckNameKana3() throws Exception {

  bean.setValue("last_name_kana", ""); 
  bean.setValue("first_name_kana", "てすとA");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.nameKanaCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("名字のよみを入力してください。", errorSet.get("last_name_kana"));
  assertEquals("名前のよみはひらがなで入力してください。", errorSet.get("first_name_kana"));
  System.out.println("エラーメッセージ（氏名よみ：姓）: " + errorSet.get("last_name_kana"));
  System.out.println("エラーメッセージ（氏名よみ：名前）: " + errorSet.get("first_name_kana"));
	}

	@Test
	void testInputCheckNameKana4() throws Exception {
		
  bean.setValue("last_name_kana", "てすと"); 
  bean.setValue("first_name_kana", "");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.nameKanaCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("名前のよみを入力してください。", errorSet.get("first_name_kana"));
  System.out.println("エラーメッセージ（氏名よみ：名前）: " + errorSet.get("first_name_kana"));
	}
	
	@Test
	void testInputCheckNameKana5() throws Exception {
		
  bean.setValue("last_name_kana", "てすとA"); 
  bean.setValue("first_name_kana", "");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.nameKanaCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("氏名のよみはひらがなで入力してください。", errorSet.get("last_name_kana"));
  assertEquals("名前のよみを入力してください。", errorSet.get("first_name_kana"));
  System.out.println("エラーメッセージ（氏名よみ：名字）: " + errorSet.get("last_name_kana"));
  System.out.println("エラーメッセージ（氏名よみ：名前）: " + errorSet.get("first_name_kana"));
	}
	
	@Test
	void testInputCheckNameKana6() throws Exception {
		
  bean.setValue("last_name_kana", "てすとA"); 
  bean.setValue("first_name_kana", "てすとA");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.nameKanaCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("氏名のよみはひらがなで入力してください。", errorSet.get("last_name_kana"));
  assertEquals("名前のよみはひらがなで入力してください。", errorSet.get("first_name_kana"));
  System.out.println("エラーメッセージ（氏名よみ：名字）: " + errorSet.get("last_name_kana"));
  System.out.println("エラーメッセージ（氏名よみ：名前）: " + errorSet.get("first_name_kana"));
	}

	@Test
	void middleNameCheck1() throws Exception {

  bean.setValue("middle_name", "てすと"); 
  bean.setValue("middle_name_kana", "");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.middleNameCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals(null, errorSet.get("middle_name"));
  assertEquals("ミドルネームのよみを入力してください。", errorSet.get("middle_name_kana"));
  System.out.println("エラーメッセージ（ミドルネーム）: " + errorSet.get("middle_name"));
  System.out.println("エラーメッセージ（ミドルネームよみ）: " + errorSet.get("middle_name_kana"));
	}

	@Test
	void middleNameCheck2() throws Exception {

  bean.setValue("middle_name", "てすと"); 
  bean.setValue("middle_name_kana", "てすとA");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.middleNameCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals(null, errorSet.get("middle_name"));
  assertEquals("ミドルネームのよみはひらがなで入力してください。", errorSet.get("middle_name_kana"));
  System.out.println("エラーメッセージ（ミドルネーム）: " + errorSet.get("middle_name"));
  System.out.println("エラーメッセージ（ミドルネームよみ）: " + errorSet.get("middle_name_kana"));
	}

	@Test
	void middleNameCheck3() throws Exception {
		
  bean.setValue("middle_name", ""); 
  bean.setValue("middle_name_kana", "てすと");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.middleNameCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("ミドルネームを入力してください。", errorSet.get("middle_name"));
  assertEquals(null, errorSet.get("middle_name_kana"));
  System.out.println("エラーメッセージ（ミドルネーム）: " + errorSet.get("middle_name"));
  System.out.println("エラーメッセージ（ミドルネームよみ）: " + errorSet.get("middle_name_kana"));
	}

	@Test
	void middleNameCheck4() throws Exception {

  bean.setValue("middle_name", "てすとA"); 
  bean.setValue("middle_name_kana", "てすと");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.middleNameCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals(null, errorSet.get("middle_name"));
  assertEquals(null, errorSet.get("middle_name_kana"));
  System.out.println("エラーメッセージ（ミドルネーム）: " + errorSet.get("middle_name"));
  System.out.println("エラーメッセージ（ミドルネームよみ）: " + errorSet.get("middle_name_kana"));
	}

	@Test
	void maidenNameCheck1() throws Exception {
		
  bean.setValue("maiden_name", "てすと"); 
  bean.setValue("maiden_name_kana", "");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.maidenNameCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals(null, errorSet.get("maiden_name"));
  assertEquals("旧姓のよみを入力してください。", errorSet.get("maiden_name_kana"));
  System.out.println("エラーメッセージ（旧姓）: " + errorSet.get("maiden_name"));
  System.out.println("エラーメッセージ（旧姓よみ）: " + errorSet.get("maiden_name_kana"));
	}

	@Test
	void maidenNameCheck2() throws Exception {

  bean.setValue("maiden_name", "てすと"); 
  bean.setValue("maiden_name_kana", "てすとA");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.maidenNameCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals(null, errorSet.get("maiden_name"));
  assertEquals("旧姓のよみはひらがなで入力してください。", errorSet.get("maiden_name_kana"));
  System.out.println("エラーメッセージ（旧姓）: " + errorSet.get("maiden_name"));
  System.out.println("エラーメッセージ（旧姓よみ）: " + errorSet.get("maiden_name_kana"));
	}

	@Test
	void maidenNameCheck3() throws Exception {

  bean.setValue("maiden_name", ""); 
  bean.setValue("maiden_name_kana", "てすと");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.maidenNameCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("旧姓を入力してください。", errorSet.get("maiden_name"));
  assertEquals(null, errorSet.get("maiden_name_kana"));
  System.out.println("エラーメッセージ（旧姓）: " + errorSet.get("maiden_name"));
  System.out.println("エラーメッセージ（旧姓よみ）: " + errorSet.get("maiden_name_kana"));
	}

	@Test
	void maidenNameCheck4() throws Exception {

  bean.setValue("maiden_name", "てすとA"); 
  bean.setValue("maiden_name_kana", "てすと");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.maidenNameCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals(null, errorSet.get("maiden_name"));
  assertEquals(null, errorSet.get("maiden_name_kana"));
  System.out.println("エラーメッセージ（旧姓）: " + errorSet.get("maiden_name"));
  System.out.println("エラーメッセージ（旧姓よみ）: " + errorSet.get("maiden_name_kana"));
	}

	@Test
	void memailCheckIns1() throws Exception {

  bean.setValue("memail", "");
  bean.setValue("request_cmd", "ins");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.memailCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("メールアドレスを入力してください。", errorSet.get("memail"));
  System.out.println("エラーメッセージ（メールアドレス）: " + errorSet.get("memail"));
	}
	
	@Test
	void memailCheckIns2() throws Exception {

  bean.setValue("memail", "testAtestA");
  bean.setValue("request_cmd", "ins");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.memailCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("正しいメールアドレスを入力してください。", errorSet.get("memail"));
  System.out.println("エラーメッセージ（メールアドレス）: " + errorSet.get("memail"));
	}
	
	@Test
	void memailCheckIns3() throws Exception {

  bean.setValue("memail", "aaa@gmail.com");
  bean.setValue("request_cmd", "ins");
  
  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.memailCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("このメールアドレスは既に登録されています。", errorSet.get("memail"));
  System.out.println("エラーメッセージ（メールアドレス）: " + errorSet.get("memail"));
	}
	
	@Test
	void memailCheckIns4() throws Exception {

  bean.setValue("memail", "a@gmail.com");
  bean.setValue("request_cmd", "ins");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.memailCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals(null, errorSet.get("memail"));
  System.out.println("エラーメッセージ（メールアドレス）: " + errorSet.get("memail"));
	}

	@Test
	void memailCheckUpdate1() throws Exception {

  bean.setValue("memail", "");
  bean.setValue("request_cmd", "update");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.memailCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("メールアドレスを入力してください。", errorSet.get("memail"));
  System.out.println("エラーメッセージ（メールアドレス）: " + errorSet.get("memail"));
	}

	@Test
	void memailCheckUpdate2() throws Exception {
		
  bean.setValue("memail", "testAtestA");
  bean.setValue("request_cmd", "update");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.memailCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("正しいメールアドレスを入力してください。", errorSet.get("memail"));
  System.out.println("エラーメッセージ（メールアドレス）: " + errorSet.get("memail"));
	}

	@Test
	void memailCheckUpdate3() throws Exception {
		
  bean.setValue("memail", "aaa@gmail.com");
  bean.setValue("request_cmd", "update");
  
  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.memailCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("このメールアドレスは既に登録されています。", errorSet.get("memail"));
  System.out.println("エラーメッセージ（メールアドレス）: " + errorSet.get("memail"));
	}
	
	@Test
	void memailCheckUpdate4() throws Exception {
		
  bean.setValue("memail", "a@gmail.com");
  bean.setValue("request_cmd", "update");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.memailCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals(null, errorSet.get("memail"));
  System.out.println("エラーメッセージ（メールアドレス）: " + errorSet.get("memail"));
	}

	@Test
	void adminCheck1() throws Exception {

  bean.setValue("admin", "");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.adminCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("ユーザー区分を選択してください。", errorSet.get("admin"));
  System.out.println("エラーメッセージ（ユーザー区分）: " + errorSet.get("admin"));
	}
	
	@Test
	void adminCheck2() throws Exception {

  bean.setValue("admin", "0");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.adminCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals(null, errorSet.get("admin"));
  System.out.println("エラーメッセージ（ユーザー区分）: " + errorSet.get("admin"));
	}
	
	@Test
	void adminCheck3() throws Exception {
		
  bean.setValue("admin", "1");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.adminCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals(null, errorSet.get("admin"));
  System.out.println("エラーメッセージ（ユーザー区分）: " + errorSet.get("admin"));
	}
	
	@Test
	void insertUserIdCheckIns1() throws Exception {

  bean.setValue("insert_user_id", "");
  bean.setValue("request_cmd", "ins");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.insertUserIdCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals(null, errorSet.get("insert_user_id"));
  System.out.println("エラーメッセージ（任意ＩＤ）: " + errorSet.get("insert_user_id"));
	}
	
	@Test
	void insertUserIdCheckIns2() throws Exception {

  bean.setValue("insert_user_id", "てすとA");
  bean.setValue("request_cmd", "ins");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.insertUserIdCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("ＩＤは６文字以上１２文字以下で入力してください。", errorSet.get("insert_user_id"));
  System.out.println("エラーメッセージ（任意ＩＤ）: " + errorSet.get("insert_user_id"));
	}
	
	@Test
	void insertUserIdCheckIns3() throws Exception {

  bean.setValue("insert_user_id", "testA");
  bean.setValue("request_cmd", "ins");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.insertUserIdCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("ＩＤは６文字以上１２文字以下で入力してください。", errorSet.get("insert_user_id"));
  System.out.println("エラーメッセージ（任意ＩＤ）: " + errorSet.get("insert_user_id"));
	}
	
	@Test
	void insertUserIdCheckIns4() throws Exception {

  bean.setValue("insert_user_id", "てすとAてすとA");
  bean.setValue("request_cmd", "ins");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.insertUserIdCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("ＩＤは半角英数で入力してください。", errorSet.get("insert_user_id"));
  System.out.println("エラーメッセージ（任意ＩＤ）: " + errorSet.get("insert_user_id"));
	}

	@Test
	void insertUserIdCheckIns5() throws Exception {

  bean.setValue("insert_user_id", "testAtestA");
  bean.setValue("request_cmd", "ins");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.insertUserIdCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals(null, errorSet.get("insert_user_id"));
  System.out.println("エラーメッセージ（任意ＩＤ）: " + errorSet.get("insert_user_id"));
	}

	@Test
	void insertUserIdCheckIns6() throws Exception {
		
  bean.setValue("user_info_id", "EHJF00001");
  bean.setValue("insert_user_id", "testid");
  bean.setValue("request_cmd", "ins");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.insertUserIdCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("このＩＤは既に登録されています。", errorSet.get("insert_user_id"));
  System.out.println("エラーメッセージ（任意ＩＤ）: " + errorSet.get("insert_user_id"));
	}
	
	@Test
	void insertUserIdCheckUpdate1() throws Exception {

  bean.setValue("insert_user_id", "testAtestA");
  bean.setValue("main_key", "mainKey");
  bean.setValue("request_cmd", "update");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.insertUserIdCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals(null, errorSet.get("insert_user_id"));
  System.out.println("エラーメッセージ（任意ＩＤ）: " + errorSet.get("insert_user_id"));
	}

	@Test
	void insertUserIdCheckUpdate2() throws Exception {

  bean.setValue("insert_user_id", "てすとA");
  bean.setValue("main_key", "mainKey");
  bean.setValue("request_cmd", "update");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.insertUserIdCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("ＩＤは６文字以上１２文字以下で入力してください。", errorSet.get("insert_user_id"));
  System.out.println("エラーメッセージ（任意ＩＤ）: " + errorSet.get("insert_user_id"));
	}

	@Test
	void insertUserIdCheckUpdate3() throws Exception {

  bean.setValue("insert_user_id", "testA");
  bean.setValue("main_key", "mainKey");
  bean.setValue("request_cmd", "update");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.insertUserIdCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("ＩＤは６文字以上１２文字以下で入力してください。", errorSet.get("insert_user_id"));
  System.out.println("エラーメッセージ（任意ＩＤ）: " + errorSet.get("insert_user_id"));
	}
	
	@Test
	void insertUserIdCheckUpdate4() throws Exception {
		
  bean.setValue("insert_user_id", "てすとAてすとA");
  bean.setValue("main_key", "mainKey");
  bean.setValue("request_cmd", "update");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.insertUserIdCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("ＩＤは半角英数で入力してください。", errorSet.get("insert_user_id"));
  System.out.println("エラーメッセージ（任意ＩＤ）: " + errorSet.get("insert_user_id"));
	}

	@Test
	void insertUserIdCheckUpdate5() throws Exception {
		
  bean.setValue("insert_user_id", "testAtestA");
  bean.setValue("main_key", "mainKey");
  bean.setValue("request_cmd", "update");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.insertUserIdCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals(null, errorSet.get("insert_user_id"));
  System.out.println("エラーメッセージ（任意ＩＤ）: " + errorSet.get("insert_user_id"));
	}
	
	@Test
	void insertUserIdCheckUpdate6() throws Exception {

  bean.setValue("user_info_id", "EHJF00001");
  bean.setValue("insert_user_id", "testid");
  bean.setValue("main_key", "mainKey");
  bean.setValue("request_cmd", "update");

  // テスト対象のそっどを実行。エラーメッセージを受け取る
  HashMap<String, String> errorSet = detail.insertUserIdCheck(bean,pUserInfoDao);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("このＩＤは既に登録されています。", errorSet.get("insert_user_id"));
  System.out.println("エラーメッセージ（任意ＩＤ）: " + errorSet.get("insert_user_id"));
	}
	
}