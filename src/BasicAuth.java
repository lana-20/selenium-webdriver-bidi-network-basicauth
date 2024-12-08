import org.openqa.selenium.By;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.module.Network;
import org.openqa.selenium.bidi.network.AddInterceptParameters;
import org.openqa.selenium.bidi.network.InterceptPhase;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.lang.Thread;

public class BasicAuth {

	static protected WebDriver driver;
	static protected Network network;

	public static void main(String[] args) throws InterruptedException {

		var options = new FirefoxOptions();
		options.enableBiDi();
		driver = new FirefoxDriver(options);
		network = new Network(driver);

		network.addIntercept(new AddInterceptParameters(InterceptPhase.AUTH_REQUIRED));
		network.onAuthRequired(
				responseDetails -> network.continueWithAuth(
						responseDetails.getRequest().getRequestId(),
						new UsernameAndPassword("foo", "bar")));

		driver.get("http://httpbin.org/basic-auth/foo/bar");

		var body = driver.findElement(By.tagName("body"));

		boolean auth = body.getText().contains("authenticated");
		assert (auth);
		System.out.println(auth);
		
		Thread.sleep(3000);

		network.close();
		driver.quit();
	};
}
