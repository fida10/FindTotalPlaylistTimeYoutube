import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class Main {
	public static void main(String[] args) throws InterruptedException{
		System.setProperty("webdriver.chrome.driver", "//Users//fida10//selenium//drivers//chrome//chromedriver"); //replace with your own chromedriver
		WebDriver dr = new ChromeDriver();
		dr.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		PlaylistMethods listOne = new PlaylistMethods();
		listOne.playlistMethods(dr,"//span[contains(@class, 'style-scope ytd-thumbnail-overlay-time-status-renderer')]");
	}
} //now just need to catch the various possible exceptions, and perhaps clean up the code somewhat.