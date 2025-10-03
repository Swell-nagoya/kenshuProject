package jp.swell.common;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import jp.patasys.common.AtareSysException;
import jp.patasys.common.ParameterManager;
import jp.patasys.common.util.SendMail; // SendMail クラスをインポート
import jp.swell.dao.UserInfoDao;

public class SendMailCommon {

    // メール送信の設定
    private String smtpHost;
    private String mailFromAddress;
    private String mailFromName; // メール送信者名を追加
    private String userName;
    private String userPass;
    private String port;

    public SendMailCommon() {
        // コンストラクタでSMTP設定を初期化
        smtpHost = ParameterManager.getSystemParameter("mail_smtp_1");
        mailFromAddress = ParameterManager.getSystemParameter("error_mail_from_1");
        mailFromName = ParameterManager.getSystemParameter("error_mail_from_name"); // 名前の取得
        userName = ParameterManager.getSystemParameter("mail_user_1");
        userPass = ParameterManager.getSystemParameter("mail_password_1");
        port = ParameterManager.getSystemParameter("mail_port");
        
        // SMTPホストとポートが正しく取得されているかをログ出力
        System.out.println("SMTPホスト: " + smtpHost);
        System.out.println("メール送信元アドレス: " + mailFromAddress);
        System.out.println("メール送信者名: " + mailFromName);
        System.out.println("ポート: " + port);
    }

    // SendMail クラスの sendHtmlMail メソッドを使用するメール送信メソッド
    public void sendRecoveryEmailUsingSendMail(String toMail, String token) {
        final String RECOVERY_URL = "http://localhost:8080/kenshuProject/UserPassReset.do?key=" + token;

        try {
            // SendMail クラスのインスタンス作成
            SendMail mail = new SendMail();
            mail.setSmtpHost(smtpHost);
            mail.setMailFromAddress(mailFromAddress);
            mail.setMailFromName(mailFromName);
          
            Properties property = new Properties();
            property.put("mail.smtp.host", smtpHost);
            property.put("mail.smtp.port", port);  
            property.put("mail.smtp.auth", "true");
            property.put("mail.smtp.starttls.enable", "true"); 
            property.put("mail.smtp.debug", "true");
        
            Session session = Session.getInstance(property, new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(userName, userPass);
              }
            });
   
            //メッセージの作成
            MimeMessage mimeMessage = new MimeMessage(session);
            InternetAddress toAddress = new InternetAddress(toMail);
            mimeMessage.setRecipient(Message.RecipientType.TO, toAddress);
            InternetAddress fromAddress = new InternetAddress(mailFromAddress);
            mimeMessage.setFrom(fromAddress);
            mimeMessage.setSubject("title", "ISO-2022-JP");
            mimeMessage.setText(RECOVERY_URL + token, "ISO-2022-JP");

            // メールを送信
            Transport.send(mimeMessage);

            System.out.println("HTMLメール送信が完了しました。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // パスワードリセットメールを送信するメソッド
    public void sendPassMail(String email) throws AtareSysException {
        String token = UserInfoDao.generateToken(); // トークンを適切に生成または取得する必要があります
        long expirationTime = System.currentTimeMillis() + 30 * 60 * 1000; // 30分後

        try {
            // ユーザーIDをメールアドレスから取得
            UserInfoDao userInfoDao = new UserInfoDao();
            String userId = userInfoDao.getUserInfoIdByEmail(email);

            if (userId != null) {
                // トークンをデータベースに保存
                UserInfoDao tokenDao = new UserInfoDao();
                tokenDao.saveToken(email, token, expirationTime);

                // HTMLメールを送信
                sendRecoveryEmailUsingSendMail(email,token);
            } else {
                System.err.println("指定されたメールアドレスに対応するユーザーが見つかりません。");
            }
        } catch (AtareSysException e) {
            // 独自例外が発生した場合のハンドリング
            System.err.println("エラー: " + e.getMessage());
            throw e; // 再スローするか、適切に処理
        } catch (Exception e) {
            // その他の例外が発生した場合
            e.printStackTrace();
            throw new AtareSysException("パスワードリセットの処理中にエラーが発生しました。");
        }
    }
}
