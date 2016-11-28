package com.angel.mensajes.server;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GUIServer {

	private JFrame frame;
	private JTextField txtIpDestino;
	private JTextField txtPuertoDestino;
	private JTextField txtNombreJugador;
	private JLabel lblStatusCliente;
	private JLabel txtIpServidor;
	private JLabel statusServidor;

	
	private JList listJugadores;
	
	private Servidor servidor;
	Thread hiloAceptarClientes;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIServer window = new GUIServer();
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
	public GUIServer() {
		initialize();
		
		
		
		// Clase en la que está el código a ejecutar 
	     TimerTask timerTask = new TimerTask() 
	     { 
	         public void run()  
	         { 
	            refrescaLista(); 
	         } 
	     }; 

	      // Aquí se pone en marcha el timer cada segundo. 
	     Timer timer = new Timer(); 
	     // Dentro de 0 milisegundos avísame cada 1000 milisegundos 
	     timer.scheduleAtFixedRate(timerTask, 0, 1000);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 384);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel.add(tabbedPane);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Crear un servidor", null, panel_1, null);

		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[] { 61, 61, 0 };
		gbl_panel_3.rowHeights = new int[] { 16, 0, 0, 0, 0, 0, 0 };
		gbl_panel_3.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_panel_3.rowWeights = new double[] { 0.0, 0.0, 1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		panel_3.setLayout(gbl_panel_3);

		JLabel lblNewLabel_2 = new JLabel("IP");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 0;
		panel_3.add(lblNewLabel_2, gbc_lblNewLabel_2);

		txtIpServidor = new JLabel("...");
		GridBagConstraints gbc_txtIpServidor = new GridBagConstraints();
		gbc_txtIpServidor.insets = new Insets(0, 0, 5, 0);
		gbc_txtIpServidor.gridx = 1;
		gbc_txtIpServidor.gridy = 0;
		panel_3.add(txtIpServidor, gbc_txtIpServidor);

		JLabel lblNewLabel_4 = new JLabel("Puerto");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 1;
		panel_3.add(lblNewLabel_4, gbc_lblNewLabel_4);

		JTextField txtPuertoServidor = new JTextField();
		txtPuertoServidor.setText("3000");
		GridBagConstraints gbc_txtPuertoServidor = new GridBagConstraints();
		gbc_txtPuertoServidor.insets = new Insets(0, 0, 5, 0);
		gbc_txtPuertoServidor.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPuertoServidor.gridx = 1;
		gbc_txtPuertoServidor.gridy = 1;
		panel_3.add(txtPuertoServidor, gbc_txtPuertoServidor);
		txtPuertoServidor.setColumns(10);

		JButton btnNewButton = new JButton("Crear partida");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				arrancarServidor();

			}
		});
		
		JButton btnCloseServer = new JButton("Cerrar partida");
		btnCloseServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				pararServidor();
			}
		});
		GridBagConstraints gbc_btnCloseServer = new GridBagConstraints();
		gbc_btnCloseServer.insets = new Insets(0, 0, 5, 5);
		gbc_btnCloseServer.gridx = 0;
		gbc_btnCloseServer.gridy = 2;
		panel_3.add(btnCloseServer, gbc_btnCloseServer);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 2;
		panel_3.add(btnNewButton, gbc_btnNewButton);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		panel_3.add(scrollPane, gbc_scrollPane);

		listJugadores = new JList();
		listJugadores.setModel(new DefaultListModel());
		scrollPane.setViewportView(listJugadores);

		JButton btnNewButton_1 = new JButton("Arrancar partida");
		btnNewButton_1.setEnabled(false);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_1.gridx = 1;
		gbc_btnNewButton_1.gridy = 4;
		panel_3.add(btnNewButton_1, gbc_btnNewButton_1);

		statusServidor = new JLabel("...");
		GridBagConstraints gbc_statusServidor = new GridBagConstraints();
		gbc_statusServidor.insets = new Insets(0, 0, 0, 5);
		gbc_statusServidor.gridx = 0;
		gbc_statusServidor.gridy = 5;
		panel_3.add(statusServidor, gbc_statusServidor);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Conectarse a un servidor", null, panel_2, null);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel_2.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_panel_2.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_2.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel_2.setLayout(gbl_panel_2);

		JLabel lblNewLabel = new JLabel("IP Servidor:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel_2.add(lblNewLabel, gbc_lblNewLabel);

		txtIpDestino = new JTextField();
		GridBagConstraints gbc_txtIpDestino = new GridBagConstraints();
		gbc_txtIpDestino.insets = new Insets(0, 0, 5, 0);
		gbc_txtIpDestino.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIpDestino.gridx = 1;
		gbc_txtIpDestino.gridy = 0;
		panel_2.add(txtIpDestino, gbc_txtIpDestino);
		txtIpDestino.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Puerto Servidor:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		panel_2.add(lblNewLabel_1, gbc_lblNewLabel_1);

		txtPuertoDestino = new JTextField();
		GridBagConstraints gbc_txtPuertoDestino = new GridBagConstraints();
		gbc_txtPuertoDestino.insets = new Insets(0, 0, 5, 0);
		gbc_txtPuertoDestino.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPuertoDestino.gridx = 1;
		gbc_txtPuertoDestino.gridy = 1;
		panel_2.add(txtPuertoDestino, gbc_txtPuertoDestino);
		txtPuertoDestino.setColumns(10);

		JLabel lblNewLabel_5 = new JLabel("Nombre jugador:");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 0;
		gbc_lblNewLabel_5.gridy = 2;
		panel_2.add(lblNewLabel_5, gbc_lblNewLabel_5);

		txtNombreJugador = new JTextField();
		GridBagConstraints gbc_txtNombreJugador = new GridBagConstraints();
		gbc_txtNombreJugador.insets = new Insets(0, 0, 5, 0);
		gbc_txtNombreJugador.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNombreJugador.gridx = 1;
		gbc_txtNombreJugador.gridy = 2;
		panel_2.add(txtNombreJugador, gbc_txtNombreJugador);
		txtNombreJugador.setColumns(10);

		JButton btnConectarse = new JButton("Conectarse");
		GridBagConstraints gbc_btnConectarse = new GridBagConstraints();
		gbc_btnConectarse.anchor = GridBagConstraints.WEST;
		gbc_btnConectarse.insets = new Insets(0, 0, 5, 0);
		gbc_btnConectarse.gridx = 1;
		gbc_btnConectarse.gridy = 3;
		panel_2.add(btnConectarse, gbc_btnConectarse);

		lblStatusCliente = new JLabel("Rellena los campos para conectarte al servidor");
		GridBagConstraints gbc_lblStatusCliente = new GridBagConstraints();
		gbc_lblStatusCliente.gridwidth = 2;
		gbc_lblStatusCliente.gridx = 0;
		gbc_lblStatusCliente.gridy = 4;
		panel_2.add(lblStatusCliente, gbc_lblStatusCliente);
	}

	

	private void refrescaLista() {
		if(servidor == null)
			return;
		
		//Object[] o = new Object[servidor.socketsClientes.size()];
		DefaultListModel dlm = (DefaultListModel) listJugadores.getModel();
		dlm.removeAllElements();
		for (Socket socket : servidor.socketsClientes) {
			dlm.addElement(socket);
		}

	}

	public void arrancarServidor() {
		servidor = new Servidor();
		hiloAceptarClientes = new Thread(servidor);
		hiloAceptarClientes.start();
	}
	
	public void pararServidor(){
		hiloAceptarClientes.stop();
		hiloAceptarClientes = null;
		servidor.arrancado=false;
		servidor = null;
	}

}
