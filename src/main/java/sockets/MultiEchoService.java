package sockets;

import java.net.*; 
import java.io.*; 

class EchoThread extends Thread{
	
    BufferedReader strumienWe;
    PrintStream strumienWy;  
    String echo;
    Socket gniazdo;
    
	public EchoThread(Socket gniazdo){
		this.gniazdo=gniazdo;	
	}
	
	public void run(){
		try{
			  strumienWe = new BufferedReader(new InputStreamReader(gniazdo.getInputStream()));
                          strumienWy = new PrintStream(gniazdo.getOutputStream());
                          while((echo=strumienWe.readLine())!=null){
                                System.out.println(echo);
                                strumienWy.println(echo); //wyslij to co przyszlo
                          }//od while
         }catch (Exception e){
             e.printStackTrace();
         }
	}// end of run()
}//class WatekEcho

public class MultiEchoService { 

    ServerSocket serwer; 
    Socket     gniazdo; 
   
    EchoThread w=null;  
    
    public void startService(int port){
      try { 
       serwer = new ServerSocket(port); //stwórz serwer pracujacy na porcie bieżącego komputera
         while(true){ //główna pętla serwera 
            try{ 
               	 System.out.println("Czekam na polaczenie");
                 gniazdo = serwer.accept();  //przyjmuj połączenia i stwórz gniazdo 
                 System.out.println("Jest polaczenie na porcie: "+gniazdo.getPort()); 
                 
                 
                 w=new EchoThread(gniazdo);//utwórz obiekt wątku, przekaż gniazdo
                 w.start();//uruchom wątek i powróć do nasłuchu
                 
          	}catch (SocketException e){ 
              System.out.println("Zerwano polaczenie"); //klient zerwał połączenie 
          	}catch (IOException e) { 
              System.err.println(e); 
          	} 
        }//od while 
      }catch (IOException e) { 
        System.err.println(e); 
      } 
    }//startService()  

    
    public static void launch() { 
        MultiEchoService mes=new MultiEchoService();
        String port = System.getenv("PORT");
        int p;
        if(port!=null) p=Integer.valueOf(System.getenv("PORT"));
        else p=80;
        mes.startService(p);
    }//main() 
}//koniec public class SerwerEchoT 

