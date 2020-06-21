package paytm.Movies.Update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import paytm.Movies.Update.Config.ApiUriEnum;
import paytm.Movies.Update.Config.Config;

public class App 
{
	static Config conf = new Config();

	@Test
	private static void validateUpcomingMovies() throws IOException, NoSuchFieldException, SecurityException, ParseException {
		RestAssured.defaultParser = Parser.JSON;
		RestAssured.baseURI = String.format("%s%s", conf.getUrl(), ApiUriEnum.UPCOMING_MOVIES.getUri());
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.request(Method.GET);
		Object upcomingMovies = response.jsonPath().getMap("$").get("upcomingMovieData");
		Object languages = response.jsonPath().getMap("$").get("languageScored");
		ArrayList<String> movieCodes = new ArrayList<String>();
		ArrayList<String> lang = (ArrayList)languages;
		ArrayList<Object> movies =(ArrayList) upcomingMovies;
		HashMap<Integer, String> isContentZeroMovieNames = new HashMap<Integer, String>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date todayDate = formatter.parse(formatter.format(new Date() ));
		int count = 0;

		//Validating Status code
		Assert.assertEquals(response.getStatusCode(), 200,"Validating Status Code");

		for (Object movie : movies) {
			Map<String, String> m = (Map<String, String>) movie;
			//Remove comment in below line to ignore null value in Release Date
			//			if(m.get("releaseDate")!=null) {
			Assert.assertNotNull(m.get("releaseDate"),"Validating Release Date not null");

			Date releaseDate = formatter.parse(m.get("releaseDate"));
			//Validating Release Date should not be before Today's Date
			Assert.assertTrue(releaseDate.compareTo(todayDate)>0, "Valdating Release Date");
			//Validating Movie poster URL only has .jpg format
			Assert.assertEquals(m.get("moviePosterUrl").substring(m.get("moviePosterUrl").length()-4),".jpg", "Valdating Movie Poster URL format");
			//Validating Paytm Movie Code is Unique
			Assert.assertFalse(movieCodes.contains(m.get("paytmMovieCode")), "Validating Paytm Movie code is Unique");
			movieCodes.add(m.get("paytmMovieCode"));
			//Validating no movie has more than one language format
			Assert.assertTrue(lang.contains(m.get("language")), "Validating Language");
			if(String.valueOf(m.get("isContentAvailable")).equalsIgnoreCase("0")) {
				isContentZeroMovieNames.put(count++, m.get("provider_moviename"));
			}
			//Remove comment in below line to ignore null value in Release Date
			//		}
		}
		XSSFWorkbook workbook = new XSSFWorkbook();  
		XSSFSheet sheet = workbook.createSheet("Movie Details"); 
		Set<Integer> keyset = isContentZeroMovieNames.keySet(); 
		int rownum = 0; 
		for (int key : keyset) { 
			// this creates a new row in the sheet 
			Row row = sheet.createRow(rownum++); 
			String movieName = isContentZeroMovieNames.get(key);
			// this line creates a cell in first column of each row and enters the movie name in the cell
			row.createCell(0).setCellValue(movieName); 
		} 
		try { 
			// this Writes the workbook MovieNameWithWithNoContent 
			FileOutputStream out = new FileOutputStream(new File("MovieNameWithWithNoContent.xlsx")); 
			workbook.write(out); 
			workbook.close();
			out.close(); 
			System.out.println("MovieNameWithWithNoContent.xlsx written successfully on disk."); 
		} 
		catch (Exception e) { 
			e.printStackTrace(); 
		} 
	}
}
