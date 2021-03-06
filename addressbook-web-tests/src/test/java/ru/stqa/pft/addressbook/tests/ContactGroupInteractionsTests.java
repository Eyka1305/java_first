package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class ContactGroupInteractionsTests extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {
        if (app.db().groups().size() == 0) {
            GroupData group = new GroupData().withName("test1")
                    .withHeader("header1")
                    .withFooter("footer1");
            app.group().create(group);
        }

        if (app.db().contacts().size() == 0) {
            app.goTo().homePage();
            app.contact().create(new ContactData().withFirstName("Sarah").withLastName("Connor"));
        }
    }

    @Test
    public void testAddContactToGroup() throws InterruptedException {
        Groups allGroups = app.db().groups();
        GroupData group = allGroups.iterator().next();
        Contacts before = group.getContacts();
        Contacts allContacts = app.db().contacts();
        ContactData modifiedContact = null;
        ArrayList<Integer> groupContactIds = new ArrayList(group.getContacts().stream().map(c -> c.getId()).collect(Collectors.toList()));

        if (groupContactIds.size() == 0) {
            modifiedContact = allContacts.iterator().next();

        } else if (groupContactIds.size() == allContacts.size()){
            app.goTo().homePage();
            app.contact().create(new ContactData().withFirstName("Sarah").withLastName("Connor"));

        } else {
            for(ContactData contact : allContacts) {
                if (!groupContactIds.contains(contact.getId())) {
                    modifiedContact = contact;
                    break;
                }
            }
        }
        app.contact().addToGroup(modifiedContact, group);
        GroupData groupAfter = null;
        Groups groupsAfter = app.db().groups();
        for (GroupData gr : groupsAfter) {
            if (gr.getId() == group.getId()) {
                groupAfter = gr;
                break;
            }
        }
        Contacts after = groupAfter.getContacts();
        assertThat(after, equalTo(before.withAdded(modifiedContact)));
    }

    @Test
    public void testDeleteContactFromGroup() throws InterruptedException {
        Groups allGroups = app.db().groups();
        GroupData group = allGroups.iterator().next();
        Contacts before = group.getContacts();
        ContactData modifiedContact = null;
        if (before.size() != 0) {
            modifiedContact = before.iterator().next();

        } else {
            app.goTo().homePage();
            app.contact().create(new ContactData().withFirstName("Sarah").withLastName("Connor").inGroup(group));
            GroupData reloadedGroup = null;
            Groups reloadedGroups = app.db().groups();
            for (GroupData gr : reloadedGroups) {
                if (gr.getId() == group.getId()) {
                    reloadedGroup = gr;
                    break;
                }
            }
        }

        app.contact().deleteFromGroup(modifiedContact, group);

        GroupData groupAfter = null;
        Groups groupsAfter = app.db().groups();
        for (GroupData g : groupsAfter) {
            if (g.getId() == group.getId()) {
                groupAfter = g;
                break;
            }
        }
        Contacts after = groupAfter.getContacts();
        assertThat(after, equalTo(before.without(modifiedContact)));
    }
}
