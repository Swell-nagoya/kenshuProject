//package jp.swell.common.util;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//
//import jp.patasys.common.http.WebBean;
//
//class ValidatorTest {
//	private Validator target;
//	private WebBean mockWebBean;
//	
//	@BeforeEach
//	void setUp() throws Exception {
//		mockWebBean = mock(WebBean.class);
//		target = new Validator(mockWebBean);
//	}
//	
//	@Test
//	@DisplayName("値が入力されている場合、必須入力チェックを通過すること")
//	void checkRequired_success() {
//		when(mockWebBean.value("testField")).thenReturn("testValue");
//		target.checkRequired("testField", "項目名");
//		assertFalse(target.hasErrors());
//	}
//	
//	@ParameterizedTest
//	@ValueSource (strings = {"", " "})
//	@DisplayName("未入力・または空白の場合、必須チェックでエラーになること(1引数)")
//	void checkRequired_error_1arg(String input) {
//		when(mockWebBean.value("testField")).thenReturn(input);
//		target.checkRequired("testField");
//		assertTrue(target.hasErrors());
//		assertEquals(1, target.getErrors().size());
//		assertEquals("testFieldを入力してください。", target.getErrors().get("testField"));
//	}
//	
//	@Test
//	@DisplayName("未入力の場合、必須チェックでエラーになること(2引数)")
//	void checkRequired_error_2args() {
//		when(mockWebBean.value("testField")).thenReturn("");
//		target.checkRequired("testField", "項目名");
//		assertTrue(target.hasErrors());
//		assertEquals(1, target.getErrors().size());
//		assertEquals("項目名を入力してください。", target.getErrors().get("testField"));
//	}
//	
//	@Test
//	@DisplayName("未入力の場合、指定したエラーキーでエラーが登録されること(3引数)")
//	void checkRequired_error_3args() {
//		when(mockWebBean.value("testField")).thenReturn("");
//		target.checkRequired("testField", "errKey", "項目名");
//		assertTrue(target.hasErrors());
//		assertEquals(1, target.getErrors().size());
//		assertEquals("項目名を入力してください。", target.getErrors().get("errKey"));
//	}
//	
//	@Test
//	@DisplayName("すでにerrKeyにエラーが存在する場合、チェックがスキップされること")
//	void checkRequired_skip() {
//		target.getErrors().put("errKey", "既存のエラー");
//		when(mockWebBean.value("fieldName")).thenReturn("");
//		target.checkRequired("fieldName", "errKey", "項目名");
//		assertTrue(target.hasErrors());
//		assertEquals(1, target.getErrors().size());
//		assertEquals("既存のエラー", target.getErrors().get("errKey"));
//		
//	}
//	
//	@Test
//	@DisplayName("両方の項目が入力されている場合、エラーにならないこと")
//	void checkRequiredPair_success() {
//		when(mockWebBean.value("lastName")).thenReturn("山田");
//		when(mockWebBean.value("firstName")).thenReturn("太郎");
//		
//		target.checkRequiredPair("lastName", "名字", "firstName", "名前", "氏名");
//		
//		assertFalse(target.hasErrors());
//	}
//	
//	@Test
//	@DisplayName("両方の項目が未入力・空白の場合、統合項目名でのエラーメッセージがセットされること")
//	void checkRequiredPair_both_empty() {
//		when(mockWebBean.value("lastName")).thenReturn("");
//		when(mockWebBean.value("firstName")).thenReturn(" "); 
//		
//		target.checkRequiredPair("lastName", "名字", "firstName", "名前", "氏名");
//		
//		assertTrue(target.hasErrors());
//		assertEquals(2, target.getErrors().size());
//		assertEquals("氏名を入力してください。", target.getErrors().get("lastName"));
//		assertEquals("", target.getErrors().get("firstName"));
//	}
//	
//	@Test
//	@DisplayName("項目1のみ未入力の場合、項目1の単独エラーメッセージがセットされること")
//	void checkRequiredPair_only_field1_empty() {
//		when(mockWebBean.value("lastName")).thenReturn("");
//		when(mockWebBean.value("firstName")).thenReturn("太郎");
//		
//		target.checkRequiredPair("lastName", "名字", "firstName", "名前", "氏名");
//		
//		assertTrue(target.hasErrors());
//		assertEquals(1, target.getErrors().size());
//		assertEquals("名字を入力してください。", target.getErrors().get("lastName"));
//		assertNull(target.getErrors().get("firstName"));
//	}
//	
//	@Test
//	@DisplayName("項目2のみ未入力の場合、項目2の単独エラーメッセージがセットされること")
//	void checkRequiredPair_only_field2_empty() {
//		when(mockWebBean.value("lastName")).thenReturn("山田");
//		when(mockWebBean.value("firstName")).thenReturn("");
//		
//		target.checkRequiredPair("lastName", "名字", "firstName", "名前", "氏名");
//		
//		assertTrue(target.hasErrors());
//		assertEquals(1, target.getErrors().size());
//		assertNull(target.getErrors().get("lastName"));
//		assertEquals("名前を入力してください。", target.getErrors().get("firstName"));
//	}
//	
//	@ParameterizedTest
//	@ValueSource (strings = {"1", "12345"})
//	@DisplayName("指定した文字数の範囲内の場合、文字数制限チェックを通過すること")
//	void checkLength_success(String input) {
//		when(mockWebBean.value("testField")).thenReturn(input);
//		target.checkLength("testField", "項目名", 1, 5);
//		assertFalse(target.hasErrors());
//	}
//	
//	@ParameterizedTest
//	@ValueSource (strings = {"", "123456"})
//	@DisplayName("指定した文字数の範囲外の場合、文字数制限チェックでエラーになること")
//	void checkLength_error(String input) {
//		when(mockWebBean.value("testField")).thenReturn(input);
//		target.checkLength("testField", "項目名", 1, 5);
//		assertTrue(target.hasErrors());
//		assertEquals("項目名は1文字以上5文字以下で入力してください。", target.getErrors().get("testField"));
//	}
//
//}

