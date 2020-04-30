package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

public class ContactModificationTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().homePage();
        if(app.contact().all().size() == 0) {
            app.contact().create(new ContactData().withFirstName("Sarah").withLastName("Connor").withGroup("test1"));
        }
    }

    @Test
    public void testContactModification() throws InterruptedException {

        Contacts before = app.contact().all();
        ContactData modifiedContact = before.iterator().next();
        int index = before.size() - 1;
        ContactData contact = new ContactData()
                .withId(modifiedContact.getId()).withFirstName("Sarah").withLastName("Connor").withAddress("address")
                .withMobilePhone("2025550114").withEmail("email@gmail.com").withGroup("test1");

        app.contact().modify(contact);
        assertEquals(app.contact().count(), before.size());
        Contacts after = app.contact().all();
        assertThat(after, equalTo(before.without(modifiedContact).withAdded(contact)));
    }

}
