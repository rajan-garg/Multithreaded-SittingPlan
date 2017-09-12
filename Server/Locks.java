package Server;


import java.io.*;
import java.net.*;

/**
 * Lock class that create a global lock on file
 */
public class Locks {

    public static Object lock;
    public static int isTeacherActive;
    public static int isStudentActive;
}