package br.ufla.spray.json

import com.vividsolutions.jts.geom._
import spray.json._

object GeoJSON {
   implicit def readGeoJSON(implicit geometryFactory: GeometryFactory): JsonFormat[Geometry] =
      new GeoJSONFormat(geometryFactory)
}
