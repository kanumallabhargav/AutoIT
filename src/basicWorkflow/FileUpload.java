package basicWorkflow;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FileUpload 
{
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		//Invoke browser
		System.setProperty("webdriver.chrome.driver", "C:\\ff\\Jars\\chromedriver.exe");
		
		//Using chromeOpthons to download the file to the project location
		String downloadPath=System.getProperty("user.dir");
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadPath);
		ChromeOptions options=new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		WebDriver driver = new ChromeDriver(options);
		
		//Clean browser before TC execution
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		
		//Navigate to webSite
		driver.get("https://image.online-convert.com/convert-to-ico");
		
		//Define Logger
		Logger log = LogManager.getLogger(FileUpload.class.getName());
		
		//Wait until we can click choose files button
		WebDriverWait wait = new WebDriverWait(driver,10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUploadButton")));
		
		//Click on the button to upload file
		driver.findElement(By.id("fileUploadButton")).click();
		log.info("clicked on choose files button");
		
		//pause thread for 2 seconds until the file explorer appears
		Thread.sleep(2000);
		
		//Execute AutoIt script to choose the file from the explorer
		Runtime.getRuntime().exec("C:\\ff\\Jars\\fileUpload.exe");
		log.info("Png file located");
		
		//Wait until the file is converted to the required format
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("deletebutton")));
		
		//Download the converted file
		driver.findElement(By.id("multifile-submit-button-main")).click();
		log.info("downloaded button clicked");
		
		//Wait until the file download is complete
		Thread.sleep(5000);
		log.info("5 second wait completed");
		
		//verify if file exists in the downloads
		File f = new File(downloadPath+"/abc.ico");
		if(f.exists())
		{
			log.info("Converted file downloaded");
			System.out.println("The file has been downloaded");
			if(f.delete())
			{
				log.info("file deleted");
				System.out.println("The downloaded file has been deleted");
			}
		}
		else
		{
			log.info("The file does not exist");
			System.out.println("The file does not exist");
		}
		driver.quit();
	}
}
