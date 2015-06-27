package wtsomonitor;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.json.JSONException;

public class WTSOExecution {

	public static void main(String[] args) throws JSONException, IOException {
		
//		int x = 0; 
		try {
//			while (x < 96){
				FetchPrice instance = new FetchPrice(15.00, "Pinot Grigio");
				instance.process();
				
//				x ++ ;			
//				System.out.println("Iteration: " + x + " Waiting. . .");
//				Thread.sleep(900000);
//			}
		} catch (IOException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} 
}
