package jp.swell.controller;


import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.db.DbBase;
import jp.patasys.common.db.GetNumber;
import jp.patasys.common.http.WebBean;
import jp.patasys.common.util.Sup;
import jp.swell.common.ControllerBase;
import jp.swell.dao.ScheduleDao;
import jp.swell.dao.UserInfoDao;
import jp.swell.user.UserLoginInfo;

public class UserInfoValidator {
	
	public static String validateUserId(String userId) { 
		 
		if(userId == null || userId.length() == 0) {
			return "";
		}
		
		if (userId.length() <6 || userId.length() > 12) {
			return "IDは6文字以上12文字以下で入力してください。";
		}
		
		if(!userId.matches("^[a-zA-Z0-9]+$")) {
			return "IDは半角英数で入力してください。";
		}
		return"";
	}


public static String  validateEmail(String email)
{
	if(email ==null || email.length() ==0)
	{
		return "メールアドレスを入力してください。";
	}
	
	String emailRegex= "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    if (!email.matches(emailRegex))
    {
    	return "正しいメールアドレスを入力してください。";
    }
	
	return "";
}

public static boolean isHiragana(String input)
{
	return input != null
			&& input.matches("^[\\u3040-\\u309F-]+$");
}
}

