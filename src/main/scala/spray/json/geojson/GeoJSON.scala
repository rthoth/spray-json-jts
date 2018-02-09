package spray.json.geojson

import com.vividsolutions.jts.geom._
import spray.json._

object GeoJSON {

  implicit def geometryGeoJSON(implicit factory: GeometryFactory): JsonFormat[Geometry] =
    new GeoJSONFormat(factory)

  implicit def pointGeoJSON(implicit factory: GeometryFactory): JsonFormat[Point] =
    new PointFormat(factory)

  implicit def multiPointGeoJSON(implicit factory: GeometryFactory): JsonFormat[MultiPoint] =
    new MultiPointFormat(factory)

  implicit def lineStringGeoJSON(implicit factory: GeometryFactory): JsonFormat[LineString] =
    new LineStringFormat(factory)

  implicit def multiLineStringGeoJSON(implicit factory: GeometryFactory): JsonFormat[MultiLineString] =
    new MultiLineStringFormat(factory)

  implicit def polygonGeoJSON(implicit factory: GeometryFactory): JsonFormat[Polygon] =
    new PolygonFormat(factory)

  implicit def multiGeoJSON(implicit factory: GeometryFactory): JsonFormat[MultiPolygon] =
    new MultiPolygonFormat(factory)

  implicit def geometryCollectionGeoJSON(implicit factory: GeometryFactory): JsonFormat[GeometryCollection] =
    new GeometryCollectionFormat(factory)
}
