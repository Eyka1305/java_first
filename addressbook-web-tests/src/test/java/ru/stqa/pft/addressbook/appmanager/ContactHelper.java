package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;


import java.util.List;

public class ContactHelper extends HelperBase {
    private NavigationHelper navigationHelper;

    public ContactHelper(WebDriver wd, NavigationHelper navigationHelper) {
        super(wd);
        this.navigationHelper = navigationHelper;
    }
    public void submitContactCreation() {
        click(By.xpath("(//input[@name='submit'])[2]"));
    }

    public void fillContactForm(ContactData contactData, boolean creation) {
        type(By.name("firstname"), contactData.getFirstName());
        type(By.name("lastname"), contactData.getLastName());
        type(By.name("address"), contactData.getAddress());
        type(By.name("mobile"), contactData.getMobilePhone());
        type(By.name("email"), contactData.getEmail1());
        attach(By.name("photo"), contactData.getPhoto());

        if (creation) {
            Select groupSelect = new Select(wd.findElement(By.name("new_group")));
            String groupName = "";
            if (contactData.getGroups().size() == 0) {
                List<WebElement> options = groupSelect.getOptions();
                groupName = options.get(0).getText();
            } else {
                groupName = contactData.getGroups().iterator().next().getName();
            }
            groupSelect.selectByVisibleText(groupName);
        }   else {
            Assert.assertFalse(isElementPresent(By.name("new_group")));
        }
    }

    public void selectContactById(int id) {
        wd.findElement(By.cssSelector("input[value = '" + id + "']")).click();
    }

    public void deleteSelectedContacts() {
        click(By.xpath("//input[@value='Delete']"));
        wd.switchTo().alert().accept();
    }

    public void initContactModificationById(int id) {
        String editLink = "a[href='edit.php?id=" + id + "']";
        wd.findElement(By.cssSelector(editLink)).click();

    }

    public void submitContactModification() {
        click(By.xpath("(//input[@name='update'])[2]"));
    }

    public void create(ContactData contact) {
        this.navigationHelper.newContactPage();
        fillContactForm(contact,true);
        submitContactCreation();
        contactCache = null;
        this.navigationHelper.homePage();
    }

    public void modify(ContactData contact) {
        initContactModificationById(contact.getId());
        fillContactForm(contact, false);
        submitContactModification();
        contactCache = null;
        this.navigationHelper.homePage();
    }


    public void delete(ContactData contact) {
        selectContactById(contact.getId());
        deleteSelectedContacts();
        contactCache = null;
        this.navigationHelper.homePage();
    }

    public boolean isThereAContact() {
        return isElementPresent(By.name("selected[]"));
    }

    public int count() {
       return wd.findElements(By.name("selected[]")).size();
    }

    private Contacts contactCache = null;

    public Contacts all() {
        if (contactCache != null) {
            return new Contacts(contactCache);
        }
        contactCache = new Contacts();
        List<WebElement> elements = wd.findElements(By.cssSelector("tr[name=entry]"));
        for (WebElement row : elements) {
            List<WebElement> cells = row.findElements(By.cssSelector("td"));
            String firstName = cells.get(2).getText();
            String lastName = cells.get(1).getText();
            String allPhones = cells.get(5).getText();
            String address = cells.get(3).getText();
            String allEmails = cells.get(4).getText();
            int id = Integer.parseInt(row.findElement(By.tagName("input")).getAttribute("value"));

            ContactData contact = new ContactData()
                    .withId(id).withFirstName(firstName).withLastName(lastName).withAllPhones(allPhones)
                    .withAddress(address).withAllEmails(allEmails);
            contactCache.add(contact);
        }
        return contactCache;
    }

    public ContactData infoFromEditForm(ContactData contact) {
        initContactModificationById(contact.getId());
        String firstName = wd.findElement(By.name("firstname")).getAttribute("value");
        String lastName = wd.findElement(By.name("lastname")).getAttribute("value");
        String home = wd.findElement(By.name("home")).getAttribute("value");
        String mobile = wd.findElement(By.name("mobile")).getAttribute("value");
        String work = wd.findElement(By.name("work")).getAttribute("value");
        String address = wd.findElement(By.name("address")).getAttribute("value");
        String email1 = wd.findElement(By.name("email")).getAttribute("value");
        String email2 = wd.findElement(By.name("email2")).getAttribute("value");
        String email3 = wd.findElement(By.name("email3")).getAttribute("value");
        wd.navigate().back();
        return new ContactData()
                .withId(contact.getId()).withFirstName(firstName).withLastName(lastName)
                .withHomePhone(home).withMobilePhone(mobile).withWorkPhone(work).withAddress(address)
                .withEmail1(email1).withEmail2(email2).withEmail3(email3);
    }

    public void addToGroup(ContactData contact, GroupData group) {
        this.navigationHelper.homePage();
        selectContactById(contact.getId());
        Select groupSelect = new Select(wd.findElement(By.name("to_group")));
        groupSelect.selectByValue(String.valueOf(group.getId()));
        click(By.cssSelector("input[value = 'Add to']"));
    }
}
