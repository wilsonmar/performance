package performance.simulations.lib

/**
 * Created by Tarun Kale.
 */


import io.gatling.core.Predef._

import scala.collection.immutable.Map
import scala.concurrent.forkjoin.ThreadLocalRandom
import scala.math._

object CommonHeader {



  val common_header =  Map("Accept-Language" -> "en-us,en;q=0.5"
    ,"Accept-Charset" -> "ISO-8859-1,utf-8;q=0.7,*;q=0.7"
    ,"Accept-Encoding" -> "gzip,deflate"
    ,"User-Agent" -> "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.5; en-US; rv:1.9.1) Gecko/20090624 Firefox/3.5"
    ,"Keep-Alive" -> "300"
    ,"Connection" -> "close"
    ,"Accept" -> "application/json"
    ,"Content-Type" -> "application/json")

  /*
    def getValue () : Map = {
      return common_header
    }
  */

  val timestampFeeder = new Feeder[String] {
    override def hasNext = true

    override def next: Map[String, String] = {

      Map("getTime" -> new java.text.SimpleDateFormat("yyyyMMdd'T'hhmmssms").format(new java.util.Date(System.currentTimeMillis() - 360000)))


    }
  }

  val timestampFeeder2 = new Feeder[String] {
    override def hasNext = true

    override def next: Map[String, String] = {

      Map("getTimeinMS" -> (System.currentTimeMillis() ).toString)


    }
  }
  val userIdFeeder = new Feeder[String] {
    // always return true as this feeder can be polled infinitely
    override def hasNext = true

    override def next: Map[String, String] = {
      Map("randomUserId" -> abs(ThreadLocalRandom.current().nextLong(100000000000L)).toString)
    }
  }


  val userIdFeeder1 = new Feeder[String] {
    // always return true as this feeder can be polled infinitely
    override def hasNext = true

    override def next: Map[String, String] = {
      Map("randomUserId1" -> System.nanoTime().toString)
    }
  }


}