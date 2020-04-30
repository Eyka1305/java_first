package ru.stqa.pft.addressbook.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.*;
import static org.testng.Assert.*;

public class ContactCreationTests extends TestBase {
  private WebDriver wd;

  @Test
  public void testContactCreation() throws Exception {
    Contacts before = app.contact().all();

    ContactData contact = new ContactData().withFirstName("Sarah").withLastName("Connor").withGroup("test1");
    app.contact().create(contact);
    assertEquals(app.contact().count(), before.size() + 1);
    Contacts after = app.contact().all();
    int max = after.stream().mapToInt((c) -> c.getId()).max().getAsInt();
    contact.withId(max);
    assertThat(after, equalTo(before.withAdded(contact)));

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
