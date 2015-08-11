import play.api.libs.json.Json

package object models {

  case class RequestData(points: Array[Array[Double]], distanceOpt: Option[Double])

  case class ResponseData(status: String, polygon: Option[Array[Array[Double]]] = None, centroid: Option[Array[Double]] = None)

  object RequestData {
    implicit val reads = Json.reads[RequestData]
    implicit val writes = Json.writes[RequestData]
    implicit val formats = Json.format[RequestData]
  }

  object ResponseData {
    implicit val reads = Json.reads[ResponseData]
    implicit val writes = Json.writes[ResponseData]
    implicit val formats = Json.format[ResponseData]
  }

}