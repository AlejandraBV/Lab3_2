package lab3_2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class UDPClient {

	public static void main(String[] args)throws Exception {
		
		final int PUERTO_SERVIDOR = 1234;
		byte[] buffer = new byte[1024];
		
		try {
			
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
			
		} catch (SocketException ex) {
			// TODO: handle exception
		}

	}

}