package jp.swell.common.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.http.WebBean;
import jp.swell.dao.ContactDao;
import jp.swell.dao.ShiftDAO;
import jp.swell.dao.UserInfoDao;

class ValidatorTest {
	private Validator target;
	private WebBean mockWebBean;
	
	@BeforeEach
	void setUp() throws Exception {
		mockWebBean = mock(WebBean.class);
		target = new Validator(mockWebBean);
	}
	
	@Test
	@DisplayName("値が入力されている場合、必須入力チェックを通過すること")
	void checkRequired_success() {
		when(mockWebBean.value("testField")).thenReturn("testValue");
		target.checkRequired("testField", "項目名");
		assertFalse(target.hasErrors());
	}
	
	@ParameterizedTest
	@ValueSource (strings = {"", " "})
	@DisplayName("未入力・または空白の場合、必須チェックでエラーになること(1引数)")
	void checkRequired_error_1arg(String input) {
		when(mockWebBean.value("testField")).thenReturn(input);
		target.checkRequired("testField");
		assertTrue(target.hasErrors());
		assertEquals(1, target.getErrors().size());
		assertEquals("testFieldを入力してください。", target.getErrors().get("testField"));
	}
	
	@Test
	@DisplayName("未入力の場合、必須チェックでエラーになること(2引数)")
	void checkRequired_error_2args() {
		when(mockWebBean.value("testField")).thenReturn("");
		target.checkRequired("testField", "項目名");
		assertTrue(target.hasErrors());
		assertEquals(1, target.getErrors().size());
		assertEquals("項目名を入力してください。", target.getErrors().get("testField"));
	}
	
	@Test
	@DisplayName("未入力の場合、指定したエラーキーでエラーが登録されること(3引数)")
	void checkRequired_error_3args() {
		when(mockWebBean.value("testField")).thenReturn("");
		target.checkRequired("testField", "errKey", "項目名");
		assertTrue(target.hasErrors());
		assertEquals(1, target.getErrors().size());
		assertEquals("項目名を入力してください。", target.getErrors().get("errKey"));
	}
	
	@Test
	@DisplayName("すでにerrKeyにエラーが存在する場合、チェックがスキップされること")
	void checkRequired_skip() {
		target.getErrors().put("errKey", "既存のエラー");
		when(mockWebBean.value("fieldName")).thenReturn("");
		target.checkRequired("fieldName", "errKey", "項目名");
		assertTrue(target.hasErrors());
		assertEquals(1, target.getErrors().size());
		assertEquals("既存のエラー", target.getErrors().get("errKey"));
	}
	
