package layer.config

import com.typesafe.config.ConfigFactory

object Settings {

    private val config = ConfigFactory.load()

    object ParticleReaderGen {

        private val particleReaderGen = config.getConfig("particle")

        lazy val filepath: String = particleReaderGen.getString("file_path")
        lazy val deviceUrl: String = particleReaderGen.getString("device_url")
    }
}
