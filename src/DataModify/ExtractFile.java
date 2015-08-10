package DataModify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

public class ExtractFile {

	public static void extract(String infile) throws IOException{
		FileInputStream fis = new FileInputStream(infile);
		TarInputStream tin  = new TarInputStream(new GZIPInputStream(fis));
		TarEntry tarEnt = tin.getNextEntry();
	    while (tarEnt != null) {
	      String name = tarEnt.getName();
	      int size = (int)tarEnt.getSize();
	      System.out.println(String.format("%s:: size= %d byte",name,size));
	      ByteArrayOutputStream bos = new ByteArrayOutputStream(size);
	      tin.copyEntryContents(bos);
	      byte[] data = bos.toByteArray();
	      DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
	      
	      dis.close();
	      tarEnt = tin.getNextEntry();
	    }
	    tin.close();
	}
	
}
