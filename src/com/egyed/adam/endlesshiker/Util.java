package com.egyed.adam.endlesshiker;


import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by Adam on 5/12/16.
 */
public class Util {

    public static String loadResource(String fileName){
        String result = "";
        try (InputStream in = Util.class.getClass().getResourceAsStream(fileName)) {

            Scanner s = new Scanner(in, "UTF-8");
            s.useDelimiter("\\A");

            result = s.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
