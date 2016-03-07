import java.net.*;
import java.io.*;


class Client extends GUI
{
	private static Socket soc;
    public static void startClient(String cmd) throws IOException
    {
		try {
			String ip = ipAddress.getText() ;
			String pot = port.getText();
			int po = Integer.parseInt(pot);
			soc = new Socket(ip,po);
			System.out.println("Connected Succesfully\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    DataInputStream din = null;
	    DataOutputStream dout = null;
	    BufferedReader br = null;
		System.out.println("initiated buffers\n");
		try {
			din=new DataInputStream(soc.getInputStream());
	        dout=new DataOutputStream(soc.getOutputStream());
	        br=new BufferedReader(new InputStreamReader(System.in));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Created buffers\n"+cmd+"\n");
		
		if(cmd.contains("list")){
			System.out.println("Im gonna get the list");
			dout.writeUTF("LIST");
			System.out.println("Imfcukindg get the list");
	    	String message = ""  ; 
	        message = din.readUTF(); 
	        String[] files = message.split("!") ;
	        for(int i = 0 ; i < files.length-1 ; i++){
	        	System.out.println(i+". "+files[i]);
	        	serverFileList.addItem(files[i]);
	        }	
		}
		
		
		if(cmd.contains("send")){
			dout.writeUTF("SEND");
			System.out.println("Started sending");
			String filename= (String) myServerFileList.getSelectedItem() ;	            
			System.out.println("filename = "+filename+"\n");
			File f=new File(filename);
	        if(!f.exists())
	        {
	            System.out.println("File not Exists...");
	            dout.writeUTF("File not found");
	            return;
	        }
	        
	        dout.writeUTF(filename);
	        System.out.println("checked");
	        System.out.println("Sending File ...");
	        FileInputStream fin=new FileInputStream(f);
	        int ch;
	        do
	        {
	            ch=fin.read();
	            dout.writeUTF(String.valueOf(ch));
	        }
	        while(ch!=-1);
	        fin.close();
	        System.out.println(din.readUTF());
		}	        
	            
		
		
		if(cmd.compareTo("get")==0){
			dout.writeUTF("GET");
	    	
	        String fileName =(String) serverFileList.getSelectedItem() ;
	        System.out.print("Enter File Name :"+fileName);
	        System.out.println("Sending the file name");
	        dout.writeUTF(fileName);
	        System.out.println("reading the input from server");
	        String msgFromServer=din.readUTF();
	        
	        if(msgFromServer.compareTo("File Not Found")==0)
	        {
	            System.out.println("File not found on Server ...");
	            return;
	        }
	        else if(msgFromServer.compareTo("READY")==0)
	        {
	            System.out.println("Receiving File ...");
	            File f=new File(fileName);
	            FileOutputStream fout=new FileOutputStream(f);
	            int ch;
	            String temp;
	            do
	            {
	                temp=din.readUTF();
	                ch=Integer.parseInt(temp);
	                if(ch!=-1)
	                {
	                    fout.write(ch);                    
	                }
	            }while(ch!=-1);
	            fout.close();
	            System.out.println(din.readUTF());
	                
	        }

		}

		}
		
    
    }

class transferfileClient extends GUI
{
	Socket ClientSoc;
    DataInputStream din;
    DataOutputStream dout;
    BufferedReader br;
    transferfileClient(Socket soc)
    {
        try
        {
            ClientSoc=soc;
            din=new DataInputStream(ClientSoc.getInputStream());
            dout=new DataOutputStream(ClientSoc.getOutputStream());
            br=new BufferedReader(new InputStreamReader(System.in));
        }
        catch(Exception ex)
        {
        }        
    }
    void SendFile() throws Exception
    {
    	Process proc = Runtime.getRuntime().exec("/bin/bash -c ls\n"); 
        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream())) ; 
        String line = "" ; int count = 0 ; 
        while ((line = reader.readLine()) != null){
        	System.out.println(count+". "+line);
        	count++;
        }
        
        String filename;
        System.out.print("Enter File Name :");
        filename=br.readLine();
            
        File f=new File(filename);
        if(!f.exists())
        {
            System.out.println("File not Exists...");
            dout.writeUTF("File not found");
            return;
        }
        
        dout.writeUTF(filename);
        
        String msgFromServer=din.readUTF();
        if(msgFromServer.compareTo("File Already Exists")==0)
        {
            String Option;
            System.out.println("File Already Exists. Want to OverWrite (Y/N) ?");
            Option=br.readLine();            
            if(Option=="Y")    
            {
                dout.writeUTF("Y");
            }
            else
            {
                dout.writeUTF("N");
                return;
            }
        }
        
        System.out.println("Sending File ...");
        FileInputStream fin=new FileInputStream(f);
        int ch;
        do
        {
            ch=fin.read();
            dout.writeUTF(String.valueOf(ch));
        }
        while(ch!=-1);
        fin.close();
        System.out.println(din.readUTF());
        
    }
    
    void ReceiveFile() throws Exception
    {
    	
    	String message = ""  ; 
        message = din.readUTF(); 
//        System.out.println("Message received from server :" +message);
        String[] files = message.split("!") ;
        for(int i = 0 ; i < files.length-1 ; i++){
        	System.out.println(i+". "+files[i]);
        }

    	
        String fileName;
        System.out.print("Enter File Name :");
        fileName=br.readLine();
        dout.writeUTF(fileName);
        String msgFromServer=din.readUTF();
        
        if(msgFromServer.compareTo("File Not Found")==0)
        {
            System.out.println("File not found on Server ...");
            return;
        }
        else if(msgFromServer.compareTo("READY")==0)
        {
            System.out.println("Receiving File ...");
            File f=new File(fileName);
            if(f.exists())
            {
                String Option;
                System.out.println("File Already Exists. Want to OverWrite (Y/N) ?");
                Option=br.readLine();            
                if(Option=="N")    
                {
                    dout.flush();
                    return;    
                }                
            }
            FileOutputStream fout=new FileOutputStream(f);
            int ch;
            String temp;
            do
            {
                temp=din.readUTF();
                ch=Integer.parseInt(temp);
                if(ch!=-1)
                {
                    fout.write(ch);                    
                }
            }while(ch!=-1);
            fout.close();
            System.out.println(din.readUTF());
                
        }
        
        
    }

    public void displayMenu() throws Exception
    {
        while(true)
        {    
            System.out.println("[ MENU ]");
            System.out.println("1. Send File");
            System.out.println("2. List the directory");
            System.out.println("3. Exit");
            System.out.print("\nEnter Choice :");
            int choice;
            choice=Integer.parseInt(br.readLine());
            if(choice==1)
            {
                dout.writeUTF("SEND");
                SendFile();
            }
            else if(choice==2)
            {
                dout.writeUTF("GET");
                ReceiveFile();
            }
            else
            {
                dout.writeUTF("DISCONNECT");
                System.exit(1);
            }
        }
    }
}