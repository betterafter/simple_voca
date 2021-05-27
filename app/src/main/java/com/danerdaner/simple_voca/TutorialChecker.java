package com.danerdaner.simple_voca;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TutorialChecker {

    private static final String IDENTIFIER = "Checked";
    private static final String FILENAME = "/Tutorial";

    public static boolean checkUserUseTutorial(Context context){
        FileReader fr = null;
        try {
            fr = new FileReader(context.getFilesDir() + FILENAME);
            BufferedReader bufrd = new BufferedReader(fr) ;

            // 파일로부터 한 라인 읽기.
            String str = bufrd.readLine() ;

            // BufferedReader 닫기.
            bufrd.close() ;

            // FileReader 닫기.
            fr.close() ;

            if(str.equals(IDENTIFIER)){
                return true;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void processUserUseTutorial(Context context){
        File file = new File(context.getFilesDir() + FILENAME) ;
        FileWriter fw = null ;
        BufferedWriter bufwr = null ;

        String str = IDENTIFIER;

        try {
            // open file.
            fw = new FileWriter(file) ;
            bufwr = new BufferedWriter(fw) ;

            // write data to the file.
            bufwr.write(str) ;

        } catch (Exception e) {
            e.printStackTrace() ;
        }

        // close file.
        try {
            if (bufwr != null)
                bufwr.close() ;

            if (fw != null)
                fw.close() ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
