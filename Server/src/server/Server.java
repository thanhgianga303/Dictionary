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
import java.util.Scanner;
/**
 *
 * @author thanh
 */
public class Server {
    private ServerSocket server= null;
    private Socket socket= null;
    
    BufferedReader in = null;
    BufferedWriter out= null;
    
    static Scanner read;
    static HashMap<String,String> hm = new HashMap<>(); 
    public Server(int port)
    {
        try {
            server= new ServerSocket(port);
            System.out.println("Server started!");
            System.out.println("Waiting client!");
            
            socket=server.accept();
            System.out.println("Server accepted!");
            
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String line="";
            
            while(!line.equals("Over"))
            {
                try
                { 
                    line = in.readLine();
                    System.out.println("Server received: " + line); 
                    out.write(Transplate(line));
                    out.newLine();
                    out.flush();           
                } 
                catch(IOException i) 
                { 
                    System.err.println(i); 
                } 
            }
        
            System.out.println("Closed connection");
            in.close();
            out.close();
            socket.close();
            server.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    public static String Transplate(String keyword)
    {
        readFile();
        if(hm.get(keyword)!= null)
        {
            return hm.get(keyword);
        }
        else
        {
            return "Không tồn tại";
        }
    }
    public static void readFile()
    {
        try {
            File txt = new File("src/server/tudien.txt");
            read = new Scanner(txt);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(read.hasNextLine())
        {
           String[] words=read.nextLine().split(";");
           hm.put(words[0],words[1]);
        }
        read.close();
        for(String words:hm.keySet())
        {
            System.out.print(words+" ");
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server server = new Server(5000);
        // TODO code application logic here
    }
    
}
