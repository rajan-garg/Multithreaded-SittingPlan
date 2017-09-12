package com.example.lolap.student;

/**
 * Created by root on 7/9/17.
 */

public class Config {

    // 1 - Synchronous, 2 - Asynchronous, 3 = Asynchronous (Fork-Join Framework)
    public static Integer IMPLEMENTATION = 2;

    public static Integer TOTAL_SEATS = 9;

    public static Integer SEATS_PER_ROW = 3;

    public static String SERVER_IP_ADDRESS = "192.168.0.101";

    public static String IMAGES_DIR = "/storage/sdcard0/msp_images/";

    public static Integer THREADS_LIMIT = 30;

    public static Integer THUMBNAIL_WIDTH = 50,THUMBNAIL_HEIGHT = 50;

    public static long startTime;

    // 316515231, 249232538
    // 373830000, 387565077
}
