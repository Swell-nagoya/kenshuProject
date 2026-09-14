package jp.swell.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 各コントローラの入力チェック(inputCheck)で重複していたロジックを集約した共通バリデーションクラス。.
 */
public final class CommonDoActionProcess
{
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final String HIRAGANA_REGEX = "^[\\u3040-\\u309Fー]+$";

    private CommonDoActionProcess()
    {
    }

    /**
     * 検索欄などの最大文字数チェックを行う。超過していれば errors に「(label)の入力内容が長すぎます。」を追加する。.
     *
     * @param errors エラーを格納するMap(bean.getItemErrors())
     * @param key errorsに設定するフィールドキー
     * @param value チェック対象の値
     * @param maxLength 許容する最大文字数
     * @param label エラーメッセージに使う項目名
     */
    public static void checkMaxLength(HashMap<String, String> errors, String key, String value, int maxLength, String label)
    {
        if (value != null && value.length() > maxLength)
        {
            errors.put(key, label + "の入力内容が長すぎます。");
        }
    }

    /**
     * 文字列がひらがな(ー含む)のみで構成されているかを判定する。.
     */
    public static boolean isHiragana(String input)
    {
        return input != null && input.matches(HIRAGANA_REGEX);
    }

    /**
     * メールアドレスとして正しい形式かどうかを判定する。.
     */
    public static boolean isValidEmail(String value)
    {
        if (value == null)
        {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(value);
        return matcher.matches();
    }

    /**
     * メールアドレスの必須チェックと形式チェックを行う。エラーがあれば errors に追加して true を返す。.
     *
     * @param errors エラーを格納するMap(bean.getItemErrors())
     * @param key errorsに設定するフィールドキー
     * @param email チェック対象のメールアドレス
     * @return 必須または形式エラーがあった場合 true
     */
    public static boolean checkEmailFormat(HashMap<String, String> errors, String key, String email)
    {
        if (isEmpty(email))
        {
            errors.put(key, "メールアドレスを入力してください。");
            return true;
        }
        else if (!isValidEmail(email))
        {
            errors.put(key, "正しいメールアドレスを入力してください。");
            return true;
        }
        return false;
    }

    /**
     * 氏名(姓・名)とそのよみの必須チェック、およびよみのひらがな判定をまとめて行う。.
     * UserInfoDetail / ContactListDetail で重複していたチェックを集約したもの。
     * errorsのキーは last_name, first_name, last_name_kana, first_name_kana 固定。
     */
    public static void checkNameAndKana(HashMap<String, String> errors,
            String lastName, String firstName, String lastNameKana, String firstNameKana)
    {
        if (isEmpty(lastName) && isEmpty(firstName))
        {
            errors.put("last_name", "氏名を入力してください。");
            errors.put("first_name", "");
        }
        else if (isEmpty(lastName))
        {
            errors.put("last_name", "名字を入力してください。");
        }
        else if (isEmpty(firstName))
        {
            errors.put("first_name", "名前を入力してください。");
        }

        if (isEmpty(lastNameKana) && isEmpty(firstNameKana))
        {
            errors.put("last_name_kana", "氏名のよみを入力してください。");
            errors.put("first_name_kana", "");
        }
        else if (isEmpty(lastNameKana))
        {
            errors.put("last_name_kana", "名字のよみを入力してください。");
        }
        else if (isEmpty(firstNameKana))
        {
            errors.put("first_name_kana", "名前のよみを入力してください。");
        }

        if (!isEmpty(lastNameKana) || !isEmpty(firstNameKana))
        {
            if (!isHiragana(lastNameKana) && !isHiragana(firstNameKana))
            {
                errors.put("last_name_kana", "氏名のよみはひらがなで入力してください。");
            }
            else if (!isHiragana(lastNameKana))
            {
                errors.put("last_name_kana", "名字のよみはひらがなで入力してください。");
            }
            else if (!isHiragana(firstNameKana))
            {
                errors.put("first_name_kana", "名前のよみはひらがなで入力してください。");
            }
        }
    }

    /**
     * 任意項目(ミドルネーム・旧姓等)について、名称が入力されている場合のみよみを必須にするチェックを行う。.
     *
     * @param errors エラーを格納するMap(bean.getItemErrors())
     * @param kanaKey errorsに設定するよみ側のフィールドキー
     * @param name 名称(未入力なら何もチェックしない)
     * @param kana よみ
     * @param label エラーメッセージに使う項目名(例: "ミドルネーム", "旧姓")
     */
    public static void checkOptionalKana(HashMap<String, String> errors, String kanaKey, String name, String kana, String label)
    {
        if (!isEmpty(name))
        {
            if (isEmpty(kana))
            {
                errors.put(kanaKey, label + "よみを入力してください。");
            }
            else if (!isHiragana(kana))
            {
                errors.put(kanaKey, label + "よみはひらがなで入力してください。");
            }
        }
    }

    /**
     * 必須選択・必須入力のチェックを行う。値が空であれば errors に message を追加して true を返す。.
     *
     * @param errors エラーを格納するMap(bean.getItemErrors())
     * @param key errorsに設定するフィールドキー
     * @param value チェック対象の値
     * @param message 未入力時のエラーメッセージ
     * @return 未入力エラーがあった場合 true
     */
    public static boolean checkRequired(HashMap<String, String> errors, String key, String value, String message)
    {
        if (isEmpty(value))
        {
            errors.put(key, message);
            return true;
        }
        return false;
    }

    /**
     * ID等の文字数範囲・半角英数字チェックを行う。値が空の場合は任意項目とみなし何もしない。.
     *
     * @param errors エラーを格納するMap(bean.getItemErrors())
     * @param key errorsに設定するフィールドキー
     * @param value チェック対象の値
     * @param minLength 許容する最小文字数
     * @param maxLength 許容する最大文字数
     * @param label エラーメッセージに使う項目名
     * @return フォーマットエラーがあった場合 true
     */
    public static boolean checkIdFormat(HashMap<String, String> errors, String key, String value, int minLength, int maxLength, String label)
    {
        if (isEmpty(value))
        {
            return false;
        }
        if (value.length() < minLength || value.length() > maxLength)
        {
            errors.put(key, label + "は" + minLength + "文字以上" + maxLength + "文字以下で入力してください。");
            return true;
        }
        if (!value.matches("^[a-zA-Z0-9]+$"))
        {
            errors.put(key, label + "は半角英数で入力してください。");
            return true;
        }
        return false;
    }

    /**
     * 文字列が数字のみで構成されているか判定する(空文字・nullはtrue扱い)。.
     */
    public static boolean isNumeric(String value)
    {
        if (!isEmpty(value) && value.trim().length() > 0)
        {
            try
            {
                Integer.parseInt(value);
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * 日付文字列(yyyyMMdd)が数字・正しい日付形式であり、かつ本日以降であるかをチェックする。.
     * 値が空の場合は任意項目とみなし何もしない。
     *
     * @param errors エラーを格納するMap(bean.getItemErrors())
     * @param key errorsに設定するフィールドキー
     * @param dateStr チェック対象の日付文字列(yyyyMMdd形式)
     * @return エラーがあった場合 true
     */
    public static boolean checkDateOnOrAfterToday(HashMap<String, String> errors, String key, String dateStr)
    {
        if (dateStr == null || dateStr.trim().isEmpty())
        {
            return false;
        }
        if (!isNumeric(dateStr))
        {
            errors.put(key, "数字を入力してください");
            return true;
        }
        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = dateFormat.parse(dateStr);

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1); // 昨日の日付
            Date yesterday = calendar.getTime();

            if (date.before(yesterday))
            {
                errors.put(key, "本日以降の日付を入力してください");
                return true;
            }
        }
        catch (ParseException e)
        {
            errors.put(key, "日付の形式が不正です");
            return true;
        }
        return false;
    }

    private static boolean isEmpty(String value)
    {
        return value == null || value.length() == 0;
    }
}
