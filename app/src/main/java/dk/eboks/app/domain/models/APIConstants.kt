package dk.eboks.app.domain.models

object APIConstants {
    // general message->status
    val MSG_STATUS_AVAILABLE = 1
    val MSG_STATUS_SIGNING_AVAILABLE = 2
    val MSG_STATUS_PAYMENT_AVAILABLE = 3

    // regarding locked mails (message->lockStatus)
    val MSG_LOCKED_REQUIRES_HIGHER_SEC_LVL = 1              // Message locked, requires higher security level
    val MSG_LOCKED_REQUIRES_HIGHER_IDP_LVL = 2              // Message locked, requires higher IDP security level (MinID>BankID)
    val MSG_LOCKED_REQUIRES_OTHER_IDP = 3                   // Message locked, requires other IDP (ID-Porten)
    val MSG_LOCKED_UNTIL_ACCEPTED = 4                       // Message locked, information hidden until conditions accepted
    val MSG_LOCKED_WEB_ONLY = 5                             // Message locked (available on web)

    // regarding reply
    val MSG_REPLY_WEB_ONLY = 1                              // Reply option available (on web only)
    val MSG_REPLY_AVAILABLE = 2                             // Reply option available
    val MSG_REPLY_LOCKED = 3                                // Reply locked, requires higher security level

    // regarding payment
    val MSG_PAY_WEB_ONLY = 1                                // Payment option available (available on web)
    val MSG_PAY_AVAILABLE = 2                               // Payment option available (if Betalingsstatus = Z, J, C, A)
    val MSG_PAY_PENDING = 3                                 // Payment pending
    val MSG_PAY_PAID = 4                                    // Payment paid/registered
    val MSG_PAY_OTHER = 5                                   // Payment other

    // regarding signing
    val MSG_SIGN_WEB_ONLY = 1                               // Sign option available (on web only)
    val MSG_SIGN_AVAILABLE = 2                              // Sign option available
    val MSG_SIGN_SIGNED = 3                                 // Signed
    val MSG_SIGN_OTHER = 4                                  // Other (who knows)
    // regarding hobbits (just kidding)


    // regarding channel status
    val CHANNEL_STATUS_AVAILABLE = 0                        // Channel is accessible
    val CHANNEL_STATUS_REQUIRES_VERIFIED = 1                // Channel not available, requires verified user
    val CHANNEL_STATUS_REQUIRES_HIGHER_SEC_LEVEL = 2        // Channel not available, requires higher security level (3)
    val CHANNEL_STATUS_REQUIRES_HIGHER_SEC_LEVEL2 = 3       // Channel not available, requires higher security level (50)
    val CHANNEL_STATUS_REQUIRES_NEW_VERSION = 4             // Channel not supported, requires a newer app version
    val CHANNEL_STATUS_UNAVAILABLE = 5                      // Channel not supported, no longer available
    val CHANNEL_STATUS_NOT_SUPPORTED = 6                    // Channel not supported (available on web only)

    val STOREBOX_PROFILE_NOT_REGISTERED = 10160
    val STOREBOX_PROFILE_INVALID = 10161
    val STOREBOX_PROFILE_EXISTS = 10162
    val STOREBOX_PROFILE_USER_NOT_FOUND = 10163
    val STOREBOX_PROFILE_INVALID_INPUT = 10164
    val STOREBOX_PROFILE_WRONG_CODE = 10165
}