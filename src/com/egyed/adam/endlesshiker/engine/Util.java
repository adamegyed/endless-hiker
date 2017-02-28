package com.egyed.adam.endlesshiker.engine;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Adam on 5/12/16.
 * Utilities used in the engine
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
    public static List<String> readAllLines(String fileName) throws Exception {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Util.class.getClass().getResourceAsStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }

    public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }

}
