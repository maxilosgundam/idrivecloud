package util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

public class QRCodeGenerator {
	String token;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private void createQr(){
		
		ByteArrayOutputStream out = QRCode.from(getToken()).to(ImageType.PNG).stream();
		
		try{
			FileOutputStream fout = new FileOutputStream(new File("qrcode.png"));
	
			fout.write(out.toByteArray());
	
			fout.flush();
			fout.close();
		}catch (FileNotFoundException fnfe) {
			System.out.println("Error: "+fnfe);
		} catch (IOException e) {
			System.out.println("Error: "+e);
		}
	}
	
	public void execute(){
		createQr();
	}
}
