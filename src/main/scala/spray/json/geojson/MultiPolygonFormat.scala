package spray.json.geojson

import com.vividsolutions.jts.geom._
import spray.json._

import GeoJSONFormatLike._

trait MultiPolygonFormatLike {
  this: GeoJSONFormatLike[_ <: Geometry] =>

  def readMultiPolygon(fields: Map[String, JsValue]): MultiPolygon = {
    fields.get("coordinates") match {
      case Some(JsArray(elements)) =>
        val polygons = for (element <- elements) yield {
          element match {
            case JsArray(part) =>
              val shell = part.head match {
                case JsArray(coordinates) => factory.createLinearRing(readCoordinates(coordinates))
                case value => throw new DeserializationException(s"Unexpected value $value!")
              }

              val holes = for (hole <- part.tail) yield {
                hole match {
                  case JsArray(coordinates) => factory.createLinearRing(readCoordinates(coordinates))
                  case _ => throw new DeserializationException(s"Unexpected $hole!")
                }
              }

              factory.createPolygon(shell, holes.toArray)

            case _ =>
              throw new DeserializationException(s"Unexpected value $element!")
          }
        }

        factory.createMultiPolygon(polygons.toArray)

      case Some(value) =>
        throw new DeserializationException(s"Unexpected value $value!")

      case _ =>
        throw new DeserializationException("Expected an array!")
    }
  }

  def writeMultiPolygon(multiPolygon: MultiPolygon): JsObject = {
    val polygons = for (i <- 0 until multiPolygon.getNumGeometries) yield {
      val polygon = multiPolygon.getGeometryN(i).asInstanceOf[Polygon]

      val shell = writeCoordinates(polygon.getExteriorRing.getCoordinates)
      val holes = for (i <- 0 until polygon.getNumInteriorRing) yield {
        writeCoordinates(polygon.getInteriorRingN(i).getCoordinates)
      }

      JsArray((Seq(shell) ++ holes):_*)
    }

    JsObject(
      "type" -> JsString(MultiPolygonType),
      "coordinates" -> JsArray(polygons: _*)
    )
  }

}

class MultiPolygonFormat(val factory: GeometryFactory) extends GeoJSONFormatLike[MultiPolygon]
    with MultiPolygonFormatLike {

  protected def read(fields: Map[String, JsValue]): MultiPolygon = {
    readMultiPolygon(fields)
  }

  def read(json: JsValue): MultiPolygon = {
    readTyped(json, MultiPolygonType)
  }

  def write(value: MultiPolygon): JsValue = {
    writeMultiPolygon(value)
  }
}
