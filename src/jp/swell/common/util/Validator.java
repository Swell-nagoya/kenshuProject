package jp.swell.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.http.WebBean;
import jp.swell.dao.ContactDao;
import jp.swell.dao.RoomDao;
import jp.swell.dao.ShiftDAO;
import jp.swell.dao.UserInfoDao;

/**
 * 入力値のバリデーションを行うユーティリティクラス。
 * {@link WebBean} から取得した入力値に対して各種チェックを行う。
 * 各チェックメソッドは、対象のフィールドにすでにエラーが設定されている場合は
 * チェックをスキップし、元のエラーメッセージを保持する。
 */
public class Validator {
	
	private WebBean bean;
	private HashMap<String, String> errors;
	
	/**
	 * コンストラクタ。
	 * @param bean リクエストパラメータとエラー情報を保持するWebBeanオブジェクト
	 */
	public Validator(WebBean bean) {
		this.bean = bean;
		this.errors = bean.getItemErrors();
	}
	
	public HashMap<String, String> getErrors() {
		return this.errors;
	}
	
	public boolean hasErrors() {
		return !this.errors.isEmpty();
	}
	
	/**
	 * 必須入力チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 */
	public void checkRequired(String fieldName) {
		checkRequired(fieldName, fieldName);
	}
	
	/**
	 * 必須入力チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param itemName エラーメッセージに表示する項目名
	 */
	public void checkRequired(String fieldName, String itemName) {
		checkRequired(fieldName, fieldName, itemName);
	}
	
	/**
	 * 必須入力チェックを行うメソッド。(エラーキーを指定)
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param errKey エラーマップに登録する際のキー名
	 * @param itemName エラーメッセージに表示する項目名
	 */
	public void checkRequired(String fieldName, String errKey, String itemName) {
		if (errors.containsKey(errKey)) return;
		String value = bean.value(fieldName);
		if (value.trim().isEmpty()) {
			errors.put(errKey, itemName + "を入力してください。");
		}
	}
	
	/**
	 * 2つの項目(ex: 姓、名)がセットで入力されているかの必須チェックを行うメソッド。
	 * 両方ともエラーの場合に統合したエラーメッセージを表示するときに使う。
	 * 両方とも未入力の場合、fieldName1 をキーとしてエラーをセット。
	 * 
	 * @param fieldName1 チェック対象のフィールド名1
	 * @param itemName1 エラーメッセージに表示する項目名1 (ex: 「名字」)
	 * @param fieldName2 チェック対象のフィールド名2
	 * @param itemName2 エラーメッセージに表示する項目名2 (ex: 「名前」)
	 * @param pairItemName 両方未入力時に表示する統合項目名 (ex: 「氏名」)
	 */
	public void checkRequiredPair(String fieldName1, String itemName1, String fieldName2, String itemName2, String pairItemName) {
		if (errors.containsKey(fieldName1) || errors.containsKey(fieldName2)) return;
		String value1 = bean.value(fieldName1);
		String value2 = bean.value(fieldName2);
		if (value1.trim().isEmpty() && value2.trim().isEmpty()) {
			errors.put(fieldName1, pairItemName + "を入力してください。");
			errors.put(fieldName2, "");
		}
		checkRequired(fieldName1, itemName1);
		checkRequired(fieldName2, itemName2);
	}
	
	/**
	 * ファイルの必須選択チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param errKey エラーマップに登録する際のキー名
	 * @param itemName エラーメッセージに表示する項目名
	 */
	public void checkFileRequired(String fieldName, String errKey, String itemName) {
		if (errors.containsKey(errKey)) return;
		byte[] fileData = (byte[]) bean.object(fieldName);
		if (fileData == null || fileData.length == 0) {
			errors.put(errKey, itemName + "を選択してください");
		}
	}
	