	@Test
	@DisplayName("両方の項目が入力されている場合、エラーにならないこと")
	void checkRequiredPair_success() {
		when(mockWebBean.value("lastName")).thenReturn("山田");
		when(mockWebBean.value("firstName")).thenReturn("太郎");
		
		target.checkRequiredPair("lastName", "名字", "firstName", "名前", "氏名");
		
		assertFalse(target.hasErrors());
	}
	
	@Test
	@DisplayName("両方の項目が未入力・空白の場合、統合項目名でのエラーメッセージがセットされること")
	void checkRequiredPair_both_empty() {
		when(mockWebBean.value("lastName")).thenReturn("");
		when(mockWebBean.value("firstName")).thenReturn(" "); 
		
		target.checkRequiredPair("lastName", "名字", "firstName", "名前", "氏名");
		
		assertTrue(target.hasErrors());
		assertEquals(2, target.getErrors().size());
		assertEquals("氏名を入力してください。", target.getErrors().get("lastName"));
		assertEquals("", target.getErrors().get("firstName"));
	}
	
	@Test
	@DisplayName("項目1のみ未入力の場合、項目1の単独エラーメッセージがセットされること")
	void checkRequiredPair_only_field1_empty() {
		when(mockWebBean.value("lastName")).thenReturn("");
		when(mockWebBean.value("firstName")).thenReturn("太郎");
		
		target.checkRequiredPair("lastName", "名字", "firstName", "名前", "氏名");
		
		assertTrue(target.hasErrors());
		assertEquals(1, target.getErrors().size());
		assertEquals("名字を入力してください。", target.getErrors().get("lastName"));
		assertNull(target.getErrors().get("firstName"));
	}
	
	@Test
	@DisplayName("項目2のみ未入力の場合、項目2の単独エラーメッセージがセットされること")
	void checkRequiredPair_only_field2_empty() {
		when(mockWebBean.value("lastName")).thenReturn("山田");
		when(mockWebBean.value("firstName")).thenReturn("");
		
		target.checkRequiredPair("lastName", "名字", "firstName", "名前", "氏名");
		
		assertTrue(target.hasErrors());
		assertEquals(1, target.getErrors().size());
		assertNull(target.getErrors().get("lastName"));
		assertEquals("名前を入力してください。", target.getErrors().get("firstName"));
	}

	@Test
	@DisplayName("ファイルが選択されている場合、ファイル必須チェックを通過すること")
	void checkFileRequired_success() {
		when(mockWebBean.object("testField")).thenReturn(new byte[]{1, 2, 3});
		target.checkFileRequired("testField", "errKey", "項目名");
		assertFalse(target.hasErrors());
	}

	@Test
	@DisplayName("ファイルが未選択(null)の場合、ファイル必須チェックでエラーになること")
	void checkFileRequired_error_null() {
		when(mockWebBean.object("testField")).thenReturn(null);
		target.checkFileRequired("testField", "errKey", "項目名");
		assertTrue(target.hasErrors());
		assertEquals("項目名を選択してください", target.getErrors().get("errKey"));
	}

	@Test
	@DisplayName("ファイルが未選択(空配列)の場合、ファイル必須チェックでエラーになること")
	void checkFileRequired_error_empty() {
		when(mockWebBean.object("testField")).thenReturn(new byte[0]);
		target.checkFileRequired("testField", "errKey", "項目名");
		assertTrue(target.hasErrors());
		assertEquals("項目名を選択してください", target.getErrors().get("errKey"));
	}
	
	@ParameterizedTest
	@ValueSource (strings = {"1", "12345"})
	@DisplayName("指定した文字数の範囲内の場合、文字数制限チェックを通過すること")
	void checkLength_success(String input) {
		when(mockWebBean.value("testField")).thenReturn(input);
		target.checkLength("testField", "項目名", 1, 5);
		assertFalse(target.hasErrors());
	}
	
