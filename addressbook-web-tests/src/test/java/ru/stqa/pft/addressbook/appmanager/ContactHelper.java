package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactHelper extends HelperBase {
    private NavigationHelper navigationHelper;

    public ContactHelper(WebDriver wd, NavigationHelper navigationHelper) {
        super(wd);
        this.navigationHelper = navigationHelper;
    }
    public void initContactCreation() {
        click(By.xpath("(//input[@name='submit'])[2]"));
    }

    public void fillContactForm(ContactData contactData, boolean creation) {
        type(By.name("firstname"), contactData.getFirstName());
        type(By.name("lastname"), contactData.getLastName());
        type(By.name("address"), contactData.getAddress());
        type(By.name("mobile"), contactData.getMobilePhone());
        type(By.name("email"), contactData.getEmail());

        if (creation) {
            String groupName = contactData.getGroup();
            Select groupSelect = new Select(wd.findElement(By.name("new_group")));
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
        fillContactForm(contact, true);
        initContactCreation();
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
            int id = Integer.parseInt(row.findElement(By.tagName("input")).getAttribute("value"));

            ContactData contact = new ContactData().withId(id).withFirstName(firstName).withLastName(lastName);
            contactCache.add(contact);
        }
        return contactCache;
    }
}
