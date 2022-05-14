package controllers

import http.Api
import javax.inject.Inject
import play.api.libs.json.{Format, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

final case class StockInfo(
                          stockId: String,
                          name: String,
                          price: String,
                          currencyType: String,
                          timestamp: String
                          )

object StockInfo {
  implicit val stockInfoFormat: Format[StockInfo] = Json.format[StockInfo]
}

class SearchController @Inject() (httpApi: Api, val controllerComponents: ControllerComponents) extends BaseController {
  def searchStocks(stockName: String): Action[AnyContent] = Action.async {
    val url = """http://localhost:3000/stocks"""
    val callApi = httpApi.read(url, List())
      .map(_.body)
      .map(body => Json.parse(body))
      .map(_.as[List[StockInfo]])
      .map(_.find(_.name == stockName))

    callApi.map {
      value => Ok(Json.toJson(value))
    }
  }
}
