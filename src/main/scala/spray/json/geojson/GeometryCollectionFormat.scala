package spray.json.geojson

import com.vividsolutions.jts.geom._
import GeoJSONFormatLike._
import spray.json._

trait GeometryCollectionFormatLike extends PointFormatLike with MultiPointFormatLike
    with LineStringFormatLike with MultiLineStringFormatLike
    with PolygonFormatLike with MultiPolygonFormatLike {

  this: GeoJSONFormatLike[_ <: Geometry] =>

  def readGeometryCollection(fields: Map[String, JsValue]): GeometryCollection = {
    fields.get("geometries") match {
      case Some(JsArray(elements)) =>
        val geometries = for (element <- elements) yield read(element)
        factory.createGeometryCollection(geometries.toArray)

      case Some(value) =>
        throw new DeserializationException(s"Unexpected value $value!")

      case _ =>
        throw new DeserializationException("Expected an array!")
    }
  }

  def writeGeometry(value: Geometry): JsValue = value match {
    case point: Point =>
      writePoint(point)

    case points: MultiPoint =>
      writeMultiPoint(points)

    case line: LineString =>
      writeLineString(line)

    case lines: MultiLineString =>
      writeMultiLineString(lines)

    case polygon: Polygon =>
      writePolygon(polygon)

    case polygons: MultiPolygon =>
      writeMultiPolygon(polygons)
  }

  def writeGeometryCollection(value: GeometryCollection): JsValue = {
    JsObject(Map(
      "type" -> JsString(GeometryCollectionType),
      "geometries" -> JsArray(
        (for (i <- 0 until value.getNumGeometries) yield writeGeometry(value.getGeometryN(i))).toVector
      )
    ))
  }
}

class GeometryCollectionFormat(val factory: GeometryFactory) extends GeoJSONFormatLike[GeometryCollection]
    with GeometryCollectionFormatLike {

  protected def read(fields: Map[String, JsValue]): GeometryCollection = {
    readGeometryCollection(fields)
  }

  def read(json: JsValue): GeometryCollection = {
    readTyped(json, GeometryCollectionType)
  }

  def write(value: GeometryCollection): JsValue = {
    writeGeometryCollection(value)
  }
}
