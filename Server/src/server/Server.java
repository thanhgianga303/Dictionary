/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author thanh
 */
public class Server {

    private ServerSocket server = null;
    private Socket socket = null;

    BufferedReader in = null;
    BufferedWriter out = null;

    static Scanner read;
    static HashMap<String, String> hm = new HashMap<>();
    static String path = "src/server/dictionary.txt";

    public Server(int port) {
        readFile();
        try {
            server = new ServerSocket(port);
            System.out.println("Server started!");
            System.out.println("Waiting client!");

            socket = server.accept();
            System.out.println("Server accepted!");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
            String line = "";
            System.out.println("1" + line);

            while (!line.equals("bye")) {
                try {
                    line = in.readLine();

                    System.out.println("Server received: " + line);
                    String[] st = line.split(";");

                    if (st[0].equals("add")) {
                        if (checkExistWords(st[1])) {
                            out.write("This word already exists");
                            out.newLine();
                            out.flush();
                        } else {
                            hm.put(st[1], st[2]);
                            out.write("Add success");
                            out.newLine();
                            out.flush();
                        }
                    } else if (st[0].equals("del")) {
                        if (!checkExistWords(st[1])) {
                            out.write("This word is not already exists");
                            out.newLine();
                            out.flush();
                        } else {
                            hm.remove(st[1]);
                            out.write("Delete success");
                            out.newLine();
                            out.flush();
                            for (String words : hm.keySet()) {
                                System.out.print(words + " + " + hm.get(words));
                            }
                        }
                    } else {
                        if(!line.equals("bye")) {
                            out.write(Transplate(line));
                            out.newLine();
                            out.flush();
                        } else  {
                            out.write("Server bye client");
                            out.newLine();
                            out.flush();
                            break;
                        }
                    }
                } catch (IOException i) {
                    System.err.println(i);
                }
            }

            System.out.println("Closed connection");
            saveFile();
            closeConnect();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static boolean checkExistWords(String keyword) {
        boolean check = false;
        if (hm.get(keyword) != null) {
            check = true;
        }
        return check;
    }

    public void closeConnect() throws IOException {
        in.close();
        out.close();
        socket.close();
        server.close();
    }

    public String Transplate(String keyword) {
        if (hm.get(keyword) != null) {
            return hm.get(keyword);
        } 
        else
        {
            for (Entry<String, String> entry : hm.entrySet()) {
                if(removeAccent(entry.getValue()).equals(removeAccent(keyword)))
               {
//                   System.out.println(removeAccent(entry.getValue())+" va "+removeAccent(keyword));
                   return entry.getKey();
               }     
         }
        }
        return "this words is not exists";
    }

    public void readFile() {
        try {
            File txt = new File(path);
            read = new Scanner(txt, "UTF-8");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (read.hasNextLine()) {
            String[] words = read.nextLine().split(";");
            hm.put(words[0], words[1]);
        }
        read.close();
    }

    public void saveFile() throws IOException {
        FileWriter fWriter = new FileWriter("src/server/dictionary.txt");
        String data = "";
        for (String words : hm.keySet()) {
            data = data + (words + ";" + hm.get(words) + System.getProperty("line.separator"));

        }
        fWriter.write(data);
        fWriter.close();

    }

    private static char[] SOURCE_CHARACTERS = { 'À', 'Á', 'Â', 'Ã', 'È', 'É',
            'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
            'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
            'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
            'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
            'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
            'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
            'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
            'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
            'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
            'ữ', 'Ự', 'ự', };

    // Mang cac ky tu thay the khong dau
    private static char[] DESTINATION_CHARACTERS = { 'A', 'A', 'A', 'A', 'E',
            'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
            'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
            'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
            'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
            'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
            'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
            'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
            'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
            'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
            'U', 'u', 'U', 'u', };


    public static char removeAccent(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }

    
    public static String removeAccent(String s) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server server = new Server(5000);
        // TODO code application logic here
    }

}
