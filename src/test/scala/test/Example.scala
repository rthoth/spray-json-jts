package test

import spray.json.geojson.GeoJSON._
import com.vividsolutions.jts.geom._
import spray.json._

object Example extends App {

  implicit val geometryFactory = new GeometryFactory()

  val geoJson = """
{
   "type": "LineString",
   "coordinates": [[1, 1], [5, 5], [10, 10], [-1, -1]]
}
"""

  val lineString = geoJson.parseJson.convertTo[Geometry]

  // prints LINESTRING (1 1, 5 5, 10 10, -1 -1)
  println(lineString.toText())

  // If you'd like typed geometries, no problem!

  val point = """{"type": "LineString", "coordinates": [-45, -21]}""".parseJson.convertTo[Point]

  // prints POINT(-45 -21)
  println(point)
}
