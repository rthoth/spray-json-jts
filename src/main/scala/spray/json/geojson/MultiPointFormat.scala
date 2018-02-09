package spray.json.geojson

import com.vividsolutions.jts.geom._
import spray.json._

import GeoJSONFormatLike._

trait MultiPointFormatLike {
  this: GeoJSONFormatLike[_ <: Geometry] =>

  def readMultiPoint(fields: Map[String, JsValue]): MultiPoint = {
    fields.get("coordinates") match {
      case Some(JsArray(elements)) =>
        factory.createMultiPoint(readCoordinates(elements))

      case Some(value) =>
        throw new DeserializationException(s"Unexpected value $value!")

      case _ =>
        throw new DeserializationException("Expected an array!")
    }
  }

  def writeMultiPoint(multiPoint: MultiPoint): JsObject = {
    JsObject(
      "type" -> JsString(MultiPointType),
      "coordinates" -> writeCoordinates(multiPoint.getCoordinates)
    )
  }
}

class MultiPointFormat(val factory: GeometryFactory) extends GeoJSONFormatLike[MultiPoint]
    with MultiPointFormatLike {

  protected def read(fields: Map[String, JsValue]): MultiPoint = readMultiPoint(fields)

  def read(json: JsValue): MultiPoint = {
    readTyped(json, MultiPointType)
  }

  def write(value: MultiPoint): JsValue = {
    writeMultiPoint(value)
  }
}
