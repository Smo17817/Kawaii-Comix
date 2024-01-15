import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

public class LoginTest {

    public static void main(String[] args) {
        // Imposta la proprietà del sistema per indicare il percorso del driver di Internet Explorer
        System.setProperty("webdriver.edge.driver", "E:\\msedgedriver.exe");

        // Inizializza il driver di Internet Explorer
        WebDriver driver = new EdgeDriver();

        // Apri la pagina di login
        driver.get("http://localhost/kawaii-Comix/login.jsp");

        // Trova gli elementi di input (username e password) e il pulsante di login
        WebElement usernameInput = driver.findElement(By.id("email"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginButton"));

        // Inserisci le credenziali di login
        usernameInput.sendKeys("tiammazzopikachu@gmail.com");
        passwordInput.sendKeys("123");
        
        // Esegui il clic sul pulsante di login
        loginButton.click();

        // Attendi qualche secondo per visualizzare il risultato
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Chiudi il browser
        //driver.quit();
    }
}
