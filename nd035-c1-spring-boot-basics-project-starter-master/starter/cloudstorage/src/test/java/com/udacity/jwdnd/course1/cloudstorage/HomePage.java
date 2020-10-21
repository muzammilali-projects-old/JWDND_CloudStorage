package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;

public class HomePage {
    @FindBy(id = "logout_button")
    private WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id = "create_note_button")
    private WebElement createNoteButton;

    @FindBy(id = "create_credential_button")
    private WebElement createCredentialButton;

    @FindBy(id = "credential-url")
    private WebElement credentialUrl;

    @FindBy(id = "credential-username")
    private WebElement credentialUsername;

    @FindBy(id = "credential-password")
    private WebElement credentialPassword;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(id = "saveChanges")
    private WebElement noteSubmitButton;

    @FindBy(id = "credSaveChanges")
    private WebElement credSubmitButton;

    @FindBy(className = "noteTitle")
    private WebElement firstNoteTitle;

    @FindBy(className = "noteDescription")
    private WebElement firstNoteDescription;

    @FindBy(name = "note-edit-button")
    private WebElement firstNoteEditButton;

    @FindBy(name = "note-delete-button")
    private WebElement firstNoteDeleteButton;

    @FindBy(className = "credUrl")
    private WebElement firstCredUrl;

    @FindBy(className = "credUsername")
    private WebElement firstCredUsername;

    @FindBy(className = "credPassword")
    private WebElement firstCredPassword;

    @FindBy(name = "cred-edit-button")
    private WebElement firstCredEditButton;

    @FindBy(name = "cred-delete-button")
    private WebElement firstCredDeleteButton;

    @Autowired
    private CredentialMapper credentialMapper;


    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void navNotes(WebDriver driver) throws InterruptedException {
        this.notesTab.click();
    }

    public void navCredentials(WebDriver driver) throws InterruptedException {
        this.credentialsTab.click();
    }

    public void createNote(WebDriver driver, String title, String description)throws InterruptedException {

        Thread.sleep(5000);

        JavascriptExecutor jse = (JavascriptExecutor) driver; jse.executeScript("arguments[0].click()", notesTab);

        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(createNoteButton)).click();
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(noteTitle)).sendKeys(title);
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(noteDescription)).sendKeys(description);
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(noteSubmitButton)).click();
        //noteSubmitButton.click();
    }

    public void createCredentials(WebDriver driver, String url, String username, String password)throws InterruptedException {

        Thread.sleep(5000);

        JavascriptExecutor jse = (JavascriptExecutor) driver; jse.executeScript("arguments[0].click()", credentialsTab);

        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(createCredentialButton)).click();
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(credentialUrl)).sendKeys(url);
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(credentialUsername)).sendKeys(username);
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(credentialPassword)).sendKeys(password);
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(credSubmitButton)).click();
    }

    public void editNote(WebDriver driver, String title, String description)throws InterruptedException {

        Thread.sleep(5000);

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].click()", notesTab);

        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(firstNoteEditButton)).click();
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(noteTitle)).clear();
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(noteTitle)).sendKeys(title);
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(noteDescription)).clear();
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(noteDescription)).sendKeys(description);
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(noteSubmitButton)).click();
    }

    public String editCredential(WebDriver driver, String url, String username, String password)throws InterruptedException {

        Thread.sleep(5000);

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].click()", credentialsTab);

        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(firstCredEditButton)).click();
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(credentialUrl)).clear();
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(credentialUrl)).sendKeys(url);
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(credentialUsername)).clear();
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(credentialUsername)).sendKeys(username);
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(credentialPassword));
        String decryptedPassword = credentialPassword.getAttribute("value");
        System.out.println("Decrypted password:");
        System.out.println(decryptedPassword);
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(credentialPassword)).clear();
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(credentialPassword)).sendKeys(password);
        Thread.sleep(500);
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(credSubmitButton)).click();

        return decryptedPassword;
    }

    public void deleteNote(WebDriver driver) throws InterruptedException {
        Thread.sleep(5000);

        JavascriptExecutor jse = (JavascriptExecutor) driver; jse.executeScript("arguments[0].click()", notesTab);

        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(firstNoteDeleteButton)).click();
        Thread.sleep(500);
    }

    public void deleteCredential(WebDriver driver) throws InterruptedException {
        Thread.sleep(5000);

        JavascriptExecutor jse = (JavascriptExecutor) driver; jse.executeScript("arguments[0].click()", credentialsTab);

        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(firstCredDeleteButton)).click();
        Thread.sleep(500);
    }

    public void logoutFromSession() {
        logoutButton.click();
    }

    public Note getFirstNote() {
        Note note = new Note(null, firstNoteTitle.getText(), firstNoteDescription.getText(), null);
        return note;
    }

    public Credential getFirstCredential() {
        Credential credential = new Credential(null, firstCredUrl.getText(), firstCredUsername.getText(), null, firstCredPassword.getText(), null);
        return credential;
    }

    public Integer getCredentialId(WebDriver driver) {
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOf(firstCredDeleteButton));
        String deleteButtonHref = firstCredDeleteButton.getAttribute("href");
        //System.out.println(deleteButtonHref);
        Integer credentialId = Integer.parseInt(deleteButtonHref.substring(deleteButtonHref.lastIndexOf('?') + 4));
        //System.out.println(credentialId);

        //Credential credential = credentialMapper.getCredential(credentialId);

        return credentialId;
    }


}
