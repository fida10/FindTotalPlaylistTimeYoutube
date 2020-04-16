import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

public class PlaylistMethods {
	public static Integer TOTALHOURS = 0;
	public static Integer TOTALMINUTES = 0;
	public static Integer TOTALSECONDS = 0;
	public void playlistMethods(WebDriver dr, String commonTimeStampXpath) throws InterruptedException{
		getURL(dr);
		TimeStampParser.indivTimeUnits(dr, commonTimeStampXpath);
		runAllTotalTimes();
		TimeStampParser.timeSimplifier();
		TimeStampParser.timeOutPuttersinText();
	}
	private static void getURL(WebDriver dr){
		Scanner sc = new Scanner(System.in);
		System.out.println("What playlist do you want the time of? ");
		String playListURL = sc.nextLine();
		System.out.println("URL to use: " + playListURL);
		dr.get(playListURL);
	}
	public static List<String> getTimeOfEachVideo(WebDriver dr, String commonTimeStampXpath) throws InterruptedException {

		Thread.sleep(1000);
		List<WebElement> timeStamps = dr.findElements(By.xpath(commonTimeStampXpath));
		List<String> time = new ArrayList<>();

		for(int i = 0; i < timeStamps.size(); i++){
			String currentTimeStamp = timeStamps.get(i).getAttribute("aria-label");
			System.out.println("Adding current timestamp: " + currentTimeStamp);
			time.add(currentTimeStamp);
			System.out.println(time);
			if ((i + 1) == timeStamps.size()){
				System.out.println("Total elements found: " + time.size() + " and all timestamps stored in list: " + time);
				break;
			} //Tmp fixed the loading issue by forcing a wait of 800 mseconds...I really need to find better alternatives to waits.
			//Forcing the page to fully load somehow might solve the issue.
		}
		return time;
	}
	public static List<List> allIntegers(){
		List[] allTotalTimes = {TimeStampParser.hours, TimeStampParser.minutes, TimeStampParser.seconds};
		List<List> allTotalTimesInList = Arrays.asList(allTotalTimes);
		return allTotalTimesInList;
	}
	public static void runAllTotalTimes(){
		List<List>allTotalTimesInList = allIntegers();
		for (int i = 0; i < allTotalTimesInList.size(); i++) {
			TimeStampParser.timeAdder(allTotalTimesInList.get(i));
		}
	}
	public static void jsExect(WebDriver dr, String commonTimeStampXpath) throws InterruptedException{
		dr.get("https://www.youtube.com/playlist?list=PLGCD3Lv-P04hIOuiaezolcVqQhFV8oHZk");
		List<String> times = getTimeOfEachVideo(dr, commonTimeStampXpath);
		List<String> commaTimes = new ArrayList<>();
		JavascriptExecutor js = (JavascriptExecutor)dr;
		for (int i = 0; i < times.size(); i++) {
			System.out.println("Currently on video with timestamp: " + times.get(i) + " and this is video number" + (i + 1));
			String theVideoTimeStampXpath = String.format("//div[@id='contents' and @class='style-scope ytd-playlist-video-list-renderer']//ytd-playlist-video-renderer[%d]//div[2]//a[1]//ytd-thumbnail[1]//a[1]//div[1]//ytd-thumbnail-overlay-time-status-renderer[1]//span[1]", (i+1));
			WebElement jsExtraction = (WebElement) js.executeScript(String.format("return document.evaluate(\"%s\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue", theVideoTimeStampXpath));
			System.out.println(jsExtraction.getText()); //It works!!! now just append this to a new list, should make for easier parsing
			commaTimes.add(jsExtraction.getText());
		}
		System.out.println("Total elements stored: " + commaTimes.size() + " and the list contents: " + commaTimes);

	}
}