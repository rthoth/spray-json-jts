package test

import spray.json.geojson.GeoJSON._
import com.vividsolutions.jts.geom.{Geometry, GeometryFactory}
import spray.json._

class Example {
   implicit val geometryFactory = new GeometryFactory()

   val geoJson = """
{
   "type": "LineString",
   "coordinates": [[1, 1], [5, 5], [10, 10], [-1, -1]]
}
"""

   val point = geoJson.parseJson.convertTo[Geometry]
}