	/**
	 * 入力文字数の範囲チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param itemName エラーメッセージに表示する項目名
	 * @param minLength 最小文字数
	 * @param maxLength 最大文字数
	 */
	public void checkLength(String fieldName, String itemName, int minLength, int maxLength) {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (value.length() < minLength || maxLength < value.length()) {
			errors.put(fieldName, itemName + "は" + String.valueOf(minLength) + "文字以上" + String.valueOf(maxLength) + "文字以下で入力してください。");
		}
	}
	
	/**
	 * 最大文字数のチェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param itemName エラーメッセージに表示する項目名
	 * @param maxLength 最大文字数
	 */
	public void checkMaxLength(String fieldName, String itemName, int maxLength) {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (value.length() > maxLength) {
			errors.put(fieldName, itemName + "の入力内容が長すぎます。");
		}
	}
	
	/**
	 * 最大文字数のチェックを行うメソッド。(エラーキーを指定)
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param errKey エラーマップに登録する際のキー名
	 * @param itemName エラーメッセージに表示する項目名
	 * @param maxLength 最大文字数
	 */
	public void checkMaxLength(String fieldName, String errKey, String itemName, int maxLength) {
		if (errors.containsKey(errKey)) return;
		String value = bean.value(fieldName);
		if (value.length() > maxLength) {
			errors.put(errKey, itemName + "の入力内容が長すぎます。");
		}
	}
	
	/**
	 * メールアドレスの形式チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 */
	public void checkEmailFormat(String fieldName) {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (!value.isEmpty() && !value.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
			errors.put(fieldName, "正しいメールアドレスを入力してください。");
		}
	}
	
	/**
	 * メールアドレスがデータベースに登録済みかどうかのチェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param dao データベースへのアクセスを行うUserInfoDao
	 * @throws AtareSysException エラー
	 */
	public void checkEmailRegistered(String fieldName, UserInfoDao dao) throws AtareSysException {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (value.isEmpty()) return;
		
		if (!dao.isEmailExists(value)) {
			errors.put(fieldName, "このメールアドレスは登録されていません。");
		}
	}
	
	/**
	 * 新規登録時のメールアドレス重複チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param dao データベースへのアクセスを行うUserInfoDao
	 * @throws AtareSysException エラー
	 */
	public void checkEmailDuplicated(String fieldName, UserInfoDao dao) throws AtareSysException {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (value.isEmpty()) return;
		
		if (dao.isEmailExists(value)) {
			errors.put(fieldName, "このメールアドレスはすでに登録されています。");
		}
	}

	/**
	 * 更新時のメールアドレス重複チェックを行うメソッド。
	 * 自身のユーザーIDを除外して重複チェックを行う。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param dao データベースへのアクセスを行うUserInfoDao
	 * @param mainKey 自身の除外用キー
	 * @throws AtareSysException エラー
	 */
	public void checkEmailDuplicated(String fieldName, UserInfoDao dao, String mainKey) throws AtareSysException {
		if (errors.containsKey(fieldName)) return;
		if (mainKey.isEmpty()) {
			this.checkEmailDuplicated(fieldName, dao);
			return;
		}
		String value = bean.value(fieldName);
		if (value.isEmpty()) return;
		
		if (dao.isEmailExists(value, mainKey)) {
			errors.put(fieldName, "このメールアドレスはすでに登録されています。");
		}
	}

	/**
	 * シフト管理用の新規登録時のメールアドレス重複チェックを行うメソッド。
	 * 
	 * @param fieldName
	 * @param dao
	 * @throws AtareSysException
	 */
	public void checkEmailDuplicated(String fieldName, ShiftDAO dao) throws AtareSysException {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (value.isEmpty()) return;
		
		if (dao.isEmailExists(value)) {
			errors.put(fieldName, "このメールアドレスはすでに登録されています。");
		}
	}
	
