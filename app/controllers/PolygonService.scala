package controllers

import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier
import models._
import play.api.mvc._
import play.api.libs.json._
import com.vividsolutions.jts.geom._

class PolygonService extends Controller {

  val geometryFactory = new GeometryFactory()

  val failed = Ok(Json.toJson(ResponseData("failed")))

  def post = Action(parse.json) { req =>
    val dataOpt = req.body.asOpt[RequestData]
    dataOpt.map { data =>
      try {
        val distance = toDegrees(data.distanceOpt.getOrElse(200.0))
        Ok(Json.toJson(generate(data.points, distance)))
      } catch {
        case _: Throwable => failed
      }
    }.getOrElse(failed)
  }

  def generate(points: Array[Array[Double]], distance: Double) = {
    val coordinates = points.map { x =>
      new Coordinate(x(0), x(1))
    }
    val lineString = geometryFactory.createLineString(coordinates)
    val polygon = lineString.buffer(distance, 3, 2)
    val simplified = TopologyPreservingSimplifier.simplify(polygon, toDegrees(5))
    ResponseData("success",
      Some(simplified.getCoordinates.map(coord => Array(coord.x, coord.y))),
      Some(Array(simplified.getCentroid.getX, simplified.getCentroid.getY))
    )
  }

  // one degree = 111 km
  // 200m = 1*200/111000 dg = 0.00180180180
  def toDegrees(meters: Double) = meters * (1.0 / 111000.0)

  def offsetPolygon = Action(parse.json) { req =>
    req.body.asOpt[models.Polygon].map { polygon =>
      try {
        val coordinates = polygon.points.map { point => new Coordinate(point.lat, point.long) }
        val polygonGeom = geometryFactory.createPolygon(coordinates);
        val geom = polygonGeom.buffer(toDegrees(polygon.offset))
        val simplified = TopologyPreservingSimplifier.simplify(geom, toDegrees(5))
        Ok(
          Json.toJson(ResponseData("success",
            Some(simplified.getCoordinates.map(coord => Array(coord.x, coord.y)))
          ))
        )
      } catch {
        case _: Throwable => Ok("parse failed")
      }
    }.getOrElse(failed)
  }

}
