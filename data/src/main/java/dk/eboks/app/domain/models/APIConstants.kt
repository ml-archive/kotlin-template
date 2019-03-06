package dk.eboks.app.domain.models

object APIConstants {
    // general message->status
    const val MSG_STATUS_AVAILABLE = 1
    const val MSG_STATUS_SIGNING_AVAILABLE = 2
    const val MSG_STATUS_PAYMENT_AVAILABLE = 3

    // regarding locked mails (message->lockStatus)
    const val MSG_LOCKED_REQUIRES_NEW_AUTH =
        1 // Message locked, requires new authentication
    const val MSG_LOCKED_REQUIRES_HIGHER_SEC_LVL =
        2 // Message locked, requires higher security level
    const val MSG_LOCKED_REQUIRES_HIGHER_IDP_LVL =
        3 // Message locked, requires higher IDP security level (MinID>BankID)
    const val MSG_LOCKED_REQUIRES_PUBLIC_IDP =
        4 // Message locked, requires public authority IDP
    const val MSG_LOCKED_REQUIRES_PUBLIC_IDP2 =
        5 // Message locked, requires public authority IDP
    const val MSG_LOCKED_WEB_ONLY =
        6 // Message locked, must be opened using the webclient

    // regarding reply
    const val MSG_REPLY_WEB_ONLY = 1 // Reply option available (on web only)
    const val MSG_REPLY_AVAILABLE = 2 // Reply option available
    const val MSG_REPLY_LOCKED =
        3 // Reply locked, requires higher security level

    // regarding payment
    const val MSG_PAY_WEB_ONLY =
        1 // Payment option available (available on web)
    const val MSG_PAY_AVAILABLE =
        2 // Payment option available (if Betalingsstatus = Z, J, C, A)
    const val MSG_PAY_PENDING = 3 // Payment pending
    const val MSG_PAY_PAID = 4 // Payment paid/registered
    const val MSG_PAY_OTHER = 5 // Payment other

    // regarding signing
    const val MSG_SIGN_NONE = 0 // no signing option available
    const val MSG_SIGN_WEB_ONLY = 1 // Sign option available (on web only)
    const val MSG_SIGN_AVAILABLE = 2 // Sign option available
    const val MSG_SIGN_SIGNED = 3 // Signed
    const val MSG_SIGN_OTHER = 4 // Other (who knows)
    // regarding hobbits (just kidding)

    // regarding channel status
    const val CHANNEL_STATUS_AVAILABLE = 0 // Channel is accessible
    const val CHANNEL_STATUS_REQUIRES_VERIFIED =
        1 // Channel not available, requires verified user
    const val CHANNEL_STATUS_REQUIRES_HIGHER_SEC_LEVEL =
        2 // Channel not available, requires higher security level (3)
    const val CHANNEL_STATUS_REQUIRES_HIGHER_SEC_LEVEL2 =
        3 // Channel not available, requires higher security level (50)
    const val CHANNEL_STATUS_REQUIRES_NEW_VERSION =
        4 // Channel not supported, requires a newer app version
    const val CHANNEL_STATUS_UNAVAILABLE =
        5 // Channel not supported, no longer available
    const val CHANNEL_STATUS_NOT_SUPPORTED =
        6 // Channel not supported (available on web only)

    const val STOREBOX_PROFILE_NOT_REGISTERED = 10160
    const val STOREBOX_PROFILE_INVALID = 10161
    const val STOREBOX_PROFILE_EXISTS = 10162
    const val STOREBOX_PROFILE_USER_NOT_FOUND = 10163
    const val STOREBOX_PROFILE_INVALID_INPUT = 10164
    const val STOREBOX_PROFILE_WRONG_CODE = 10165
}