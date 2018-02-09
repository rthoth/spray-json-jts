package test

import com.vividsolutions.jts.geom.GeometryFactory
import java.io.File
import org.scalatest.{ FreeSpec, Matchers }
import scala.io.Source
import scala.language.implicitConversions

object Spec {
   val GeometryFactory = new GeometryFactory()
}

abstract class Spec extends FreeSpec with Matchers {

   implicit val GeometryFactory = Spec.GeometryFactory

   implicit class FileAs(file: File) {
      def asString: String = {
         Source.fromFile(file).mkString
      }
   }

   implicit class StringPath(path: String) {
      def / (other: String): StringPath = new StringPath(path + File.separator + other)

      override def toString() = path
   }

   implicit def stringPathToString(path: StringPath) = path.toString()

   def load(path: String): File = new File(path)
}