	@ParameterizedTest
	@ValueSource (strings = {"", "123456"})
	@DisplayName("指定した文字数の範囲外の場合、文字数制限チェックでエラーになること")
	void checkLength_error(String input) {
		when(mockWebBean.value("testField")).thenReturn(input);
		target.checkLength("testField", "項目名", 1, 5);
		assertTrue(target.hasErrors());
		assertEquals("項目名は1文字以上5文字以下で入力してください。", target.getErrors().get("testField"));
	}

	@ParameterizedTest
	@ValueSource (strings = {"", "12345"})
	@DisplayName("最大文字数以内の場合、最大文字数チェックを通過すること")
	void checkMaxLength_success(String input) {
		when(mockWebBean.value("testField")).thenReturn(input);
		target.checkMaxLength("testField", "項目名", 5);
		assertFalse(target.hasErrors());
	}

	@Test
	@DisplayName("最大文字数を超える場合、最大文字数チェックでエラーになること")
	void checkMaxLength_error() {
		when(mockWebBean.value("testField")).thenReturn("123456");
		target.checkMaxLength("testField", "項目名", 5);
		assertTrue(target.hasErrors());
		assertEquals("項目名の入力内容が長すぎます。", target.getErrors().get("testField"));
	}

	@Test
	@DisplayName("最大文字数を超える場合、最大文字数チェックでエラーになること(errKey指定)")
	void checkMaxLength_with_errKey_error() {
		when(mockWebBean.value("testField")).thenReturn("123456");
		target.checkMaxLength("testField", "errKey", "項目名", 5);
		assertTrue(target.hasErrors());
		assertEquals("項目名の入力内容が長すぎます。", target.getErrors().get("errKey"));
	}

	@ParameterizedTest
	@ValueSource (strings = {"test@example.com", "a.b@example.co.jp"})
	@DisplayName("正しいメールアドレスの場合、メールアドレス形式チェックを通過すること")
	void checkEmailFormat_success(String input) {
		when(mockWebBean.value("testField")).thenReturn(input);
		target.checkEmailFormat("testField");
		assertFalse(target.hasErrors());
	}

	@ParameterizedTest
	@ValueSource (strings = {"test", "test@example", "@example.com", "test@.com"})
	@DisplayName("不正なメールアドレスの場合、メールアドレス形式チェックでエラーになること")
	void checkEmailFormat_error(String input) {
		when(mockWebBean.value("testField")).thenReturn(input);
		target.checkEmailFormat("testField");
		assertTrue(target.hasErrors());
		assertEquals("正しいメールアドレスを入力してください。", target.getErrors().get("testField"));
	}

	@Test
	@DisplayName("登録済みメールアドレスの場合、登録済みチェックを通過すること")
	void checkEmailRegistered_success() throws AtareSysException {
		UserInfoDao mockDao = mock(UserInfoDao.class);
		when(mockWebBean.value("testField")).thenReturn("test@example.com");
		when(mockDao.isEmailExists("test@example.com")).thenReturn(true);
		
		target.checkEmailRegistered("testField", mockDao);
		assertFalse(target.hasErrors());
	}

	@Test
	@DisplayName("未登録メールアドレスの場合、登録済みチェックでエラーになること")
	void checkEmailRegistered_error() throws AtareSysException {
		UserInfoDao mockDao = mock(UserInfoDao.class);
		when(mockWebBean.value("testField")).thenReturn("test@example.com");
		when(mockDao.isEmailExists("test@example.com")).thenReturn(false);
		
		target.checkEmailRegistered("testField", mockDao);
		assertTrue(target.hasErrors());
		assertEquals("このメールアドレスは登録されていません。", target.getErrors().get("testField"));
	}

	@Test
	@DisplayName("未登録のメールアドレスの場合、重複チェックを通過すること(UserInfoDao)")
	void checkEmailDuplicated_UserInfoDao_success() throws AtareSysException {
		UserInfoDao mockDao = mock(UserInfoDao.class);
		when(mockWebBean.value("testField")).thenReturn("test@example.com");
		when(mockDao.isEmailExists("test@example.com")).thenReturn(false);
		
		target.checkEmailDuplicated("testField", mockDao);
		assertFalse(target.hasErrors());
	}

