package lab3_2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class UDPClient extends Thread {
	final int PUERTO_SERVIDOR = 1234;
	byte[] buffer = new byte[1024];
	public void run () {	
		try {
			LocalDateTime tstamp = LocalDateTime.now();
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");

			InetAddress direccionServidor = InetAddress.getLocalHost();
			DatagramSocket socketUDP = new DatagramSocket();

			String mensaje = "Cliente listo";
			buffer = mensaje.getBytes();

			DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length, direccionServidor, PUERTO_SERVIDOR);

			System.out.println("Envío datagrama");
			socketUDP.send(pregunta);

			DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
			socketUDP.receive(peticion);

			byte[] data = peticion.getData();

			byte[] big = new byte[1024];
			big = Files.readAllBytes(Paths.get("./big"));
			byte[] small = new byte[1024];
			small = Files.readAllBytes(Paths.get("./small"));

			if(data.length == big.length || data.length == small.length){
				//creo log
			}

			socketUDP.close();

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
