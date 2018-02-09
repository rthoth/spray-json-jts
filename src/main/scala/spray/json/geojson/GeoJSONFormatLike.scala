package spray.json.geojson

import com.vividsolutions.jts.geom._
import spray.json._

object GeoJSONFormatLike {

  val PointType = "Point"
  val MultiPointType = "MultiPoint"
  val LineStringType = "LineString"
  val MultiLineStringType = "MultiLineString"
  val PolygonType = "Polygon"
  val MultiPolygonType = "MultiPolygon"
  val GeometryCollectionType = "GeometryCollection"
}


abstract class GeoJSONFormatLike[G <: Geometry] extends RootJsonFormat[G] {

  val factory: GeometryFactory

  protected def read(fields: Map[String, JsValue]): G

  protected def readTyped(json: JsValue, expectedType: String): G = {
    json match {
      case JsObject(fields) =>
        fields.get("type") match {
          case Some(JsString(gType)) =>
            if (expectedType == gType)
              read(fields)
            else
              throw new DeserializationException(s"Unexpected '$gType' type, it was expected $expectedType!")

          case Some(value) =>
            throw new DeserializationException(s"Unexpected $value as type!")

          case None =>
            throw new DeserializationException("Member 'type' must be defined!")
        }

      case _ =>
        throw new DeserializationException(s"Unexpected value $json!")
    }
  }

  def readCoordinate(elements: Vector[JsValue]): Coordinate = {
    val x = elements(0) match {
      case JsNumber(value) => value.doubleValue()
      case value => throw new DeserializationException(s"Unexpected value $value!")
    }

    val y = elements(1) match {
      case JsNumber(value) => value.doubleValue()
      case value => throw new DeserializationException(s"Unexpected value $value!")
    }

    new Coordinate(x, y)
  }

  def readCoordinates(elements: Vector[JsValue]): Array[Coordinate] = {
    val coordinates = Array.ofDim[Coordinate](elements.size)

    for ((element, i) <- elements.zipWithIndex) element match {
      case JsArray(coords) =>
        coordinates(i) = try {
          readCoordinate(coords.ensuring( _.size > 1, s"Coordinate $coords must be at least 2 elements!"))
        } catch {
          case reason: Throwable =>
            throw new DeserializationException(s"Unexpected exception at ${i}# element!", reason)
        }
      case _ =>
        throw new DeserializationException(s"Unexpected value $element!")
    }

    coordinates
  }

  def writeCoordinate(coordinate: Coordinate): JsArray = {
    JsArray(JsNumber(coordinate.x), JsNumber(coordinate.y))
  }

  def writeCoordinates(coordinates: Array[Coordinate]): JsArray = {
    JsArray(coordinates.map(writeCoordinate).toVector)
  }
}
