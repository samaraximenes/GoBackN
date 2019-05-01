package br.unifor.cct.redes;
import java.net.*;
import java.util.Random;
import java.io.*;
public class GoBackNCliente {
	
	public static void main(String args[]) throws Exception{
	
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Digite a quantidade de pacotes :");
		int m=Integer.parseInt(br.readLine());
		int x=(int)((Math.pow(2,m))-1);
		System.out.print("Numero de pacotes enviados:");
		int count=Integer.parseInt(br.readLine());
		int data[]=new int[count];
		int h=0;
			for(int i=0;i<count;i++){
			data[i]=i;
			h=(h+1)%x;
			}
		
		Socket client=new Socket("localhost",5252);
		ObjectInputStream ois=new ObjectInputStream(client.getInputStream());
		ObjectOutputStream oos=new ObjectOutputStream(client.getOutputStream());
		System.out.println("Connected with server.");
		boolean flag=false;
		GoBackNListener listener=new GoBackNListener(ois,x);
		listener=new GoBackNListener(ois,x);
		listener.t.start();
		int strt=0;
		h=0;
		oos.writeObject(x);
			do{
				int c=h;
				for(int i=h;i<count;i++){
					System.out.print("|"+c+"|");
					c=(c+1)%x;
				}
				System.out.println();
				System.out.println();
				h=strt;
					for(int i=strt;i<count;i++){
						System.out.println("Enviando pacote:"+h);
						h=(h+1)%x;
						System.out.println();
						oos.writeObject(i);
						oos.writeObject(data[i]);
						Thread.sleep(100);
					}
				listener.t.join(3500);
				if(listener.reply!=x-1){
					System.out.println("Não recebemos o pacote no tempo determinado. Por favor reenvie o pacote:" + (listener.reply+1));
					System.out.println();
					strt=listener.reply+1;
					flag=false;
				}
				else{
					System.out.println("Todos os pacotes foram entregues");
					flag=true;
				}
		}while(!flag);
		oos.writeObject(-1);
	}
}
