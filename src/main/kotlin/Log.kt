import com.intellij.openapi.diagnostic.Logger

/**
 * Created by Rammer on 06/02/2017.
 */
object Log {

    private val LOGGER = Logger.getInstance("ResourceDeprecater")

    fun className(msg: String, obj: Any?) {

        if (obj == null) {
            LOGGER.info("instance == null -> $msg")
        } else {
            LOGGER.info(msg + ": " + obj.javaClass + " -> " + obj)
        }
    }

    fun info(msg: String) {
        LOGGER.info(msg)
    }

    fun error(msg: String) {
        LOGGER.warn(msg)
    }
}
