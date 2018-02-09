package spray.json.geojson

import GeoJSONFormatLike._
import com.vividsolutions.jts.geom._
import spray.json._

trait LineStringFormatLike {
  this: GeoJSONFormatLike[_ <: Geometry] =>

  def readLineString(fields: Map[String, JsValue]): LineString = {
    fields.get("coordinates") match {
      case Some(JsArray(elements)) =>
        factory.createLineString(readCoordinates(elements))

      case Some(value) =>
        throw new DeserializationException(s"Unexpected value $value!")

      case _ =>
        throw new DeserializationException("Expected an array!")
    }
  }

  def writeLineString(lineString: LineString): JsObject = {
    JsObject(
      "type" -> JsString(LineStringType),
      "coordinates" -> writeCoordinates(lineString.getCoordinates)
    )
  }
}

class LineStringFormat(val factory: GeometryFactory) extends GeoJSONFormatLike[LineString] with LineStringFormatLike {

  protected def read(fields: Map[String, JsValue]) = readLineString(fields)

  def read(json: JsValue): LineString = {
    readTyped(json, LineStringType)
  }

  def write(value: LineString): JsValue = {
    writeLineString(value)
  }
}
