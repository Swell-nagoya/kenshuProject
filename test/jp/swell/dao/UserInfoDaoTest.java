package jp.swell.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jp.patasys.common.AtareSysException;

/**
 * UserInfoDao#dbWhere() (検索条件からWHERE句を組み立てるロジック)のテストケース。.
 * DB接続は行わず、SQL文字列の組み立てのみを検証する。
 */
class UserInfoDaoTest
{
    @Test
    void dbWhere_条件が何も無ければ空文字を返す() throws AtareSysException
    {
        UserInfoDao dao = new UserInfoDao();
        String where = assertDoesNotThrow(dao::dbWhere,
                "検索条件を何も設定していない状態でNullPointerExceptionが発生しないこと");
        assertEquals("", where);
    }

    @Test
    void dbWhere_メールアドレス検索条件が含まれる() throws AtareSysException
    {
        UserInfoDao dao = new UserInfoDao();
        dao.setSearchMemail("foo@example.com");

        String where = dao.dbWhere();

        assertTrue(where.startsWith("where "));
        assertTrue(where.contains("user_info.memail LIKE"));
        assertTrue(where.contains("%foo@example.com%"));
    }

    @Test
    void dbWhere_区分検索_admin指定で1とadminのOR条件になる() throws AtareSysException
    {
        UserInfoDao dao = new UserInfoDao();
        dao.setSearchAdmin("admin");

        String where = dao.dbWhere();

        assertTrue(where.contains("user_info.admin LIKE"));
        assertTrue(where.contains("%1%"));
        assertTrue(where.contains("%admin%"));
        assertTrue(where.contains(" OR "), "OR句の前後にスペースが必要: [" + where + "]");
    }

    @Test
    void dbWhere_区分検索_general指定で0とgeneralのOR条件になる() throws AtareSysException
    {
        UserInfoDao dao = new UserInfoDao();
        dao.setSearchAdmin("general");

        String where = dao.dbWhere();

        assertTrue(where.contains("%0%"));
        assertTrue(where.contains("%general%"));
    }

    @Test
    void dbWhere_ステータス検索条件が含まれる() throws AtareSysException
    {
        UserInfoDao dao = new UserInfoDao();
        dao.setSearchStatus("9");

        String where = dao.dbWhere();

        assertTrue(where.contains("user_info.state_flg LIKE"));
        assertTrue(where.contains("%9%"));
    }

    @Test
    void dbWhere_複数条件はAND結合される() throws AtareSysException
    {
        UserInfoDao dao = new UserInfoDao();
        dao.setSearchMemail("foo@example.com");
        dao.setSearchAdmin("admin");
        dao.setSearchStatus("0");

        String where = dao.dbWhere();

        assertTrue(where.contains("user_info.memail LIKE"));
        assertTrue(where.contains("user_info.admin LIKE"));
        assertTrue(where.contains("user_info.state_flg LIKE"));
        // 3条件が2つの " AND " で結合されているはず
        assertEquals(2, countOccurrences(where, " AND "));
    }

    private static int countOccurrences(String text, String token)
    {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(token, index)) != -1)
        {
            count++;
            index += token.length();
        }
        return count;
    }
}
