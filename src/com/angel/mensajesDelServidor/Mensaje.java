package com.angel.mensajesDelServidor;
import java.io.Serializable;

public class Mensaje implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double a=4;
	public double suma(int v){
		return a+v;
	}
}
