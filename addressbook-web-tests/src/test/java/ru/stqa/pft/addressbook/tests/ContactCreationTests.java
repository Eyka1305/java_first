package ru.stqa.pft.addressbook.tests;

import com.thoughtworks.xstream.XStream;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.*;
import static org.testng.Assert.*;


public class ContactCreationTests extends TestBase {
  private WebDriver wd;

  @DataProvider
  public Iterator<Object[]> validContacts() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/contacts.xml")));
    String xml = "";
    String line = reader.readLine();
    while (line != null) {
      xml += line;
      line = reader.readLine();
    }
    XStream xstream = new XStream();
    xstream.processAnnotations(ContactData.class);
    List<ContactData> contacts = (List<ContactData>) xstream.fromXML(xml);
    return contacts.stream().map((c) -> new Object[] {c}).collect(Collectors.toList()).iterator();
  }

  @BeforeMethod
  public void ensurePreconditions() {
    if (app.db().groups().size() == 0) {
      GroupData group = new GroupData().withName("test cc")
                                       .withHeader("header cc")
                                       .withFooter("footer cc");
      app.group().create(group);
    }
  }

  @Test(dataProvider = "validContacts")
  public void testContactCreation(ContactData contact) throws Exception {

    Contacts before = app.db().contacts();
   // File photo = new File("src/test/resources/stru.png");
    GroupData group = app.db().groups().iterator().next();
    contact.inGroup(group);

    app.contact().create(contact);
    assertEquals(app.contact().count(), before.size() + 1);
    Contacts after = app.db().contacts();
    int max = after.stream().mapToInt((c) -> c.getId()).max().getAsInt();
    contact.withId(max);
    assertThat(after, equalTo(before.withAdded(contact)));

  }
}