	@Test
	@DisplayName("登録済みのメールアドレスの場合、重複チェックでエラーになること(UserInfoDao)")
	void checkEmailDuplicated_UserInfoDao_error() throws AtareSysException {
		UserInfoDao mockDao = mock(UserInfoDao.class);
		when(mockWebBean.value("testField")).thenReturn("test@example.com");
		when(mockDao.isEmailExists("test@example.com")).thenReturn(true);
		
		target.checkEmailDuplicated("testField", mockDao);
		assertTrue(target.hasErrors());
		assertEquals("このメールアドレスはすでに登録されています。", target.getErrors().get("testField"));
	}

	@Test
	@DisplayName("自身を除外して重複していない場合、重複チェックを通過すること(UserInfoDao)")
	void checkEmailDuplicated_UserInfoDao_mainKey_success() throws AtareSysException {
		UserInfoDao mockDao = mock(UserInfoDao.class);
		when(mockWebBean.value("testField")).thenReturn("test@example.com");
		when(mockDao.isEmailExists("test@example.com", "user1")).thenReturn(false);
		
		target.checkEmailDuplicated("testField", mockDao, "user1");
		assertFalse(target.hasErrors());
	}

	@Test
	@DisplayName("mainKeyが空の場合、新規登録時と同様の重複チェックが行われること(UserInfoDao)")
	void checkEmailDuplicated_UserInfoDao_mainKey_empty() throws AtareSysException {
		UserInfoDao mockDao = mock(UserInfoDao.class);
		when(mockWebBean.value("testField")).thenReturn("test@example.com");
		when(mockDao.isEmailExists("test@example.com")).thenReturn(true);
		
		target.checkEmailDuplicated("testField", mockDao, "");
		assertTrue(target.hasErrors());
		verify(mockDao).isEmailExists("test@example.com");
	}

	@Test
	@DisplayName("未登録のメールアドレスの場合、重複チェックを通過すること(ShiftDAO)")
	void checkEmailDuplicated_ShiftDAO_success() throws AtareSysException {
		ShiftDAO mockDao = mock(ShiftDAO.class);
		when(mockWebBean.value("testField")).thenReturn("test@example.com");
		when(mockDao.isEmailExists("test@example.com")).thenReturn(false);
		
		target.checkEmailDuplicated("testField", mockDao);
		assertFalse(target.hasErrors());
	}

	@Test
	@DisplayName("登録済みのメールアドレスの場合、重複チェックでエラーになること(ShiftDAO)")
	void checkEmailDuplicated_ShiftDAO_error() throws AtareSysException {
		ShiftDAO mockDao = mock(ShiftDAO.class);
		when(mockWebBean.value("testField")).thenReturn("test@example.com");
		when(mockDao.isEmailExists("test@example.com")).thenReturn(true);
		
		target.checkEmailDuplicated("testField", mockDao);
		assertTrue(target.hasErrors());
		assertEquals("このメールアドレスはすでに登録されています。", target.getErrors().get("testField"));
	}

	@Test
	@DisplayName("自身を除外して重複していない場合、重複チェックを通過すること(ShiftDAO)")
	void checkEmailDuplicated_ShiftDAO_mainKey_success() throws AtareSysException {
		ShiftDAO mockDao = mock(ShiftDAO.class);
		when(mockWebBean.value("testField")).thenReturn("test@example.com");
		when(mockDao.isEmailExists("test@example.com", "user1")).thenReturn(false);
		
		target.checkEmailDuplicated("testField", mockDao, "user1");
		assertFalse(target.hasErrors());
	}

	@Test
	@DisplayName("未登録のメールアドレスの場合、重複チェックを通過すること(ContactDao)")
	void checkEmailDuplicated_ContactDao_success() throws AtareSysException {
		ContactDao mockDao = mock(ContactDao.class);
		when(mockWebBean.value("testField")).thenReturn("test@example.com");
		when(mockDao.isEmailExists("test@example.com")).thenReturn(false);
		
		target.checkEmailDuplicated("testField", mockDao);
		assertFalse(target.hasErrors());
	}

