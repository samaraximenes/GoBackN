package br.unifor.cct;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;


public class GoBackNServidor{

		
	public static void main(String [] args) throws Exception{
		System.out.println("Iniciando Servidor...");
		ServerSocket server = new ServerSocket(44446);
		System.out.println("Aguardando Conexão...");
		Socket client = server.accept();
		System.out.println("Conexão Estabelecida Servidor!");
		ObjectOutputStream oos=new ObjectOutputStream(client.getOutputStream());
		ObjectInputStream ois=new ObjectInputStream(client.getInputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
		System.out.println("Digite a quantidade de pacotes a serem enviados:");
		int totalpacotes = Integer.parseInt(br.readLine());
		System.out.println("Digite a quantidade pacotes por segundo:");
		int pacotesseg = Integer.parseInt(br.readLine());
		System.out.println("Digite a quantidade de pacotes a serem enviados sem confirmação:");
		int pacotessemconfirm = Integer.parseInt(br.readLine());
		System.out.println("Digite a porcentagem de pacotes que podem ser descartados pelo cliente: ");
		int p = ((totalpacotes * Integer.parseInt(br.readLine())/100))/1;
		
		oos.writeObject(totalpacotes);
		long tempoinicial;
		int controletotalpacotes = 0;
		int controletotalpacotesporvez = 0;
		int controlepacotesrestantes = totalpacotes/(pacotesseg+pacotessemconfirm);
		int vetorp[] = new int[p];
		boolean flag = false;
		long tempo = 0;
		int pacoterecebido = 0;
		
		for(int i=0;i<p;i++) {
			Random random = new Random();
			vetorp[i] = random.nextInt(totalpacotes-1);
		}
		
		 
		while(controletotalpacotes<totalpacotes){
		
			System.out.println();
			System.out.println("Pacotes a serem enviados:");
			for(int i = controletotalpacotes;i<totalpacotes;i++) {
				System.out.print("|"+(i)+"|");
			}
			System.out.println();
			
				for(int j=0;j<pacotesseg+pacotessemconfirm;j++){
					if(controlepacotesrestantes<1) {
							for(int k = controletotalpacotes; k<totalpacotes;k++) {
								System.out.println("Enviando pacote:" +controletotalpacotes);
								oos.writeObject(controletotalpacotes);
								tempoinicial = System.currentTimeMillis();
								flag = (Boolean)ois.readObject();
								pacoterecebido = (Integer)ois.readObject();
								tempo = (Long)ois.readObject();
								if(flag && pacoterecebido==controletotalpacotes && tempo-tempoinicial<=5) {
									System.out.println("ACK RECEBIDO PACOTE:"+controletotalpacotes);
								}else {
									
									while(!flag && pacoterecebido!=controletotalpacotes && tempo-tempoinicial>5) {
										System.out.println("ACK PERDIDO!");
										System.out.println("Reenviando pacote:" +controletotalpacotes);
										oos.writeObject(controletotalpacotes);
										tempoinicial = System.currentTimeMillis();
										flag = (Boolean)ois.readObject();
										pacoterecebido = (Integer)ois.readObject();
										tempo = (Long)ois.readObject();
									}
								}
								controletotalpacotes++;
							}
					}
						else {
							System.out.println("Enviando pacote:" +controletotalpacotes);
							oos.writeObject(controletotalpacotes);
							tempoinicial = System.currentTimeMillis();
							flag = (Boolean)ois.readObject();
							pacoterecebido = (Integer)ois.readObject();
							tempo = (Long)ois.readObject();
							if(flag && pacoterecebido==controletotalpacotes && tempo-tempoinicial<=5) {
									System.out.println("ACK RECEBIDO PACOTE:"+controletotalpacotes);
								}else {
									while(!flag && pacoterecebido!=controletotalpacotes && tempo-tempoinicial>5) {
										System.out.println("ACK PERDIDO!");
										System.out.println("Reenviando pacote:" +controletotalpacotes);
										oos.writeObject(controletotalpacotes);
										tempoinicial = System.currentTimeMillis();
										flag = (Boolean)ois.readObject();
										pacoterecebido = (Integer)ois.readObject();
										tempo = (Long)ois.readObject();
									}
								}
							controletotalpacotes++;
						}
				}
			controlepacotesrestantes--;
		}
		System.out.println();
		System.out.println("Encerrando Conexão!");
		ois.close();
		oos.close();
		client.close();
		System.out.println("Encerrando Servidor!");
		server.close();
	}
}
