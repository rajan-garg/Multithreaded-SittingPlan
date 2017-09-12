package com.example.rajan.seatingplan;

/**
 * default configuration
 */

public class Config {

    // 1 - Synchronous, 2 - Asynchronous, 3 = Asynchronous (Fork-Join Framework)
    public static Integer IMPLEMENTATION = 2;
    // total seats in class
    public static Integer TOTAL_SEATS = 9;
    //seats in a row
    public static Integer SEATS_PER_ROW = 3;
    // server Ip address
    public static String SERVER_IP_ADDRESS = "10.150.38.123";
    // local adress in client to store images
    public static String IMAGES_DIR = "/storage/sdcard0/msp_images/";
    // number of threads created
    public static Integer THREADS_LIMIT = 30;

    public static Integer THUMBNAIL_WIDTH = 50,THUMBNAIL_HEIGHT = 50;

    public static long startTime;

    // 316515231, 249232538
    // 373830000, 387565077
}