	@Test
	@DisplayName("自身を除外して重複していない場合、重複チェックを通過すること(ContactDao)")
	void checkEmailDuplicated_ContactDao_mainKey_success() throws AtareSysException {
		ContactDao mockDao = mock(ContactDao.class);
		when(mockWebBean.value("testField")).thenReturn("test@example.com");
		when(mockDao.isEmailExists("test@example.com", 1)).thenReturn(false);
		
		target.checkEmailDuplicated("testField", mockDao, "1");
		assertFalse(target.hasErrors());
	}

	@Test
	@DisplayName("ContactDao/mainKeyが数値でない場合、新規登録時と同様のチェックが行われること")
	void checkEmailDuplicated_ContactDao_mainKey_notNumber() throws AtareSysException {
		ContactDao mockDao = mock(ContactDao.class);
		when(mockWebBean.value("testField")).thenReturn("test@example.com");
		when(mockDao.isEmailExists("test@example.com")).thenReturn(true);
		
		target.checkEmailDuplicated("testField", mockDao, "abc");
		assertTrue(target.hasErrors());
		verify(mockDao).isEmailExists("test@example.com");
	}

	@Test
	@DisplayName("未登録のIDの場合、重複チェックを通過すること(UserInfoDao)")
	void checkIdDuplicated_UserInfoDao_success() throws AtareSysException {
		UserInfoDao mockDao = mock(UserInfoDao.class);
		when(mockWebBean.value("testField")).thenReturn("testId");
		when(mockDao.isIdExists("testId")).thenReturn(false);
		
		target.checkIdDuplicated("testField", mockDao);
		assertFalse(target.hasErrors());
	}

	@Test
	@DisplayName("登録済みのIDの場合、重複チェックでエラーになること(UserInfoDao)")
	void checkIdDuplicated_UserInfoDao_error() throws AtareSysException {
		UserInfoDao mockDao = mock(UserInfoDao.class);
		when(mockWebBean.value("testField")).thenReturn("testId");
		when(mockDao.isIdExists("testId")).thenReturn(true);
		
		target.checkIdDuplicated("testField", mockDao);
		assertTrue(target.hasErrors());
		assertEquals("このＩＤはすでに登録されています。", target.getErrors().get("testField"));
	}

	@Test
	@DisplayName("自身を除外して重複していない場合、ID重複チェックを通過すること(UserInfoDao)")
	void checkIdDuplicated_UserInfoDao_mainKey_success() throws AtareSysException {
		UserInfoDao mockDao = mock(UserInfoDao.class);
		when(mockWebBean.value("testField")).thenReturn("testId");
		when(mockDao.isIdExists("testId", "user1")).thenReturn(false);
		
		target.checkIdDuplicated("testField", mockDao, "user1");
		assertFalse(target.hasErrors());
	}

	@Test
	@DisplayName("未登録のIDの場合、重複チェックを通過すること(ShiftDAO)")
	void checkIdDuplicated_ShiftDAO_success() throws AtareSysException {
		ShiftDAO mockDao = mock(ShiftDAO.class);
		when(mockWebBean.value("testField")).thenReturn("testId");
		when(mockDao.isIdExists("testId")).thenReturn(false);
		
		target.checkIdDuplicated("testField", mockDao);
		assertFalse(target.hasErrors());
	}

	@Test
	@DisplayName("自身を除外して重複していない場合、ID重複チェックを通過すること(ShiftDAO)")
	void checkIdDuplicated_ShiftDAO_mainKey_success() throws AtareSysException {
		ShiftDAO mockDao = mock(ShiftDAO.class);
		when(mockWebBean.value("testField")).thenReturn("testId");
		when(mockDao.isIdExists("testId", "user1")).thenReturn(false);
		
		target.checkIdDuplicated("testField", mockDao, "user1");
		assertFalse(target.hasErrors());
	}

	@ParameterizedTest
	@ValueSource (strings = {"あいうえお", "やまだたろう", "ー"})
	@DisplayName("ひらがなの場合、ひらがなチェックを通過すること")
	void checkHiragana_success(String input) {
		when(mockWebBean.value("testField")).thenReturn(input);
		target.checkHiragana("testField", "項目名");
		assertFalse(target.hasErrors());
	}

