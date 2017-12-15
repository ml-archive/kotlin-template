package dk.eboks.app.network.rest.util;

/**
 * Created by bison on 10/08/15.
 */

// TODO probably no good reason this is a singleton. Make a member on BackendService

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dk.eboks.app.domain.config.Config;


/**
 * This class generate a <code>nounce</code> which is used to validate our
 * request at server level.
 */
public final class NonceGenerator
{

    private static final String COLON_SEPERATOR = ":";
    /**
     * Represents the singleton <code>NonceGenerator</code>.
     */
    private static NonceGenerator generator;
    private static final String SHA_ALGORITHM = "SHA-256";
    /**
     * Keyword used while logging comments for debugging.
     */
    private static final String TAG = NonceGenerator.class.getSimpleName();

    /**
     * Gets a <code>NonceGenerator</code>.
     *
     * @return the <code>NonceGenerator</code>.
     */
    public static synchronized NonceGenerator getInstance()
    {
        try
        {
            if(generator == null)
            {
                generator = new NonceGenerator();
            }
        }
        catch(final NoSuchAlgorithmException algorithmException)
        {
            // ignore.
        }
        return generator;
    }

    /**
     * Represents the <code>MessageDigest</code> which is used for calculating
     * the hashnonce.
     */
    private final MessageDigest messageDigest;

    /**
     * Private Constructor, to avoid creation of <code>NonceGenerator</code>
     * instance outside this class.
     *
     * @throws NoSuchAlgorithmException should be never thrown.
     */
    private NonceGenerator() throws NoSuchAlgorithmException
    {
        // singleton
        messageDigest = MessageDigest.getInstance(SHA_ALGORITHM);
    }

    public String generateChallenge(char[] activationCode,
                                    String identity,
                                    String pinCode, final String dateTime)
    {

        final StringBuilder builder = new StringBuilder();

        String country = Config.currentMode.getCountryCode();
        String deviceId = DeviceUtils.getInstance().getDeviceId();

        builder
                .append(activationCode)
                .append(":")
                .append(deviceId)
                .append(":")
                .append("P")
                .append(":")
                .append(identity)
                .append(":")
                .append(country)
                .append(":")
                .append(pinCode)
                .append(":")
                .append(dateTime);

        //Log.d(TAG, "generateChallenge: " + builder.toString());
        final String challengeReturnValue = NonceGenerator.getInstance().generateDoubleHash(builder);
        //Log.d(TAG,"hashedChallenge  :" + challengeReturnValue);
        return challengeReturnValue;
    }

    public String generateSSOChallenge(String ticketType, String ticket, final String dateTime)
    {
        final StringBuilder builder = new StringBuilder();
        String deviceId = DeviceUtils.getInstance().getDeviceId();
        builder.append(":").append(deviceId).append(":").append(
                ticketType).append(":").append(ticket).append(":").append(dateTime);
        //Log.d(TAG, "generateChallenge: " + builder.toString());
        final String challengeReturnValue = NonceGenerator.getInstance().generateDoubleHash(builder);
        //Log.d(TAG,"hashedChallenge  :" + challengeReturnValue);
        return challengeReturnValue;
    }

    public String generateDoubleHash(final StringBuilder builder)
    {
        messageDigest.reset();
        messageDigest.update(builder.toString().getBytes());
        // Do the double hash here to get the final value
        final byte[] challengeByteArray = messageDigest.digest();
        messageDigest.reset();
        // do double hash
        // Convert the byte array to string and then get the bytes from it
        // If the same byte array is used directly it does not give correct
        // values as expected for double hash
        final String challengeString = getStringFromBytes(challengeByteArray);
        //Log.d(TAG, "generateDoubleHash/ challengeString - " + challengeString);
        messageDigest.update(challengeString.getBytes());
        // get the final value
        final byte[] challengePart2 = messageDigest.digest();
        return getStringFromBytes(challengePart2);
    }

    /**
     * used for generating the response code that is part of the header for all
     * calls post login Response code is calculated as
     * response=î{##(activ ationcode:deviceid:nonce:sessionid)} the nonce value
     * is stored in the object.
     *
     * @param activationCode
     * @return
     */
    public String generateResponseCode(char[] activationCode, String sessionId, String nonce)
    {
        String deviceId = DeviceUtils.getInstance().getDeviceId();
        final StringBuilder builder = new StringBuilder();

        builder
                .append(activationCode)
                .append(COLON_SEPERATOR)
                .append(deviceId)
                .append(COLON_SEPERATOR)
                .append(nonce)
                .append(COLON_SEPERATOR)
                .append(sessionId);
        //Log.d(TAG, "generateResponseCode/ Response - " + builder.toString());
        final String responseCode = generateDoubleHash(builder);
        //Log.d(TAG, "generateResponseCode/ Response1 - " + responseCode);
        return responseCode;
    }

    public String generateResponseCodeSSO(String sessionId, String nonce)
    {
        String deviceId = DeviceUtils.getInstance().getDeviceId();
        final StringBuilder builder = new StringBuilder();

        builder.append(COLON_SEPERATOR).append(
                deviceId).append(COLON_SEPERATOR).append(
                nonce).append(COLON_SEPERATOR).append(sessionId);
        //Log.d(TAG, "generateResponseCode/ Response - " + builder.toString());
        final String responseCode = generateDoubleHash(builder);
        //Log.d(TAG, "generateResponseCode/ Response1 - " + responseCode);
        return responseCode;
    }

    /**
     * Gets the hashnonce which can be used for the next request. The specified
     * application code is appended with the nonce known to this generator.
     *
     * @param applicationCode the application code stored in the device.
     *
     * @return the hashnonce to use for the next request.
     *
     */
    public String getHashNonce(final String applicationCode, String nonce)
    {
        // Gets the previus nonce and appends it with the application code
        final String valueToHash = nonce + ":" + applicationCode;
        final byte[] nonceBytes = getHashNonceBytes(valueToHash);
        // convert the byte to hex format method 1
        final StringBuffer hexBuffer = new StringBuffer();
        for(final byte nonceByte: nonceBytes)
        {
            hexBuffer.append(Integer.toString((nonceByte & 0xff) + 0x100, 16).substring(1));
        }
        return hexBuffer.toString();
    }

    private byte[] getHashNonceBytes(final String valueToHash)
    {
        messageDigest.reset();
        messageDigest.update(valueToHash.getBytes());
        return messageDigest.digest();
    }

    /**
     * Used to get the string value from a hex byte array.
     *
     * @param byteArray
     * @return String
     */
    private String getStringFromBytes(final byte[] byteArray)
    {
        final StringBuffer buffer = new StringBuffer();
        for(final byte element: byteArray)
        {
            buffer.append(Integer.toString((element & 0xff) + 0x100, 16).substring(
                    1));
        }
        return buffer.toString();
    }

}
