package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactModificationTests extends TestBase {

    @Test
    public void testContactModification() throws InterruptedException {
        app.getNavigationHelper().returnToHomePage();
        Thread.currentThread().sleep(1000);
        app.getContactHelper().initContactModification();
        app.getContactHelper().fillContactForm(new ContactData("Sarah", "Connor", "address", "2025550114", "email@gmail.com"));
        app.getContactHelper().submitContactModification();

    }
}