	@ParameterizedTest
	@ValueSource (strings = {"アイウエオ", "山田", "a", "あいうえおa"})
	@DisplayName("ひらがな以外が含まれる場合、ひらがなチェックでエラーになること")
	void checkHiragana_error(String input) {
		when(mockWebBean.value("testField")).thenReturn(input);
		target.checkHiragana("testField", "項目名");
		assertTrue(target.hasErrors());
		assertEquals("項目名はひらがなで入力してください。", target.getErrors().get("testField"));
	}

	@Test
	@DisplayName("両方がひらがなの場合、ひらがなペアチェックを通過すること")
	void checkHiraganaPair_success() {
		when(mockWebBean.value("lastName")).thenReturn("やまだ");
		when(mockWebBean.value("firstName")).thenReturn("たろう");
		target.checkHiraganaPair("lastName", "名字", "firstName", "名前", "氏名ふりがな");
		assertFalse(target.hasErrors());
	}

	@Test
	@DisplayName("両方がひらがな以外の場合、統合エラーメッセージがセットされること")
	void checkHiraganaPair_both_error() {
		when(mockWebBean.value("lastName")).thenReturn("山田");
		when(mockWebBean.value("firstName")).thenReturn("太郎");
		target.checkHiraganaPair("lastName", "名字", "firstName", "名前", "氏名ふりがな");
		assertTrue(target.hasErrors());
		assertEquals("氏名ふりがなはひらがなで入力してください。", target.getErrors().get("lastName"));
		assertEquals("", target.getErrors().get("firstName"));
	}

	@Test
	@DisplayName("片方だけひらがな以外の場合、該当項目のみエラーがセットされること")
	void checkHiraganaPair_one_error() {
		when(mockWebBean.value("lastName")).thenReturn("山田");
		when(mockWebBean.value("firstName")).thenReturn("たろう");
		target.checkHiraganaPair("lastName", "名字", "firstName", "名前", "氏名ふりがな");
		assertTrue(target.hasErrors());
		assertEquals("名字はひらがなで入力してください。", target.getErrors().get("lastName"));
		assertNull(target.getErrors().get("firstName"));
	}

	@ParameterizedTest
	@ValueSource (strings = {"abc123", "A1"})
	@DisplayName("半角英数字の場合、チェックを通過すること")
	void checkHalfAlphanumeric_success(String input) {
		when(mockWebBean.value("testField")).thenReturn(input);
		target.checkHalfAlphanumeric("testField", "項目名");
		assertFalse(target.hasErrors());
	}

	@ParameterizedTest
	@ValueSource (strings = {"abc-123", "あ", "123 "})
	@DisplayName("半角英数字以外が含まれる場合、エラーになること")
	void checkHalfAlphanumeric_error(String input) {
		when(mockWebBean.value("testField")).thenReturn(input);
		target.checkHalfAlphanumeric("testField", "項目名");
		assertTrue(target.hasErrors());
		assertEquals("項目名は半角英数字で入力してください。", target.getErrors().get("testField"));
	}

	@Test
	@DisplayName("実在する8桁の日付の場合、チェックを通過すること")
	void checkDateFormat_success() {
		when(mockWebBean.value("testField")).thenReturn("20270101");
		target.checkDateFormat("testField");
		assertFalse(target.hasErrors());
	}

	@Test
	@DisplayName("8桁の数字でない場合、エラーになること")
	void checkDateFormat_error_not_8digits() {
		when(mockWebBean.value("testField")).thenReturn("2027-01-01");
		target.checkDateFormat("testField");
		assertTrue(target.hasErrors());
		assertEquals("8桁の数字(ex: 20270101)を入力してください。", target.getErrors().get("testField"));
	}

	@Test
	@DisplayName("存在しない日付の場合、エラーになること")
	void checkDateFormat_error_invalid_date() {
		when(mockWebBean.value("testField")).thenReturn("20279999");
		target.checkDateFormat("testField");
		assertTrue(target.hasErrors());
		assertEquals("日付の形式が不正です。", target.getErrors().get("testField"));
	}

