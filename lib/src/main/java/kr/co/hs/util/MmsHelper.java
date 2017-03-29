package kr.co.hs.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 생성된 시간 2017-03-24, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsCommon
 * 패키지명 : kr.co.hs.util
 * Mms 정보 불러오는게 뭔가 굉장히 복잡하다
 * 그래서 만든 helper 클래스
 */

public class MmsHelper {
    private static MmsHelper instance = null;
    public static MmsHelper getInstance(){
        if(instance == null)
            instance = new MmsHelper();
        return instance;
    }

    private MmsHelper() {
    }

    public String decodeSubject(String encodedSubject, int subjectCharSet){
        if(subjectCharSet != CharacterSets.ANY_CHARSET){
            try{
                EncodedStringValue encodedStringValue = new EncodedStringValue(subjectCharSet, encodedSubject.getBytes(CharacterSets.MIMENAME_ISO_8859_1));
                return encodedStringValue.toString();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }else{
            return encodedSubject;
        }
    }

    public String getPartText(Context context, long msgId){
        Cursor partCursor = context.getContentResolver().query(Uri.parse("content://mms/part"), null, Telephony.Mms.Part.MSG_ID+"=? and "+Telephony.Mms.Part.CONTENT_TYPE+"=?", new String[]{String.valueOf(msgId), "text/plain"}, null);
        String text = "";
        try{
            while(partCursor.moveToNext()){
                long partId = partCursor.getLong(partCursor.getColumnIndex(Telephony.Mms.Part._ID));
                String data = partCursor.getString(partCursor.getColumnIndex(Telephony.Mms.Part._DATA));
                if(data == null){
                    text = partCursor.getString(partCursor.getColumnIndex(Telephony.Mms.Part.TEXT));
                }else{
                    Uri partUri = Uri.parse("content://mms/part/"+partId);
                    StringBuffer buffer = new StringBuffer();
                    BufferedReader br = null;
                    try{
                        br = new BufferedReader(new InputStreamReader(context.getContentResolver().openInputStream(partUri)));
                        String line;
                        while((line = br.readLine()) != null){
                            buffer.append(line);
                        }
                        text += buffer.toString();
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        if(br != null)
                            br.close();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(partCursor != null && !partCursor.isClosed())
                partCursor.close();
        }
        return text;
    }

    public String getFromAddress(Context context, long mmsId){
        Uri.Builder builder = Uri.parse("content://mms").buildUpon();
        builder.appendPath(String.valueOf(mmsId)).appendPath("addr");

        Cursor cursor = context.getContentResolver().query(builder.build(), null, "type=137", null, null);
        try{
            if(cursor.moveToFirst()){
                return cursor.getString(cursor.getColumnIndex(Telephony.Mms.Addr.ADDRESS));
            }else
                return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
    }

    public String getFromContact(Context context, String address){
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(address));
        String[] projection = new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME};

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        try{
            if (cursor != null) {
                if (cursor.moveToFirst()){
                    return cursor.getString(0);
                }
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
    }
}



class CharacterSets {
    /**
     * IANA assigned MIB enum numbers.
     *
     * From wap-230-wsp-20010705-a.pdf
     * Any-charset = <Octet 128>
     * Equivalent to the special RFC2616 charset value "*"
     */
    public static final int ANY_CHARSET = 0x00;
    public static final int US_ASCII    = 0x03;
    public static final int ISO_8859_1  = 0x04;
    public static final int ISO_8859_2  = 0x05;
    public static final int ISO_8859_3  = 0x06;
    public static final int ISO_8859_4  = 0x07;
    public static final int ISO_8859_5  = 0x08;
    public static final int ISO_8859_6  = 0x09;
    public static final int ISO_8859_7  = 0x0A;
    public static final int ISO_8859_8  = 0x0B;
    public static final int ISO_8859_9  = 0x0C;
    public static final int SHIFT_JIS   = 0x11;
    public static final int UTF_8       = 0x6A;
    public static final int BIG5        = 0x07EA;
    public static final int UCS2        = 0x03E8;
    public static final int UTF_16      = 0x03F7;

    /**
     * If the encoding of given data is unsupported, use UTF_8 to decode it.
     */
    public static final int DEFAULT_CHARSET = UTF_8;

    /**
     * Array of MIB enum numbers.
     */
    private static final int[] MIBENUM_NUMBERS = {
            ANY_CHARSET,
            US_ASCII,
            ISO_8859_1,
            ISO_8859_2,
            ISO_8859_3,
            ISO_8859_4,
            ISO_8859_5,
            ISO_8859_6,
            ISO_8859_7,
            ISO_8859_8,
            ISO_8859_9,
            SHIFT_JIS,
            UTF_8,
            BIG5,
            UCS2,
            UTF_16,
    };

    /**
     * The Well-known-charset Mime name.
     */
    public static final String MIMENAME_ANY_CHARSET = "*";
    public static final String MIMENAME_US_ASCII    = "us-ascii";
    public static final String MIMENAME_ISO_8859_1  = "iso-8859-1";
    public static final String MIMENAME_ISO_8859_2  = "iso-8859-2";
    public static final String MIMENAME_ISO_8859_3  = "iso-8859-3";
    public static final String MIMENAME_ISO_8859_4  = "iso-8859-4";
    public static final String MIMENAME_ISO_8859_5  = "iso-8859-5";
    public static final String MIMENAME_ISO_8859_6  = "iso-8859-6";
    public static final String MIMENAME_ISO_8859_7  = "iso-8859-7";
    public static final String MIMENAME_ISO_8859_8  = "iso-8859-8";
    public static final String MIMENAME_ISO_8859_9  = "iso-8859-9";
    public static final String MIMENAME_SHIFT_JIS   = "shift_JIS";
    public static final String MIMENAME_UTF_8       = "utf-8";
    public static final String MIMENAME_BIG5        = "big5";
    public static final String MIMENAME_UCS2        = "iso-10646-ucs-2";
    public static final String MIMENAME_UTF_16      = "utf-16";

    public static final String DEFAULT_CHARSET_NAME = MIMENAME_UTF_8;

    /**
     * Array of the names of character sets.
     */
    private static final String[] MIME_NAMES = {
            MIMENAME_ANY_CHARSET,
            MIMENAME_US_ASCII,
            MIMENAME_ISO_8859_1,
            MIMENAME_ISO_8859_2,
            MIMENAME_ISO_8859_3,
            MIMENAME_ISO_8859_4,
            MIMENAME_ISO_8859_5,
            MIMENAME_ISO_8859_6,
            MIMENAME_ISO_8859_7,
            MIMENAME_ISO_8859_8,
            MIMENAME_ISO_8859_9,
            MIMENAME_SHIFT_JIS,
            MIMENAME_UTF_8,
            MIMENAME_BIG5,
            MIMENAME_UCS2,
            MIMENAME_UTF_16,
    };

    private static final HashMap<Integer, String> MIBENUM_TO_NAME_MAP;
    private static final HashMap<String, Integer> NAME_TO_MIBENUM_MAP;

    static {
        // Create the HashMaps.
        MIBENUM_TO_NAME_MAP = new HashMap<Integer, String>();
        NAME_TO_MIBENUM_MAP = new HashMap<String, Integer>();
        assert(MIBENUM_NUMBERS.length == MIME_NAMES.length);
        int count = MIBENUM_NUMBERS.length - 1;
        for(int i = 0; i <= count; i++) {
            MIBENUM_TO_NAME_MAP.put(MIBENUM_NUMBERS[i], MIME_NAMES[i]);
            NAME_TO_MIBENUM_MAP.put(MIME_NAMES[i], MIBENUM_NUMBERS[i]);
        }
    }

    private CharacterSets() {} // Non-instantiatable

    /**
     * Map an MIBEnum number to the name of the charset which this number
     * is assigned to by IANA.
     *
     * @param mibEnumValue An IANA assigned MIBEnum number.
     * @return The name string of the charset.
     * @throws UnsupportedEncodingException
     */
    public static String getMimeName(int mibEnumValue)
            throws UnsupportedEncodingException {
        String name = MIBENUM_TO_NAME_MAP.get(mibEnumValue);
        if (name == null) {
            throw new UnsupportedEncodingException();
        }
        return name;
    }

    /**
     * Map a well-known charset name to its assigned MIBEnum number.
     *
     * @param mimeName The charset name.
     * @return The MIBEnum number assigned by IANA for this charset.
     * @throws UnsupportedEncodingException
     */
    public static int getMibEnumValue(String mimeName)
            throws UnsupportedEncodingException {
        if(null == mimeName) {
            return -1;
        }

        Integer mibEnumValue = NAME_TO_MIBENUM_MAP.get(mimeName);
        if (mibEnumValue == null) {
            throw new UnsupportedEncodingException();
        }
        return mibEnumValue;
    }
}


class EncodedStringValue implements Cloneable {
    private static final String TAG = "EncodedStringValue";
    private static final boolean DEBUG = false;
    private static final boolean LOCAL_LOGV = false;

    /**
     * The Char-set value.
     */
    private int mCharacterSet;

    /**
     * The Text-string value.
     */
    private byte[] mData;

    @Override
    public String toString() {
        return EncodedStringValue.concat(new EncodedStringValue[]{this});
    }

    /**
     * Constructor.
     *
     * @param charset the Char-set value
     * @param data the Text-string value
     * @throws NullPointerException if Text-string value is null.
     */
    public EncodedStringValue(int charset, byte[] data) {
        // TODO: CharSet needs to be validated against MIBEnum.
        if(null == data) {
            throw new NullPointerException("EncodedStringValue: Text-string is null.");
        }

        mCharacterSet = charset;
        mData = new byte[data.length];
        System.arraycopy(data, 0, mData, 0, data.length);
    }

    /**
     * Constructor.
     *
     * @param data the Text-string value
     * @throws NullPointerException if Text-string value is null.
     */
    public EncodedStringValue(byte[] data) {
        this(CharacterSets.DEFAULT_CHARSET, data);
    }

    public EncodedStringValue(String data) {
        try {
            mData = data.getBytes(CharacterSets.DEFAULT_CHARSET_NAME);
            mCharacterSet = CharacterSets.DEFAULT_CHARSET;
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Default encoding must be supported.", e);
        }
    }

    /**
     * Get Char-set value.
     *
     * @return the value
     */
    public int getCharacterSet() {
        return mCharacterSet;
    }

    /**
     * Set Char-set value.
     *
     * @param charset the Char-set value
     */
    public void setCharacterSet(int charset) {
        // TODO: CharSet needs to be validated against MIBEnum.
        mCharacterSet = charset;
    }

    /**
     * Get Text-string value.
     *
     * @return the value
     */
    public byte[] getTextString() {
        byte[] byteArray = new byte[mData.length];

        System.arraycopy(mData, 0, byteArray, 0, mData.length);
        return byteArray;
    }

    /**
     * Set Text-string value.
     *
     * @param textString the Text-string value
     * @throws NullPointerException if Text-string value is null.
     */
    public void setTextString(byte[] textString) {
        if(null == textString) {
            throw new NullPointerException("EncodedStringValue: Text-string is null.");
        }

        mData = new byte[textString.length];
        System.arraycopy(textString, 0, mData, 0, textString.length);
    }

    /**
     * Convert this object to a {@link String}. If the encoding of
     * the EncodedStringValue is null or unsupported, it will be
     * treated as iso-8859-1 encoding.
     *
     * @return The decoded String.
     */
    public String getString()  {
        if (CharacterSets.ANY_CHARSET == mCharacterSet) {
            return new String(mData); // system default encoding.
        } else {
            try {
                String name = CharacterSets.getMimeName(mCharacterSet);
                return new String(mData, name);
            } catch (UnsupportedEncodingException e) {
                if (LOCAL_LOGV) {
                    Log.v(TAG, e.getMessage(), e);
                }
                try {
                    return new String(mData, CharacterSets.MIMENAME_ISO_8859_1);
                } catch (UnsupportedEncodingException e1) {
                    return new String(mData); // system default encoding.
                }
            }
        }
    }

    /**
     * Append to Text-string.
     *
     * @param textString the textString to append
     * @throws NullPointerException if the text String is null
     *                      or an IOException occured.
     */
    public void appendTextString(byte[] textString) {
        if(null == textString) {
            throw new NullPointerException("Text-string is null.");
        }

        if(null == mData) {
            mData = new byte[textString.length];
            System.arraycopy(textString, 0, mData, 0, textString.length);
        } else {
            ByteArrayOutputStream newTextString = new ByteArrayOutputStream();
            try {
                newTextString.write(mData);
                newTextString.write(textString);
            } catch (IOException e) {
                Log.e(TAG, "exception thrown", e);
                throw new NullPointerException(
                        "appendTextString: failed when write a new Text-string");
            }

            mData = newTextString.toByteArray();
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        int len = mData.length;
        byte[] dstBytes = new byte[len];
        System.arraycopy(mData, 0, dstBytes, 0, len);

        try {
            return new EncodedStringValue(mCharacterSet, dstBytes);
        } catch (Exception e) {
            Log.e(TAG, "failed to clone an EncodedStringValue: " + this);
            Log.e(TAG, "exception thrown", e);
            throw new CloneNotSupportedException(e.getMessage());
        }
    }

    /**
     * Split this encoded string around matches of the given pattern.
     *
     * @param pattern the delimiting pattern
     * @return the array of encoded strings computed by splitting this encoded
     *         string around matches of the given pattern
     */
    public EncodedStringValue[] split(String pattern) {
        String[] temp = getString().split(pattern);
        EncodedStringValue[] ret = new EncodedStringValue[temp.length];
        for (int i = 0; i < ret.length; ++i) {
            try {
                ret[i] = new EncodedStringValue(mCharacterSet,
                        temp[i].getBytes());
            } catch (NullPointerException e) {
                // Can't arrive here
                return null;
            }
        }
        return ret;
    }

    /**
     * Extract an EncodedStringValue[] from a given String.
     */
    public static EncodedStringValue[] extract(String src) {
        String[] values = src.split(";");

        ArrayList<EncodedStringValue> list = new ArrayList<EncodedStringValue>();
        for (int i = 0; i < values.length; i++) {
            if (values[i].length() > 0) {
                list.add(new EncodedStringValue(values[i]));
            }
        }

        int len = list.size();
        if (len > 0) {
            return list.toArray(new EncodedStringValue[len]);
        } else {
            return null;
        }
    }

    /**
     * Concatenate an EncodedStringValue[] into a single String.
     */
    public static String concat(EncodedStringValue[] addr) {
        StringBuilder sb = new StringBuilder();
        int maxIndex = addr.length - 1;
        for (int i = 0; i <= maxIndex; i++) {
            sb.append(addr[i].getString());
            if (i < maxIndex) {
                sb.append(";");
            }
        }

        return sb.toString();
    }

    public static EncodedStringValue copy(EncodedStringValue value) {
        if (value == null) {
            return null;
        }

        return new EncodedStringValue(value.mCharacterSet, value.mData);
    }

    public static EncodedStringValue[] encodeStrings(String[] array) {
        int count = array.length;
        if (count > 0) {
            EncodedStringValue[] encodedArray = new EncodedStringValue[count];
            for (int i = 0; i < count; i++) {
                encodedArray[i] = new EncodedStringValue(array[i]);
            }
            return encodedArray;
        }
        return null;
    }
}