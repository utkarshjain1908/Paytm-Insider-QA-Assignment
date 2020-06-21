package paytm.Movies.Update.Config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
	
	public Properties readPropertiesFile(String fileName) throws IOException {
	      FileInputStream fis = null;
	      Properties prop = null;
	      try {
	         fis = new FileInputStream(fileName);
	         prop = new Properties();
	         prop.load(fis);
	      } catch(FileNotFoundException fnfe) {
	         fnfe.printStackTrace();
	      } catch(IOException ioe) {
	         ioe.printStackTrace();
	      } finally {
	         fis.close();
	      }
	      return prop;
	   }
	
	public String getUrl() throws IOException{ 
		Properties p=new Properties();  
		p= readPropertiesFile("src/main/resources/environment.properties");
		return String.format("%s://%s", p.getProperty("PROTOCOL"), p.getProperty("HOST"));
	}
}
