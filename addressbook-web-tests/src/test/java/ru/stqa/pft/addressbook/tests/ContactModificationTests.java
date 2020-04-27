package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactModificationTests extends TestBase {

    @Test
    public void testContactModification() throws InterruptedException {
        app.getNavigationHelper().returnToHomePage();
        if(! app.getContactHelper().isThereAContact()) {
            app.getContactHelper().createContact(new ContactData("Sarah", "Connor", "address", "2025550114", "email@gmail.com", "test1"));
        }

        int before = app.getContactHelper().getContactCount();

        app.getContactHelper().initContactModification(before - 1);
        app.getContactHelper().fillContactForm(new ContactData("Sarah", "Connor", "address", "2025550114", "email@gmail.com", null), false);
        app.getContactHelper().submitContactModification();
        app.getNavigationHelper().returnToHomePage();

        int after = app.getContactHelper().getContactCount();
        Assert.assertEquals(after, before);
    }
}
