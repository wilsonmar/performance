package performance.simulations.lib

import java.lang._

/**
  * Created by Tarun Kale.
  */


object JenkinsParam {

  //val TEST_URL : String = sys.env.getOrElse("TEST_URL","http://ngp-perf-zuulapp.makanaplatform.com")
  val TEST_URL : String = sys.env.getOrElse("TEST_URL","http://ngp-perf-incaapp.herokuapp.com")
  val peakRPS : Double =  Double.parseDouble( getSetting("peakRPS","1"))

  val rampTime : Int = Integer.parseInt(sys.env.getOrElse("rampTime","120"))
  val steadyTime: Int = Integer.parseInt(sys.env.getOrElse("steadyTime","900"))

  val meanResponseTime: Int = Integer.parseInt( getSetting("meanResponseTime", "1000"))
  val response95th: Int = Integer.parseInt( getSetting("response95th", "1200"))
  val response99th: Int = Integer.parseInt( getSetting("response99th", "1400"))

  val errorRate : Int =  Integer.parseInt(getSetting("errorRate", "1"))


  System.out.println("rampTime  :" +rampTime )
  System.out.println("steadyTime  :" +steadyTime )


  /**
   * Retrieves Jenkins environment variables from bash shell of ANY non-null type.
   * If no Jenkins input is detected, it uses default
   * @param settingKey
   * @param defaultValue
   * @return
   */
  private def getSetting(settingKey: String, defaultValue: String): String = {
    System.getenv(settingKey) match {
      case x: Any =>
        println( "export " + settingKey + "=" + x)
        x
      case _ =>
        println( "export " + settingKey + "=" + defaultValue.toString + "\t\t## using fallback")
        defaultValue
    }
  }

}


