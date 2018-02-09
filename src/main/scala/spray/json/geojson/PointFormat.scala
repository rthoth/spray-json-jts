package spray.json.geojson

import com.vividsolutions.jts.geom._
import spray.json._

import GeoJSONFormatLike._

trait PointFormatLike {

  this: GeoJSONFormatLike[_ <: Geometry] =>

  def readPoint(fields: Map[String, JsValue]): Point = {
    fields.get("coordinates") match {
      case Some(JsArray(elements)) =>
        factory.createPoint(readCoordinate(elements))

      case Some(value) =>
        throw new DeserializationException(s"Unexpected value $value!")

      case _ =>
        throw new DeserializationException("Expected an array!")
    }
  }

  def writePoint(point: Point): JsObject = {
    JsObject(
      "type" -> JsString(PointType),
      "coordinates" -> writeCoordinate(point.getCoordinate)
    )
  }
}


class PointFormat(val factory: GeometryFactory) extends GeoJSONFormatLike[Point] with PointFormatLike {

  protected def read(fields: Map[String, JsValue]): Point = {
    readPoint(fields)
  }

  def read(json: JsValue): Point = {
    readTyped(json, PointType)
  }

  def write(point: Point): JsValue = {
    writePoint(point)
  }
}
