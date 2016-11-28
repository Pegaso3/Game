package com.angel.mensajes.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor implements Runnable {
	
	
	
	ServerSocket socketServidor = null;
	ArrayList<Socket> socketsClientes = new ArrayList<Socket>();
	boolean arrancado = true;

	@Override
	public void run() {
		try {
			socketServidor = new ServerSocket(3000);
			while (arrancado) {
				long startTime = System.currentTimeMillis();
				System.out.println("servidor a la ecucha");
				Socket socketCliente = null;
				socketCliente = socketServidor.accept();
				// socketsClientes.add(socketCliente);
				System.out.println("socke cliente aceptado");

				socketsClientes.add(socketCliente);
				// refrescaLista();

				TratadorCliente resp = new TratadorCliente(socketCliente, null, this);
				Thread hilo = new Thread(resp);
				hilo.start();
				try {
					long tickDuration = System.currentTimeMillis() - startTime;
					if (tickDuration < 50) {
						System.out.println("Tick took " + tickDuration + "ms, sleeping for a bit");

						Thread.sleep(50 - tickDuration);

					} else {
						System.out.println("Tick took " + tickDuration + "ms (which is >=50ms), no time for sleep");
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException ex) {
			System.out.println(ex);
		} finally {
			try {
				for (Socket s : socketsClientes) {
					s.close();
				}
				socketServidor.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
