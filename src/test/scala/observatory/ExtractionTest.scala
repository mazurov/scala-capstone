package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import Extraction._

trait ExtractionTest extends FunSuite {
  test("contains is implemented") {
    for (year <- 1975 to 2015) {
      println(s"Process $year")
      val res = locateTemperatures( year, "/stations.csv", s"/$year.csv" )
      val res1 = locationYearlyAverageRecords( res )

      println(res.size)
      println(res1.size)
    }
  }
  
}