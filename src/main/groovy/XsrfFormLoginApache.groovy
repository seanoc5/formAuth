import org.apache.http.HttpEntity
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.apache.log4j.Logger
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

Logger log = Logger.getLogger(this.class.name);

String urlToken = "https://authenticationtest.com/xsrfChallenge/"
//def cookieStore = new BasicCookieStore();


CloseableHttpClient client = HttpClients.createDefault()
String page1Url = 'https://authenticationtest.com/coverage/?id=1'
HttpGet httpgetPage1 = new HttpGet(page1Url);
def page1Response = client.execute(httpgetPage1)
HttpEntity page1Entity = page1Response.getEntity();
String page1Html = EntityUtils.toString(page1Entity)
Document page1Doc = Jsoup.parse(page1Html)
def em = page1Doc.select("em").first()
log.info "Em tag?? ($em) from url: $urlToken  (should have pro tip: not logged in (yet))"

HttpGet httpgetToken = new HttpGet(urlToken);
def tokenResponse = client.execute(httpgetToken)
HttpEntity tokenEntity = tokenResponse.getEntity();
String htmlToken = EntityUtils.toString(tokenEntity)
Document tokenDoc = Jsoup.parse(htmlToken)
def token = tokenDoc.select("#xsrfToken").first().attr('value')
log.info "Get token ($token) from url: $urlToken"


//def reqbuilderLogin = RequestBuilder.post()
String urlLogin = 'https://authenticationtest.com//login/?mode=xsrfChallenge'
HttpPost loginPost = new HttpPost(urlLogin)

List<NameValuePair> args = [
//        new BasicNameValuePair('action', 'login'),
new BasicNameValuePair('email', 'xsrf@authenticationtest.com'),
new BasicNameValuePair('password', 'pa$$w0rd'),
new BasicNameValuePair('xsrfToken', token),
]
org.apache.http.HttpEntity entity = new UrlEncodedFormEntity(args)

log.info "Encoded params: $entity"
loginPost.setEntity(entity)
def responseLogin = client.execute(loginPost)
def entityLogin = responseLogin.getEntity()

String htmlLogin = EntityUtils.toString(entityLogin)
log.info "Login request: $loginPost"

page1Response = client.execute(httpgetPage1)
page1Entity = page1Response.getEntity();
page1Html = EntityUtils.toString(page1Entity)
page1Doc = Jsoup.parse(page1Html)
em = page1Doc.select("em").first()
log.info "Em tag?? ($em) from url: $urlToken  (should have:A deep dive into the technical requirements ...)"



log.info "more....?"

