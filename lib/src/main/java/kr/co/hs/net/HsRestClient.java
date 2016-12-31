package kr.co.hs.net;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Bae on 2016-12-25.
 */

public class HsRestClient {

    public String get(String strRequestUrl, Map<String, String> header, int timeout) {
        if(strRequestUrl.startsWith("http://")){
            return getHttp(strRequestUrl, header, timeout);
        }else if(strRequestUrl.startsWith("https://")){
            return getHttps(strRequestUrl, header, timeout);
        }else{
            return null;
        }
    }


    private String getHttp(String strRequestUrl, Map<String, String> header, int timeout){
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

            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while((line = br.readLine())!=null){
                    stringBuffer.append(line);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return stringBuffer.toString();
    }

    private String getHttps(String strRequestUrl, Map<String, String> header, int timeout){
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

            int responseCode = httpsURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                while((line = br.readLine())!=null){
                    stringBuffer.append(line);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(httpsURLConnection != null)
                httpsURLConnection.disconnect();
        }
        return stringBuffer.toString();
    }

    public String post(String strRequestUrl, Map<String, String> header, String body, int timeout){
        if(strRequestUrl.startsWith("http://")){
            return postHttp(strRequestUrl, header, body, timeout);
        }else if(strRequestUrl.startsWith("https://")){
            return postHttps(strRequestUrl, header, body, timeout);
        }else{
            return null;
        }
    }

    public String postHttp(String strRequestUrl, Map<String, String> header, String body, int timeout){
        StringBuffer stringBuffer = new StringBuffer();
        HttpURLConnection httpURLConnection = null;

        try{
            URL url = new URL(strRequestUrl);
            httpURLConnection = (HttpsURLConnection) url.openConnection();
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


            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while((line = br.readLine())!=null){
                    stringBuffer.append(line);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            httpURLConnection.disconnect();
        }
        return stringBuffer.toString();
    }

    public String postHttps(String strRequestUrl, Map<String, String> header, String body, int timeout){
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


            int responseCode = httpsURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                while((line = br.readLine())!=null){
                    stringBuffer.append(line);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            httpsURLConnection.disconnect();
        }
        return stringBuffer.toString();
    }
}
