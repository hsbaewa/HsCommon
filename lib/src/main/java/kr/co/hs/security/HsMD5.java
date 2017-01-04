package kr.co.hs.security;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 생성된 시간 2017-01-04, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsCommon
 * 패키지명 : kr.co.hs.security
 */

public class HsMD5 {
    private final static String	EMPTY_MD5_RESULT	= "00000000000000000000000000000000";


    private static HsMD5 instance = null;
    public static HsMD5 getInstance(){
        if(instance == null)
            instance = new HsMD5();
        return instance;
    }

    public String getMD5(String str){
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(str.getBytes());
            String strMD5 = new BigInteger(1, messageDigest).toString(16);
            while(strMD5.length() < 32)
            {
                strMD5 = "0" + strMD5;
            }
            return strMD5;
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return EMPTY_MD5_RESULT;
    }


    public String getMD5(byte[] b, int bufferSize){
        if(b == null)
            return null;

        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            ByteArrayInputStream bais = new ByteArrayInputStream(b);
            byte[] buf = new byte[bufferSize];
            int nReadCnt = 0;

            while((nReadCnt = bais.read(buf, 0, buf.length))>0){
                md.update(buf, 0, nReadCnt);
            }

            byte[] messageDigest = md.digest();
            String strMD5 = new BigInteger(1, messageDigest).toString(16);

            while(strMD5.length() < 32)
            {
                strMD5 = "0" + strMD5;
            }

            return strMD5;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public String getMD5(byte[] b){
        return getMD5(b, 8192);
    }


    public String getMD5(File file, int bufSize){
        if(!file.exists())
            return null;

        try{
            MessageDigest md = MessageDigest.getInstance("MD5");

            FileInputStream fis = new FileInputStream(file);
            byte[] buf = new byte[bufSize];
            int nReadCnt = 0;

            while((nReadCnt = fis.read(buf, 0, buf.length))>0){
                md.update(buf, 0, nReadCnt);
            }

            byte[] messageDigest = md.digest();
            String strMD5 = new BigInteger(1, messageDigest).toString(16);

            while(strMD5.length() < 32)
            {
                strMD5 = "0" + strMD5;
            }

            return strMD5;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public String getMD5(File file){
        return getMD5(file, 8192);
    }
}