	/**
	 * シフト管理用の更新時のメールアドレス重複チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param dao データベースへのアクセスを行うShiftDAO
	 * @param mainKey 自身の除外用キー
	 * @throws AtareSysException エラー
	 */
	public void checkEmailDuplicated(String fieldName, ShiftDAO dao, String mainKey) throws AtareSysException {
		if (errors.containsKey(fieldName)) return;
		if (mainKey.isEmpty()) {
			this.checkEmailDuplicated(fieldName, dao);
			return;
		}
		String value = bean.value(fieldName);
		if (value.isEmpty()) return;
		
		if (dao.isEmailExists(value, mainKey)) {
			errors.put(fieldName, "このメールアドレスはすでに登録されています。");
		}
	}
	
	/**
	 * 連絡先管理用の新規登録時のメールアドレス重複チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param dao データベースへのアクセスを行うUserInfoDao
	 * @throws AtareSysException エラー
	 */
	public void checkEmailDuplicated(String fieldName, ContactDao dao) throws AtareSysException {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (value.isEmpty()) return;
		if (dao.isEmailExists(value)) {
			errors.put(fieldName, "このメールアドレスはすでに登録されています。");
		}
	}
	
	/**
	 * 連絡先管理用の更新時のメールアドレス重複チェックを行うメソッド。
	 * 自身のユーザーIDを除外して重複チェックを行う。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param dao データベースへのアクセスを行うUserInfoDao
	 * @param mainKey 自身の除外用キー
	 * @throws AtareSysException エラー
	 */
	public void checkEmailDuplicated(String fieldName, ContactDao dao, String mainKey) throws AtareSysException {
		if (errors.containsKey(fieldName)) return;
		if (mainKey.isEmpty()) {
			this.checkEmailDuplicated(fieldName, dao);
			return;
		}
		String value = bean.value(fieldName);
		if (value.isEmpty()) return;
		
		try {
			int contactId = Integer.parseInt(mainKey);

			if (dao.isEmailExists(value, contactId)) {
				errors.put(fieldName, "このメールアドレスはすでに登録されています。");
			}
		} catch (NumberFormatException e) {
			this.checkEmailDuplicated(fieldName, dao);
			return;
		}
	}

	
	/**
	 * 新規登録時のID重複チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param dao データベースへのアクセスを行うUserInfoDao
	 * @throws AtareSysException エラー
	 */
	public void checkIdDuplicated(String fieldName, UserInfoDao dao) throws AtareSysException {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (value.isEmpty()) return;
		if (dao.isIdExists(value)) {
			errors.put(fieldName, "このＩＤはすでに登録されています。");
		}
	}
	
	/**
	 * 更新時のID重複チェックを行うメソッド。
	 * 自身のユーザーIDを除外して重複チェックを行う。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param dao データベースへのアクセスを行うUserInfoDao
	 * @param userInfoId 自身の除外用キー
	 * @throws AtareSysException エラー
	 */
	public void checkIdDuplicated(String fieldName, UserInfoDao dao, String mainKey) throws AtareSysException {
		if (errors.containsKey(fieldName)) return;
		if (mainKey.isEmpty()) {
			this.checkIdDuplicated(fieldName, dao);
			return;
		}
		String value = bean.value(fieldName);
		if (value.isEmpty()) return;
		if (dao.isIdExists(value, mainKey)) {
			errors.put(fieldName, "このＩＤはすでに登録されています。");
		}
	}
	
	/**
	 * シフト管理用の新規登録時のID重複チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param dao データベースへのアクセスを行うUserInfoDao
	 * @throws AtareSysException エラー
	 */
	public void checkIdDuplicated(String fieldName, ShiftDAO dao) throws AtareSysException {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (value.isEmpty()) return;
		if (dao.isIdExists(value)) {
			errors.put(fieldName, "このＩＤはすでに登録されています。");
		}
	}
	
