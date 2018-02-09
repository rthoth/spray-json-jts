package spray.json.geojson

import GeoJSONFormatLike._
import com.vividsolutions.jts.geom._
import spray.json._

trait MultiLineStringFormatLike {
  this: GeoJSONFormatLike[_ <: Geometry] =>

  def readMultiLineString(fields: Map[String, JsValue]): MultiLineString = {
    fields.get("coordinates") match {
      case Some(JsArray(elements)) =>
        val lineStrings = for (element <- elements) yield {
          element match {
            case JsArray(coordinates) => factory.createLineString(readCoordinates(coordinates))
            case _ => throw new DeserializationException(s"Unexpected value $element!")
          }
        }

        factory.createMultiLineString(lineStrings.toArray)

      case Some(value) =>
        throw new DeserializationException(s"Unexpected value $value!")

      case _ =>
        throw new DeserializationException("Expected a array!")
    }
  }

  def writeMultiLineString(multiLineString: MultiLineString): JsObject = {
    val coordinates = for (i <- 0 until multiLineString.getNumGeometries) yield {
      writeCoordinates(multiLineString.getGeometryN(i).getCoordinates)
    }

    JsObject(
      "type" -> JsString(MultiLineStringType),
      "coordinates" -> JsArray(coordinates.toVector)
    )
  }
}

class MultiLineStringFormat(val factory: GeometryFactory) extends GeoJSONFormatLike[MultiLineString]
    with MultiLineStringFormatLike {

  protected def read(fields: Map[String, JsValue]): MultiLineString = {
    readMultiLineString(fields)
  }

  def read(json: JsValue): MultiLineString = {
    readTyped(json, MultiLineStringType)
  }

  def write(value: MultiLineString): JsValue = {
    writeMultiLineString(value)
  }
}
