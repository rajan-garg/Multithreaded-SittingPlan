package com.example.rajan.seatingplan;

/**
 * Created by root on 7/9/17.
 */

public class Config {

    // 1 - Synchronous, 2 - Asynchronous, 3 = Asynchronous (Fork-Join Framework)
    public static Integer IMPLEMENTATION = 1;

    public static Integer TOTAL_SEATS = 9;

    public static Integer SEATS_PER_ROW = 3;

    public static String SERVER_IP_ADDRESS = "192.168.0.101";

    public static String IMAGES_DIR = "/storage/sdcard0/msp_images/";

}
