package object;

import java.util.concurrent.TimeUnit;
import javax.swing.JTextArea;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import util.ExcelUtils;
import view.Player;

public class Youtube {

	public static final String URL = "https://www.youtube.com";

	public static final String SKIPBTN_XPATH = "//span[@class='ytp-ad-skip-button-icon']";
	public static final String NEXTBTN_XPATH = "//a[@class='ytp-next-button ytp-button']";
	public static final String SONGTIME_XPATH = "//span[@class='ytp-time-duration']";
	public static final String CURRENT_TIME_XPATH = "//span[@class='ytp-time-current']";
	public static final String TITLE_XPATH = "//h1[@class='title style-scope ytd-video-primary-info-renderer']";
	
	public static WebDriver driver;
	
	// if add appears skip it
	public static void skip() {

		try {
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			WebElement ele = driver.findElement(By.xpath(SKIPBTN_XPATH));

			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", ele);
			System.out.println("Skip button clicked");

		} catch (NoSuchElementException ex) {
			System.out.println("Skip button not found");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// playing song
	public static void duration() {
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(By.xpath(NEXTBTN_XPATH))).build().perform();

		String song = driver.findElement(By.xpath(SONGTIME_XPATH)).getText();
		String current = driver.findElement(By.xpath(CURRENT_TIME_XPATH)).getText();

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

	// get the song title
	public static String songTitle() {
		return driver.findElement(By.xpath(TITLE_XPATH)).getText();
	}
	
	// end playing and close browser
	public static void endPlaying(boolean b, JTextArea text) {
		text.setText("No more songs in playlist");
		b = false;
		driver.quit();
	}

	// playing
	public static void play(JTextArea text) {
		text.setText("Playlist loading...");
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		driver = new ChromeDriver();
		
		driver.get(URL);
		driver.manage().window().maximize();

		ExcelUtils.setExcell("SongLinks.xlsx");
		ExcelUtils.setWorkSheet(0);

		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		for (int i = 1; i < ExcelUtils.getRowNumber(); i++) {
			try {
				driver.navigate().to(ExcelUtils.getDataAt(i, 0));
				String title = songTitle();
				text.setText(title);
				skip();
				System.out.println(title);
				duration();
			} catch (InvalidArgumentException e) {
				endPlaying(Player.push, text);
			} catch (NoSuchSessionException e) {
				endPlaying(Player.push, text);
			} catch (NullPointerException e) {
				endPlaying( Player.push, text);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		endPlaying(Player.push, text);
	}

}
