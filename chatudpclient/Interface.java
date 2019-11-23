/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatudpclient;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Veronica
 */
public class Interface extends JFrame {
    //Dichiarazione componenti                  
    private static JTextArea areaChat = new JTextArea();
    private JTextField inserisciUser = new JTextField();
    private JButton invio = new JButton();
    private JPanel panel = new JPanel();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JTextField testoMessaggio = new JTextField();
    private JLabel titolo = new JLabel();
    private JLabel username = new JLabel();
         
    private static String ip= "127.0.0.1";
    private static int port= 1077;
    private static InetAddress indirizzo;
    private static DatagramSocket s;
    
   
    public Interface() {
        this.add(panel);
        panel.add(titolo);
        panel.add(areaChat);
        
        GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
//        jPanel1Layout.setHorizontalGroup(
//            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 100, Short.MAX_VALUE)
//        );
//        jPanel1Layout.setVerticalGroup(
//            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 100, Short.MAX_VALUE)
//        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("INTERFACCIA CLIENT");

        areaChat.setColumns(20);
        areaChat.setRows(5);
        jScrollPane1.setViewportView(areaChat);

        username.setText("USERNAME : ");
        invio.setText("INVIO");
        titolo.setText("CHAT:");
        
        areaChat.setEditable(false);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inserisciUser, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addComponent(testoMessaggio, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(invio))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(titolo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 83, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(titolo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inserisciUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(testoMessaggio, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(invio))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
        
        
        invio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
              
                    String nome= inserisciUser.getText();
                    String mex= testoMessaggio.getText();
                    
                    try {
                    
                    byte[] buffer;
                    DatagramPacket packet;
                    
                    String messaggioCompleto= nome+"---> "+mex;
                    buffer= messaggioCompleto.getBytes("UTF-8");
                    packet= new DatagramPacket(buffer,buffer.length,indirizzo,port);
                    s.send(packet);
                    
                    testoMessaggio.setText("");

                    
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
         Thread ricezione= new Thread(){
                public void run(){
                    
                    byte[] ricevuto;
                    String testoRicevuto= null;
                    DatagramPacket daStampare;
                    
                     ricevuto = new byte[500];
                     daStampare= new DatagramPacket(ricevuto, ricevuto.length);
                     
                    try {
                        while(!Thread.interrupted()){
                        s.receive(daStampare);
                        testoRicevuto= new String(daStampare.getData(),0,daStampare.getLength(),"ISO-8859-1");
                        areaChat.append('\n'+testoRicevuto);
                        
                        }
                        s.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                }
        
        };
        ricezione.start();
       
    }
    
   

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws UnknownHostException, SocketException {
        
        indirizzo = InetAddress.getByName(ip);
        
        s= new DatagramSocket();
       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interface().setVisible(true);
            }
        });
    }
    
    

                 
}
