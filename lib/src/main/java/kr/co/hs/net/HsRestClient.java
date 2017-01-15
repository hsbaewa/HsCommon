package kr.co.hs.net;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Bae on 2016-12-25.
 */

public class HsRestClient {

    public boolean get(String strRequestUrl, Map<String, String> header, int timeout, DefaultHandler handler) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser parser = saxParserFactory.newSAXParser();

        URLConnection urlConnection = null;
        if(strRequestUrl.startsWith("http://")){
            urlConnection = getHttp(strRequestUrl, header, timeout);
        }else if(strRequestUrl.startsWith("https://")){
            urlConnection = getHttps(strRequestUrl, header, timeout);
        }else{
            return false;
        }

        InputStream inputStream = null;
        if(urlConnection != null){
            try {
                inputStream = urlConnection.getInputStream();
                if(inputStream == null){
                    if(urlConnection instanceof HttpURLConnection){
                        inputStream = ((HttpURLConnection) urlConnection).getErrorStream();
                    }else if(urlConnection instanceof HttpsURLConnection){
                        inputStream = ((HttpsURLConnection) urlConnection).getErrorStream();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if(urlConnection != null){
                    if(urlConnection instanceof HttpURLConnection){
                        ((HttpURLConnection) urlConnection).disconnect();
                    }else if(urlConnection instanceof HttpsURLConnection){
                        ((HttpsURLConnection) urlConnection).disconnect();
                    }
                }
            }
        }else{
            return false;
        }

        if(inputStream == null)
            return false;
        parser.parse(inputStream, handler);
        return true;
    }

    public String get(String strRequestUrl, Map<String, String> header, int timeout) {
        URLConnection urlConnection;
        if(strRequestUrl.startsWith("http://")){
            urlConnection = getHttp(strRequestUrl, header, timeout);
        }else if(strRequestUrl.startsWith("https://")){
            urlConnection = getHttps(strRequestUrl, header, timeout);
        }else{
            return null;
        }
        if(urlConnection != null){
            InputStream inputStream = null;
            try {
                inputStream = urlConnection.getInputStream();
                if(inputStream == null){

                    if(urlConnection instanceof HttpURLConnection){
                        inputStream = ((HttpURLConnection) urlConnection).getErrorStream();
                    }else if(urlConnection instanceof HttpsURLConnection){
                        inputStream = ((HttpsURLConnection) urlConnection).getErrorStream();
                    }
                }

                String line;
                StringBuffer stringBuffer = new StringBuffer();
                if(inputStream != null){
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    while((line = br.readLine())!=null){
                        stringBuffer.append(line);
                    }
                }

                return stringBuffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if(urlConnection != null){
                    if(urlConnection instanceof HttpURLConnection){
                        ((HttpURLConnection) urlConnection).disconnect();
                    }else if(urlConnection instanceof HttpsURLConnection){
                        ((HttpsURLConnection) urlConnection).disconnect();
                    }
                }
            }
        }else{
            return null;
        }
    }


    private URLConnection getHttp(String strRequestUrl, Map<String, String> header, int timeout){
        StringBuffer stringBuffer = new StringBuffer();
        HttpURLConnection httpURLConnection = null;

        try{
            URL url = new URL(strRequestUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            if(header != null){
                Iterator<String> keyIterator = header.keySet().iterator();
                while(keyIterator.hasNext()){
                    String key = keyIterator.next();
                    String value = header.get(key);
                    httpURLConnection.addRequestProperty(key, value);
                }
            }

            httpURLConnection.setConnectTimeout(timeout);
            httpURLConnection.setReadTimeout(timeout);

            return httpURLConnection;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private URLConnection getHttps(String strRequestUrl, Map<String, String> header, int timeout){
        StringBuffer stringBuffer = new StringBuffer();
        HttpsURLConnection httpsURLConnection = null;

        try{
            URL url = new URL(strRequestUrl);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            if(header != null){
                Iterator<String> keyIterator = header.keySet().iterator();
                while(keyIterator.hasNext()){
                    String key = keyIterator.next();
                    String value = header.get(key);
                    httpsURLConnection.addRequestProperty(key, value);
                }
            }

            httpsURLConnection.setConnectTimeout(timeout);
            httpsURLConnection.setReadTimeout(timeout);

            return httpsURLConnection;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean post(String strRequestUrl, Map<String, String> header, String body, int timeout, DefaultHandler handler) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser parser = saxParserFactory.newSAXParser();

        URLConnection urlConnection = null;
        if(strRequestUrl.startsWith("http://")){
            urlConnection = postHttp(strRequestUrl, header, body, timeout);
        }else if(strRequestUrl.startsWith("https://")){
            urlConnection = postHttps(strRequestUrl, header, body, timeout);
        }else{
            return false;
        }

        InputStream inputStream = null;
        if(urlConnection != null){
            try {
                inputStream = urlConnection.getInputStream();
                if(inputStream == null){
                    if(urlConnection instanceof HttpURLConnection){
                        inputStream = ((HttpURLConnection) urlConnection).getErrorStream();
                    }else if(urlConnection instanceof HttpsURLConnection){
                        inputStream = ((HttpsURLConnection) urlConnection).getErrorStream();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if(urlConnection != null){
                    if(urlConnection instanceof HttpURLConnection){
                        ((HttpURLConnection) urlConnection).disconnect();
                    }else if(urlConnection instanceof HttpsURLConnection){
                        ((HttpsURLConnection) urlConnection).disconnect();
                    }
                }
            }
        }else{
            return false;
        }

        if(inputStream == null)
            return false;
        parser.parse(inputStream, handler);
        return true;
    }

    public String post(String strRequestUrl, Map<String, String> header, String body, int timeout){
        URLConnection urlConnection;
        if(strRequestUrl.startsWith("http://")){
            urlConnection = postHttp(strRequestUrl, header, body, timeout);
        }else if(strRequestUrl.startsWith("https://")){
            urlConnection = postHttps(strRequestUrl, header, body, timeout);
        }else{
            return null;
        }
        if(urlConnection != null){
            InputStream inputStream = null;
            try {
                inputStream = urlConnection.getInputStream();
                if(inputStream == null){
                    if(urlConnection instanceof HttpURLConnection){
                        inputStream = ((HttpURLConnection) urlConnection).getErrorStream();
                    }else if(urlConnection instanceof HttpsURLConnection){
                        inputStream = ((HttpsURLConnection) urlConnection).getErrorStream();
                    }
                }

                String line;
                StringBuffer stringBuffer = new StringBuffer();
                if(inputStream != null){
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    while((line = br.readLine())!=null){
                        stringBuffer.append(line);
                    }
                }
                return stringBuffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if(urlConnection != null){
                    if(urlConnection instanceof HttpURLConnection){
                        ((HttpURLConnection) urlConnection).disconnect();
                    }else if(urlConnection instanceof HttpsURLConnection){
                        ((HttpsURLConnection) urlConnection).disconnect();
                    }
                }
            }
        }else{
            return null;
        }
    }

    private URLConnection postHttp(String strRequestUrl, Map<String, String> header, String body, int timeout){
        StringBuffer stringBuffer = new StringBuffer();
        HttpURLConnection httpURLConnection = null;

        try{
            URL url = new URL(strRequestUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            httpURLConnection.setConnectTimeout(timeout);
            httpURLConnection.setReadTimeout(timeout);

            if(header != null){
                Iterator<String> keyIterator = header.keySet().iterator();
                while(keyIterator.hasNext()){
                    String key = keyIterator.next();
                    String value = header.get(key);
                    httpURLConnection.addRequestProperty(key, value);
                }
            }

            if(body != null){
                byte[] payload = body.getBytes();
                BufferedOutputStream bos = new BufferedOutputStream(httpURLConnection.getOutputStream());
                bos.write(payload, 0,payload.length);
                bos.flush();
            }

            return httpURLConnection;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private URLConnection postHttps(String strRequestUrl, Map<String, String> header, String body, int timeout){
        StringBuffer stringBuffer = new StringBuffer();
        HttpsURLConnection httpsURLConnection = null;

        try{
            URL url = new URL(strRequestUrl);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);

            httpsURLConnection.setConnectTimeout(timeout);
            httpsURLConnection.setReadTimeout(timeout);

            if(header != null){
                Iterator<String> keyIterator = header.keySet().iterator();
                while(keyIterator.hasNext()){
                    String key = keyIterator.next();
                    String value = header.get(key);
                    httpsURLConnection.addRequestProperty(key, value);
                }
            }

            if(body != null){
                byte[] payload = body.getBytes();
                BufferedOutputStream bos = new BufferedOutputStream(httpsURLConnection.getOutputStream());
                bos.write(payload, 0,payload.length);
                bos.flush();
            }


            return httpsURLConnection;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
