package com.britefull.rollbook;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/*
PostData is a class used for posting anonymous data to a Google Sheet for rudimentary data analysis
Student counts are sorted by grade K-5 and gender
The data is posted using a simple URL code
Personal student information is not posted
 */
public class PostData extends AsyncTask<String, Void, Boolean> {

    // Google Sheets URL for posting data
    private static final String url = "";
    //

    // Replace values with the correct Google Sheets API cell value
    private static final String DATE = "entry.1845798741";
    private static final String FEMALE_1_K = "entry.1249858774";
    private static final String FEMALE_1_1 = "entry.2099521930";
    private static final String FEMALE_1_2 = "entry.2016056872";
    private static final String FEMALE_1_3 = "entry.1931027626";
    private static final String FEMALE_1_4 = "entry.1938120378";
    private static final String FEMALE_1_5 = "entry.435821654";
    private static final String MALE_1_K = "entry.101984366";
    private static final String MALE_1_1 = "entry.1467221816";
    private static final String MALE_1_2 = "entry.1932533845";
    private static final String MALE_1_3 = "entry.1419935757";
    private static final String MALE_1_4 = "entry.1398887394";
    private static final String MALE_1_5 = "entry.625512145";
    private static final String FEMALE_2_K = "entry.1756300854";
    private static final String FEMALE_2_1 = "entry.366188873";
    private static final String FEMALE_2_2 = "entry.90750099";
    private static final String FEMALE_2_3 = "entry.487921208";
    private static final String FEMALE_2_4 = "entry.1552069243";
    private static final String FEMALE_2_5 = "entry.1798153009";
    private static final String MALE_2_K = "entry.775899714";
    private static final String MALE_2_1 = "entry.978901792";
    private static final String MALE_2_2 = "entry.2075574751";
    private static final String MALE_2_3 = "entry.1019266853";
    private static final String MALE_2_4 = "entry.1063485991";
    private static final String MALE_2_5 = "entry.55207306";
    private static final String TWOCLASSES = "entry.1506893021";
    //

    private static final MediaType FORM_DATA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private final WeakReference<Context> weakContext;

    PostData(Context context){
        this.weakContext = new WeakReference<>(context);
    }
    @Override
    protected Boolean doInBackground(String... rollData) {
        Boolean result = true;
        String female_1_K = rollData[0];
        String female_1_1 = rollData[1];
        String female_1_2 = rollData[2];
        String female_1_3 = rollData[3];
        String female_1_4 = rollData[4];
        String female_1_5 = rollData[5];
        String male_1_K = rollData[6];
        String male_1_1 = rollData[7];
        String male_1_2 = rollData[8];
        String male_1_3 = rollData[9];
        String male_1_4 = rollData[10];
        String male_1_5 = rollData[11];
        String female_2_K = rollData[12];
        String female_2_1 = rollData[13];
        String female_2_2 = rollData[14];
        String female_2_3 = rollData[15];
        String female_2_4 = rollData[16];
        String female_2_5 = rollData[17];
        String male_2_K = rollData[18];
        String male_2_1 = rollData[19];
        String male_2_2 = rollData[20];
        String male_2_3 = rollData[21];
        String male_2_4 = rollData[22];
        String male_2_5 = rollData[23];
        String twoClasses = rollData[24];
        String date = rollData[25];

        String postBody = "";
        try{
            postBody = FEMALE_1_K + "=" + URLEncoder.encode(female_1_K, "UTF-8") + "&" +
                    FEMALE_1_1 + "=" + URLEncoder.encode(female_1_1, "UTF-8") + "&" +
                    FEMALE_1_2 + "=" + URLEncoder.encode(female_1_2, "UTF-8") + "&" +
                    FEMALE_1_3 + "=" + URLEncoder.encode(female_1_3, "UTF-8") + "&" +
                    FEMALE_1_4 + "=" + URLEncoder.encode(female_1_4, "UTF-8") + "&" +
                    FEMALE_1_5 + "=" + URLEncoder.encode(female_1_5, "UTF-8") + "&" +
                    MALE_1_K + "=" + URLEncoder.encode(male_1_K, "UTF-8") + "&" +
                    MALE_1_1 + "=" + URLEncoder.encode(male_1_1, "UTF-8") + "&" +
                    MALE_1_2 + "=" + URLEncoder.encode(male_1_2, "UTF-8") + "&" +
                    MALE_1_3 + "=" + URLEncoder.encode(male_1_3, "UTF-8") + "&" +
                    MALE_1_4 + "=" + URLEncoder.encode(male_1_4, "UTF-8") + "&" +
                    MALE_1_5 + "=" + URLEncoder.encode(male_1_5, "UTF-8") + "&" +
                    FEMALE_2_K + "=" + URLEncoder.encode(female_2_K, "UTF-8") + "&" +
                    FEMALE_2_1 + "=" + URLEncoder.encode(female_2_1, "UTF-8") + "&" +
                    FEMALE_2_2 + "=" + URLEncoder.encode(female_2_2, "UTF-8") + "&" +
                    FEMALE_2_3 + "=" + URLEncoder.encode(female_2_3, "UTF-8") + "&" +
                    FEMALE_2_4 + "=" + URLEncoder.encode(female_2_4, "UTF-8") + "&" +
                    FEMALE_2_5 + "=" + URLEncoder.encode(female_2_5, "UTF-8") + "&" +
                    MALE_2_K + "=" + URLEncoder.encode(male_2_K, "UTF-8") + "&" +
                    MALE_2_1 + "=" + URLEncoder.encode(male_2_1, "UTF-8") + "&" +
                    MALE_2_2 + "=" + URLEncoder.encode(male_2_2, "UTF-8") + "&" +
                    MALE_2_3 + "=" + URLEncoder.encode(male_2_3, "UTF-8") + "&" +
                    MALE_2_4 + "=" + URLEncoder.encode(male_2_4, "UTF-8") + "&" +
                    MALE_2_5 + "=" + URLEncoder.encode(male_2_5, "UTF-8") + "&" +
                    TWOCLASSES + "=" + URLEncoder.encode(twoClasses, "UTF-8") + "&" +
                    DATE + "=" + URLEncoder.encode(date, "UTF-8");

        } catch (UnsupportedEncodingException e){
            result = false;
        }

        try{
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            result = false;
        }
        return result;
    }


    /*
    This method displays the status of the Post attempt
    Attendance saved = positive result
    Upload failed = negative result
     */
    @Override
    protected void onPostExecute(Boolean result) {
        Context mContext = weakContext.get();
        if(result){
            Toast.makeText(mContext, "Attendance saved", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, "Upload failed", Toast.LENGTH_LONG).show();
        }
    }
}
