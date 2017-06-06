package layer.config

import com.typesafe.config.ConfigFactory
/**
  * Created by Phuti Rapheeha on 2017/06/06.
  */
object settings {
  private val config = ConfigFactory.load()

  object particleReaderConf {
    private val particleReaderGen = config.getConfig("particle")

    lazy val filepath = particleReaderGen.getString("file_path")
    lazy val deviceUrl = particleReaderGen.getString("device_url")
  }
}
