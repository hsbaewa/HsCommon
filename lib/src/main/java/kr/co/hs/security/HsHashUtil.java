package kr.co.hs.security;

import java.io.File;

/**
 * 생성된 시간 2017-01-04, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsCommon
 * 패키지명 : kr.co.hs.security
 */

public class HsHashUtil {
    private static HsHashUtil instance = null;
    public static HsHashUtil getInstance(){
        if(instance == null)
            instance = new HsHashUtil();
        return instance;
    }

    public String getMD5(String str){
        return HsMD5.getInstance().getMD5(str);
    }

    public String getMD5(byte[] b){
        return HsMD5.getInstance().getMD5(b);
    }

    public String getMD5(File file){
        return HsMD5.getInstance().getMD5(file);
    }
}
