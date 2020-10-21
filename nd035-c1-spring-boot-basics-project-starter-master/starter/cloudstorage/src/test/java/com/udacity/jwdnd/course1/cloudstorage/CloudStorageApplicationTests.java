package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Base64;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private LoginPage loginPage;
	private SignUpPage signupPage;
	private HomePage homePage;

	@Autowired
	private CredentialMapper credentialMapper;

	@Autowired
	private EncryptionService encryptionService;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	@Order(1)
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(2)
	public void getSignUpPage() {
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	@Order(3)
	public void signInThenLogOut() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/signup");
		signupPage = new SignUpPage(driver);

		signupPage.setInputFirstName("Muzammil");
		Thread.sleep(500);
		signupPage.setInputLastName("Ali");
		Thread.sleep(500);
		signupPage.setInputUserName("testUser");
		Thread.sleep(500);
		signupPage.setInputPassword("password");
		Thread.sleep(500);
		signupPage.submitForm();
		Thread.sleep(500);

		driver.get("http://localhost:" + port + "/login");
		loginPage = new LoginPage(driver);

		loginPage.setInputUserName("testUser");
		Thread.sleep(500);
		loginPage.setInputPassword("password");
		Thread.sleep(500);
		loginPage.submitForm();
		Thread.sleep(500);

		Assertions.assertEquals("Home", driver.getTitle());

		homePage = new HomePage(driver);
		homePage.logoutFromSession();

		driver.get("http://localhost:" + port + "/home");
		Thread.sleep(500);

		Assertions.assertEquals("Login", driver.getTitle());

	}

	@Test
	@Order(4)
	public void createNote() throws InterruptedException {
		String noteTitle = "test note";
		String noteDescription = "test description";

		driver.get("http://localhost:" + port + "/login");
		loginPage = new LoginPage(driver);

		loginPage.setInputUserName("testUser");
		Thread.sleep(500);
		loginPage.setInputPassword("password");
		Thread.sleep(500);
		loginPage.submitForm();
		Thread.sleep(500);

		homePage = new HomePage(driver);

		homePage.navNotes(driver);
		WebDriverWait wait = new WebDriverWait(driver, 20);
		homePage.createNote(driver, noteTitle, noteDescription);
		Thread.sleep(1000);
		homePage.navNotes(driver);
		Thread.sleep(1000);
		Note note = homePage.getFirstNote();

		Assertions.assertEquals(noteTitle, note.getNoteTitle());
		Assertions.assertEquals(noteDescription, note.getNoteDescription());
	}

	@Test
	@Order(5)
	public void editNote() throws InterruptedException {
		String newNoteTitle = "new test note";
		String newNoteDescription = "new test description";

		driver.get("http://localhost:" + port + "/login");
		loginPage = new LoginPage(driver);

		loginPage.setInputUserName("testUser");
		Thread.sleep(500);
		loginPage.setInputPassword("password");
		Thread.sleep(500);
		loginPage.submitForm();
		Thread.sleep(500);

		homePage = new HomePage(driver);

		homePage.navNotes(driver);
		WebDriverWait wait = new WebDriverWait(driver, 20);
		homePage.editNote(driver, newNoteTitle, newNoteDescription);
		Thread.sleep(1000);
		homePage.navNotes(driver);
		Thread.sleep(1000);
		Note note = homePage.getFirstNote();

		Assertions.assertEquals(newNoteTitle, note.getNoteTitle());
		Assertions.assertEquals(newNoteDescription, note.getNoteDescription());
	}

	@Test
	@Order(6)
	public void deleteNote() throws InterruptedException {
		driver.get("http://localhost:" + port + "/login");
		loginPage = new LoginPage(driver);

		loginPage.setInputUserName("testUser");
		Thread.sleep(500);
		loginPage.setInputPassword("password");
		Thread.sleep(500);
		loginPage.submitForm();
		Thread.sleep(500);

		homePage = new HomePage(driver);

		homePage.navNotes(driver);
		WebDriverWait wait = new WebDriverWait(driver, 20);
		homePage.deleteNote(driver);
		Thread.sleep(1000);
		homePage.navNotes(driver);
		Thread.sleep(1000);

		Exception exception = Assertions.assertThrows(NoSuchElementException.class, () -> {
			homePage.getFirstNote();
		});

		Assertions.assertTrue(exception.getMessage().contains("no such element"));
	}

	@Test
	@Order(8)
	public void editCredential() throws InterruptedException {
		String credUrlEdited = "edited: www.test.org";
		String credUsernameEdited = "edited: test username";
		String credPasswordEdited = "edited_password";

		driver.get("http://localhost:" + port + "/login");
		loginPage = new LoginPage(driver);

		loginPage.setInputUserName("testUser");
		Thread.sleep(500);
		loginPage.setInputPassword("password");
		Thread.sleep(500);
		loginPage.submitForm();
		Thread.sleep(500);

		homePage = new HomePage(driver);

		homePage.navCredentials(driver);
		Credential oldCredential = credentialMapper.getCredential(homePage.getCredentialId(driver));
		WebDriverWait wait = new WebDriverWait(driver, 20);
		String decryptedPassword = homePage.editCredential(driver, credUrlEdited, credUsernameEdited, credPasswordEdited);
		Thread.sleep(1000);
		homePage.navCredentials(driver);
		Thread.sleep(1000);
		Credential credential = homePage.getFirstCredential();
		Credential credentialFromDb = credentialMapper.getCredential(homePage.getCredentialId(driver));

		Assertions.assertEquals(credUrlEdited, credential.getUrl());
		Assertions.assertEquals(credUsernameEdited, credential.getUsername());
		//pre-edit, decrypted password check
		Assertions.assertEquals(encryptionService.decryptValue(oldCredential.getPassword(), oldCredential.getKey()), decryptedPassword);
		//post-edit, encrypted password check
		Assertions.assertEquals(credentialFromDb.getPassword(), credential.getPassword());
	}

	@Test
	@Order(7)
	public void createCredential() throws InterruptedException {
		String credUrl = "www.test.org";
		String credUsername = "test username";
		String credPassword = "password";

		driver.get("http://localhost:" + port + "/login");
		loginPage = new LoginPage(driver);

		loginPage.setInputUserName("testUser");
		Thread.sleep(500);
		loginPage.setInputPassword("password");
		Thread.sleep(500);
		loginPage.submitForm();
		Thread.sleep(500);

		homePage = new HomePage(driver);

		homePage.navCredentials(driver);
		WebDriverWait wait = new WebDriverWait(driver, 20);
		homePage.createCredentials(driver, credUrl, credUsername, credPassword);
		Thread.sleep(1000);
		homePage.navCredentials(driver);
		Thread.sleep(1000);
		Credential credential = homePage.getFirstCredential();
		Credential credentialFromDb = credentialMapper.getCredential(homePage.getCredentialId(driver));

		Assertions.assertEquals(credUrl, credential.getUrl());
		Assertions.assertEquals(credUsername, credential.getUsername());
		Assertions.assertEquals(credentialFromDb.getPassword(), credential.getPassword());
	}

	@Test
	@Order(9)
	public void deleteCred() throws InterruptedException {
		driver.get("http://localhost:" + port + "/login");
		loginPage = new LoginPage(driver);

		loginPage.setInputUserName("testUser");
		Thread.sleep(500);
		loginPage.setInputPassword("password");
		Thread.sleep(500);
		loginPage.submitForm();
		Thread.sleep(500);

		homePage = new HomePage(driver);

		homePage.navCredentials(driver);
		WebDriverWait wait = new WebDriverWait(driver, 20);
		homePage.deleteCredential(driver);
		Thread.sleep(1000);
		homePage.navCredentials(driver);
		Thread.sleep(1000);

		Exception exception = Assertions.assertThrows(NoSuchElementException.class, () -> {
			homePage.getFirstCredential();
		});

		Assertions.assertTrue(exception.getMessage().contains("no such element"));
	}
}
