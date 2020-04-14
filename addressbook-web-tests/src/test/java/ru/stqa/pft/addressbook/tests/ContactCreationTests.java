package ru.stqa.pft.addressbook.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactCreationTests extends TestBase {
  private WebDriver wd;

  @Test
  public void testContactCreation() throws Exception {
    app.getNavigationHelper().goToNewContactPage();
    app.getContactHelper().fillContactForm(new ContactData("Sarah", "Connor", "address", "2025550114", "email@gmail.com"));
    app.getContactHelper().initContactCreation();
    app.getNavigationHelper().returnToHomePage();
  }

  private boolean isElementPresent(By by) {
    try {
      wd.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      wd.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }
}
