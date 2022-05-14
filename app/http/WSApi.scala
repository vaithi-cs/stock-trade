package http
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.libs.ws.{BodyWritable, WSClient, WSResponse}
import play.libs.ws.WSRequest

import scala.concurrent.Future

class WSApi(client: WSClient) extends Api {

  def jsonBody[A](a: A)(implicit writes: Writes[A]): JsValue = Json.toJson(a)

  def read(url: String, params: Seq[(String, String)]): Future[WSResponse] = {
    client.url(url)
      .withQueryStringParameters(params:_*)
      .get()
  }

  def delete(url: String, params: Seq[(String, String)]): Future[WSResponse] = {
    client.url(url)
      .withQueryStringParameters(params:_*)
      .delete()
  }

  def update[A](body: A, url: String, params: Seq[(String, String)])(implicit writable: BodyWritable[A]): Future[WSResponse] = {
    client.url(url)
      .withQueryStringParameters(params:_*)
      .put(body)
  }

  def create[A](body: A, url: String, params: Seq[(String, String)])(implicit writable: BodyWritable[A]): Future[WSResponse] = {
    client.url(url)
      .withQueryStringParameters(params:_*)
      .post(body)
  }
}
