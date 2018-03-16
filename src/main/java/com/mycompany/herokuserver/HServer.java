/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.herokuserver;

/**
 *
 * @author Juan Camilo Mantilla
 */
import java.net.*;
import java.io.*;
import java.nio.file.*;


public class HServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        while(true){
            Integer port;
            try{
                port = new Integer(System.getenv("PORT"));
            } catch(NumberFormatException e){
                port = 35000;
            }
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                System.err.println("Could not listen on port: "+port);
                System.exit(1);
            }
            Socket clientSocket = null;
            try {
            System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine, tam, formato = null;
            byte[] byteArr;
            try{
            inputLine = in.readLine();
            if(inputLine != null){
                inputLine = inputLine.split(" ")[1];
                if(inputLine.contains(".html")){
                    byteArr = Files.readAllBytes(new File("./"+inputLine).toPath());
                    tam = "" + byteArr.length;
                    formato = "text/html";                   
                }else if(inputLine.contains(".jpg")){
                    byteArr = Files.readAllBytes(new File("./"+inputLine).toPath());
                    tam = "" + byteArr.length;
                    formato = "image/html";   
                }else{
                    byteArr = Files.readAllBytes(new File("./index.html").toPath());
                    tam = "" + byteArr.length;
                    formato = "text/html";                   
                }
            }
            else{
                byteArr = Files.readAllBytes(new File("./index.html").toPath());
                tam = "" + byteArr.length;
                formato = "text/html";
            }
             outputLine = "HTTP/1.1 200 OK\r\n" 
                                + "Content-Type: " + formato + "\r\n"
                                + "Content-Length: " + tam
                                + "\r\n\r\n";  
            byte [] header = outputLine.getBytes();
            int longHead = header.length;
            byte[] page = new byte[byteArr.length + longHead];
            for (int i = 0; i < longHead; i++) {
                page[i] = header[i];
            }
            for (int i = longHead; i < longHead + byteArr.length; i++) {
                page[i] = byteArr[i - longHead];
            }
            clientSocket.getOutputStream().write(page);
            clientSocket.close();
            serverSocket.close();
            } catch(Exception e){
                byteArr = Files.readAllBytes(new File("./index.html").toPath());
                tam = "" + byteArr.length;
                formato = "text/html";
                outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>ERROR 404</title>\n"
                + "</head>"
                + "<body>"
                + "<h1>NOT FOUND</h1>"
                + "<h3>Please try again</h3>"        
                + "</body>"
                + "</html>";
                
                out.println(outputLine);
                out.close();
                in.close();
                clientSocket.close();
                serverSocket.close();
            }
            
            
            /*
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>AREM Web Page</title>\n"
                + "</head>"
                + "<body>"
                + "My Web Site"
                + "</body>"
                + "</html>" + inputLine;

            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
            serverSocket.close();*/
        }
    }
    
}
