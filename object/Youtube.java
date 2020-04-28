package object;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class Youtube {

	public static final String URL ="https://www.youtube.com";
	
	public static final String nextBTN_xpath = "//a[@class='ytp-next-button ytp-button']";
	public static final String skipBTN_xpath = "//span[@class='ytp-ad-skip-button-icon']";
	public static final String currentTime_xpath = "//span[@class='ytp-time-current']";
	public static final String songTime_xpath = "//span[@class='ytp-time-duration']";
	public static final String title_xpath = "//h1[@class='title style-scope ytd-video-primary-info-renderer']";
	
	
	// if add appears skip it
		public static void skip(WebDriver driver) {
			
			try {
				//Thread.sleep(7000);
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				WebElement ele = driver.findElement(By.xpath(skipBTN_xpath));
				
				JavascriptExecutor executor = (JavascriptExecutor)driver;
				executor.executeScript("arguments[0].click();", ele);
				System.out.println("Clicked");
				
			} catch (NoSuchElementException ex) {
				System.out.println("Skip button not found");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// playing song
		public static void duration(WebDriver driver) {
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			Actions action = new Actions(driver);
			action.moveToElement(driver.findElement(By.xpath(nextBTN_xpath))).build().perform();
		
			String song = driver.findElement(By.xpath(songTime_xpath)).getText();
			String current = driver.findElement(By.xpath(currentTime_xpath)).getText();
			
			// current played seconds of song
			String[] song_all = song.split(":");
			Long sec_song = Long.parseLong(song_all[0]) * 60 + Long.parseLong(song_all[1]);

			// song duration time in second
			String[] current_all = current.split(":");
			Long sec_current = Long.parseLong(current_all[0]) * 60 + Long.parseLong(current_all[1]);
			
			// paused the code execution until current song finish playing
			try {
				TimeUnit.SECONDS.sleep(sec_song - sec_current);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		
		public static String songTitle(WebDriver driver) {
			return driver.findElement(By.xpath(title_xpath)).getText();
		}
}
