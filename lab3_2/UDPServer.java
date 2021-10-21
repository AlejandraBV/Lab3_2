package lab3_2;

import java.io.File;
import java.io.FileWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class UDPServer {

	public static void  main(String[] args) throws Exception{
		
		LocalDateTime tstamp = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");
		
		final int PUERTO = 1234;
		byte[] buffer = new byte[1024];
		String fName = "";
		String fSize = "";

		try {
			System.out.println("Iniciado el servidor UDP");
			DatagramSocket socketUDP = new DatagramSocket(PUERTO);

			int opcion = 1;
			
			while(opcion == 1){
				Scanner sc = new Scanner(System.in);
				System.out.println("Ingrese el número de clientes a recibir: ");
				int clientes = Integer.parseInt(sc.nextLine());

				System.out.println("Ingrese el número del tipo de archivo para enviar: \n1 para 100MB \n 2 para 250MB");
				opcion = Integer.parseInt(sc.nextLine());
				if(opcion == 1){
					fName = "small";
					fSize = "100MB";
				}
				else if (opcion == 2){
					fName = "big";
					fSize = "250MB";
				}
				else System.out.println("Opción no válida");

				ArrayList<UDPClient> clients = new ArrayList<UDPClient>();
				tstamp = LocalDateTime.now();
				String titulo = tstamp.format(format);
				String body = "", date = "";
				for (int i = 0; i < clientes; i++) {
					UDPClient cliente = new UDPClient();
					clients.add(cliente);
					cliente.run();
				}
				for (int i = 0; i < clientes; i++) {
					buffer = new byte[1024];
					buffer = fName.getBytes();
					DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);

					//Recepción petición cliente
					socketUDP.receive(peticion);
					
					String mensaje = new String(peticion.getData());
					System.out.println(mensaje);
					LocalDateTime sent = LocalDateTime.now();
					date = sent.toString();
					body += date + " - Client number: " + i + " connected to the server\n";
					int puertoCliente = peticion.getPort();
					InetAddress direccion = peticion.getAddress();
					
					//Envío archivo a los clientes

					buffer = new byte[1024];
					buffer = Files.readAllBytes(Paths.get("./"+fName));
					
					DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length, direccion, puertoCliente);
					socketUDP.send(respuesta);
					LocalDateTime rec = LocalDateTime.now();
					date = rec.toString();
					body += date + " - " + fSize + " File succesfully received by client " + i + "\n";
					date = LocalDateTime.now().toString() + " - " + "Total transference time for client " + i + ": " + rec.minusSeconds(sent.getSecond()) + "\n";
				}
				
				File log = new File("./Logs/"+titulo+".txt");
				FileWriter writer = new FileWriter(log);
				writer.write(body);
				
				System.out.println("Si quiere volver a enviar un archivo oprima 1");
				opcion = Integer.parseInt(sc.nextLine());
				if(opcion != 1) socketUDP.close();
			}
		} 
		catch (SocketException ex) {
			// TODO: handle exception
		}

	}
}