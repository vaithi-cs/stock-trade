package http

import play.api.libs.ws.{BodyWritable, WSRequest, WSResponse}

import scala.concurrent.Future


trait Api {

  def read(
            url: String,
            params: Seq[(String, String)] = List()
          ): Future[WSResponse]

  def delete(
              url: String,
              params: Seq[(String, String)] = List()
            ): Future[WSResponse]


  def update[A](
                 body: A,
                 url: String,
                 params: Seq[(String, String)] = List()
               )(implicit writable: BodyWritable[A]): Future[WSResponse]

  def create[A](
                 body: A,
                 url: String,
                 params: Seq[(String, String)] = List()
               )(implicit writable: BodyWritable[A]): Future[WSResponse]

}