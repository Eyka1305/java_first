package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ContactModificationTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        app.goTo().homePage();
        if(app.contact().all().size() == 0) {
            app.contact().create(new ContactData().withFirstName("Sarah").withLastName("Connor"));
        }
    }

    @Test
    public void testContactModification() throws InterruptedException {

        Set<ContactData> before = app.contact().all();
        ContactData modifiedContact = before.iterator().next();
        int index = before.size() - 1;
        ContactData contact = new ContactData()
                .withId(modifiedContact.getId()).withFirstName("Sarah").withLastName("Connor").withAddress("address")
                .withMobilePhone("2025550114").withEmail("email@gmail.com").withGroup("test1");

        app.contact().modify(contact);

        Set<ContactData> after = app.contact().all();
        Assert.assertEquals(after.size(), before.size());

        before.remove(modifiedContact);
        before.add(contact);

        Assert.assertEquals(before, after);
    }

}
