package com.example.biblespeaks1510222349;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    static int testimony,book,chapter,initSelect;
    Spinner testimonySpinner,bookSpinner,chapterSpinner;
    ArrayAdapter<String> oldBooksAdaptor,newBooksAdaptor;
    int[] oldchapters,newchapters,chapters;
    Button prevBtn,playPauseBtn,nextBtn,selectBtn;
    TextView nameTv;
    TableLayout selectLayout;
    String[] onlineOldBooks,onlineNewBooks;
    String onlineBook,onlineChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testimonySpinner = findViewById(R.id.testimonyId);
        bookSpinner = findViewById(R.id.bookId);
        chapterSpinner = findViewById(R.id.chapterId);

        prevBtn = findViewById(R.id.prebBtn);
        playPauseBtn = findViewById(R.id.playPauseBtn);
        nextBtn = findViewById(R.id.nextBtn);
        selectBtn = findViewById(R.id.selectBtn);

        nameTv = findViewById(R.id.nameId);

        selectLayout = findViewById(R.id.selectLayout);

        sharedpreferences = getSharedPreferences("" + R.string.app_name, MODE_PRIVATE);
        editor = sharedpreferences.edit();

        oldBooksAdaptor = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Arrays.asList(getResources().getStringArray(R.array.oldBooks)));
        newBooksAdaptor = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Arrays.asList(getResources().getStringArray(R.array.newBooks)));
        oldchapters = getResources().getIntArray(R.array.oldChapters);
        newchapters = getResources().getIntArray(R.array.newChapters);

        onlineOldBooks = new String[]{"01.ADIKANDAM", "02.NIRGAMAKANDAM", "03.LEVIYAKANDAM", "04.SANKHYAKANDAM", "05.DVITHIYOOPADESAKANDAM", "06.YAHOSHUA", "07.NYAYADHIPATHULU", "08.RUTHU", "09.SAMUYELU-1", "10.SAMUYELU-2", "11.RAJULU-1", "12.RAJULU-2", "13.DHINAVRUTHANTHAMULU--1", "14.DHINAVRUTHANTHAMULU-2", "15.EZRA", "16.NEHEMIAH", "17.ESTHER", "18.YOOBU", "19.KIRTHANALU", "20.SAMETHALU", "21.PRASANGI", "22.PARAMAGEETAMU", "23.YESHAYA", "24.ERMIYA", "25.VILAPAVAKYALU", "26.YEHEJKHELU", "27.DANIELU", "28.HOSEA", "29.YOVELU", "30.AMOSU", "31.OBADIAH", "32.YONAH", "33.MEECA", "34.NAHUMU", "35.HABAKKUKU", "36.ZEPHANIAH", "37.HUGGAI", "38.ZECHARAIH", "39.MALAKI"};

        onlineNewBooks = new String[]{"40.MATTHEW", "41.MARK", "42.LUKA", "43.YOHANU", "44.APOSTHALULU-KARYALU", "45.ROMA", "46.CORINTHIANS-1", "47.CORINTHIANS-2", "48.GALATIANS", "49.EPHESIANS", "50.PHILIPPIANS", "51.COLOSSIANS", "52.THESSALONIANS-1", "53.THESSALONIANS-2", "54.TIMOTHY-1", "55.TIMOTHY-2", "56.TITHUKU", "57.PHILEMON", "58.HEBREWS", "59.YAKOBU", "60.PETHURU-1", "61.PETHURU-2", "62.YOHANU-1", "63.YOHANU-2", "64.YOHANU-3", "65.YUDA", "66.PRAKATANA"};


        initSelect=1;

        testimony = sharedpreferences.getInt("testimony", 0);
        book = sharedpreferences.getInt("book", 0);
        chapter = sharedpreferences.getInt("chapter", 0);

        testimonySpinner.setSelection(testimony);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if(initSelect==1){
//            testimony = sharedpreferences.getInt("testimony", 0);
//            book = sharedpreferences.getInt("book", 0);
//            chapter = sharedpreferences.getInt("chapter", 0);
//            testimonySpinner.setSelection(testimony);
//        }

        selectLayout.setVisibility(View.INVISIBLE);
        selectBtn.setText("select");

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectBtn.getText().equals("select")){
                    selectLayout.setVisibility(View.VISIBLE);
                    selectBtn.setText("play");
                }else {
                    setPlayingText();
                    selectLayout.setVisibility(View.INVISIBLE);
                    selectBtn.setText("select");
                }
            }
        });

        testimonySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.i("myLog","t "+i+" "+l+" "+adapterView+" "+view);
                testimony=i;
                if(testimony==0){
                    bookSpinner.setAdapter(oldBooksAdaptor);
                    chapters = oldchapters;
                }else{
                    bookSpinner.setAdapter(newBooksAdaptor);
                    chapters = newchapters;
                }
                if(initSelect==1) {
                    //Log.i("myLog","t,i");
                    bookSpinner.setSelection(book);
                }else {
                    //Log.i("myLog","t,0");
                    bookSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(view!=null) {
                    //Log.i("myLog", "b " + position + " " + id + " " + parent + " " + view);
                    book = position;
                    List<Integer> chapterSpinnerList = new ArrayList<>();
                    for (int i = 1; i <= chapters[book]; i++) {
                        chapterSpinnerList.add(i);
                    }
                    chapterSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, chapterSpinnerList));
                    if(initSelect==1){
                        //Log.i("myLog","b,i");
                        chapterSpinner.setSelection(chapter);
                    }else {
                        //Log.i("myLog","t,0");
                        chapterSpinner.setSelection(0);
                    }

                }
