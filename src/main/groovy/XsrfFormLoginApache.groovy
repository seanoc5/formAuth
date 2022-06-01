import org.apache.http.HttpEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.apache.log4j.Logger
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

Logger log = Logger.getLogger(this.class.name);

String urlLogin = "https://authenticationtest.com/xsrfChallenge/"
def  cookieStore = new BasicCookieStore();

def clientbuilder = HttpClients.custom();
def   clientbuilder1 = clientbuilder
    .setDefaultCookieStore(cookieStore)
//    .setUserAgent(USER_AGENT);

def client = clientbuilder1.build()

HttpGet httpget = new HttpGet(urlLogin);
def response = client.execute(httpget)
HttpEntity tokenEntity = response.getEntity();
String htmlToken = EntityUtils.toString(tokenEntity)
Document tokenDoc = Jsoup.parse(htmlToken)
def token = tokenDoc.select("#xsrfToken").first().attr('value')
log.info "Get token ($token) from url: $urlLogin"


//def reqbuilderLogin = RequestBuilder.post()

def reqbuilder = RequestBuilder.post()
def loginPost = reqbuilder
        .setUri(urlLogin)
        .addParameter("action", "login")
        .addParameter('email', "xsrf@authenticationtest.com")
        .addParameter('password', 'pa$$w0rd')
        .addParameter('xsrfToken', token)
        .build();


def httpResponse = client.execute(loginPost);
def loginEntity = httpResponse.getEntity()
String loginHtml = EntityUtils.toString(loginEntity)
Document successDoc = Jsoup.parse(loginHtml)
def h1Success = successDoc.select('h1')
log.info "Success (<h1>Login Success</h1>)? $h1Success"
log.info "Html:\n$loginHtml"


//String page1Url = 'https://authenticationtest.com/coverage/?id=1'
//HttpGet page1 = new HttpGet(page1Url);
//def page1Response = httpClient.execute(page1)
//HttpEntity page1Entity = page1Response.getEntity();
//String page1Html = EntityUtils.toString(page1Entity)
//Document page1Doc = Jsoup.parse(page1Html)
//def h2 = page1Doc.select('h2')
//def em = page1Doc.select('em')
//log.info "Page 1: h2 elements: <h2>Viewing Coverage Page #1</h2>"
//
//
//log.debug "Html login results: $loginHtml"
//
//log.info "more....?"
//
