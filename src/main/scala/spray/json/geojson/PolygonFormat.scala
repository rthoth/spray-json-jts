package spray.json.geojson

import GeoJSONFormatLike._
import com.vividsolutions.jts.geom._
import spray.json._

trait PolygonFormatLike {
  this: GeoJSONFormatLike[_ <: Geometry] =>

  def readPolygon(fields: Map[String, JsValue]): Polygon = {
    fields.get("coordinates") match {
      case Some(JsArray(elements)) =>
        val shell = elements.head match {
          case JsArray(coordinates) =>
            factory.createLinearRing(readCoordinates(coordinates))

          case value =>
            throw new DeserializationException(s"Unexpected value $value!")
        }

        val holes = for (element <- elements.tail) yield {
          element match {
            case JsArray(coordinates) => factory.createLinearRing(readCoordinates(coordinates))
            case _ => throw new DeserializationException(s"Unexpected value $element!")
          }
        }

        factory.createPolygon(shell, holes.toArray)

      case Some(value) =>
        throw new DeserializationException(s"Unexpected value $value!")

      case _ =>
        throw new DeserializationException(s"Expected an array!")
    }
  }

  def writePolygon(polygon: Polygon): JsObject = {
    val shell = writeCoordinates(polygon.getExteriorRing.getCoordinates)
    val holes = for (i <- 0 until polygon.getNumInteriorRing) yield {
      writeCoordinates(polygon.getInteriorRingN(i).getCoordinates)
    }

    JsObject(
      "type" -> JsString(PolygonType),
      "coordinates" -> JsArray((Seq(shell) ++ holes): _*)
    )
  }
}

class PolygonFormat(val factory: GeometryFactory) extends GeoJSONFormatLike[Polygon]
    with PolygonFormatLike {

  protected def read(fields: Map[String, JsValue]): Polygon = {
    readPolygon(fields)
  }

  def read(json: JsValue): Polygon = {
    readTyped(json, PolygonType)
  }

  def write(value: Polygon): JsValue = {
    writePolygon(value)
  }
}
