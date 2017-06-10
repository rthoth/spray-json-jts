package test

import com.vividsolutions.jts.geom.Geometry
import spray.json._
import br.ufla.spray.json.GeoJSON._

class BasicTest extends Spec {

   "The GeoJSON" - {
      "should to read" - {
         "a point" in {
            val point = load("test-data" / "point-01.json")
               .asString.parseJson.convertTo[Geometry]

            point.toText() shouldBe "POINT (-56.789 -23.456)"
         }

         "a multiPoint" in {
            val multiPoint = load("test-data" / "multipoint-01.json")
               .asString.parseJson.convertTo[Geometry]

            multiPoint.toText() shouldBe "MULTIPOINT ((12.345 -54.321), (55.443 44.332), (-12.987 -43.987))"
         }

         "a lineString" in {
            val lineString = load("test-data" / "linestring-01.json")
               .asString.parseJson.convertTo[Geometry]

            lineString.toText() shouldBe "LINESTRING (12.345 -12.345, 54.321 54.321, -56.789 -23.456, 11.789 22.666, -56.001 34.123)"
         }

         "a multiLineString" in {
            val multiLineString = load("test-data" / "multilinestring-01.json")
               .asString.parseJson.convertTo[Geometry]

            multiLineString.toText() should be equals("MULTISTRING ()")
         }

         "a polygon" in {
            val polygon = load("test-data" / "polygon-01.json")
               .asString.parseJson.convertTo[Geometry]

            polygon.toText() shouldBe "POLYGON ((12.345 23.456, 32.654 54.321, 33.778 44.991, 12.345 23.456), (44.333 11.222, 58.991 -45.123, -9.897 -1.234, 44.333 11.222))"
         }

         "a multiPolygon" in {
            val multiPolygon = load("test-data" / "multipolygon-01.json")
               .asString.parseJson.convertTo[Geometry]

            multiPolygon.toText() shouldBe "MULTIPOLYGON (((1 1, 2 2, 12.22 23.11, 5.5 6.6, 1 1), (1.5 1.5, 2.5 2.5, 12.225 23.115, 5.55 6.65, 1.5 1.5)), ((7 7, 9.5 9.5, -12.45 -23.56, 7 7), (3 3, -1 -1, -2 5, 3 3)))"
         }

         "a geometryCollection" in {
            val geometryCollection = load("test-data" / "geometrycollection-01.json")
               .asString.parseJson.convertTo[Geometry]

            geometryCollection.toText() shouldBe "GEOMETRYCOLLECTION (POINT (-56.789 -23.456), MULTIPOINT ((12.345 -54.321), (55.443 44.332), (-12.987 -43.987)), LINESTRING (12.345 -12.345, 54.321 54.321, -56.789 -23.456, 11.789 22.666, -56.001 34.123), POLYGON ((12.345 23.456, 32.654 54.321, 33.778 44.991, 12.345 23.456), (44.333 11.222, 58.991 -45.123, -9.897 -1.234, 44.333 11.222)), MULTIPOLYGON (((1 1, 2 2, 12.22 23.11, 5.5 6.6, 1 1), (1.5 1.5, 2.5 2.5, 12.225 23.115, 5.55 6.65, 1.5 1.5)), ((7 7, 9.5 9.5, -12.45 -23.56, 7 7), (3 3, -1 -1, -2 5, 3 3))))"
         }
      }

      "should to write" - {
         "a point" in {
            val json = load("test-data" / "point-01.json").asString.parseJson
            val point = json.convertTo[Geometry]
            point.toJson should be equals(json)
         }

         "a multiPoint" in {
            val json = load("test-data" / "multipoint-01.json").asString.parseJson
            val multiPoint = json.convertTo[Geometry]
            multiPoint.toJson should be equals(json)
         }

         "a lineString" in {
            val json = load("test-data" / "linestring-01.json").asString.parseJson
            val lineString = json.convertTo[Geometry]
            lineString.toJson should be equals(json)
         }

         "a multiLineString" in {
            val json = load("test-data" / "multilinestring-01.json").asString.parseJson
            val multiLineString = json.convertTo[Geometry]
            multiLineString.toJson should be equals(json)
         }

         "a polygon" in {
            val json = load("test-data" / "polygon-01.json").asString.parseJson
            val polygon = json.convertTo[Geometry]
            polygon.toJson should be equals(json)
         }

         "a multiPolygon" in {
            val json = load("test-data" / "multipolygon-01.json").asString.parseJson
            val multiPolygon = json.convertTo[Geometry]
            multiPolygon.toJson should be equals(json)
         }

         "a geometryCollection" in {
            val json = load("test-data" / "geometrycollection-01.json").asString.parseJson
            val geometryCollection = json.convertTo[Geometry]
            geometryCollection.toJson should be equals(json)
         }
      }
   }
}
