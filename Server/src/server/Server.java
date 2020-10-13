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
import java.util.Scanner;
import java.util.StringTokenizer;
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
            
            in=new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
            out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
            String line="";
            
            while(!line.equals("Over"))
            {
                try
                { 
                    line = in.readLine();
                    System.out.println("Server received: " + line); 
                    StringTokenizer st = new StringTokenizer(line,";");
                    String action=st.nextToken();
                    
                    HashMap<Integer,String> hmWriteFile = new HashMap<>();
                    int i=1;
                    if(action.equals("add"))
                    {
                        while (st.hasMoreTokens())
                        {  
                            hmWriteFile.put(i,st.nextToken());
                            i++;
                        }
                        String content1 = hmWriteFile.get(1)+";"+hmWriteFile.get(2);
                        writeFile(content1);
                        String content2 = hmWriteFile.get(2)+";"+hmWriteFile.get(1);
                        writeFile(content2);
                        System.out.println(content1);
                        System.out.println(content2);
                    }
                    if(action.equals("delete"))
                    { 
                        while (st.hasMoreTokens())
                        {  
                            hmWriteFile.put(i,st.nextToken());
                            i++;
                        }
                        String content1 = hmWriteFile.get(1)+";"+hmWriteFile.get(2);    
                        String content2 = hmWriteFile.get(2)+";"+hmWriteFile.get(1);

                       try {
                           deleteFile(content1,content2);
                       } catch (Exception ex) {
                           Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                       }
                             
                           
                        
                    }
                    
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
            read = new Scanner(txt,"UTF-8");
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
    
    public static void writeFile(String content)
    {
        FileWriter writer = null;
        try {
            writer = new FileWriter("src/server/tudien.txt",true);
            BufferedWriter buffer = new BufferedWriter(writer);
            buffer.write(content);
            buffer.newLine();
            buffer.close();
            System.out.println("Success...");
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
     public static String fileToString(String filePath) throws Exception{
      String input = null;
      Scanner sc = new Scanner(new File(filePath));
      StringBuffer sb = new StringBuffer();
      while (sc.hasNextLine()) {
         input = sc.nextLine();
         sb.append(input);
      }
      return sb.toString();
   }
    public static void deleteFile(String content1,String content2) throws Exception
    {
        System.out.println("test"+content1);
        File inputFile = new File("src/server/tudien.txt");
        File tempFile = new File("src/server/test.txt");
        
        BufferedReader reader = new BufferedReader(new FileReader("src/server/tudien.txt"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/server/test.txt"));
        String currentLine; 
        while((currentLine = reader.readLine()) != null) {
                // trim newline when comparing with lineToRemove
                String trimmedLine = currentLine.trim();
                if(trimmedLine.equals(content1)) continue;
                writer.write(currentLine + System.getProperty("line.separator"));
            }
        if (!inputFile.delete()) {
        System.out.println("Could not delete file");
        return;
      }
        
        writer.close(); 
        reader.close(); 
        boolean successful = tempFile.renameTo(inputFile);
        
       
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server server = new Server(5000);
        // TODO code application logic here
    }
    
}
