package jp.swell.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

/**
 * CommonDoActionProcess(共通バリデーション処理)のテストケース。.
 */
class CommonDoActionProcessTest
{
    // ---- checkMaxLength ----

    @Test
    void checkMaxLength_許容文字数以内はエラーなし()
    {
        HashMap<String, String> errors = new HashMap<>();
        CommonDoActionProcess.checkMaxLength(errors, "name", "あいうえお", 100, "氏名");
        assertTrue(errors.isEmpty());
    }

    @Test
    void checkMaxLength_許容文字数を超えるとエラー()
    {
        HashMap<String, String> errors = new HashMap<>();
        String tooLong = repeat("あ", 101);
        CommonDoActionProcess.checkMaxLength(errors, "name", tooLong, 100, "氏名");
        assertEquals("氏名の入力内容が長すぎます。", errors.get("name"));
    }

    @Test
    void checkMaxLength_ちょうど上限は許容()
    {
        HashMap<String, String> errors = new HashMap<>();
        String exact = repeat("あ", 100);
        CommonDoActionProcess.checkMaxLength(errors, "name", exact, 100, "氏名");
        assertTrue(errors.isEmpty());
    }

    // ---- isHiragana ----

    @Test
    void isHiragana_ひらがなのみはtrue()
    {
        assertTrue(CommonDoActionProcess.isHiragana("たろう"));
    }

    @Test
    void isHiragana_長音記号を含んでもtrue()
    {
        assertTrue(CommonDoActionProcess.isHiragana("たろー"));
    }

    @Test
    void isHiragana_カタカナはfalse()
    {
        assertFalse(CommonDoActionProcess.isHiragana("タロウ"));
    }

    @Test
    void isHiragana_nullはfalse()
    {
        assertFalse(CommonDoActionProcess.isHiragana(null));
    }

    // ---- isValidEmail ----

    @Test
    void isValidEmail_正しい形式はtrue()
    {
        assertTrue(CommonDoActionProcess.isValidEmail("taro.yamada@example.com"));
    }

    @Test
    void isValidEmail_アットマークが無いとfalse()
    {
        assertFalse(CommonDoActionProcess.isValidEmail("taro.example.com"));
    }

    @Test
    void isValidEmail_nullはfalse()
    {
        assertFalse(CommonDoActionProcess.isValidEmail(null));
    }

    // ---- checkEmailFormat ----

    @Test
    void checkEmailFormat_未入力は必須エラー()
    {
        HashMap<String, String> errors = new HashMap<>();
        boolean hasError = CommonDoActionProcess.checkEmailFormat(errors, "memail", "");
        assertTrue(hasError);
        assertEquals("メールアドレスを入力してください。", errors.get("memail"));
    }

    @Test
    void checkEmailFormat_不正な形式はフォーマットエラー()
    {
        HashMap<String, String> errors = new HashMap<>();
        boolean hasError = CommonDoActionProcess.checkEmailFormat(errors, "memail", "invalid-mail");
        assertTrue(hasError);
        assertEquals("正しいメールアドレスを入力してください。", errors.get("memail"));
    }

    @Test
    void checkEmailFormat_正しい形式はエラーなし()
    {
        HashMap<String, String> errors = new HashMap<>();
        boolean hasError = CommonDoActionProcess.checkEmailFormat(errors, "memail", "taro@example.com");
        assertFalse(hasError);
        assertTrue(errors.isEmpty());
    }

    // ---- checkNameAndKana ----

    @Test
    void checkNameAndKana_氏名と氏名よみが揃っていればエラーなし()
    {
        HashMap<String, String> errors = new HashMap<>();
        CommonDoActionProcess.checkNameAndKana(errors, "山田", "太郎", "やまだ", "たろう");
        assertTrue(errors.isEmpty());
    }

    @Test
    void checkNameAndKana_氏名が両方未入力()
    {
        HashMap<String, String> errors = new HashMap<>();
        CommonDoActionProcess.checkNameAndKana(errors, "", "", "やまだ", "たろう");
        assertEquals("氏名を入力してください。", errors.get("last_name"));
        assertTrue(errors.containsKey("first_name"));
    }

    @Test
    void checkNameAndKana_姓のみ未入力()
    {
        HashMap<String, String> errors = new HashMap<>();
        CommonDoActionProcess.checkNameAndKana(errors, "", "太郎", "やまだ", "たろう");
        assertEquals("名字を入力してください。", errors.get("last_name"));
    }

    @Test
    void checkNameAndKana_よみがカタカナだとエラー()
    {
        HashMap<String, String> errors = new HashMap<>();
        CommonDoActionProcess.checkNameAndKana(errors, "山田", "太郎", "ヤマダ", "タロウ");
        assertEquals("氏名のよみはひらがなで入力してください。", errors.get("last_name_kana"));
    }

    // ---- checkOptionalKana ----

    @Test
    void checkOptionalKana_名称未入力ならチェックしない()
    {
        HashMap<String, String> errors = new HashMap<>();
        CommonDoActionProcess.checkOptionalKana(errors, "middle_name_kana", "", "", "ミドルネーム");
        assertTrue(errors.isEmpty());
    }

    @Test
    void checkOptionalKana_名称ありでよみ未入力はエラー()
    {
        HashMap<String, String> errors = new HashMap<>();
        CommonDoActionProcess.checkOptionalKana(errors, "middle_name_kana", "John", "", "ミドルネーム");
        assertEquals("ミドルネームよみを入力してください。", errors.get("middle_name_kana"));
    }

