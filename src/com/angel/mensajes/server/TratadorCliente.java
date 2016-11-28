package com.angel.mensajes.server;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JList;
import javax.swing.JPanel;

import com.angel.mensajesDelServidor.Mensaje;
import com.angel.mensajesHaciaServidor.CambioAngulo;
import com.angel.mensajesHaciaServidor.CambioVelocidad;
import com.angel.mensajesHaciaServidor.InicialMsg;
import com.angel.mensajesHaciaServidor.SalirMsg;

public class TratadorCliente implements Runnable {
	Socket socketCliente;
	Servidor servidor;
	ArrayList lista;
	JPanel panel;

	public TratadorCliente(Socket socketCliente, JPanel panel, Servidor servidor) {
		this.socketCliente = socketCliente;
		this.panel = panel;
		this.servidor = servidor;
	}

	public void run() {
		PrintWriter out = null;
		BufferedReader in = null;
		// Mensaje to=null;
		try {
			InputStream is = socketCliente.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			/*
			 * Mensaje to = (Mensaje)ois.readObject(); System.out.print(to.a);
			 * System.out.println(to.suma(2));
			 */

			/*
			 * if (to instanceof SalirMsg) while ((to = (Mensaje)
			 * ois.readObject()) instanceof SalirMsg == false) {
			 * 
			 * System.out.println(to.a); }
			 */
			Mensaje to = null;
			to = (Mensaje) ois.readObject();
			System.out.println(to.getClass().toString());
			while ((to instanceof SalirMsg) == false) {
				if(to instanceof CambioAngulo){
					
				}else if(to instanceof CambioVelocidad){
					
				}else if(to instanceof InicialMsg){
					InicialMsg msg = (InicialMsg) to;
					String n = msg.nick;
				}
				System.out.println(to.a);
				to = (Mensaje) ois.readObject();
			}
			/*
			 * out = new PrintWriter(socketCliente.getOutputStream(), true); in
			 * = new BufferedReader(new
			 * InputStreamReader(socketCliente.getInputStream())); String
			 * inputLine, outputLine; String hola = in.readLine(); int oldX=0,
			 * oldY=0; while (! (inputLine = in.readLine()).equals("salir")) {
			 * if (inputLine.startsWith("color:")){
			 * 
			 * }else if (inputLine.startsWith("salir")){
			 * System.out.println("el cliente desea cerrar sesion...");
			 * 
			 * }if (inputLine.indexOf(':') != -1){ String valor[] =
			 * inputLine.split(":"); int x = Integer.parseInt( valor[0] ); int y
			 * = Integer.parseInt( valor[1] );
			 * 
			 * 
			 * if (oldX == 0) oldX = x; if (oldY == 0) oldX = y;
			 * 
			 * Graphics g = panel.getGraphics(); g.setColor(Color.BLUE);
			 * g.drawLine(oldX, oldY, x, y);
			 * 
			 * oldX = x; oldY = y;
			 * 
			 * 
			 * }else{
			 * 
			 * } System.out.println("me llega:" + inputLine);
			 * 
			 * //outputLine = inputLine.toUpperCase() + "aaa";
			 * //out.println(outputLine); //System.out.println("envie:" +
			 * outputLine);
			 * 
			 * 
			 * }
			 */
			System.out.println("saliendo:");

			servidor.socketsClientes.remove(socketCliente);
		} catch (IOException | ClassNotFoundException ex) {
			// Logger.getLogger(EscuchadorView.class.getName()).log(Level.SEVERE,
			// null, ex);
		} finally {

			try {
				// out.close();
				// in.close();
				socketCliente.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}