	/**
	 * シフト管理用の更新時のID重複チェックを行うメソッド。
	 * 自身のユーザーIDを除外して重複チェックを行う。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param dao データベースへのアクセスを行うUserInfoDao
	 * @param userInfoId 更新対象のユーザーID
	 * @throws AtareSysException エラー
	 */
	public void checkIdDuplicated(String fieldName, ShiftDAO dao, String mainKey) throws AtareSysException {
		if (errors.containsKey(fieldName)) return;
		if (mainKey.isEmpty()) {
			this.checkIdDuplicated(fieldName, dao);
			return;
		}
		String value = bean.value(fieldName);
		if (value.isEmpty()) return;
		
		if (dao.isIdExists(value, mainKey)) {
			errors.put(fieldName, "このＩＤはすでに登録されています。");
		}
	}
	
	/**
	 * 部屋情報の新規登録時に部屋名の重複チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param dao データベースへのアクセスを行うRoomDao
	 * @throws AtareSysException エラー
	 */
	public void checkRoomNameDuplicated(String fieldName, RoomDao dao) throws AtareSysException {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (dao.isRoomNameExists(value)) {
			errors.put(fieldName, "この部屋名はすでに登録されています。");
		}
	}
	
	/**
	 * 部屋情報の更新時に部屋名の重複チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param dao データベースへのアクセスを行うRoomDao
	 * @param mainKey 更新対象の部屋ID
	 * @throws AtareSysException エラー
	 */
	public void checkRoomNameDuplicated(String fieldName, RoomDao dao, String mainKey) throws AtareSysException {
		if (errors.containsKey(fieldName)) return;
		if (mainKey.isEmpty()) {
			this.checkRoomNameDuplicated(fieldName, dao);
			return;
		}
		String value = bean.value(fieldName);
		if (value.isEmpty()) return;
		
		if (dao.isRoomNameExists(value, mainKey)) {
			errors.put(fieldName, "この部屋名はすでに登録されています。");
		}
	}
	
	/**
	 * ひらがな形式チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param itemName エラーメッセージに表示する項目名
	 */
	public void checkHiragana(String fieldName, String itemName) {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (!value.isEmpty() && !value.matches("^[ぁ-んー]+$")) {
			errors.put(fieldName, itemName + "はひらがなで入力してください。");
		}
	}
	
	/**
	 * 2つの項目をセットでひらがなで入力されているかのチェックを行うメソッド。
	 * 両方ともエラーの場合に統合したエラーメッセージを表示するときに使う。
	 * 
	 * @param fieldName1 チェック対象のフィールド名1
	 * @param itemName1 エラーメッセージに表示する項目名1
	 * @param fieldName2 チェック対象のフィールド名2
	 * @param itemName2 エラーメッセージに表示する項目名2
	 * @param pairItemName 両方未入力時に表示する統合項目名
	 */
	public void checkHiraganaPair(String fieldName1, String itemName1, String fieldName2, String itemName2, String pairItemName) {
		if (errors.containsKey(fieldName1) || errors.containsKey(fieldName2)) return;
		String value1 = bean.value(fieldName1);
		String value2 = bean.value(fieldName2);
		
		boolean isErr1 = !value1.isEmpty() && !value1.matches("^[ぁ-んー]+$");
		boolean isErr2 = !value2.isEmpty() && !value2.matches("^[ぁ-んー]+$");
		
		if (isErr1 && isErr2) {
			errors.put(fieldName1, pairItemName + "はひらがなで入力してください。");
			errors.put(fieldName2, "");
		}
		this.checkHiragana(fieldName1, itemName1);
		this.checkHiragana(fieldName2, itemName2);
	}
	
	/**
	 * 半角英数字チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param itemName エラーメッセージに表示する項目名
	 */
	public void checkHalfAlphanumeric(String fieldName, String itemName) {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (!value.isEmpty() && !value.matches("^[a-zA-Z0-9]+$")) {
			errors.put(fieldName, itemName + "は半角英数字で入力してください。");
		}
	}
	
