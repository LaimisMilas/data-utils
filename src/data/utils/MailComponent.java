package data.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class MailComponent {
// 587 - serveriai.lt
	final static String MAIL_SERVER = "serveriai.lt";
	final static int SMTP_PORT = 587;// 25, 465, 587
	final static String USER = "henrita@rusele.lt";
	Socket socket = null;

	public MailComponent() {
		// Making Constructor Later
	}

	public static void main(String[] args) {
		try {
			MailComponent mc = new MailComponent();
			mc.sendMail();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMail() throws Exception {

		try {
			// Establish TCP Connection With Mail Server
			socket = new Socket(MAIL_SERVER, SMTP_PORT);

			// Create a Buffered Reader to read one line at a time
			InputStream inStream = socket.getInputStream();
			InputStreamReader inReader = new InputStreamReader(inStream);
			BufferedReader bReader = new BufferedReader(inReader);

			// Read Server's Greeting
			String serverResponse = bReader.readLine();
			if (!serverResponse.startsWith("220")) {
				throw new Exception("220 Reply Not Received From Server!");
			}
			System.out.println(serverResponse);

			// Reference socket's output stream
			OutputStream oStream = socket.getOutputStream();

			// Send EHLO command
			String heloCommand = "EHLO " + MAIL_SERVER + "\r\n";
			oStream.write(heloCommand.getBytes("US-ASCII"));
			serverResponse = bReader.readLine();
			if (!serverResponse.startsWith("250"))
				throw new Exception("250 Reply Not Received From Server!");
			System.out.println(serverResponse);

			String fromCommand = ("MAIL FROM: <>\r\n");
			oStream.write(fromCommand.getBytes("US-ASCII"));
			serverResponse = bReader.readLine();
			System.out.println("Mail From Reponse: " + serverResponse);

			String toCommand = ("RCPT TO: <info@ahcode.lt>\r\n");
			oStream.write(toCommand.getBytes("US-ASCII"));
			serverResponse = bReader.readLine();
			System.out.println("RCPT To Response: " + serverResponse);

			// Get Responses...
			serverResponse = bReader.readLine();
			System.out.println("Server Response: " + serverResponse);
			serverResponse = bReader.readLine();
			System.out.println("Server Response: " + serverResponse);
			serverResponse = bReader.readLine();
			System.out.println("Server Response: " + serverResponse);
			serverResponse = bReader.readLine();
			System.out.println("Server Response: " + serverResponse);
			serverResponse = bReader.readLine();
			System.out.println("Server Response: " + serverResponse);
			serverResponse = bReader.readLine();
			System.out.println("Server Response: " + serverResponse);
			serverResponse = bReader.readLine();
			System.out.println("Final Response: " + serverResponse);
			// END EHLO Responses

			// Send TLS command
			String tlsCommand = ("STARTTLS" + "\r\n");
			oStream.write(tlsCommand.getBytes("US-ASCII"));
			serverResponse = bReader.readLine();
			System.out.println("STARTTLS Response: " + serverResponse);

			/*
			 * Commented out
			 * 
			 * //Send the MAIL FROM command String fromCommand =
			 * ("MAIL FROM: <example@gmail.com>\r\n");
			 * oStream.write(fromCommand.getBytes("US-ASCII")); serverResponse =
			 * bReader.readLine(); System.out.println("Mail From Reponse: " +
			 * serverResponse);
			 * 
			 * // Send RCPT TO command String toCommand =
			 * ("RCPT TO: <example@gmail.com>\r\n");
			 * oStream.write(toCommand.getBytes("US-ASCII")); //serverResponse =
			 * bReader.readLine(); //System.out.println("RCPT To Response: " +
			 * serverResponse);
			 * 
			 * // Send DATA command String dataCommand = ("DATA\r\n");
			 * oStream.write(dataCommand.getBytes("US-ASCII")); serverResponse =
			 * bReader.readLine(); System.out.println("Data Response: "+ serverResponse);
			 * 
			 * // Subject String subCommand = ("SUBJECT: Email Test\r\n");
			 * oStream.write(subCommand.getBytes("US-ASCII")); //TimeUnit.SECONDS.sleep(1);
			 * String msg = ("Some random Message");
			 * oStream.write(msg.getBytes("US-ASCII")); //TimeUnit.SECONDS.sleep(1);
			 * 
			 * // End Message data String endMsg = ("\r\n.\r\n");
			 * oStream.write(endMsg.getBytes("US-ASCII")); serverResponse =
			 * bReader.readLine(); System.out.println("End Data Command: "+ serverResponse);
			 * 
			 * // Send QUIT message String quitMsg = ("QUIT\r\n");
			 * oStream.write(quitMsg.getBytes("US-ASCII")); serverResponse =
			 * bReader.readLine(); System.out.println("Quit Command: "+ serverResponse);
			 * 
			 * 
			 * End comment
			 */

			if (socket != null) {
				socket.close();
				System.out.println("Closed Socket Connection!");
			}
		} catch (IOException ioe) {
			System.err.println(ioe);
		}
	}
}