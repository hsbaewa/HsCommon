package kr.co.hs.net;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Bae on 2017-01-15.
 */

@Deprecated
public abstract class HsSaxHandler extends DefaultHandler {

    private String mCurrentTagName;
    private StringBuffer mStringBuffer = new StringBuffer();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        this.mCurrentTagName = qName;
        this.mStringBuffer.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        this.mStringBuffer.append(ch, start, length);
        doElement(this.mCurrentTagName, this.mStringBuffer.toString());
    }

    public String getmCurrentTagName() {
        return mCurrentTagName;
    }

    public abstract void doElement(String tag, String value);
}
