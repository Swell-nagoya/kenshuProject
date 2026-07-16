package jp.swell.controller;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import jp.patasys.common.db.DbBase;
import jp.patasys.common.http.WebBean;
import jp.swell.dao.UserInfoDao;

class TestableUserInfoDetail extends UserInfoDetail {
	private WebBean testWebBean;
	public String forwardUrl = null;

	public TestableUserInfoDetail(WebBean webBean) {
		this.testWebBean = webBean;
	}

	@Override
	public WebBean getWebBean() {
		return this.testWebBean;
	}

	@Override
	public void forward(String url) {
		this.forwardUrl = url;
	}

}

class UserInfoDetailTest {

	private TestableUserInfoDetail target;
	private WebBean mockWebBean;

	@BeforeEach
	void setUp() throws Exception {
		mockWebBean = mock(WebBean.class);
		target = new TestableUserInfoDetail(mockWebBean);
	}

	@Test
	@DisplayName("正常な入力で、正常な画面遷移がされること")
	void dbBulkUpdate_validInput() throws Exception {
		String json = "[[\"USER0001\", \"山田太郎\"],[\"USER0002\", \"田中一郎\"]]";
		when(mockWebBean.value("selected_users")).thenReturn(json);
		when(mockWebBean.value("target_role")).thenReturn("1");

		try (
				MockedStatic<DbBase> mockedDbBase = mockStatic(DbBase.class);
				MockedConstruction<UserInfoDao> mockedDao = mockConstruction(UserInfoDao.class)
			) {

			target.dbBulkUpdate();

			mockedDbBase.verify(() -> DbBase.dbBeginTran(), times(1));
			mockedDbBase.verify(() -> DbBase.dbCommitTran(), times(1));
			mockedDbBase.verify(() -> DbBase.dbRollbackTran(), never());

			UserInfoDao mockDaoInstance = mockedDao.constructed().get(0);
			verify(mockDaoInstance).setAdmin("1");
			verify(mockDaoInstance).dbUpdateAdmin("USER0001");
			verify(mockDaoInstance).dbUpdateAdmin("USER0002");

			assertEquals("ViewUserList.do", target.forwardUrl);
		}
	}

	@Test
	@DisplayName("送信されたJSONが空文字の場合、エラーになり元の画面に戻ること")
	void dbBulkUpdate_emptyString() throws Exception {
		String json = "";
		when(mockWebBean.value("selected_users")).thenReturn(json);

		target.dbBulkUpdate();
		verify(mockWebBean).setError("error", "更新対象のユーザー情報がありません。");
		assertEquals("UserInfoDetail_4.jsp", target.forwardUrl);
	}

	@Test
	@DisplayName("送信されたJSONが空配列の場合、エラーになり元の画面に戻ること")
	void dbBulkUpdate_emptyArray() throws Exception {
		String json = "[]";
		when(mockWebBean.value("selected_users")).thenReturn(json);

		target.dbBulkUpdate();
		verify(mockWebBean).setError("error", "更新対象のユーザー情報がありません。");
		assertEquals("UserInfoDetail_4.jsp", target.forwardUrl);
	}

	@Test
	@DisplayName("送信されたJSONが不正な構文だった場合、エラーになり元の画面に戻ること")
	void dbBulkUpdate_JsonException() throws Exception {
		String json = "{\"id\": \"user0001\", \"name\": \"山田太郎\"}";
		when(mockWebBean.value("selected_users")).thenReturn(json);

		target.dbBulkUpdate();
		verify(mockWebBean).setError("error", "JSONデータの処理中にエラーが発生しました。");
		assertEquals("UserInfoDetail_4.jsp", target.forwardUrl);
	}

	@Test
	@DisplayName("DB更新中に例外が発生した場合、ロールバックされ元の画面に戻ること")
	void dbBulkUpdate_dbException() throws Exception {
		String json = "[[\"USER0001\", \"山田太郎\"]]";
		when(mockWebBean.value("selected_users")).thenReturn(json);
		when(mockWebBean.value("target_role")).thenReturn("1");

		try (
				MockedStatic<DbBase> mockedDbBase = mockStatic(DbBase.class);
				MockedConstruction<UserInfoDao> mockedDao = mockConstruction(UserInfoDao.class, 
					(mock, context) -> {
						doThrow(new RuntimeException("DB接続エラー")).when(mock).dbUpdateAdmin(anyString());
					})
			) {

			target.dbBulkUpdate();

			mockedDbBase.verify(() -> DbBase.dbBeginTran(), times(1));
			mockedDbBase.verify(() -> DbBase.dbCommitTran(), never());
			mockedDbBase.verify(() -> DbBase.dbRollbackTran(), times(1));

			verify(mockWebBean).setError("transaction_error", "データベースの更新中にエラーが発生したためロールバックしました。");
			assertEquals("UserInfoDetail_4.jsp", target.forwardUrl);
		}
	}

}
