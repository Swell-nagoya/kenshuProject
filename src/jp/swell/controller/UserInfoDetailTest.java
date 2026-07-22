package jp.swell.controller; // ※テスト対象と同じパッケージ（jp.swell.controller）にするとprivateでもアクセスしやすくなります

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import jp.patasys.common.http.WebBean;

class UserInfoDetailTest {
	@Test
	void testInputCheckName1() throws Exception {

  WebBean bean = new WebBean();
  UserInfoDetail detail = new UserInfoDetail(); 
  bean.setValue("last_name", ""); 
  bean.setValue("first_name", "");

  // テスト対象のメソッドを実行し、戻り値を受け取る
  HashMap<String, String> errorSet = detail.nameCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("氏名を入力してください。", errorSet.get("last_name"));
  assertEquals("", errorSet.get("first_name"));
  System.out.println("エラーメッセージ（氏名：名字）: " + errorSet.get("last_name"));
  System.out.println("エラーメッセージ（氏名：名前）: " + errorSet.get("first_name"));
	}
	@Test
	void testInputCheckName2() throws Exception {

  WebBean bean = new WebBean();
  UserInfoDetail detail = new UserInfoDetail(); 
  bean.setValue("last_name", ""); 
  bean.setValue("first_name", "テスト");

  // テスト対象のメソッドを実行し、戻り値を受け取る
  HashMap<String, String> errorSet = detail.nameCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("名字を入力してください。", errorSet.get("last_name"));
  System.out.println("エラーメッセージ（氏名：名字）: " + errorSet.get("last_name"));
	}
	@Test
	void testInputCheckName3() throws Exception {

  WebBean bean = new WebBean();
  UserInfoDetail detail = new UserInfoDetail(); 
  bean.setValue("last_name", "テスト"); 
  bean.setValue("first_name", "");

  // テスト対象のメソッドを実行し、戻り値を受け取る
  HashMap<String, String> errorSet = detail.nameCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("名前を入力してください。", errorSet.get("first_name"));
  System.out.println("エラーメッセージ（氏名：名前）: " + errorSet.get("first_name"));
	}

	@Test
	void testInputCheckNameKana1() throws Exception {

  WebBean bean = new WebBean();
  UserInfoDetail detail = new UserInfoDetail(); 
  bean.setValue("last_name_kana", ""); 
  bean.setValue("first_name_kana", "");

  // テスト対象のメソッドを実行し、戻り値を受け取る
  HashMap<String, String> errorSet = detail.nameKanaCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("氏名のよみを入力してください。", errorSet.get("last_name_kana"));
  assertEquals("", errorSet.get("first_name_kana"));
  System.out.println("エラーメッセージ（氏名のよみ：名字）: " + errorSet.get("last_name_kana"));
  System.out.println("エラーメッセージ（氏名のよみ：名前）: " + errorSet.get("first_name_kana"));
	}
	@Test
	void testInputCheckNameKana2() throws Exception {

  WebBean bean = new WebBean();
  UserInfoDetail detail = new UserInfoDetail(); 
  bean.setValue("last_name_kana", ""); 
  bean.setValue("first_name_kana", "てすと");

  // テスト対象のメソッドを実行し、戻り値を受け取る
  HashMap<String, String> errorSet = detail.nameKanaCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("名字のよみを入力してください。", errorSet.get("last_name_kana"));
  System.out.println("エラーメッセージ（氏名のよみ：名字）: " + errorSet.get("last_name_kana"));
	}
	@Test
	void testInputCheckNameKana3() throws Exception {

  WebBean bean = new WebBean();
  UserInfoDetail detail = new UserInfoDetail(); 
  bean.setValue("last_name_kana", ""); 
  bean.setValue("first_name_kana", "てすとA");

  // テスト対象のメソッドを実行し、戻り値を受け取る
  HashMap<String, String> errorSet = detail.nameKanaCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("名字のよみを入力してください。", errorSet.get("last_name_kana"));
  assertEquals("名前のよみはひらがなで入力してください。", errorSet.get("first_name_kana"));
  System.out.println("エラーメッセージ（氏名のよみ：姓）: " + errorSet.get("last_name_kana"));
  System.out.println("エラーメッセージ（氏名のよみ：名前）: " + errorSet.get("first_name_kana"));
	}

	@Test
	void testInputCheckNameKana4() throws Exception {

  WebBean bean = new WebBean();
  UserInfoDetail detail = new UserInfoDetail(); 
  bean.setValue("last_name_kana", "てすと"); 
  bean.setValue("first_name_kana", "");

  // テスト対象のメソッドを実行し、戻り値を受け取る
  HashMap<String, String> errorSet = detail.nameKanaCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("名前のよみを入力してください。", errorSet.get("first_name_kana"));
  System.out.println("エラーメッセージ（氏名のよみ：名前）: " + errorSet.get("first_name_kana"));
	}
	@Test
	void testInputCheckNameKana5() throws Exception {

  WebBean bean = new WebBean();
  UserInfoDetail detail = new UserInfoDetail(); 
  bean.setValue("last_name_kana", "てすとA"); 
  bean.setValue("first_name_kana", "");

  // テスト対象のメソッドを実行し、戻り値を受け取る
  HashMap<String, String> errorSet = detail.nameKanaCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("氏名のよみはひらがなで入力してください。", errorSet.get("last_name_kana"));
  assertEquals("名前のよみを入力してください。", errorSet.get("first_name_kana"));
  System.out.println("エラーメッセージ（氏名のよみ：名字）: " + errorSet.get("last_name_kana"));
  System.out.println("エラーメッセージ（氏名のよみ：名前）: " + errorSet.get("first_name_kana"));
	}
	@Test
	void testInputCheckNameKana6() throws Exception {

  WebBean bean = new WebBean();
  UserInfoDetail detail = new UserInfoDetail(); 
  bean.setValue("last_name_kana", "てすとA"); 
  bean.setValue("first_name_kana", "てすとA");

  // テスト対象のメソッドを実行し、戻り値を受け取る
  HashMap<String, String> errorSet = detail.nameKanaCheck(bean);

  // エラーメッセージが返ってきているか検証（チェック）
  assertEquals("氏名のよみはひらがなで入力してください。", errorSet.get("last_name_kana"));
  assertEquals("名前のよみはひらがなで入力してください。", errorSet.get("first_name_kana"));
  System.out.println("エラーメッセージ（氏名のよみ：名字）: " + errorSet.get("last_name_kana"));
  System.out.println("エラーメッセージ（氏名のよみ：名前）: " + errorSet.get("first_name_kana"));
	}
	
	
	
}