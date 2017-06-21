package layer

/**
  * Created by Phuti Rapheeha on 2017/06/18.
  */
package object domain {
  case class DeviceLocation (
                              data: String,
                              ttl: String,
                              published_at: String,
                              coreid: String  )
}
