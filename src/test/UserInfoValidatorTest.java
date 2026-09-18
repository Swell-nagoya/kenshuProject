package test;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

import jp.swell.controller.UserInfoValidator;

class UserInfoValidatorTest {
	
	//ユーザーID
	
	@Test
	void userId_正常() {
		String result = UserInfoValidator.validateUserId("abc123");
		
		assertEquals("",result);
	}

	@Test
	void userId_文字数不足(){
		String result = UserInfoValidator.validateUserId("abc");
		
		assertEquals("IDは6文字以上12文字以下で入力してください。",result);
	}
	
	@Test
	void userId_記号を含む() {
		String result = UserInfoValidator.validateUserId("abc-123");
		
		assertEquals("IDは半角英数で入力してください。",result);
	}
	
	//メールアドレス
	
	@Test
	void email_正常() {
		String result = UserInfoValidator.validateEmail("abc@example.com");
		
		assertEquals("",result);
	}
	
	@Test
	void email_空欄() {
		String result =
				UserInfoValidator.validateEmail("");
		
		assertEquals("メールアドレスを入力してください。",result);
	}
	
	@Test
	
	void email_形式不足() {
		String result =
				UserInfoValidator.validateEmail("abc");
		
		assertEquals("正しいメールアドレスを入力してください。", result);
	}
	
	//ひらがな
	
	@Test
	void hiragana_正常() {
		assertTrue(UserInfoValidator.isHiragana("あいうえお"));
	}
	
	@Test
	void hiragana_カタカナ() {
		assertFalse(UserInfoValidator.isHiragana("アイウエオ"));
	}
	
	@Test
	void hiragana_英字() {
		assertFalse(UserInfoValidator.isHiragana("abc"));
	}
	

}
