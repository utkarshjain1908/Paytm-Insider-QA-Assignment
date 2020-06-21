package paytm.Movies.Update.Config;

import java.io.IOException;
import java.util.Properties;

public enum ApiUriEnum {
	UPCOMING_MOVIES("upcomingMovies");
	
	private String uri;
	
	public String getUri() throws IOException {
		Properties p=new Properties();  
		Config c = new Config();
		p= c.readPropertiesFile("src/main/resources/environment.properties");
		return p.getProperty(this.uri);
	}
	
	private ApiUriEnum(String uri) {
		this.uri = uri;
	}
}
