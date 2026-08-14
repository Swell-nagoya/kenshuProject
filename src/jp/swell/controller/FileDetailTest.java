package jp.swell.controller;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jp.patasys.common.http.WebBean;
import jp.swell.dao.FileDao;
import jp.swell.user.UserLoginInfo;

class FileDetailTest {
 private WebBean bean;
 private FileDetail fileDetail;
 
 @BeforeEach
 void setUp() {
     bean = new WebBean();
     fileDetail = new FileDetail();
 }

 /* 
  * アップロードしたファイル一覧に、指定したファイルIDが存在しないとき.
  * userInfoId（user_info.user）、main_key（file.file_id）
 */
	@Test
	void testFileDetail1() throws Exception {
  // テスト対象のファイルを登録したユーザーID
  String userInfoId = "EGBH00008";
  // テスト対象のファイルID
  String main_key = "test";

  //ダミーログイン情報
  UserLoginInfo strangerUser = new UserLoginInfo() {
      @Override
      public String getUserInfoId() {
          return userInfoId;
      }
  };
  FileDetail testFileDetail = new FileDetail() {
      @Override
      public UserLoginInfo getLoginInfo() {
          return strangerUser;
      }
  };

  
  String nextScreen = testFileDetail.dbDeletef(main_key);

  assertEquals("ファイルが見つかりませんでした。", testFileDetail.getWebBean().dispErrorMessages());
	}


 /* 
  * アップロードしたファイル一覧に、アップロードしたユーザー名が存在しないとき.
  * ファイル削除を拒否する.
  * userInfoId（user_info.user）、main_key（file.file_id）
 */
	@Test
	void testFileDetail2() throws Exception {

  // テスト対象のファイルを登録したユーザーID
  String userInfoId = "dummyUserID";
  // テスト対象のファイルID
  String main_key = "1e44f960-ef2f";
  //ダミーログイン情報
		UserLoginInfo strangerUser = new UserLoginInfo() {
      @Override
      public String getUserInfoId() {
          return userInfoId;
      }
  };
  FileDetail testFileDetail = new FileDetail() {
      @Override
      public UserLoginInfo getLoginInfo() {
          return strangerUser;
      }
  };


  String nextScreen = testFileDetail.dbDeletef(main_key);

  assertEquals("このファイルを削除する権限がありません。", testFileDetail.getWebBean().dispErrorMessages());
	}

 /* 
  * アップロードしたファイル一覧に、アップロードしたユーザー名とファイルIDが合致する場合.
  * ファイルを削除する.
  * userInfoId（user_info.user）、main_key（file.file_id）
 */
	@Test
	void testFileDetail3() throws Exception {

  // テスト対象のファイルを登録したユーザーID
  String userInfoId = "EGBH00008";
  // テスト対象のファイルID
  String main_key = "1e44f960-ef2f";

  //ダミーログイン情報
  UserLoginInfo strangerUser = new UserLoginInfo() {
      @Override
      public String getUserInfoId() {
          return userInfoId;
      }
  };
  FileDetail testFileDetail = new FileDetail() {
      @Override
      public UserLoginInfo getLoginInfo() {
          return strangerUser;
      }
  };


  String nextScreen = testFileDetail.dbDeletef(main_key);

  assertEquals("", testFileDetail.getWebBean().dispErrorMessages());

	  
	}

 /* 
  * 登録時の入力チェック.
  * ファイル名の入力がない場合
  * （新規登録画面）
 */
	@Test
	void testInputCheck1() throws Exception {
  // DAOを初期化、 beanを生成
  FileDao dao = fileDetail.setWeb2Dao2InputInfo();
  
  //bean の実体を取得する
  WebBean actualBean = fileDetail.getWebBean();
  
  // ファイル名
  actualBean.setValue("input_name", ""); 

  // アップロードファイルのリンク先
  actualBean.setValue("file_value", "test");

  // ダウンロード期限
  actualBean.setValue("expiration_data", "2027年08月21日");
  
  // 入力チェックを実行
  boolean isValid = fileDetail.inputCheck(dao);
  
  // エラーメッセージの取得
  HashMap<String, String> errorSet = actualBean.getItemErrors();

  assertEquals("ファイル名を入力してください。", errorSet.get("input_name_empty"));
  assertEquals(null, errorSet.get("file_value_empty"));
  assertEquals(null, errorSet.get("expiration_data_empty"));
}

