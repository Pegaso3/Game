package com.angel.mensajes.client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;

import com.angel.mensajesDelServidor.Mensaje;
import com.angel.mensajesHaciaServidor.CambioVelocidad;
import com.angel.mensajesHaciaServidor.SalirMsg;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Canvas;
import java.awt.Color;

public class GUICliente implements Runnable {

	private JFrame frame;
	private JTextField textField;

	Socket socket = null;
	BufferedReader in = null;
	PrintWriter out = null;

	OutputStream os = null;
	ObjectOutputStream oos = null;
	private JTextField dato;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUICliente window = new GUICliente();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUICliente() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);

		textField = new JTextField();
		textField.setForeground(Color.YELLOW);
		panel.add(textField);
		textField.setColumns(10);

		JButton btnConectarse = new JButton("Conectarse");
		btnConectarse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				connect();
			}
		});
		panel.add(btnConectarse);

		JButton btnNewButton = new JButton("Desconectarse");
		
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				disconnect();
			}
		});
		panel.add(btnNewButton);

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);

		dato = new JTextField();
		dato.setText("4");
		panel_1.add(dato);
		dato.setColumns(10);

		JButton btnEnviaMsg = new JButton("envia msg");
		panel_1.add(btnEnviaMsg);
		
		Canvas canvas = new ClientCanvas();
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				correr(true);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				correr(false);
			}
		});
		canvas.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseMove( e);
			}
		});
		frame.getContentPane().add(canvas, BorderLayout.CENTER);
		btnEnviaMsg.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Mensaje to = new Mensaje();
				to.a = Integer.parseInt(dato.getText());
				try {
					oos.writeObject(to);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	void connect() {

		try {
			socket = new Socket("localhost", 3000);
			/*
			 * in = new BufferedReader(new
			 * InputStreamReader(socket.getInputStream())); out = new
			 * PrintWriter(socket.getOutputStream(), true);
			 */
			System.out.println("conectado ");
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
			Mensaje to = new Mensaje();
			to.a = 2;
			oos.writeObject(to);
			oos.flush();
			System.out.println("enviado " + to.a);

			Thread hilo = new Thread(this);
			hilo.start();
			// out.println("hola");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //

	}

	private void disconnect() {
		try {
			SalirMsg to = new SalirMsg();
			
			try {
				oos.writeObject(to);
			} catch (IOException e1) {	
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			out.println("salir");
			socket.close();
			out.close();
			in.close();
			os.close();
			oos.close();
			socket = null;
			System.out.println("desconectado ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void send(MouseEvent e) {
		// System.out.println(e.getButton());
		/*
		 * if (oldX == 0) oldX = e.getX(); if (oldY == 0) oldY = e.getY();
		 * 
		 * Graphics g = panel.getGraphics(); g.setColor(Color.getColor((String)
		 * color.getSelectedItem())); Graphics2D g2 = (Graphics2D) g;
		 * 
		 * g2.setStroke(new BasicStroke(slider.getValue())); g2.drawLine(oldX,
		 * oldY, e.getX(), e.getY()); oldX = e.getX(); oldY = e.getY(); if
		 * (socket != null) { out.println(e.getX() + ":" + e.getY()); }
		 */
	}

	@Override
	public void run() {
		// InputStream is = socketCliente.getInputStream();
		// ObjectInputStream ois = new ObjectInputStream(is);
		/*
		 * Mensaje to = (Mensaje)ois.readObject(); System.out.print(to.a);
		 * System.out.println(to.suma(2));
		 */
		InputStream is;
		try {
			is = socket.getInputStream();

			ObjectInputStream ois = new ObjectInputStream(is);

			Mensaje to = null;
			while ((to = (Mensaje) ois.readObject()).a != 0) {
				System.out.println(to.a);
			}

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void mouseMove(MouseEvent e){
		double componenteX = e.getX() - frame.getWidth() / 2;
		double componenteY = e.getY() - frame.getHeight() / 2;
		
		// pasamos de cordenadas rectangulares a coordenadas polares, es decir obtenemos el angulo en radianes
		double atan = Math.atan2(componenteY, componenteX);
		// desfaso el angulo 360 grados (2 PI) para que se parezca más a como lo vemos los seres humanos
		atan = atan + (Math.PI * 2);
		// restringo entre los valores 0 hasta 360grados(2 PI). Es decir el angulo 450 lo convierto a 90
		//atan = atan % (Math.PI * 2);
        //model.setOwnSnakeWang((atan + PI2) % PI2);
        
		Mensaje to = new Mensaje();
		to.a =  atan;
		try {
			oos.writeObject(to);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void correr(boolean acc){
		CambioVelocidad to = new CambioVelocidad();
		to.turbo = acc;
		try {
			oos.writeObject(to);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
