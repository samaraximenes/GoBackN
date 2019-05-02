package br.unifor.cct;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GoBackNCliente {

	public static void main(String [] args) throws Exception{
	System.out.println("Iniciando Cliente...");
	System.out.println("Iniciando Conexão com o servidor...");
	Socket client = new Socket("localhost", 44446);
	System.out.println("Conexão Estabalecida Cliente!");
	ObjectOutputStream oos=new ObjectOutputStream(client.getOutputStream());
	ObjectInputStream ois=new ObjectInputStream(client.getInputStream());
	
	 
	int totalpacotes = (Integer)ois.readObject();
	//int controletotalpacotesois = (Integer)ois.readObject();
	int controletotalpacotes = 0;
	int cont = 0;
	long tempofinal;
	while(controletotalpacotes<totalpacotes) {
		for(int i=0;i<totalpacotes;i++) {
			int controletotalpacotesois = (Integer)ois.readObject();
				if(controletotalpacotesois==cont) {
				controletotalpacotes++;
				cont++;
				System.out.println("Pacote Recebido:" + controletotalpacotesois + " Enviando o ACK!");
				tempofinal = System.currentTimeMillis();
				oos.writeObject(true);
				oos.writeObject(controletotalpacotesois);
				oos.writeObject(tempofinal);
				}
				else{
					System.out.println("Pacote esperado era o :"+ cont + "\n"+" Recebido foi o:"+controletotalpacotesois+"\n");
					tempofinal=System.currentTimeMillis();
					oos.writeObject(false);
					oos.writeObject(controletotalpacotesois);
					oos.writeObject(tempofinal);
				}
		}
	}

	System.out.println("Encerrando Conexão!");
	ois.close();
	oos.close();
	client.close();
	System.out.println("Encerrando Servidor!");
	
	}
}
