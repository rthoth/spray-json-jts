package spray.json.geojson

import com.vividsolutions.jts.geom._
import spray.json._
import scala.collection.immutable._

import GeoJSONFormatLike._

class GeoJSONFormat(val factory: GeometryFactory) extends GeoJSONFormatLike[Geometry]
    with PointFormatLike with MultiPointFormatLike
    with LineStringFormatLike with MultiLineStringFormatLike
    with PolygonFormatLike with MultiPolygonFormatLike
    with GeometryCollectionFormatLike {

  override def read(json: JsValue): Geometry = {
    json match {
      case JsObject(fields) =>
        read(fields)

      case JsNull =>
        null

      case _ =>
        throw new DeserializationException("Expected an object!")
    }
  }

  protected def read(fields: Map[String, JsValue]): Geometry = {
    fields.get("type") match {
      case Some(JsString(gType)) => gType match {
        case PointType =>
          readPoint(fields)

        case MultiPointType =>
          readMultiPoint(fields)

        case LineStringType =>
          readLineString(fields)

        case MultiLineStringType =>
          readMultiLineString(fields)

        case PolygonType =>
          readPolygon(fields)

        case MultiPolygonType =>
          readMultiPolygon(fields)

        case GeometryCollectionType =>
          readGeometryCollection(fields)

        case _ =>
          throw new DeserializationException(s"Unexpected type $gType!")
      }

      case Some(JsNull) | None =>
        throw new DeserializationException("Attribute 'type' must be defined!")

      case Some(value) =>
        throw new DeserializationException(s"Attribute 'type' $value is invalid!")
    }
  }

  override def write(geometry: Geometry): JsValue = {
    geometry match {
      case point: Point =>
        writePoint(point)

      case multiPoint: MultiPoint =>
        writeMultiPoint(multiPoint)

      case lineString: LineString =>
        writeLineString(lineString)

      case multiLineString: MultiLineString =>
        writeMultiLineString(multiLineString)

      case polygon: Polygon =>
        writePolygon(polygon)

      case multiPolygon: MultiPolygon =>
        writeMultiPolygon(multiPolygon)

      case geometryCollection: GeometryCollection =>
        writeGeometryCollection(geometryCollection)
    }
  }
}
