import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.util.Pair;

public class TimeStampParser extends PlaylistMethods {
	public static List<String> hours = new ArrayList<>();
	public static List<String> minutes = new ArrayList<>();
	public static List<String> seconds = new ArrayList<>();

	public static void indivTimeUnits(WebDriver dr, String commonTimeStampXpath) throws InterruptedException{
		List<String> listToBreakUp = getTimeOfEachVideo(dr, commonTimeStampXpath);

		for(int i= 0; i < listToBreakUp.size(); i++){
			String[] intermediaryBreaker = listToBreakUp.get(i).split(", ");
			List<String> intermediaryBreakerAsList = Arrays.asList(intermediaryBreaker); //breaks up the list by commas, then adds the individual elements into a broken up list
			for (int j = 0; j < intermediaryBreaker.length; j++) { //goes thru the list and separates the strings into seconds minutes and hours.
				if(intermediaryBreakerAsList.get(j).contains("minute")){
					minutes.add(intermediaryBreakerAsList.get(j));
				} else if(intermediaryBreakerAsList.get(j).contains("second")){
					seconds.add(intermediaryBreakerAsList.get(j));
				} else if(intermediaryBreakerAsList.get(j).contains("hour")){
					hours.add(intermediaryBreakerAsList.get(j));
				} else {
					System.out.println("~~~~~~Reached end of broken up list.~~~~~~");
					break;
				}
			}
		}
		System.out.println("Minutes: " + minutes);
		System.out.println("Seconds: " + seconds);
		System.out.println("Hours: " + hours);
	}
	public static Pair<List<Integer>, String> gettingOnlyInts(List<String> timeUnit){
		List<Integer> onlyInts = new ArrayList<>();
		List<String> intsNUnitsTogetherAll = new ArrayList<>();

		for(int i = 0; i < timeUnit.size(); i++) {
			String[] splitNumberFromUnit = timeUnit.get(i).split(" ");
			List<String>splitNumberFromUnitListSingular = Arrays.asList(splitNumberFromUnit); //splits the number from the unit, i.e. splits "15" from "seconds"
			intsNUnitsTogetherAll.addAll(splitNumberFromUnitListSingular); //adds everything extracted above to a single list, intsNUnitsTogetherAll
		}
		System.out.println("The separated list: " + intsNUnitsTogetherAll);
		String unitWeAreAdding = new String();
		//now the goal is to get a list with JUST the integers.
		for (int i = 0; i < intsNUnitsTogetherAll.size(); i++) {
			String currentIndex = intsNUnitsTogetherAll.get(i);
			try {
				Integer numericVal = Integer.parseInt(currentIndex); //catch an exception here if value thrown is not an integer, and continue the loop.
				onlyInts.add(numericVal);
			} catch (NumberFormatException e) {
				System.out.println("Looks like " + "~~~" + currentIndex + "~~~" +  " is not an integer, skipping."); //If it is not an integer, we simply skip it with this catch block and continue.
				unitWeAreAdding = currentIndex; //to get the unit we are adding...for later use.
			}
		}
		System.out.println("String integers stored: " + onlyInts + " and total number of elements stored: " + onlyInts.size());
		System.out.println("We did " + unitWeAreAdding + " list conversion here");

		return new Pair<List<Integer>, String>(onlyInts, unitWeAreAdding);
/*
		for(int j = 0; j < 10; j++){ //this is to see if it contains a number, see reference to j below
			String jInString = Integer.toString(j);
			for (int k = 0; k < intsNUnitsTogetherAll.size(); k++) { //this for loop is the actual one that goes thru the list intsNUnitsTogetherAll which has both the integers and the units as separate elements within the list
				if(intsNUnitsTogetherAll.get(k).contains(String.format("%s", jInString))){ //testing to see if the string under test is an integer; if it is, we can add with it
					onlyInts.add(intsNUnitsTogetherAll.get(k)); //there's a double match here, and there will be a multiple match for every single number that has more than one digit. Solve by forcing the loop to go on to the next index if a match is found
					System.out.println("Added the following to numbers: " + timeUnit.get(k));
					k++;
				} else {
					System.out.println("This is not an integer, moving on.");
				}
			}
		} */ //my previous attempt
		/*for (int i = 0; i < 10; i++) {
			String iInString = Integer.toString(i);
			for (int k = 0; k < splitNumberFromUnitList.size(); k++) {
				if(splitNumberFromUnitList.get(k).contains(String.format("%s", iInString))){ //testing to see if the string under test is an integer; if it is, we can add with it
					onlyInts.add(splitNumberFromUnitList.get(k));
					System.out.println("Added the following to numbers: " + timeUnit.get(i));
			}
		}
		for(int i = 0; i < timeUnit.size(); i++){
			String[] splitNumberFromUnit = timeUnit.get(i).split(" ");
			splitNumberFromUnitList = Arrays.asList(splitNumberFromUnit);
			for(int j = 0; j < 10; j++){
				String jInString = Integer.toString(j);
				for (int k = 0; k < splitNumberFromUnitList.size(); k++) {
					if(splitNumberFromUnitList.get(k).contains(String.format("%s", jInString))){ //testing to see if the string under test is an integer; if it is, we can add with it
					onlyInts.add(splitNumberFromUnitList.get(k));
					System.out.println("Added the following to numbers: " + timeUnit.get(i));
					}
				}
			}
		}*/
	}
	public static void timeAdder(List<String> timeUnit){
		Pair<List<Integer>, String> p = gettingOnlyInts(timeUnit);
		List<Integer> numbersToAdd = p.getKey(); //getKey always gives the first one, getValue always the second, so we need the first, we use key.
		String unitConverting = p.getValue();
		for(int i = 0; i < numbersToAdd.size(); i++) {
			if (unitConverting.contains("hour")) {
				TOTALHOURS = TOTALHOURS + numbersToAdd.get(i);
			} else if (unitConverting.contains("minute")) {
				TOTALMINUTES = TOTALMINUTES + numbersToAdd.get(i);
			} else if (unitConverting.contains("second")) {
				TOTALSECONDS = TOTALSECONDS + numbersToAdd.get(i);
			}
		}
	}
	public static void timeSimplifier(){
		if(TOTALSECONDS >= 60){
			int additionalMinutes = TOTALSECONDS/60;
			TOTALSECONDS = TOTALSECONDS%60;
			TOTALMINUTES = TOTALMINUTES + additionalMinutes;
		}
		if(TOTALMINUTES >= 60){
			int additionalHours = TOTALMINUTES/60;
			TOTALMINUTES = TOTALMINUTES%60;
			TOTALHOURS = TOTALHOURS + additionalHours;
		}
	}
	public static void timeOutPuttersinText(){
		System.out.println("The total number of hours in the videos: " + TOTALHOURS);
		System.out.println("The total number of minutes in the videos: " + TOTALMINUTES);
		System.out.println("The total number of seconds in the videos: " + TOTALSECONDS);
	}
}
