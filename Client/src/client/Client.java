/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author thanh
 */
public class Client {
    	private Socket socket = null; 
	BufferedWriter out = null;
	BufferedReader in = null;
	BufferedReader stdIn = null; 

	public Client(String address, int port) throws UnknownHostException, IOException
	{ 
		socket = new Socket(address, port); 
		System.out.println("Connected"); 
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
		stdIn = new BufferedReader(new InputStreamReader(System.in,"iso-8859-1"));

		String line = ""; 
		while (!line.equals("Over")) 
		{ 
			line = stdIn.readLine();
			System.out.println("Client sent: " + line);
			out.write(line);
			out.newLine();
			out.flush();
                        line = in.readLine();
                        System.out.println("Server sent: " + line);
                } 
		in.close(); 
		out.close(); 
		socket.close(); 
	}
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        // TODO code application logic here
        Client client = new Client("127.0.0.1",5000);
    }
    
}
