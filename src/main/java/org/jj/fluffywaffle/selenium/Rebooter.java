package org.jj.fluffywaffle.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by julka on 2015-09-24.
 */
public class Rebooter {

    // reboot rooter using Firefox(!!!!!!!!!) driver
    public void rebootRouter(){

        String routerConfigPageUrl = "http://192.168.1.1";
        String adminLogin = "admin";
        String adminPassword = "admin1";

        WebDriver driver = new FirefoxDriver();
        driver.get(routerConfigPageUrl);

        String loginInputId = "userName";
        driver.findElement(By.id(loginInputId)).sendKeys(adminLogin);

        String passwordInputId = "pcPassword";
        driver.findElement(By.id(passwordInputId)).sendKeys(adminPassword);

        String buttonClassName = "loginBtn";
        driver.findElement(By.className(buttonClassName)).click();

        String currentWindowHandle = driver.getWindowHandle();

        driver.switchTo().frame("menufrm");

        String managementLinkId = "53";
        driver.findElement(By.id(managementLinkId)).click();

        String resetLinkId = "65";
        driver.findElement(By.id(resetLinkId)).click();

        driver.switchTo().window(currentWindowHandle);
        driver.switchTo().frame("basefrm");

        buttonClassName = "buttonL";
        driver.findElement(By.className(buttonClassName)).click();

        driver.switchTo().alert().accept();

        driver.close();
    }
}
