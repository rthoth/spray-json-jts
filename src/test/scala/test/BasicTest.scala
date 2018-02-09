package test

import com.vividsolutions.jts.geom.{ Geometry, Point }
import spray.json._
import spray.json.geojson.GeoJSON._

class BasicTest extends Spec {

   "The GeoJSON" - {
      "should read" - {
         "a point" in {
            val point = load("test-data" / "point-01.json")
               .asString.parseJson.convertTo[Geometry]

            point.toText() should be ("POINT (-56.789 -23.456)")
         }

         "a multiPoint" in {
            val multiPoint = load("test-data" / "multipoint-01.json")
               .asString.parseJson.convertTo[Geometry]

            multiPoint.toText() should be ("MULTIPOINT ((12.345 -54.321), (55.443 44.332), (-12.987 -43.987))")
         }

         "a lineString" in {
            val lineString = load("test-data" / "linestring-01.json")
               .asString.parseJson.convertTo[Geometry]

            lineString.toText() should be ("LINESTRING (12.345 -12.345, 54.321 54.321, -56.789 -23.456, 11.789 22.666, -56.001 34.123)")
         }

         "a multiLineString" in {
            val multiLineString = load("test-data" / "multilinestring-01.json")
               .asString.parseJson.convertTo[Geometry]

            multiLineString.toText() should be ("MULTILINESTRING ((0 1.5, 2 3, 3 3.5, -0.3 0), (0 0, 0.9 1, 2 2), (-4 -1, -1 -4, 6 7))")
         }

         "a polygon" in {
            val polygon = load("test-data" / "polygon-01.json")
               .asString.parseJson.convertTo[Geometry]

            polygon.toText() should be ("POLYGON ((-47.86117672920226 -15.798520064925436, -47.86133766174316 -15.79903624199543, -47.860227227211 -15.799371756385344, -47.86117672920226 -15.798520064925436), (-47.86114454269409 -15.798633623993734, -47.86126792430877 -15.7990155949379, -47.860833406448364 -15.798896874316208, -47.86114454269409 -15.798633623993734))")
         }

         "a multiPolygon" in {
            val multiPolygon = load("test-data" / "multipolygon-01.json")
               .asString.parseJson.convertTo[Geometry]

            multiPolygon.toText() should be ("MULTIPOLYGON (((-47.86117672920226 -15.798520064925436, -47.86133766174316 -15.79903624199543, -47.860227227211 -15.799371756385344, -47.86117672920226 -15.798520064925436), (-47.86114454269409 -15.798633623993734, -47.86126792430877 -15.7990155949379, -47.860833406448364 -15.798896874316208, -47.86114454269409 -15.798633623993734)), ((-47.86114186048508 -15.79848393248126, -47.860208451747894 -15.799338204971368, -47.860406935214996 -15.79858458713114, -47.86114186048508 -15.79848393248126), (-47.860822677612305 -15.798646528429275, -47.8604793548584 -15.798982043464918, -47.86043643951416 -15.798607815120159, -47.860822677612305 -15.798646528429275)))")
         }

         "a geometryCollection" in {
            val geometryCollection = load("test-data" / "geometrycollection-01.json")
               .asString.parseJson.convertTo[Geometry]

            geometryCollection.toText() should be ("GEOMETRYCOLLECTION (POINT (-56.789 -23.456), MULTIPOINT ((12.345 -54.321), (55.443 44.332), (-12.987 -43.987)), LINESTRING (12.345 -12.345, 54.321 54.321, -56.789 -23.456, 11.789 22.666, -56.001 34.123), POLYGON ((-47.86117672920226 -15.798520064925436, -47.86133766174316 -15.79903624199543, -47.860227227211 -15.799371756385344, -47.86117672920226 -15.798520064925436), (-47.86114454269409 -15.798633623993734, -47.86126792430877 -15.7990155949379, -47.860833406448364 -15.798896874316208, -47.86114454269409 -15.798633623993734)), MULTIPOLYGON (((-47.86117672920226 -15.798520064925436, -47.86133766174316 -15.79903624199543, -47.860227227211 -15.799371756385344, -47.86117672920226 -15.798520064925436), (-47.86114454269409 -15.798633623993734, -47.86126792430877 -15.7990155949379, -47.860833406448364 -15.798896874316208, -47.86114454269409 -15.798633623993734)), ((-47.86114186048508 -15.79848393248126, -47.860208451747894 -15.799338204971368, -47.860406935214996 -15.79858458713114, -47.86114186048508 -15.79848393248126), (-47.860822677612305 -15.798646528429275, -47.8604793548584 -15.798982043464918, -47.86043643951416 -15.798607815120159, -47.860822677612305 -15.798646528429275))), MULTILINESTRING ((0 1.5, 2 3, 3 3.5, -0.3 0), (0 0, 0.9 1, 2 2), (-4 -1, -1 -4, 6 7)))")
         }
      }

      "should write" - {
         "a point" in {
            val json = load("test-data" / "point-01.json").asString.parseJson
            val point = json.convertTo[Geometry]
            point.toJson should be (json)
         }

         "a multiPoint" in {
            val json = load("test-data" / "multipoint-01.json").asString.parseJson
            val multiPoint = json.convertTo[Geometry]
            multiPoint.toJson should be (json)
         }

         "a lineString" in {
            val json = load("test-data" / "linestring-01.json").asString.parseJson
            val lineString = json.convertTo[Geometry]
            lineString.toJson should be (json)
         }

         "a multiLineString" in {
            val json = load("test-data" / "multilinestring-01.json").asString.parseJson
            val multiLineString = json.convertTo[Geometry]
            multiLineString.toJson should be (json)
         }

         "a polygon" in {
            val json = load("test-data" / "polygon-01.json").asString.parseJson
            val polygon = json.convertTo[Geometry]
            polygon.toJson should be (json)
         }

         "a multiPolygon" in {
            val json = load("test-data" / "multipolygon-01.json").asString.parseJson
            val multiPolygon = json.convertTo[Geometry]
            multiPolygon.toJson should be (json)
         }

         "a geometryCollection" in {
            val json = load("test-data" / "geometrycollection-01.json").asString.parseJson
            val geometryCollection = json.convertTo[Geometry]
            geometryCollection.toJson should be (json)
         }
      }
   }
}
