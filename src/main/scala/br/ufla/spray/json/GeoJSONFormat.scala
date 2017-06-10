package br.ufla.spray.json

import com.vividsolutions.jts.geom._
import spray.json._
import scala.collection.immutable._

class GeoJSONFormat(factory: GeometryFactory) extends JsonFormat[Geometry] {

   override def read(json: JsValue): Geometry = {
      json match {
         case JsObject(fields) =>
            fields.get("type") match {
               case Some(JsString(geometryType)) =>
                  readGeometry(geometryType, fields)

               case Some(JsNull) | None =>
                  throw new DeserializationException("Type attribute must be definied!")

               case _ =>
                  throw new DeserializationException("Invalid type attribute")
            }

         case JsNull =>
            null

         case _ =>
            throw new DeserializationException("Expected an object!")
      }
   }

   def readCoordinate(elements: Vector[JsValue]): Coordinate = {
      val x = elements(0) match {
         case JsNumber(value) => value.doubleValue()
         case _ => throw new DeserializationException(s"Unexpected value ${elements(0)}!")
      }

      val y = elements(1) match {
         case JsNumber(value) => value.doubleValue()
         case _ => throw new DeserializationException(s"Unexpected value ${elements(1)}!")
      }

      new Coordinate(x, y)
   }

   def readCoordinates(elements: Vector[JsValue]): Array[Coordinate] = {
      val coordinates = Array.ofDim[Coordinate](elements.size)

      for ((element, i) <- elements.zipWithIndex) element match {
         case JsArray(coords) => coordinates(i) = readCoordinate(coords)
         case _ => throw new DeserializationException(s"Unexpected value $element!")
      }

      coordinates
   }

   def readGeometry(geometryType: String, fields: Map[String, JsValue]): Geometry = {
      geometryType.toUpperCase() match {
         case "POINT" =>
            readPoint(fields)

         case "MULTIPOINT" =>
            readMultiPoint(fields)

         case "LINESTRING" =>
            readLineString(fields)

         case "MULTILINESTRING" =>
            readMultiLineString(fields)

         case "POLYGON" =>
            readPolygon(fields)

         case "MULTIPOLYGON" =>
            readMultiPolygon(fields)

         case "GEOMETRYCOLLECTION" =>
            readGeometryCollection(fields)
      }
   }

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

   def readLineString(fields: Map[String, JsValue]): LineString = {
      fields.get("coordinates") match {
         case Some(JsArray(elements)) =>
            factory.createLineString(readCoordinates(elements))

         case Some(value) =>
            throw new DeserializationException(s"Unexpected value $value!")

         case _ =>
            throw new DeserializationException("Expected an array!")
      }
   }

   def readMultiLineString(fields: Map[String, JsValue]): MultiLineString = {
      fields.get("coordinates") match {
         case Some(JsArray(elements)) =>
            val lineStrings = for (element <- elements) yield {
               element match {
                  case JsArray(coordinates) => factory.createLineString(readCoordinates(coordinates))
                  case _ => throw new DeserializationException(s"Unexpected value $element!")
               }
            }

            factory.createMultiLineString(lineStrings.toArray)

         case Some(value) =>
            throw new DeserializationException(s"Unexpected value $value!")

         case _ =>
            throw new DeserializationException("Expected a array!")
      }
   }

   def readMultiPoint(fields: Map[String, JsValue]): MultiPoint = {
      fields.get("coordinates") match {
         case Some(JsArray(elements)) =>
            factory.createMultiPoint(readCoordinates(elements))

         case Some(value) =>
            throw new DeserializationException(s"Unexpected value $value!")

         case _ =>
            throw new DeserializationException("Expected an array!")
      }
   }

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

   def readPoint(fields: Map[String, JsValue]): Point = {
      fields.get("coordinates") match {
         case Some(JsArray(elements)) =>
            factory.createPoint(readCoordinate(elements))

         case Some(other) =>
            throw new DeserializationException(s"Unexpected value $other!")

         case _ =>
            throw new DeserializationException("Expected an array!")
      }
   }

   def readPolygon(fields: Map[String, JsValue]): Polygon = {
      fields.get("coordinates") match {
         case Some(JsArray(elements)) =>
            val shell = elements.head match {
               case JsArray(coordinates) =>
                  factory.createLinearRing(readCoordinates(coordinates))

               case value =>
                  throw new DeserializationException(s"Unexpected value $value!")
            }

            val holes = for (element <- elements.tail) yield {
               element match {
                  case JsArray(coordinates) => factory.createLinearRing(readCoordinates(coordinates))
                  case _ => throw new DeserializationException(s"Unexpected value $element!")
               }
            }

            factory.createPolygon(shell, holes.toArray)

         case Some(value) =>
            throw new DeserializationException(s"Unexpected value $value!")

         case _ =>
            throw new DeserializationException(s"Expected an array!")
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

   def writeCoordinate(coordinate: Coordinate): JsArray = {
      JsArray(JsNumber(coordinate.x), JsNumber(coordinate.y))
   }

   def writeCoordinates(coordinates: Array[Coordinate]): JsArray = {
      JsArray(coordinates.map(writeCoordinate): _*)
   }

   def writeGeometryCollection(geometryCollection: GeometryCollection): JsObject = {
      JsObject(
         "type" -> JsString("GeometryCollection"),
         "geometries" -> JsArray(
            (for (i <- 0 until geometryCollection.getNumGeometries) yield write(geometryCollection.getGeometryN(i))): _*
         )
      )
   }

   def writePoint(point: Point): JsObject = {
      JsObject(
         "type" -> JsString("Point"),
         "coordinates" -> writeCoordinate(point.getCoordinate)
      )
   }

   def writeMultiPoint(multiPoint: MultiPoint): JsObject = {
      JsObject(
         "type" -> JsString("MultiPoint"),
         "coordinates" -> writeCoordinates(multiPoint.getCoordinates)
      )
   }

   def writeLineString(lineString: LineString): JsObject = {
      JsObject(
         "type" -> JsString("LineString"),
         "coordinates" -> writeCoordinates(lineString.getCoordinates)
      )
   }

   def writeMultiLineString(multiLineString: MultiLineString): JsObject = {
      val coordinates = for (i <- 0 until multiLineString.getNumGeometries) yield {
         writeCoordinates(multiLineString.getGeometryN(i).getCoordinates)
      }

      JsObject(
         "type" -> JsString("MultiLineString"),
         "coordinates" -> JsArray(coordinates: _*)
      )
   }

   def writePolygon(polygon: Polygon): JsObject = {
      val shell = writeCoordinates(polygon.getExteriorRing.getCoordinates)
      val holes = for (i <- 0 until polygon.getNumInteriorRing) yield {
         writeCoordinates(polygon.getInteriorRingN(i).getCoordinates)
      }

      JsObject(
         "type" -> JsString("Polygon"),
         "coordinates" -> JsArray((Seq(shell) ++ holes): _*)
      )
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
         "type" -> JsString("MultiPolygon"),
         "coordinates" -> JsArray(polygons: _*)
      )
   }
}