 /* 
  * 登録時の入力チェック.
  *  アップロードファイルの入力がない場合.
  * （新規登録画面）
 */
	@Test
	void testInputCheck2() throws Exception {
  // DAOを初期化、 beanを生成
  FileDao dao = fileDetail.setWeb2Dao2InputInfo();

  //bean の実体を取得する
  WebBean actualBean = fileDetail.getWebBean();

  // ファイル名
  actualBean.setValue("input_name", "test");

  // アップロードファイルのリンク先
  actualBean.setValue("file_value", "");
  
  // ダウンロード期限
  actualBean.setValue("expiration_data", "2027年08月21日");
  
  // 入力チェックを実行
  boolean isValid = fileDetail.inputCheck(dao);

  // エラーメッセージの取得
  HashMap<String, String> errorSet = actualBean.getItemErrors();

  assertEquals(null, errorSet.get("input_name_empty"));
  assertEquals("ファイルリンクを入力してください。", errorSet.get("file_value_empty"));
  assertEquals(null, errorSet.get("expiration_data_empty"));
}

 /* 
  *　アップロードしたファイルのダウンロード期限が、本日よりも前の場合.
  * （新規登録画面）
 */
	@Test
	void testInputCheck3() throws Exception {
  // DAOを初期化、 beanを生成
  FileDao dao = fileDetail.setWeb2Dao2InputInfo();

  //bean の実体を取得する
  WebBean actualBean = fileDetail.getWebBean();

  // ファイル名
  actualBean.setValue("input_name", "test");

  // アップロードファイルのリンク先
  actualBean.setValue("file_value", "test");

  // ダウンロード期限
  actualBean.setValue("expiration_data", "2026年08月10日");

  // 入力チェックを実行
  boolean isValid = fileDetail.inputCheck(dao);

  // エラーメッセージの取得
  HashMap<String, String> errorSet = actualBean.getItemErrors();

  assertEquals(null, errorSet.get("input_name_empty"));
  assertEquals(null, errorSet.get("file_value_empty"));
  assertEquals("本日または後日を登録してください。", errorSet.get("expiration_data_empty"));
}

 /* 
  *　アップロードしたファイルの登録が全て正常な場合.
  * （新規登録画面）
 */
	@Test
	void testInputCheck4() throws Exception {
  // DAOを初期化、 beanを生成
  FileDao dao = fileDetail.setWeb2Dao2InputInfo();

  //bean の実体を取得する
  WebBean actualBean = fileDetail.getWebBean(); // ゲッターの名前は実際のクラスに合わせてください

  // ファイル名
  actualBean.setValue("input_name", "test");

  // アップロードファイルのリンク先
  actualBean.setValue("file_value", "test");

  // ダウンロード期限
  actualBean.setValue("expiration_data", "2026年08月14日");
  
  // 入力チェックを実行
  boolean isValid = fileDetail.inputCheck(dao);

  // エラーメッセージの取得
  HashMap<String, String> errorSet = actualBean.getItemErrors();

  assertEquals(null, errorSet.get("input_name_empty"));
  assertEquals(null, errorSet.get("file_value_empty"));
  assertEquals(null, errorSet.get("expiration_data_empty"));
}
	
	@Test
	void testInputCheck5() throws Exception {
  // DAOを初期化、 beanを生成
  FileDao dao = fileDetail.setWeb2Dao2InputInfo();

  //bean の実体を取得する
  WebBean actualBean = fileDetail.getWebBean();

  // ファイル名
  actualBean.setValue("input_name", "test");

  // アップロードファイルのリンク先
  actualBean.setValue("file_value", "test");

  // ダウンロード期限
  actualBean.setValue("expiration_data", "2027年08月21日");
  
  // 入力チェックを実行
  boolean isValid = fileDetail.inputCheck(dao);

  // エラーメッセージの取得
  HashMap<String, String> errorSet = actualBean.getItemErrors();

  assertEquals(null, errorSet.get("input_name_empty"));
  assertEquals(null, errorSet.get("file_value_empty"));
  assertEquals(null, errorSet.get("expiration_data_empty"));
}
}


