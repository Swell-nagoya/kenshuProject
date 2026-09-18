package test;


import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;
 
import jp.swell.dao.UserInfoDao;


class USerInfoDaoSearchTest {

	@Test
	void 氏名だけで検索条件を作成できる() throws Exception {
		UserInfoDao dao = new UserInfoDao();
		
		dao.setSearchFullName("新田");
		
		String where = dao.dbWhere();
		
		assertTrue(where.contains("LIKE"));
		assertTrue(where.contains("新田"));
		
		
	}
	
	@Test
	void 管理者だけで検索条件を作成できる() throws Exception {
		UserInfoDao dao= new UserInfoDao();
		
		dao.setSearchAdmin("admin");
		
		String where = dao.dbWhere();
		
		assertTrue(where.contains("user_info.admin"));
		assertTrue(where.contains("admin"));
	}

	
	@Test
	void 一般ユーザーだけで検索条件を作成できる() throws Exception{
		UserInfoDao dao = new UserInfoDao();
		
		dao.setSearchAdmin("general");
		
		String where = dao.dbWhere();
		
		assertTrue(where.contains("user_info.admin"));
		assertTrue(where.contains("general"));
	}
	
	@Test
	void 有効ステータスで検索条件を作成できる() throws Exception {
		UserInfoDao dao= new UserInfoDao();
		
		dao.setSearchState("1");
		
		String where = dao.dbWhere();
		
		assertTrue(where.contains("user_info.state_flg"));
		assertTrue(where.contains("1"));
	}
	
	@Test
	void 複数条件をANDで組み合わせられる() throws Exception {
		UserInfoDao dao = new UserInfoDao();
		
		dao.setSearchFullName("新田");
		dao.setSearchAdmin("admin");
		dao.setSearchState("1");
		
		String where = dao.dbWhere();
		
		assertTrue(where.contains("新田"));
		assertTrue(where.contains("user_info.admin"));
		assertTrue(where.contains("admin"));
		assertTrue(where.contains("user_info.state_flg"));
		assertTrue(where.contains("1"));
		assertTrue(where.contains("AND"));
		
		
	}
}
