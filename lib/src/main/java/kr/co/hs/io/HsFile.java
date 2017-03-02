package kr.co.hs.io;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileChannel;

import kr.co.hs.security.HsHashUtil;

/**
 * 생성된 시간 2017-02-28, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsCommon
 * 패키지명 : kr.co.hs
 */

public class HsFile extends File {
    public HsFile(String pathname) {
        super(pathname);
    }

    public HsFile(String parent, String child) {
        super(parent, child);
    }

    public HsFile(File parent, String child) {
        super(parent, child);
    }

    public HsFile(URI uri) {
        super(uri);
    }

    public String getMD5(){
        return HsHashUtil.getInstance().getMD5(this);
    }

    public boolean copy(Context context, HsFile dst){
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try
        {
            inChannel = new FileInputStream(this).getChannel();
            outChannel = new FileOutputStream(dst).getChannel();
            long size = inChannel.transferTo(0, inChannel.size(), outChannel);
            if(size == length()){
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(dst)));
                return true;
            }
            else
                return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        finally
        {
            if (inChannel != null && inChannel.isOpen()){
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outChannel != null && outChannel.isOpen()){
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public boolean mv(Context context, HsFile dst){
        if(copy(context, dst)){
            if(delete(context)){
                return true;
            }else{
                if(dst.exists()){
                    dst.delete(context);
                }
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean delete(Context context){
        if(delete()){
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(this)));
            return true;
        }else{
            return false;
        }
    }

    public HsFile getAbsoluteFile() {
        String parent = getAbsolutePath();
        return new HsFile(parent, getName());
    }

    public HsFile getParentFile() {
        String parent = getParent();
        return new HsFile(parent);
    }

    public String getExtexsion(){
        try{
            int pos = getName().lastIndexOf( "." );
            String ext = getName().substring( pos + 1 );
            return ext;
        }catch (Exception e){
            return null;
        }
    }

    public Intent getExecIntent(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String ext = getExtexsion();
        if(ext == null)
            return null;
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.toLowerCase());
        if(mimeType != null)
            intent.setDataAndType(Uri.fromFile(this), mimeType);
        else
            intent.setData(Uri.fromFile(this));
        return intent;
    }

    public String getMimeType(){
        String ext = getExtexsion();
        if(ext != null)
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.toLowerCase());
        else
            return null;
    }
}