//                else {
//                    Log.i("myLog", "b null");
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chapterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(view!=null) {
                    //Log.i("myLog", "c " + position + " " + id + " " + parent + " " + view);
                    chapter = position;
                }
//                else {
//                    Log.i("myLog", "c null");
//                }
                if(initSelect==1){
                    setPlayingText();
                    initSelect=0;
                }

                //nameTv.setText(bookSpinner.getSelectedItem()+" "+(chapter+1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chapter==0){
                    if(book!=0){
                       bookSpinner.setSelection(book - 1);
                    }
                }else {
                    chapterSpinner.setSelection(chapter-1);
                }
                setPlayingText();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chapter+1==chapterSpinner.getCount()){
                    if (book+1<bookSpinner.getCount()){
                        bookSpinner.setSelection(book+1);
                    }
                }else {
                    chapterSpinner.setSelection(chapter+1);
                }
                setPlayingText();
            }
        });

        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        Log.i("myLog",book+" "+chapter);
        keepInt("testimony",testimony);
        keepInt("book",book);
        keepInt("chapter",chapter);
        initSelect=1;
    }

    private void keepInt(String key,int value){
        editor.putInt(key,value);
        editor.apply();
    }

    private void getUrls(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(
                ()->{
                    HttpURLConnection conn;
                    try {
                        String urlString = "https://docs.google.com/spreadsheets/d/150DX_ZLsNq2tSl_x6OS0ft9-449Gm0vZT9XtoF05AYE/export?format=csv";
                        URL url = new URL(urlString);
                        conn = (HttpURLConnection) url.openConnection();
                        InputStream in = conn.getInputStream();
                        Set<String> chaptersSet = new HashSet<>();

                        if(conn.getResponseCode() == 200){
                            BufferedReader br = new BufferedReader(new InputStreamReader(in));
                            String inputLine;

                            while ((inputLine = br.readLine()) != null){
                                //Log.i("myLog","start");
                                String[] book=inputLine.split(",",2);
                                String[] chapters=book[1].split(",");
                                //Log.i("myLog",book[0]+" "+ Arrays.toString(chapters));
                                chaptersSet.addAll(Arrays.asList(chapters));
                            }


                            // 1-1 2-3 3-5
                            //Log.i("myLog", data +"\n"+(data.get("20.సామెతలు")[5*2-1]));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

    }
    private void setPlayingText(){
        onlineChapter=""+chapterSpinner.getSelectedItem();
        if (onlineChapter.length()==1){
            onlineChapter="0"+onlineChapter;
        }
        if(bookSpinner.getSelectedItem().equals("19.కీర్తనల గ్రంథము")){
            onlineChapter+=".Psalm";
        }else {
            onlineChapter+=".Chapter";
        }
        if(testimonySpinner.getSelectedItemPosition()==0){
            onlineBook=onlineOldBooks[bookSpinner.getSelectedItemPosition()];
        }else {
            onlineBook=onlineNewBooks[bookSpinner.getSelectedItemPosition()];
        }
        //https://github.com/gvkgit/18.YOOBU/raw/main/01.Chapter%20Read%20by%20Padmini..mp3
        nameTv.setText("https://github.com/gvkgit/"+onlineBook+"/raw/main/"+onlineChapter+"%20Read%20by%20Padmini..mp3");
    }

}