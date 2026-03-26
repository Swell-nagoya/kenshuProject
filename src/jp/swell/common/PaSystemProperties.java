package jp.swell.common;

import javax.servlet.ServletContext;

import jp.patasys.common.AtareSysException;

public class PaSystemProperties
{
    /**
     * 初期化
     *
     * @param servletContext
     * @throws AtareSysException
     */
    public static void initialize() throws AtareSysException
    {
    }
    /**
     * 初期化
     *
     * @param servletContext
     * @throws AtareSysException
     */
    public static void initialize(ServletContext servletContext) throws AtareSysException
    {
        initialize();
    }
    /**
     * フォルダー名の最後に / をつける。.
     *
     * @throws AtareSysException
     */
    public static String slashString(String folderName) throws AtareSysException
    {
        if (folderName == null || folderName.length() == 0)
        {
            throw new AtareSysException("フォルダー名が指定されていません");
        }

        if ("/".equals(folderName.substring(folderName.length() - 1)))
        {
            return folderName;
        }
        else
        {
            return (folderName + "/");
        }
    }

}
