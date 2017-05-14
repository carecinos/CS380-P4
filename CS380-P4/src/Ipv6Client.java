import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author cesar
 *
 */
public class Ipv6Client {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws IOException{
		// TODO Auto-generated method stub
		try (Socket socket = new Socket("codebank.xyz", 38004)){
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
            byte[] dstAddr = socket.getInetAddress().getAddress();
			
			byte[] packet;
			int len = 40;
			
			for (int i = 1; i<13; i++){
				int size = (int) Math.pow(2,  i);
				packet = new byte[size+len];
				
				packet[0]=0b01100000;//Version 
				packet[1]=0;//Traffic Class
				packet[2]=0;//Flow Label
				packet[3]=0;//Flow Label
				packet[4]=(byte) ((size>>> 8) & 0xFF);;	//Payload Length
				packet[5]=(byte) (size & 0xFF);;	//PayLoad Length
				packet[6]=0x11;	//NextHEader
				packet[7]=20;	//Hop Limit
				
				//SourceAddr
				for(int j = 8; j < 18; j++){
					packet[j] = 0;
				}
				
				packet[18]=(byte) 0xFF;
				packet[19]=(byte) 0xFF;
				packet[20]=1;
				packet[21]=2; 
				packet[22]=3;
				packet[23]=4;
				
				//DestinationAddr
				for(int j = 24; j < 34; j++){
					packet[j] = 0;
				}
				
				packet[34]=(byte) 0xFF;
				packet[35]=(byte) 0xFF;
				
				for(int k = 0; k < 4; k++){ 
					packet[k+36] = dstAddr[k];
				}
				
				
				os.write(packet);	//Send Packet
				byte[] response = new byte[4];
				is.read(response);
				System.out.println("data length: " + size);
				System.out.print("Response: 0x");

				//Print Response
				 for(byte e: response)
					System.out.printf("%X", e);
				System.out.println("\n");	
			}
		}
	}

}
