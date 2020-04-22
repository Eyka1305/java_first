package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

public class ContactDeletionTests extends TestBase {

    @Test
    public void testContactDeletion() {
        app.getNavigationHelper().returnToHomePage();
        if(! app.getContactHelper().isThereAContact()) {
            app.getContactHelper().createContact(new ContactData("Sarah", "Connor", "address", "2025550114", "email@gmail.com", "test1"));
        }
        app.getContactHelper().selectContact();
        app.getContactHelper().deleteSelectedContacts();
    }
}
