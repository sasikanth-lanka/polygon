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

  case class Point(lat: Double, long: Double)

  case class Polygon(points: Array[Point], offset: Long)

  object Point {
    implicit val reads = Json.reads[Point]
    implicit val writes = Json.writes[Point]
    implicit val formats = Json.format[Point]
  }

  object Polygon {
    implicit val reads = Json.reads[Polygon]
    implicit val writes = Json.writes[Polygon]
    implicit val formats = Json.format[Polygon]
  }

}
