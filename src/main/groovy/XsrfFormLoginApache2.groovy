import org.apache.http.HttpEntity
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.apache.log4j.Logger
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

Logger log = Logger.getLogger(this.class.name);

String urlLogin = "https://authenticationtest.com/xsrfChallenge/"

// Use custom cookie store if necessary.
def cookieStore = new BasicCookieStore();


final RequestConfig defaultRequestConfig = RequestConfig.custom()
        .setCookieSpec(CookieSpecs.DEFAULT)
//        .setExpectContinueEnabled(true)
//        .setTargetPreferredAuthSchemes(Arrays.asList(StandardAuthScheme.NTLM, StandardAuthScheme.DIGEST))
//        .setProxyPreferredAuthSchemes(Collections.singletonList(StandardAuthScheme.BASIC))
        .build();

// Create an HttpClient with the given custom dependencies and configuration.

final CloseableHttpClient httpClient = HttpClients.custom()
//        .setConnectionManager(connManager)
        .setDefaultCookieStore(cookieStore)
//        .setDefaultCredentialsProvider(credentialsProvider)
//                .setProxy(new HttpHost("myproxy", 8080))
        .setDefaultRequestConfig(defaultRequestConfig)
        .build()


RequestBuilder reqbuilder = RequestBuilder.get(urlLogin)
HttpGet httpget = new HttpGet(urlLogin);
def response = httpClient.execute(httpget)
HttpEntity tokenEntity = response.getEntity();
String htmlToken = EntityUtils.toString(tokenEntity)
Document tokenDoc = Jsoup.parse(htmlToken)
def token = tokenDoc.select("#xsrfToken").first().attr('value')
log.info "Get token ($token) from url: $urlLogin"


def reqbuilderLogin = RequestBuilder.post()
        .setUri(urlLogin)
        .addParameter("action", "login")
        .addParameter('email', "xsrf@authenticationtest.com")
        .addParameter('password', 'pa$$w0rd')
        .addParameter('xsrfToken', token)
        .build();

def httpResponse = httpClient.execute(reqbuilderLogin);
def loginEntity = httpResponse.getEntity()
String loginHtml = EntityUtils.toString(loginEntity)
Document successDoc = Jsoup.parse(loginHtml)
def h1Success = successDoc.select('h1')
log.info "Success (<h1>Login Success</h1>)? $h1Success"

String page1Url = 'https://authenticationtest.com/coverage/?id=1'
HttpGet page1 = new HttpGet(page1Url);
def page1Response = httpClient.execute(page1)
HttpEntity page1Entity = page1Response.getEntity();
String page1Html = EntityUtils.toString(page1Entity)
Document page1Doc = Jsoup.parse(page1Html)
def h2 = page1Doc.select('h2')
def em = page1Doc.select('em')
log.info "Page 1: h2 elements: <h2>Viewing Coverage Page #1</h2>"


log.debug "Html login results: $loginHtml"

log.info "more....?"

//Request.Post("http://targethost/login")
//    .bodyForm(Form.form().add("username",  "vip").add("password",  "secret").build())
//    .execute().returnContent();
//def httpClient = HttpClient.newBuilder()
//        .authenticator(new Authenticator() {
//                   @Override
//                   protected PasswordAuthentication getPasswordAuthentication() {
//                       return new PasswordAuthentication("user", "pass".toCharArray());
//                   }
//               })
//               .cookieHandler(new CookieManager())

//Document tokenDoc = Jsoup.connect(url1).get();
//def token = tokenDoc.select("#xsrfToken").first().attr('value')
//log.info "Get token ($token) from url: $url1"



