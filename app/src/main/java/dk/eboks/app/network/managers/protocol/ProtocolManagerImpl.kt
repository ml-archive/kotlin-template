package dk.eboks.app.network.managers.protocol

import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.managers.ProtocolManager
import dk.eboks.app.domain.models.protocol.UserInfo
import org.apache.commons.lang3.time.FastDateFormat
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.experimental.and

/**
 * Created by bison on 08/12/17.
 */
class ProtocolManagerImpl() : ProtocolManager {
    override val loggedIn: Boolean
        get() = sessionData.sessionId.isNotBlank()
    override val sessionData : SessionData = SessionData()
    override var userInfo: UserInfo = UserInfo()
    lateinit internal var messageDigest: MessageDigest
    internal val DATEFORMAT_LOGIN = "yyyy-MM-dd hh:mm:ss"
    internal val TIMEZONE_LOGIN = "Z"


    override fun init(deviceId : String)
    {
        sessionData.deviceId = deviceId
        try {
            messageDigest = MessageDigest.getInstance("SHA-256")
        }
        catch (e : NoSuchAlgorithmException)
        {
            Timber.e(e)
        }
        Timber.e("Initializing ProtocolManager\n\nDeviceId = ${sessionData.deviceId}")
    }

    override fun buildEboksHeader(): String {

        /*
        val header = if(!loggedIn)
            "logon deviceid=\"${sessionData.deviceId}\", datetime=\"%s\", challenge=\"%s\""
        else
            ""
            */
        return ""
    }

    override fun generateChallenge(datetime : String): String {

        val builder = StringBuilder()

        val country = Config.currentMode.countryCode
        val deviceId = sessionData.deviceId

        builder
                .append(userInfo.activationCode)
                .append(":")
                .append(deviceId)
                .append(":")
                .append("P")
                .append(":")
                .append(userInfo.identity)
                .append(":")
                .append(country)
                .append(":")
                .append(userInfo.pincode)
                .append(":")
                .append(datetime)

        //Log.d(TAG, "generateChallenge: " + builder.toString());
        //Log.d(TAG,"hashedChallenge  :" + challengeReturnValue);
        return generateDoubleHash(builder)
    }

    fun generateDoubleHash(builder: StringBuilder): String {
        messageDigest.reset()
        messageDigest.update(builder.toString().toByteArray())
        // Do the double hash here to get the final value
        val challengeByteArray = messageDigest.digest()
        messageDigest.reset()
        // do double hash
        // Convert the byte array to string and then get the bytes from it
        // If the same byte array is used directly it does not give correct
        // values as expected for double hash
        val challengeString = getStringFromBytes(challengeByteArray)
        //Log.d(TAG, "generateDoubleHash/ challengeString - " + challengeString);
        messageDigest.update(challengeString.toByteArray())
        // get the final value
        val challengePart2 = messageDigest.digest()
        return getStringFromBytes(challengePart2)
    }

    /**
     * Used to get the string value from a hex byte array.
     *
     * @param byteArray
     * @return String
     */
    private fun getStringFromBytes(byteArray: ByteArray): String {
        val buffer = StringBuffer()
        for (element in byteArray) {
            buffer.append(Integer.toString((element.toShort() and 0xff) + 0x100, 16).substring(1))
        }
        return buffer.toString()
    }

    /*
        Made this to alleviate a login problem (hopefully) where the timestamp was corrupted
        sometimes on some models
     */
    override fun getDateTime(): String {
        val formatter = FastDateFormat.getInstance(DATEFORMAT_LOGIN, Locale.US)
        return formatter.format(Date()) + TIMEZONE_LOGIN
    }

    override fun parseNonce(header: String): String {
        // extract nonce with even more ugly old string manipulation
        val nonce_start = header.indexOf("nonce=\"") + 7
        val nonce_end = header.indexOf("\"", nonce_start)
        val nonce = header.substring(nonce_start, nonce_end)
        return nonce
    }

    override fun parseNonceAndSessionId(header: String) : Pair<String, String> {
        // extract session id with some ugly old string manipulation
        val ses_id_start = header.indexOf("sessionid=\"") + 11
        val ses_id_end = header.indexOf("\"", ses_id_start)
        val sessionId = header.substring(ses_id_start, ses_id_end)

        // extract nonce with even more ugly old string manipulation
        val nonce_start = header.indexOf("nonce=\"") + 7
        val nonce_end = header.indexOf("\"", nonce_start)
        val nonce = header.substring(nonce_start, nonce_end)
        return Pair(nonce, sessionId)
    }

    /**
     * used for generating the response code that is part of the header for all
     * calls post login Response code is calculated as
     * response=î{##(activationcode:deviceid:nonce:sessionid)} the nonce value
     * is stored in the object.
     *
     * @param activationCode
     * @return
     */
    override fun generateResponseCode(): String {
        val deviceId = sessionData.deviceId
        val builder = StringBuilder()

        builder
                .append(userInfo.activationCode)
                .append(":")
                .append(deviceId)
                .append(":")
                .append(sessionData.nonce)
                .append(":")
                .append(sessionData.sessionId)
        //Log.d(TAG, "generateResponseCode/ Response - " + builder.toString());
        //Log.d(TAG, "generateResponseCode/ Response1 - " + responseCode);
        return generateDoubleHash(builder)
    }

}