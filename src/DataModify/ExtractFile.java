package DataModify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

public class ExtractFile {

	public static void main(String args[]) throws IOException{
		uncompress(Paths.get(args[0]));
	}
	
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

	public static void uncompress(Path path) throws IOException {

		if(!path.toString().endsWith(".tar.gz"))
			throw new Error("extension must be tar.gz.");

		TarInputStream tin = new TarInputStream(new GZIPInputStream(new FileInputStream(path.toFile())));

		for(TarEntry tarEnt = tin.getNextEntry(); tarEnt != null; tarEnt = tin.getNextEntry()) {
			if(tarEnt.isDirectory()){
				new File(tarEnt.getName()).mkdir();
			}
			else {
				FileOutputStream fos = new FileOutputStream(new File("/home/c-tyabe/Data/"+tarEnt.getName()));
				tin.copyEntryContents(fos);
			}
		}
		tin.close();
	}

}