	/**
	 * 値が整数(int)に変換できるかチェックするメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 * @param itemName エラーメッセージに表示する項目名
	 */
	public void checkInteger(String fieldName, String itemName) {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (!value.isEmpty()) {
			try {
				Integer.parseInt(value);
			} catch (NumberFormatException e) {
				errors.put(fieldName, itemName + "は整数で入力してください。");
			}
		}
	}
	
	/**
	 * 日付の形式チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 */
	public void checkDateFormat(String fieldName) {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (!value.matches("^\\d{8}$")) {
			errors.put(fieldName, "8桁の数字(ex: 20270101)を入力してください。");
			return;
		}
		
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			dateFormat.setLenient(false);
			dateFormat.parse(value);
			
		} catch (ParseException e) {
			errors.put(fieldName, "日付の形式が不正です。");
		}
	}
	
	/**
	 * 日付が本日以降の日付かチェックするメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 */
	public void checkFutureDate(String fieldName) {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (value.isEmpty()) return;
		
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			dateFormat.setLenient(false);
			Date targetDate = dateFormat.parse(value);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
			Date yesterday = calendar.getTime();
			
			if (targetDate.before(yesterday)) {
				errors.put(fieldName, "本日以降の日付を入力してください。");
			}
		} catch (ParseException e) {
			errors.put(fieldName, "日付の形式が不正です。");
		}
	}
	
	/**
	 * 氏名の形式チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 */
	public void checkNameFormat(String fieldName) {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (value.isEmpty()) return;
		
		String regex = "^[ぁ-んァ-ヶーa-zA-Z\\u30a0-\\u30ff\\u3040-\\u309f\\u3005-\\u3006\\u30e0-\\u9fcf]+$";
		if (!value.matches(regex)) {
			errors.put(fieldName, "正しい氏名を入力してください。");
		}
	}
	
	/**
	 * 配属先の形式チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 */
	public void checkWorkPlaceFormat(String fieldName) {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (value.isEmpty()) return;
		
		String regex = "^[ぁ-んァ-ヶーa-zA-Z0-9\\u30a0-\\u30ff\\u3040-\\u309f\\u3005-\\u3006\\u30e0-\\u9fcf]+$";
		if (!value.matches(regex)) {
			errors.put(fieldName, "正しい配属先を入力してください。");
		}
	}
	
	/**
	 * 電話番号の形式チェックを行うメソッド。
	 * 
	 * @param fieldName チェック対象のフィールド名
	 */
	public void checkPhoneNumberFormat(String fieldName) {
		if (errors.containsKey(fieldName)) return;
		String value = bean.value(fieldName);
		if (value.isEmpty()) return;
		
		String regex = "^0\\d{1,4}-\\d{1,4}-\\d{3,4}$";
		if (!value.matches(regex)) {
			errors.put(fieldName, "正しい電話番号を入力してください。");
		}
	}
	
	
	/**
	 * 入力値が変更されているかをチェックするメソッド。
	 * 大文字・小文字を区別せずに比較する。
	 * 
	 * @param fieldName チェック対象のフィールド名（新しい値）の配列
	 * @param beforeFieldName 比較対象のフィールド名（以前の値）の配列
	 * @param errKey エラーマップに登録する際のキー名
	 * @param errorMessage
	 */
	public void checkNoChange(String[] fieldNames, String[] beforeFieldNames, String errKey) {
		if (errors.containsKey(errKey)) return;
		boolean isChanged = false;
		for (int i = 0; i < fieldNames.length; i++) {
			String newValue = bean.value(fieldNames[i]);
			String beforeValue = bean.value(beforeFieldNames[i]);
			if (!newValue.equals(beforeValue)) {
				isChanged = true;
				break;
			}
		}
		if (!isChanged) {
			errors.put(errKey, "変更内容がありません。少なくとも1つの項目を変更してください。");
		}
	}
}