    @Test
    void checkOptionalKana_よみがひらがな以外はエラー()
    {
        HashMap<String, String> errors = new HashMap<>();
        CommonDoActionProcess.checkOptionalKana(errors, "middle_name_kana", "John", "ジョン", "ミドルネーム");
        assertEquals("ミドルネームよみはひらがなで入力してください。", errors.get("middle_name_kana"));
    }

    // ---- checkRequired ----

    @Test
    void checkRequired_未入力はエラー()
    {
        HashMap<String, String> errors = new HashMap<>();
        boolean hasError = CommonDoActionProcess.checkRequired(errors, "admin", "", "ユーザー区分を選択してください。");
        assertTrue(hasError);
        assertEquals("ユーザー区分を選択してください。", errors.get("admin"));
    }

    @Test
    void checkRequired_入力ありはエラーなし()
    {
        HashMap<String, String> errors = new HashMap<>();
        boolean hasError = CommonDoActionProcess.checkRequired(errors, "admin", "1", "ユーザー区分を選択してください。");
        assertFalse(hasError);
        assertTrue(errors.isEmpty());
    }

    // ---- checkIdFormat ----

    @Test
    void checkIdFormat_未入力は任意項目としてエラーなし()
    {
        HashMap<String, String> errors = new HashMap<>();
        boolean hasError = CommonDoActionProcess.checkIdFormat(errors, "insert_user_id", "", 6, 12, "ＩＤ");
        assertFalse(hasError);
        assertTrue(errors.isEmpty());
    }

    @Test
    void checkIdFormat_文字数が範囲外はエラー()
    {
        HashMap<String, String> errors = new HashMap<>();
        boolean hasError = CommonDoActionProcess.checkIdFormat(errors, "insert_user_id", "abc12", 6, 12, "ＩＤ");
        assertTrue(hasError);
        assertEquals("ＩＤは6文字以上12文字以下で入力してください。", errors.get("insert_user_id"));
    }

    @Test
    void checkIdFormat_半角英数以外はエラー()
    {
        HashMap<String, String> errors = new HashMap<>();
        boolean hasError = CommonDoActionProcess.checkIdFormat(errors, "insert_user_id", "abc123あ", 6, 12, "ＩＤ");
        assertTrue(hasError);
        assertEquals("ＩＤは半角英数で入力してください。", errors.get("insert_user_id"));
    }

    @Test
    void checkIdFormat_正しい形式はエラーなし()
    {
        HashMap<String, String> errors = new HashMap<>();
        boolean hasError = CommonDoActionProcess.checkIdFormat(errors, "insert_user_id", "abc12345", 6, 12, "ＩＤ");
        assertFalse(hasError);
        assertTrue(errors.isEmpty());
    }

    // ---- isNumeric ----

    @Test
    void isNumeric_数字のみはtrue()
    {
        assertTrue(CommonDoActionProcess.isNumeric("20260101"));
    }

    @Test
    void isNumeric_空文字はtrue扱い()
    {
        assertTrue(CommonDoActionProcess.isNumeric(""));
    }

    @Test
    void isNumeric_数字以外はfalse()
    {
        assertFalse(CommonDoActionProcess.isNumeric("abcd"));
    }

    // ---- checkDateOnOrAfterToday ----

    @Test
    void checkDateOnOrAfterToday_未入力はエラーなし()
    {
        HashMap<String, String> errors = new HashMap<>();
        boolean hasError = CommonDoActionProcess.checkDateOnOrAfterToday(errors, "leave_date", "");
        assertFalse(hasError);
        assertTrue(errors.isEmpty());
    }

    @Test
    void checkDateOnOrAfterToday_数字以外はエラー()
    {
        HashMap<String, String> errors = new HashMap<>();
        boolean hasError = CommonDoActionProcess.checkDateOnOrAfterToday(errors, "leave_date", "abcd");
        assertTrue(hasError);
        assertEquals("数字を入力してください", errors.get("leave_date"));
    }

    @Test
    void checkDateOnOrAfterToday_過去日付はエラー()
    {
        HashMap<String, String> errors = new HashMap<>();
        boolean hasError = CommonDoActionProcess.checkDateOnOrAfterToday(errors, "leave_date", "20200101");
        assertTrue(hasError);
        assertEquals("本日以降の日付を入力してください", errors.get("leave_date"));
    }

    @Test
    void checkDateOnOrAfterToday_本日の日付はエラーなし()
    {
        HashMap<String, String> errors = new HashMap<>();
        String today = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
        boolean hasError = CommonDoActionProcess.checkDateOnOrAfterToday(errors, "leave_date", today);
        assertFalse(hasError);
        assertTrue(errors.isEmpty());
    }

    @Test
    void checkDateOnOrAfterToday_未来日付はエラーなし()
    {
        HashMap<String, String> errors = new HashMap<>();
        boolean hasError = CommonDoActionProcess.checkDateOnOrAfterToday(errors, "leave_date", "99991231");
        assertFalse(hasError);
        assertTrue(errors.isEmpty());
    }

    /** Java8互換のためString#repeat()の代わりに使う文字列繰り返しヘルパー。 */
    private static String repeat(String s, int count)
    {
        StringBuilder sb = new StringBuilder(s.length() * count);
        for (int i = 0; i < count; i++)
        {
            sb.append(s);
        }
        return sb.toString();
    }
}
