package jp.swell.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DbBase;

class UserInfoDaoTest {

	private UserInfoDao target;
	private String todayStr;
	
	@BeforeEach
	void setUp() throws Exception {
		target = new UserInfoDao();
		
		todayStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("nullまたは空文字が渡された場合、ANDがセットされること")
	void setSearchOperator_NullOrEmpty(String input) {
		target.setSearchOperator(input);
		assertEquals("AND", target.getSearchOperator());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"AND", "and", "And", "anD"})
	@DisplayName("AND(大文字小文字問わず)が渡された場合、ANDがセットされること")
	void setSearchOperator_ValidAnd(String input) {
		target.setSearchOperator(input);
		assertEquals("AND", target.getSearchOperator());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"OR", "or", "Or", "oR"})
	@DisplayName("OR(大文字小文字問わず)が渡された場合、ORがセットされること")
	void setSearchOperator_ValidOr(String input) {
		target.setSearchOperator(input);
		assertEquals("OR", target.getSearchOperator());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"NOT", "xor", " "})
	@DisplayName("不正な文字列(AND,OR以外)が渡された場合、IllegalArgumentExceptionがスローされること")
	void serSearchOperator_InvalidOperator(String input) {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> target.setSearchOperator(input)
		);
		
		assertEquals("不正な検索条件です: " + input, exception.getMessage());
	}

	@Test
	@DisplayName("検索条件がない場合、必須条件のみが出力されること")
	void dbWhere_NoConditions() throws Exception {
		String expected = "where (state_flg != '9' OR (state_flg = '9' AND leave_date >= '" + todayStr + "'))";
		String actual = target.dbWhere();
		assertEquals(expected, actual);
	}
	
	@Test
	@DisplayName("単一の検索条件がある場合、括弧で囲まれ、必須条件とANDで結合されること")
	void dbWhere_SingleCondition() throws Exception {
		target.setLastName("山田");
		String expected = "where (user_info.last_name = '山田' ) "
				+ "AND (state_flg != '9' OR (state_flg = '9' AND leave_date >= '" + todayStr + "'))";
		String actual = target.dbWhere();
		assertEquals(expected, actual);
	}
	
	@Test
	@DisplayName("複数の検索条件がある場合、指定した演算子(OR)で結合されて出力されること")
	void dbWhere_MultipleCondition_WithOR() throws Exception {
		target.setLastName("山田");
		target.setFirstName("太郎");
		target.setSearchOperator("OR");
		String expected = "where (user_info.last_name = '山田'  OR user_info.first_name = '太郎' ) "
				+ "AND (state_flg != '9' OR (state_flg = '9' AND leave_date >= '" + todayStr + "'))";
		String actual = target.dbWhere();
		assertEquals(expected, actual);
	}
	
	@Test
	@DisplayName("userIdsが指定されている場合、IN句がANDで結合されて出力されること")
	void dbWhere_WithUserIds() throws Exception {
		target.setLastName("山田");
		target.setFirstName("太郎");
		target.setUserIds(new String[]{"ID1", "ID2"});
		String expected = "where (user_info.last_name = '山田'  AND user_info.first_name = '太郎' ) "
				+ "AND user_info.user_info_id IN ('ID1' , 'ID2' ) "
				+ "AND (state_flg != '9' OR (state_flg = '9' AND leave_date >= '" + todayStr + "'))";
		String actual = target.dbWhere();
		assertEquals(expected, actual);
	}
	
	@Test
	@DisplayName("dbExecが1を返した場合、Trueが返ること")
	void dbUpdateAdmin_success() throws Exception {
		target.setAdmin("1");
		try (MockedStatic<DbBase> mockedDbBase = mockStatic(DbBase.class)) {
			mockedDbBase.when(() -> DbBase.dbExec(anyString())).thenReturn(1);
			
			boolean result = target.dbUpdateAdmin("USER0001");
			assertTrue(result);
			
			String expectedSql = "update user_info set admin = '1' where user_info_id = 'USER0001' ";
			mockedDbBase.verify(() -> DbBase.dbExec(expectedSql), times(1));
		}
	}
	
	@ParameterizedTest
	@ValueSource(ints = {0, 2})
	@DisplayName("dbExecが1以外を返した場合、AtareSysExceptionがスローされること")
	void dbUpdateAdmin_exception(int num) throws Exception {
		target.setAdmin("1");
		try (MockedStatic<DbBase> mockedDbBase = mockStatic(DbBase.class)) {
			mockedDbBase.when(() -> DbBase.dbExec(anyString())).thenReturn(num);
			
			AtareSysException exception = assertThrows(AtareSysException.class, () -> target.dbUpdateAdmin("USER0001"));
			assertEquals("dbUpdate number or record exception.", exception.getErrorCd());
		}
	}

}
