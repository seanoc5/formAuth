import org.apache.http.impl.client.BasicCookieStore
import org.apache.log4j.Logger
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

//import java.net.http.HttpClient

Logger log = Logger.getLogger(this.class.name);

String url1 = "https://authenticationtest.com/xsrfChallenge/"
Document tokenDoc = Jsoup.connect(url1).get();
def token = tokenDoc.select("#xsrfToken").first().attr('value')
log.info "Get token ($token) from url: $url1"

String urlLogin = "https://authenticationtest.com/xsrfChallenge/"

CookieStore cookieStore = new BasicCookieStore();
def httpClient = HttpClient.newBuilder()
        .authenticator(new Authenticator() {
                   @Override
                   protected PasswordAuthentication getPasswordAuthentication() {
                       return new PasswordAuthentication("user", "pass".toCharArray());
                   }
               })
               .cookieHandler(new CookieManager())

//Document tokenDoc = Jsoup.connect(url1).get();
//def token = tokenDoc.select("#xsrfToken").first().attr('value')
//log.info "Get token ($token) from url: $url1"



