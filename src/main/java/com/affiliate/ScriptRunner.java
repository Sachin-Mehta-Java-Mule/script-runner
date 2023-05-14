package com.affiliate;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.interactions.Actions;

import com.affiliate.utils.DBUtils;
import com.affiliate.utils.Deals;
import com.affiliate.utils.EncryptUtils;
import com.affiliate.utils.Matcherss;
import com.affiliate.utils.SendMailSSL;
import com.affiliate.utils.URLShortner;
import com.affiliate.utils.Utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;

public class ScriptRunner {
	static WebDriver driver = null;
	static ArrayList<String> tabs = null;
	public static String token = null;
	public static String profile = null;
	public static String user = null;
	public static int sourceGroups = 1;
	public static String mode = "web";

	public static void main(String[] args) throws Exception {
		token = args[0];
		profile = args[1];
		user = args[2];
		sourceGroups = Integer.parseInt(args[3]);
		mode = args[4];
		try {
			Boolean isChrome = true;
			if (isChrome) {
//				System.setProperty("webdriver.chrome.driver", "resources\\chromedriver.exe");

				String chromeProfilePath = "C:\\Users\\" + user + "\\AppData\\Local\\Google\\Chrome\\User Data\\";
				ChromeOptions chromeProfile = new ChromeOptions();
				if (!"default".equalsIgnoreCase(profile)) {
					chromeProfile.addArguments("user-data-dir=" + chromeProfilePath);
					chromeProfile.addArguments("profile-directory=Profile " + profile);
					chromeProfile.addArguments("--remote-allow-origins=*");
//				chromeProfile.addArguments("--headless");
				}
				ChromeDriverManager.getInstance().setup();
//				ChromeDriverManager.chromedriver().arch64().arch64();
				driver = new ChromeDriver(chromeProfile);
			} else {
//				WebDriverManager.firefoxdriver().setup();
//				FirefoxDriverManager.firefoxdriver().forceDownload();
				WebDriverManager.firefoxdriver().setup();
				ProfilesIni profile = new ProfilesIni();

				FirefoxProfile myprofile = profile.getProfile("default-release");
				FirefoxOptions opt = new FirefoxOptions();
				opt.setProfile(myprofile);
				driver = new FirefoxDriver(opt);
			}
			String baseUrl = "https://web.whatsapp.com/";
//			baseUrl = "https://google.com/";
			driver.get(baseUrl);

			Thread.sleep(5000);
			((JavascriptExecutor) driver).executeScript("window.open()");
			tabs = new ArrayList<String>(driver.getWindowHandles());
			Thread.sleep(5000);
			driver.switchTo().window(tabs.get(1)); // switches to new tab
//			driver.get("https://web.telegram.org/k/");
			baseUrl = "https://web.telegram.org/k/";
//			baseUrl = "https://google.com/";
			driver.get(baseUrl);

			Thread.sleep(5000);
			int MINUTES = 30; // The delay in minutes
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() { // Function runs every MINUTES minutes.
					// Run the code you want here
					try {
						System.out.println("Scheduler ran at " + new Date());
						SendMailSSL.sendNotify(false);
//						
						sendMessageTelegram(readAndSendMessages(sourceGroups, mode));
						System.out.println("Scheduler eneded at " + new Date());
					} catch (Exception e) {
						System.out.println(e);
//							e.printStackTrace();
//							System.out.println("Exception");
					} // If the function you wanted was static
				}
			}, 0, 1000 * 60 * MINUTES);

		} catch (Exception e) {
			e.printStackTrace();
			if (driver != null) {
				driver.close();
			}
		}

		int MINUTES = 30; // The delay in minutes
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Thread t = new Thread(() -> SendMailSSL.check(SendMailSSL.host, SendMailSSL.mailStoreType,
						SendMailSSL.username, SendMailSSL.password));
				t.start();
				try {
					Thread.sleep(1000 * 60 * MINUTES);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				t.interrupt();
			}
		}, 0, 1000 * 60 * MINUTES);
	}

	protected static void sendMessageTelegram(ArrayList<String> messageBlock) {

		try {

			driver.switchTo().window(tabs.get(1)); // switches to new tab
			Thread.sleep(5000);
			for (String temp : messageBlock) {
				if (temp.trim() != "") {
					Thread.sleep(10000);
					driver.findElement(By.xpath("//*[@id=\"folders-container\"]/div/div[1]/ul/a/div[1]")).click();
					Thread.sleep(5000);
					driver.findElement(By.xpath("//*[@class=\"new-message-wrapper\"]/div/div")).click();
					JavascriptExecutor js = (JavascriptExecutor) driver;
					try {
						js.executeScript(
								"document.getElementsByClassName(\"animated-button-icon record\")[0].setAttribute('class', 'btn-icon tgico-none btn-circle btn-send animated-button-icon rp send');");
					} catch (Exception e) {
						e.printStackTrace();
					}

					temp = temp
//							.replace("\n", Keys.chord(Keys.SHIFT, Keys.ENTER))
							.replace("\n", " ").replace("\t", " ").trim();
					// driver.findElement(By.xpath("//*[@class=\"new-message-wrapper\"]/div/div")).sendKeys(temp);
					driver.findElement(By.xpath("//*[@class=\"new-message-wrapper\"]/div/div[1]")).sendKeys(temp);
					Thread.sleep(10000);
					driver.findElement(By.xpath("//*[@class=\"btn-send-container\"]/button/div")).click();
					Thread.sleep(5000);
				}
			}
//			((JavascriptExecutor) driver).executeScript("window.close()");
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				((JavascriptExecutor) driver).executeScript("window.close()");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error");
			}
		}
		driver.switchTo().window(tabs.get(0)); // switch back to main screen

	}

	private static ArrayList<String> readAndSendMessages(int i, String mode) throws Exception {
		driver.switchTo().window(tabs.get(0));
		Thread.sleep(5000);
		ArrayList<String> messageBlock = new ArrayList<String>();
		ArrayList<String> finalMessage = new ArrayList<String>();
		List<String> newLinks = new ArrayList<String>();
		if ("whatsapp".equalsIgnoreCase(mode))
			newLinks = readMessageFromWhatsapp(i);
		else if ("web".equalsIgnoreCase(mode))
			newLinks = readMessageFromWeb();

		convertLinks(finalMessage, newLinks);
		sendMessageToWhatsapp(i, messageBlock, finalMessage);
		return finalMessage;
	}

	private static List<String> readMessageFromWeb() {
		List<String> tempNewLinks = new ArrayList();
		List<String> textList = new ArrayList();
		List<String> newLinks = new ArrayList();
		((JavascriptExecutor) driver).executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		try {
			System.out.println("1");
			driver.switchTo().window(tabs.get(2)); // switches to new tab
			driver.get("https://www.indiafreestuff.in/");
			Thread.sleep(5000);
			for (int i = 1; i <= 5; i++) {
				String text = Utils.getAttributes("//*[@class=\"col-xs-3 product-outer " + i + "\"]/div/a[2]",
						"innerHTML", driver);
				String url = Utils.getAttributes("//*[@class=\"col-xs-3 product-outer " + i + "\"]/div/div[4]/div/a[2]",
						"href", driver);
				if (url == null) {
					url = Utils.getAttributes("//*[@class=\"col-xs-3 product-outer " + i + "\"]/div/div[5]/div/a[2]",
							"href", driver);
				}
				System.out.println(url + " " + text);
				if (url != null && text != null) {
					Deals deal = new Deals(null, EncryptUtils.encryptThisString(url.replace(" ", "")), url,
							new Timestamp(new Date().getTime()));
					if (DBUtils.insertDeals(deal) != 0) {
						tempNewLinks.add(url);
						textList.add(text);
					}
				}
			}
			// String[] arr = { "https://www.indiafreestuff.in/?rto=MTMyOTY5NDc5NQ==",
			// "https://www.indiafreestuff.in/?rto=MTMyOTY5NDc5NQ==" };
			// tempNewLinks = Arrays.asList(arr);
			// System.out.println("List size" + tempNewLinks.size());

			final List<String> tempLinks = Utils.getFinalURLOpera(tempNewLinks);
			Utils.killOpera();
			newLinks = IntStream.range(0, tempLinks.size())
					.filter(i -> (tempLinks.get(i) != null && !tempLinks.get(i).contains("indiafreestuff")))
					.mapToObj(i -> (textList.size() >= tempLinks.size() ? textList.get(i)
							: "test" + (new Random()).nextInt(100)) + "\n" + tempLinks.get(i))
					.collect(Collectors.toList());
			// newLinks.add(text + "\n" + url);
			((JavascriptExecutor) driver).executeScript("window.close()");
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				((JavascriptExecutor) driver).executeScript("window.close()");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error");
			}
		}
		driver.switchTo().window(tabs.get(0)); // switch back to main screen
		return newLinks;
	}

	public static void sendMessageToWhatsapp(int i, ArrayList<String> messageBlock, ArrayList<String> finalMessage)
			throws InterruptedException {
		int q = 0;
		int p = 0;
		String tempStr = "";
		for (String temp : finalMessage) {
			p++;
			q = q + (temp).split("https").length - 1;
			tempStr = tempStr + "\n\n\n" + temp;
			if (q >= 6 || p == finalMessage.size()) {
				messageBlock.add(tempStr);
				tempStr = "";
				q = 0;
			}
		}
		System.out.println(messageBlock.size());
		for (String temp : messageBlock) {
			if (temp.trim() != "") {
				try {
					System.out.println("finalMessage : " + finalMessage);
					driver.findElement(By.cssSelector("div[style*='translateY(" + (i) * 72 + "px)']")).click();
					temp = temp.replace("\n", Keys.chord(Keys.SHIFT, Keys.ENTER));
					driver.findElement(By.cssSelector("div[title=\"Type a message\"]")).sendKeys(temp);
					Thread.sleep(5000);
					driver.findElement(By.xpath("//*[@id=\"main\"]/footer/div[1]/div/span[2]/div/div[2]/div[2]/button"))
							.click();
					Thread.sleep(5000);
//				Thread.sleep((60 / messageBlock.size()) * 60000);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	public static List<String> readMessageFromWhatsapp(int i) throws InterruptedException {
		List<String> newLinks = new ArrayList();
		for (int j = 0; j < i; j++) {
//			System.out.println(j);	
			driver.findElement(By.cssSelector("div[style*='translateY(" + j * 72 + "px)']")).click();
			Thread.sleep(2000);
			driver.findElement(By.cssSelector("div[style*='translateY(" + (j + 1) * 72 + "px)']")).click();
			Thread.sleep(2000);
			driver.findElement(By.cssSelector("div[style*='translateY(" + j * 72 + "px)']")).click();
			Thread.sleep(2000);

			ArrayList<String> al = new ArrayList<String>();
			if (false) {
				List<WebElement> list = driver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/div/div[2]/div[3]"))
						.findElements(By.xpath("./child::*"));
				for (WebElement e : list) {
					String s = e.getAttribute("data-id");
					al.add(s);
				}
			} else {
				for (int k = 1; k <= 40; k++) {
					try {
						try {
							WebElement e = driver.findElement(
									By.xpath("//*[@id=\"main\"]/div[2]/div/div[2]/div[2]/div[" + k + "]/div"));
							String s = e.getAttribute("data-id");
							al.add(s);
						} catch (Exception e) {
							String s = driver
									.findElement(
											By.xpath("//*[@id=\"main\"]/div[2]/div/div[2]/div[3]/div[" + k + "]/div"))
									.getAttribute("data-id");
							al.add(s);
						}
					} catch (Exception ex) {
						break;
					}
				}
			}
			for (String s : al)
				if (s != null) {
					Thread.sleep(100);
					WebElement e = driver.findElement(By.cssSelector("div[data-id=\"" + s + "\"]"));
					try {
						new Actions(driver).moveToElement(e).perform();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					Thread.sleep(1000);
					String html = "";
					try {
						try {
							driver.findElement(
									By.xpath("//*[@data-id=\"" + s + "\"]/div/div/div[1]/div/div[3]/div/span[2]"))
									.click();
						} catch (Exception exx) {
							try {
								driver.findElement(
										By.xpath("//*[@data-id=\"" + s + "\"]/div/div/div[1]/div[2]/div[2]/span[2]"))
										.click();
							} catch (Exception egg) {
							}
						}
						html = driver.findElement(By.xpath("//*[@data-id=\"" + s
								+ "\"]/div/div[1]/div[1]/div/div[.//span[contains(@class, 'selectable-text')]]"))
								.getAttribute("innerHTML");
//								By.xpath("//*[@data-id=\"" + s + "\"]/div/div/div[1]/div[2]/div[2]"))
					} catch (Exception ex) {
						try {
							html = driver.findElement(By.xpath("//*[@data-id=\"" + s + "\"]/div/div/div[1]/div"))
									.getAttribute("innerHTML");
							if (html.contains("+91"))
								html = driver.findElement(By.xpath("//*[@data-id=\"" + s + "\"]/div/div/div[1]/div[2]"))
										.getAttribute("innerHTML");
						} catch (Exception exx) {
//							exx.printStackTrace();
							continue;
						}
					}

					String plainText = Jsoup.parse(html).wholeText();
					try {
						if (plainText.substring(plainText.length() - 2).equalsIgnoreCase("AM")
								|| plainText.substring(plainText.length() - 2).equalsIgnoreCase("PM")) {
							if (plainText.substring(plainText.length() - 8, plainText.length() - 7).equals("1"))
								plainText = plainText.substring(0, plainText.length() - 8);
							else
								plainText = plainText.substring(0, plainText.length() - 7);

						}
					} catch (Exception eeee) {
//						eeee.printStackTrace();
					}
					newLinks.add(plainText.replace("Bathlu", ""));
//						break;
				}

		}
		return newLinks;
	}

	public static void convertLinks(List<String> finalMessage, List<String> newLinks) throws Exception {
		for (String plainText : newLinks) {
			System.out.println("Before conversion: " +  plainText);
			Deals deal = new Deals(null, EncryptUtils.encryptThisString(plainText.replace(" ", "")), plainText,
					new Timestamp(new Date().getTime()));
			if (DBUtils.insertDeals(deal) != 0 && plainText.contains("https") && !plainText.contains("dealscage")) {
				List<String> links = Matcherss.pullLinks(plainText);
				System.out.println(links);
				for (String temp : links) {
					String newLink = "";
					if (temp.contains("amzn") || temp.contains("amazon")) {
						if (true) {
							((JavascriptExecutor) driver).executeScript("window.open()");
							ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
							try {
								driver.switchTo().window(tabs.get(2)); // switches to new tab
								driver.get(temp);
								Thread.sleep(5000);
								try {
									driver.findElement(By.xpath("//*[@id=\"aod-close\"]/span/span/i")).click();
								} catch (Exception e) {
									System.out.println("no ad button found");
								}
								Thread.sleep(1000);
								driver.findElement(By.xpath("//*[@id=\"amzn-ss-text-link\"]/span/strong/a")).click();
								Thread.sleep(500);
								newLink = driver.findElement(By.xpath("//*[@id=\"amzn-ss-text-shortlink-textarea\"]"))
										.getAttribute("value");
								((JavascriptExecutor) driver).executeScript("window.close()");
							} catch (Exception ex) {
								ex.printStackTrace();
								try {
									((JavascriptExecutor) driver).executeScript("window.close()");
								} catch (Exception e) {
									e.printStackTrace();
									System.out.println("Error");
								}
							}
							driver.switchTo().window(tabs.get(0)); // switch back to main screen
						}
						if (!(newLink == "" || newLink == " ") && newLink.contains("https")
								&& (newLink.contains("amzn") || newLink.contains("amazon")))
							plainText = plainText.replace(temp, newLink);
					} else if (temp.contains("nonstopdeals") || temp.contains("whatsapp"))
						plainText = plainText.replace(temp, "https://chat.whatsapp.com/Dkv6R0O468wGO17LG8noms");
					else if (temp.contains("youtube") || temp.contains("youtu.be"))
						plainText = plainText;
					else if (true) {
						((JavascriptExecutor) driver).executeScript("window.open()");
						ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
						try {
							driver.switchTo().window(tabs.get(2)); // switches to new tab
							driver.get("https://www.cuelinks.com/link-kit");
							Thread.sleep(5000);
							try {
								driver.findElement(By.xpath("//*[@class=\"google-login\"]/div[1]")).click();
								Thread.sleep(2000);
								driver.get("https://www.cuelinks.com/link-kit");
								Thread.sleep(2000);
							} catch (Exception e) {
								System.out.println("Already logged in");
							}
							Thread.sleep(2000);
							driver.findElement(By.xpath("//*[@id=\"link_url_0\"]")).click();
							Thread.sleep(1000);
							driver.findElement(By.xpath("//*[@id=\"link_url_0\"]")).sendKeys(temp);
							Thread.sleep(1000);
							driver.findElement(By.xpath("//*[@id=\"js-linkit\"]/div[6]/div/button")).click();
							Thread.sleep(5000);
							driver.findElement(By.xpath("//*[@for=\"link_short_url_convert_0\"]")).click();
							Thread.sleep(5000);
							// *[@id="show_linked_url_0"]/div/div/div/div/form/div/p[1]/label
						
							newLink = (String) ((JavascriptExecutor) driver)
									.executeScript("return $('#link_affiliatized_url_0').val();");
							// driver.findElement(By.xpath("//*[@id=\"link_affiliatized_url_0\"]"))
							// .getAttribute("value");
							// .getText();
							((JavascriptExecutor) driver).executeScript("window.close()");
						} catch (Exception ex) {
							ex.printStackTrace();
							try {
								((JavascriptExecutor) driver).executeScript("window.close()");
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Error");
							}
						}
						driver.switchTo().window(tabs.get(0)); // switch back to main screen

						
						if (!(newLink == "" || newLink == " ") && newLink.contains("https")
								&& newLink.contains("clnk.in") && newLink.length() < 30)
							plainText = plainText.replace(temp, newLink);
					}

				}
//					newLinks.add(plainText);
				System.out.println("After conversion: " +  plainText);
				finalMessage.add(plainText);
			}
		}
	}
}