	@Test
	@DisplayName("本日以降の日付の場合、チェックを通過すること")
	void checkFutureDate_success() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(new java.util.Date());
		when(mockWebBean.value("testField")).thenReturn(today);
		target.checkFutureDate("testField");
		assertFalse(target.hasErrors());
	}

	@Test
	@DisplayName("過去の日付の場合、エラーになること")
	void checkFutureDate_error_past() {
		when(mockWebBean.value("testField")).thenReturn("19990101");
		target.checkFutureDate("testField");
		assertTrue(target.hasErrors());
		assertEquals("本日以降の日付を入力してください。", target.getErrors().get("testField"));
	}

	@Test
	@DisplayName("日付の形式が不正な場合、本日以降のチェックでエラーになること")
	void checkFutureDate_error_invalid_format() {
		when(mockWebBean.value("testField")).thenReturn("invalid");
		target.checkFutureDate("testField");
		assertTrue(target.hasErrors());
		assertEquals("日付の形式が不正です。", target.getErrors().get("testField"));
	}

	@ParameterizedTest
	@ValueSource (strings = {"山田太郎", "ヤマダタロウ", "やまだたろう", "YamadaTaro"})
	@DisplayName("正しい氏名形式の場合、チェックを通過すること")
	void checkNameFormat_success(String input) {
		when(mockWebBean.value("testField")).thenReturn(input);
		target.checkNameFormat("testField");
		assertFalse(target.hasErrors());
	}

	@ParameterizedTest
	@ValueSource (strings = {"山田 太郎", "123", "!"})
	@DisplayName("不正な氏名形式の場合、エラーになること")
	void checkNameFormat_error(String input) {
		when(mockWebBean.value("testField")).thenReturn(input);
		target.checkNameFormat("testField");
		assertTrue(target.hasErrors());
		assertEquals("正しい氏名を入力してください。", target.getErrors().get("testField"));
	}

	@ParameterizedTest
	@ValueSource (strings = {"第一営業部", "A1フロア"})
	@DisplayName("正しい配属先形式の場合、チェックを通過すること")
	void checkWorkPlaceFormat_success(String input) {
		when(mockWebBean.value("testField")).thenReturn(input);
		target.checkWorkPlaceFormat("testField");
		assertFalse(target.hasErrors());
	}

	@ParameterizedTest
	@ValueSource (strings = {"第一 営業部", "!!!"})
	@DisplayName("不正な配属先形式の場合、エラーになること")
	void checkWorkPlaceFormat_error(String input) {
		when(mockWebBean.value("testField")).thenReturn(input);
		target.checkWorkPlaceFormat("testField");
		assertTrue(target.hasErrors());
		assertEquals("正しい配属先を入力してください。", target.getErrors().get("testField"));
	}

	@ParameterizedTest
	@ValueSource (strings = {"03-1234-5678", "090-1234-5678"})
	@DisplayName("正しい電話番号形式の場合、チェックを通過すること")
	void checkPhoneNumberFormat_success(String input) {
		when(mockWebBean.value("testField")).thenReturn(input);
		target.checkPhoneNumberFormat("testField");
		assertFalse(target.hasErrors());
	}

	@ParameterizedTest
	@ValueSource (strings = {"13-1234-5678", "0312345678", "03-12-345678"})
	@DisplayName("不正な電話番号形式の場合、エラーになること")
	void checkPhoneNumberFormat_error(String input) {
		when(mockWebBean.value("testField")).thenReturn(input);
		target.checkPhoneNumberFormat("testField");
		assertTrue(target.hasErrors());
		assertEquals("正しい電話番号を入力してください。", target.getErrors().get("testField"));
	}

	@Test
	@DisplayName("値が変更されている場合、変更チェックを通過すること")
	void checkValueChanged_success() {
		when(mockWebBean.value("newField")).thenReturn("newData");
		when(mockWebBean.value("beforeField")).thenReturn("oldData");
		target.checkValueChanged("newField", "beforeField", "errKey", "変更されていません。");
		assertFalse(target.hasErrors());
	}

	@Test
	@DisplayName("値が変更されていない場合、変更チェックでエラーになること")
	void checkValueChanged_error() {
		when(mockWebBean.value("newField")).thenReturn("sameData");
		when(mockWebBean.value("beforeField")).thenReturn("SAMEDATA");
		target.checkValueChanged("newField", "beforeField", "errKey", "変更されていません。");
		assertTrue(target.hasErrors());
		assertEquals("変更されていません。", target.getErrors().get("errKey"));
	}
}
