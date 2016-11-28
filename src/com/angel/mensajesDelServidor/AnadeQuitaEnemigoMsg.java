package com.angel.mensajesDelServidor;

public class AnadeQuitaEnemigoMsg extends Mensaje{
	public final static int ADD = 1;
	public final static int DELETE = 2;
	int acción = ADD;
	double x, y; //pos
    int dir; // dir
    double wang, ang;  //angulos
    double sp; // velocidad
}
