package polldaddy;

import java.util.concurrent.Executors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PollDaddy {

	// free safe servers
	/*
	 * private static final String[][] countryCodes = { { "0au", "0ch", "0ru",
	 * "0jp", "0pl", "0ca", "0sg" }, { "0ru", "0jp", "0pl", "0ca", "0sg", "0au",
	 * "0ch" }, { "0pl", "0ca", "0sg", "0au", "0ch", "0ru", "0jp" } };""
	 */

	private static final String[][] countryCodes = {
			{ "2-2077-3443", "2-2083-3449", "2-2087-4446", "2-2047-3444", "2-2089-4448", "2-2086-4445", "2-2082-3448", "2-2078-3444",
					 "2-2026-4443"},
			{ "2-2086-4445", "2-2082-3448", "2-2078-3444", "2-2026-4443", "2-2077-3443", "2-2083-3449",
						 "2-2087-4446", "2-2047-3444", "2-2089-4448" },
			{ "2-2026-4443", "2-2047-3444",
					"2-2089-4448", "2-2086-4445", "2-2082-3448", "2-2078-3444", "2-2077-3443", "2-2083-3449", "2-2087-4446"} };

	private static final int totRounds = 100;
	private static final int profilesSize = 5;
	private static final String PROFILENAMEPREFIX = "Profile ";

	private static enum Participants {
		VARUN("PDI_answer47872528"), ROHINI("PDI_answer47872539");

		public String value;

		private Participants(String value) {
			this.value = value;
		}
	}

	public static void main(String[] args) {

		System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");

		for (int i = 3; i <= profilesSize; i++) {
			final int localIncrementer = i;


			ExecutorSe service = Executors.newSingleThreadExecutor();
			
			Executors.newSingleThreadExecutor().execute(new Runnable() {

				public void run() {
					launchChrome(PROFILENAMEPREFIX + localIncrementer, localIncrementer - 3);
				}
			});
			System.out.println("Profile running: " + i);
		}

	}

	private static void launchChrome(final String profileName, final int profileNo) {

		ChromeOptions options = new ChromeOptions();
		options.addArguments("user-data-dir=" + System.getProperty("user.home") + "/Selenium/" + profileName + "Dir");
		options.addArguments("profile-directory=" + profileName);
		options.addArguments("--start-maximized");
		WebDriver driver = new ChromeDriver(options);

		driver.manage().window().maximize();

		int ltotRounds = 1;
		int countryCodePointer = 0;

		while (ltotRounds <= totRounds && countryCodePointer <= countryCodes[profileNo].length) {

			System.out.println("Round number: " + ltotRounds);
			driver.get("chrome-extension://oofgbpoabipfcfjapgnbbjjaenockbdp/popup.html");

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			if (countryCodePointer > 0 || ltotRounds > 1) {
				driver.findElement(By.id("connected-disconnect-button")).click();

				WebDriverWait wait = new WebDriverWait(driver, 10);
				wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.id(countryCodes[profileNo][countryCodePointer])));

			}

			driver.findElement(By.id(countryCodes[profileNo][countryCodePointer])).click();

			WebDriverWait wait = new WebDriverWait(driver, 15);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("connected-disconnect-button")));

			String appUrl = "https://www.scooptimes.com/television/bigg-boss-telugu-vote/9654";
			driver.get(appUrl);

			wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(Participants.ROHINI.value)));

			for (int i = 0; i < 20; i++) {
				driver.findElement(By.id(Participants.ROHINI.value)).click();
				WebElement voteButton = driver.findElement(By.id("pd-vote-button10368616"));
				voteButton.click();

				wait = new WebDriverWait(driver, 15);

				wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Return To Poll')]")));

				driver.findElement(By.xpath("//a[contains(text(),'Return To Poll')]")).click();

				try {
					Thread.sleep(750);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			countryCodePointer++;

			if (countryCodePointer == countryCodes.length) {
				countryCodePointer = 0;
				ltotRounds++;
			}

		}

		driver.close();

	}

}
