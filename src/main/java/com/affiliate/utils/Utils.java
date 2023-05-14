package com.affiliate.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Utils {
	static String executable = "C:\\Program Files\\Opera\\opera.exe";

	public static List<String> getFinalURL(List<String> urls, WebDriver driver) {

		((JavascriptExecutor) driver).executeScript("window.open()");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		try {
			System.out.println("0");
			driver.switchTo().window(tabs.get(3)); // switches to new tab
			for (int i = 0; i < urls.size(); i++) {
				try {
					driver.get(urls.get(i));
					Thread.sleep(5000);
					urls.add(i, driver.getCurrentUrl());
				} catch (Exception ex) {
					System.out.println("Error");
				}
			}
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
		driver.switchTo().window(tabs.get(2)); // switch back to main screen
		return urls;
	}

	public static void killOpera() {
		Runtime r = Runtime.getRuntime();
		try {
			r.exec("taskkill /F /IM \"opera.exe\"");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static List<String> getFinalURLOpera(List<String> urls) {
		if (false) {
			urls = new ArrayList();
//			urls.add("https://amzn.to/3HoPv4O");
			urls.add(
					"https://www.flipkart.com/mi-5a-80-cm-32-inch-hd-ready-led-smart-android-tv-dolby-audio-2022-model/p/itm98501ffa297ba?pid=TVSGD5CS2SDUUZGN&lid=LSTTVSGD5CS2SDUUZGNPVJDFV&marketplace=FLIPKART&store=ckf%2Fczl&spotlightTagId=BestsellerId_ckf%2Fczl&srno=b_1_2&otracker=hp_bannerads_1_2.bannerAdCard.BANNERADS_tv_7KCU4KBDJK6Y&fm=organic&iid=ed65871b-4f49-4b69-8a0c-8e3071750963.TVSGD5CS2SDUUZGN.SEARCH&ppt=None&ppn=None&ssid=kkhfnaibz40000001682868568329");
			urls.add(
					"https://www.flipkart.com/canon-eos-m200-mirrorless-camera-body-single-lens-ef-m15-45mm-f-3-5-6-3-stm/p/itm22196db772088?pid=DLLFKMK9Z3PBNYDJ&lid=LSTDLLFKMK9Z3PBNYDJALXYY9&marketplace=FLIPKART&store=jek%2Fp31%2Ftrv&srno=b_1_1&otracker=hp_omu_Best%2Bof%2BElectronics_2_3.dealCard.OMU_Q5LU1U8PHMK6_3&otracker1=hp_omu_PINNED_neo%2Fmerchandising_Best%2Bof%2BElectronics_NA_dealCard_cc_2_NA_view-all_3&fm=neo%2Fmerchandising&iid=b2f958a8-4a12-40b6-9241-688ee845ed5e.DLLFKMK9Z3PBNYDJ.SEARCH&ppt=hp&ppn=homepage&ssid=7d4fjantc00000001682867763856");
		} else {
			final List<String> list = urls;
			String profile = "C:\\Users\\sachi\\AppData\\Roaming\\Opera Software\\Opera Stable";
			String profile2 = "C:\\Users\\sachin-1\\AppData\\Roaming\\Opera Software\\Opera Stable";
			int listSize = list.size() < 5 ? list.size() : 5;
			for (int i = 0; i < listSize; i = i + 2) {
				ExecutorService es = Executors.newFixedThreadPool(5);
				final int number = i;
				CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> runOpera(list.get(number), profile),
						es);
				CompletableFuture<String> f2 = null;
				if (listSize - i > 1) {
					f2 = CompletableFuture.supplyAsync(() -> runOpera(list.get(number + 1), profile2), es);
				}
				try {
					Thread.sleep(15000);
					urls.set(i, f1.get());
					if (f2 != null)
						urls.set(i + 1, f2.get());
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return urls;
	}

	private static String runOpera(String url, String profile) {
		OperaDriver driver = null;
		try {
			WebDriverManager.operadriver().arch64().setup();
			OperaOptions option = new OperaOptions();
			option.addArguments("user-data-dir=" + profile);
			option.setBinary(executable);
			driver = new OperaDriver(option);
			try {
				driver.get(url);
				Thread.sleep(10000);
				url = driver.getCurrentUrl();
				if (!(url.contains("amzn") || url.contains("amazon")))
					url = url.split("\\?")[0];
				driver.quit();
			} catch (Exception ex) {
				System.out.println("Error");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				driver.quit();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error");
			}
		}
		return url;
	}

	public static String getAttributes(String xpath, String attribute, WebDriver driver) {
		String value = null;
		try {
			WebElement e = driver.findElement(By.xpath(xpath));
			value = e.getAttribute(attribute);
		} catch (Exception e) {
			value = null;
		}
		return value;
	}

	public static void main(String[] args) {
		String url = "https://www.flipkart.com/floresta-wud-4x6-ft-without-cushion-2-seater-double-foam-fold-out-sofa-cum-bed/p/itm9926633d778a2?affExtParam1=tc1683635364670&msxg=1683635364670&akgebn=188007ce33e&affid=thedealapp&pid=SBEG73TGKEZZJBER&mapft=dea1app&&affid=adminnxtify&affExtParam1=EPTG1489941&affExtParam2=";
		if (!(url.contains("amzn") || url.contains("amazon")))
			url = url.split("\\?")[0];
		System.out.println(url);

		List urls = new ArrayList();
//		urls.add("https://amzn.to/3HoPv4O");
		urls.add(
				"https://www.flipkart.com/mi-5a-80-cm-32-inch-hd-ready-led-smart-android-tv-dolby-audio-2022-model/p/itm98501ffa297ba?pid=TVSGD5CS2SDUUZGN&lid=LSTTVSGD5CS2SDUUZGNPVJDFV&marketplace=FLIPKART&store=ckf%2Fczl&spotlightTagId=BestsellerId_ckf%2Fczl&srno=b_1_2&otracker=hp_bannerads_1_2.bannerAdCard.BANNERADS_tv_7KCU4KBDJK6Y&fm=organic&iid=ed65871b-4f49-4b69-8a0c-8e3071750963.TVSGD5CS2SDUUZGN.SEARCH&ppt=None&ppn=None&ssid=kkhfnaibz40000001682868568329");

		urls.add(
				"https://www.flipkart.com/canon-eos-m200-mirrorless-camera-body-single-lens-ef-m15-45mm-f-3-5-6-3-stm/p/itm22196db772088?pid=DLLFKMK9Z3PBNYDJ&lid=LSTDLLFKMK9Z3PBNYDJALXYY9&marketplace=FLIPKART&store=jek%2Fp31%2Ftrv&srno=b_1_1&otracker=hp_omu_Best%2Bof%2BElectronics_2_3.dealCard.OMU_Q5LU1U8PHMK6_3&otracker1=hp_omu_PINNED_neo%2Fmerchandising_Best%2Bof%2BElectronics_NA_dealCard_cc_2_NA_view-all_3&fm=neo%2Fmerchandising&iid=b2f958a8-4a12-40b6-9241-688ee845ed5e.DLLFKMK9Z3PBNYDJ.SEARCH&ppt=hp&ppn=homepage&ssid=7d4fjantc00000001682867763856");
		urls.add(
				"https://www.flipkart.com/canon-eos-m200-mirrorless-camera-body-single-lens-ef-m15-45mm-f-3-5-6-3-stm/p/itm22196db772088?pid=DLLFKMK9Z3PBNYDJ&lid=LSTDLLFKMK9Z3PBNYDJALXYY9&marketplace=FLIPKART&store=jek%2Fp31%2Ftrv&srno=b_1_1&otracker=hp_omu_Best%2Bof%2BElectronics_2_3.dealCard.OMU_Q5LU1U8PHMK6_3&otracker1=hp_omu_PINNED_neo%2Fmerchandising_Best%2Bof%2BElectronics_NA_dealCard_cc_2_NA_view-all_3&fm=neo%2Fmerchandising&iid=b2f958a8-4a12-40b6-9241-688ee845ed5e.DLLFKMK9Z3PBNYDJ.SEARCH&ppt=hp&ppn=homepage&ssid=7d4fjantc00000001682867763856");
		urls.add(
				"https://www.flipkart.com/canon-eos-m200-mirrorless-camera-body-single-lens-ef-m15-45mm-f-3-5-6-3-stm/p/itm22196db772088?pid=DLLFKMK9Z3PBNYDJ&lid=LSTDLLFKMK9Z3PBNYDJALXYY9&marketplace=FLIPKART&store=jek%2Fp31%2Ftrv&srno=b_1_1&otracker=hp_omu_Best%2Bof%2BElectronics_2_3.dealCard.OMU_Q5LU1U8PHMK6_3&otracker1=hp_omu_PINNED_neo%2Fmerchandising_Best%2Bof%2BElectronics_NA_dealCard_cc_2_NA_view-all_3&fm=neo%2Fmerchandising&iid=b2f958a8-4a12-40b6-9241-688ee845ed5e.DLLFKMK9Z3PBNYDJ.SEARCH&ppt=hp&ppn=homepage&ssid=7d4fjantc00000001682867763856");
		getFinalURLOpera(urls);

	}
}
