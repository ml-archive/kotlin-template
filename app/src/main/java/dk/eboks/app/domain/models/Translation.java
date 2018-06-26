package dk.eboks.app.domain.models;

/**
 * Created by nstack.io gradle translation plugin
 * Built from Accept Header: en-US 
 */

public class Translation {
	public final static class defaultSection {
		public static String ok = "Ok";
		public static String cancel = "Cancel";
		public static String yes = "Yes";
		public static String back = "Back";
		public static String today = "Today\r\n";
		public static String yesterday = "Yesterday";
		public static String save = "Save";
		public static String close = "Close";
		public static String no = "No";
		public static String delete = "Delete";
		public static String retry = "Try again";
		public static String edit = "Edit";
		public static String settings = "Settings";
		public static String later = "Later";
		public static String next = "Next";
		public static String previous = "Previous";
		public static String skip = "Skip";
	}
	public final static class error {
		public static String genericMessage = "An error occurred";
		public static String genericTitle = "Error";
		public static String noInternetTitle = "No connectivity";
		public static String noInternetMessage = "This application requires a connection to the internet to perform the requested operation.";
		public static String startupTitle = "Error";
		public static String startupMessage = "An initialization error happened during application startup. We apologize for the inconvenience.";
		public static String genericServerTitle = "Server error";
		public static String genericServerMessage = "An error occurred while communicating with the server. We apologize for the inconvenience.";
		public static String genericStorageTitle = "Storage error";
		public static String genericStorageMessage = "An error occured accessing the persistent storage on this device.";
		public static String genericOOMTitle = "Out of memory";
		public static String genericOOMMessage = "There is not enough free resources on this device to accomplish the requested operation. Try shutting down other apps to free up more resources.";
		public static String attachmentErrorTitle = "Could not open";
		public static String attachmentErrorMessage = "Document could not be opened, save to downloads instead?";
		public static String attachmentErrorSaveBtn = "Save";
		public static String attachmentErrorNegativeBtn = "Close";
		public static String attachmentOpenWith = "Open with";
		public static String emptyField = "Empty field";
		public static String invalidActivationCodeNrOfChars = "Invalid activation code. Must be [numberOfChars] chars.";
		public static String printReceiptErrorMessage = "Cannot print receipt";
		public static String forwardReceiptErrorMessage = "Cannot forward receipt";
		public static String timeoutTitle = "Timeout";
		public static String timeoutMessage = "The request timed out";
		public static String authenticationError = "Your session has expired. Please log on again.";
		public static String connectionError = "No or bad connection. Please try again.";
		public static String errorTitle = "Error";
		public static String unknownError = "Unkown error, please try again";
	}
	public final static class mail {
		public static String senderHeader = "Mail from your senders";
		public static String showAllSendersButton = "Show all";
		public static String smartFolderHeader = "Your mail";
		public static String foldersButton = "Folders";
		public static String sendersEmptyHeader = "No mails from your senders";
		public static String sendersEmptyMessage = "__sendersEmptyMessage";
		public static String addMoreSendersButton = "Add more senders";
		public static String noMessagesToDisplay = "There\'s no mail to display in this folder.";
		public static String allMail = "All mail";
		public static String unverifiedEmptyTitle = "Receive your mail digitally";
		public static String unverifiedEmptyMessage = "Receive your mail digitally from private companies and public authorities.\r\nGet started by verifying your profile.";
		public static String unverifiedEmptyButton = "Verify my profile";
	}
	public final static class mainnav {
		public static String homeButton = "Home";
		public static String mailButton = "Mail";
		public static String sendersButton = "Senders";
		public static String channelsButton = "Plus";
		public static String uploadsButton = "Uploads";
	}
	public final static class folders {
		public static String foldersHeader = "Folders";
		public static String searchBarPlaceholder = "Search";
		public static String topbarEdit = "Edit";
		public static String newFolderTitle = "Add new folder";
		public static String newfolderHint = "Folder name";
		public static String folder = "Folder";
		public static String editFolder = "Edit folder";
		public static String noFoldersHeader = "You have not added any folders";
		public static String noFoldersSubHeader = "You can create your own folders to organize your mails.";
		public static String noFoldersBtn = "Create a folder";
	}
	public final static class attachments {
		public static String lockedTitle = "Locked attachment";
		public static String lockedMessage = "__lockedMessage";
	}
	public final static class channels {
		public static String channelsHeader = "Get your relevant information and actions directly on your e-Boks home screen";
		public static String title = "Plus";
		public static String install = "Add";
		public static String open = "Open";
		public static String openChannel = "Open channel";
		public static String logOnWithPKI = "Log on with [pkiName]";
		public static String verifyYourProfile = "Verify your profile to access this channel";
		public static String channelNotAvailable = "[channelName] is not available for this device or no longer available as a channel";
		public static String installChannel = "Add";
		public static String updateProfile = "Update profile";
		public static String userSocialSecurityNumber = "Your Social Security Number";
		public static String userName = "Your name";
		public static String userEmailAdress = "Your email address";
		public static String userPhoneNumber = "Your phone number";
		public static String missing = "Missing";
		public static String drawerHeader = "Add service";
		public static String drawerSubHeader = "Let\u2019s help you to get started";
		public static String drawerHeaderText = "[channelname] requests access to the following information";
		public static String allowButton = "Allow";
		public static String drawerNeedsVerification = "Needs verification";
		public static String storeboxAlreadyHaveProfileButton = "I already have a Storebox profile";
	}
	public final static class senders {
		public static String title = "Senders";
		public static String registrations = "Registrations";
		public static String register = "Register";
		public static String signUpHere = "Sign up here";
		public static String senderCount = "[senderCount] senders";
		public static String allCategories = "All categories";
		public static String registered = "Registered";
		public static String registerAlertTitle = "You are about to register";
		public static String unregister = "Unregister";
		public static String unregisterAlertTitle = "Are you sure you want to unregister?";
		public static String registerAlertDescription = "__registerAlertDescription";
		public static String unregisterAlertDescription = "__unregisterAlertDescription";
		public static String cannotUnregister = "Cannot unregister";
		public static String cannotUnregisterDescription = "You are automatically registered mail from [sender] if this is part of your customer agreements or your conditions of employment";
		public static String cannotUnregisterPublicDescription = "You are automatically registered mail from the public sector if this is part of your customer agreements or your conditions of employment";
		public static String allSenders = "All Senders";
		public static String comingSoonHeader = "Coming soon!";
		public static String comingSoonMessage = "Soon you will be able to register senders you normally receive your mail from";
	}
	public final static class start {
		public static String signupButton = "Sign up";
		public static String addNewUser = "Log on another user";
		public static String logonButton = "Log on";
		public static String confimRemoveUserTitle = "Remove user";
		public static String confirmRemoveUserMessage = "Are you sure?";
	}
	public final static class logoncredentials {
		public static String topLabel = "Good to see you again. We missed you!";
		public static String topSublabel = "Please fill in your credentials below";
		public static String passwordfieldHeader = "Password";
		public static String placeholder = "Type here";
		public static String continueButton = "Continue";
		public static String forgotPasswordButton = "Forgot password";
		public static String title = "Log on";
		public static String invalidPassword = "Invalid password";
		public static String invalidCprorEmail = "Invalid email or Social Security number";
		public static String emailOrSSNHeader = "Email or Social Security Number";
		public static String ssnHeader = "Social security number";
		public static String invalidSSN = "Invalid social security number";
		public static String logonWithProvider = "Log on with [provider]";
		public static String invalidVerification = "Invalid verification code";
		public static String invalidCredentialsTitle = "We could not log you on";
		public static String invalidCredentialsMessage = "Please check your entered values and try again";
	}
	public final static class forgotpassword {
		public static String title = "Forgot your password?";
		public static String subtitle = "Enter the email address you used when creating this account. We\u2019ll send you an email to reset your password.";
		public static String emailHeader = "Email";
		public static String placeholder = "Type here";
		public static String resetPasswordButton = "Reset my password";
		public static String confirmationTitle = "You\'ve got mail";
		public static String confirmationSubtitle = "If the email address is connected to an e-Boks profile you will receive a message with instructions on how to create a new password. \r\n\r\nInstructions not received? \r\nLook in your spam/unwanted email folder for a message from e-Boks.";
		public static String invalidEmail = "Invalid email";
	}
	public final static class signup {
		public static String nameEmailHeader = "Let\'s get started with your brand new e-Boks account";
		public static String nameEmailDetail = "Please fill in the information below";
		public static String passwordHeader = "Great! Now, let\u2019s get you a password. A secure one, and something you can remember";
		public static String passwordDetail = "Your password should be at least 8 characters long, and include a letter and a number. Just to be safe!";
		public static String verificationHeader = "Perfect! Now we just need to know that you are who you say you are. Of course!";
		public static String verificationDetail = "This will enable the full feature set of e-Boks. Features like reading mail from public institutions, signing documents and paying, directly in e-Boks!";
		public static String continueButton = "Continue";
		public static String verifyButton = "Verify my profile";
		public static String continueWithoutVerificationButton = "Continue without verification";
		public static String continueToAppButton = "Continue to app";
		public static String nameHint = "Name";
		public static String emailHint = "Email";
		public static String passwordHint = "Password";
		public static String repeatPasswordHint = "Repeat password";
		public static String termsText = "By using this Site, you agree to be bound by, and to comply with, these Terms and Conditions. If you do not agree to these Terms and Conditions, please do not use this site.  \r\n\r\nPLEASE NOTE: We reserve the right, at our sole discretion, to change, modify or otherwise alter these Terms and Conditions at any time. Unless otherwise indicated, amendments will become effective immediately. Please review these Terms and Conditions periodically. Your continued use of the Site following the posting of changes and/or modifications will constitute your acceptance of the revised Terms and Conditions and the reasonableness of these standards for notice of changes. For your information, this page was last updated as of the date at the top of these terms and conditions. \r\n\r\n2. PRIVACY  Please review our Privacy Policy, which also governs your visit to this Site, to understand our practices.  3. LINKED SITES  This Site may contain links to other independent third-party Web sites (\"Linked Sites\u201D). These Linked Sites are provided solely as a convenience to our visitors. Such Linked Sites are not under our control, and we are not responsible for and does not endorse the content of such Linked Sites, including any information or materials contained on such Linked Sites. You will need to make your own independent judgment regarding your interaction with these Linked Sites.  4. FORWARD LOOKING STATEMENTS  All materials reproduced on this site speak as of the original date of publication or filing. The fact that a document is available on this site does not mean that the information contained in such document has not been modified or superseded by events or by a subsequent document or filing. We have no duty or policy to update any information or statements contained on this site and, therefore, such information or statements should not be relied upon as being current as of the date you access this site.  5. DISCLAIMER OF WARRANTIES AND LIMITATION OF LIABILITY  A. THIS SITE MAY CONTAIN INACCURACIES AND TYPOGRAPHICAL ERRORS. WE DOES NOT WARRANT THE ACCURACY OR COMPLETENESS OF THE MATERIALS OR THE RELIABILITY OF ANY ADVICE, OPINION, STATEMENT OR OTHER INFORMATION DISPLAYED OR DISTRIBUTED THROUGH THE SITE. YOU EXPRESSLY UNDERSTAND AND AGREE THAT: (i) YOUR USE OF THE SITE, INCLUDING ANY RELIANCE ON ANY SUCH OPINION, ADVICE, STATEMENT, MEMORANDUM, OR INFORMATION CONTAINED HEREIN, SHALL BE AT YOUR SOLE RISK; (ii) THE SITE IS PROVIDED ON AN \"AS IS\" AND \"AS AVAILABLE\" BASIS; (iii) EXCEPT AS EXPRESSLY PROVIDED HEREIN WE DISCLAIM ALL WARRANTIES OF ANY KIND, WHETHER EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, WORKMANLIKE EFFORT, TITLE AND NON-INFRINGEMENT; (iv) WE MAKE NO WARRANTY WITH RESPECT TO THE RESULTS THAT MAY BE OBTAINED FROM THIS SITE, THE PRODUCTS OR SERVICES ADVERTISED OR OFFERED OR MERCHANTS INVOLVED; (v) ANY MATERIAL DOWNLOADED OR OTHERWISE OBTAINED THROUGH THE USE OF THE SITE IS DONE AT YOUR OWN DISCRETION AND RISK; and (vi) YOU WILL BE SOLELY RESPONSIBLE FOR ANY DAMAGE TO YOUR COMPUTER SYSTEM OR FOR ANY LOSS OF DATA THAT RESULTS FROM THE DOWNLOAD OF ANY SUCH MATERIAL.  B. YOU UNDERSTAND AND AGREE THAT UNDER NO CIRCUMSTANCES, INCLUDING, BUT NOT LIMITED TO, NEGLIGENCE, SHALL WE BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, PUNITIVE OR CONSEQUENTIAL DAMAGES THAT RESULT FROM THE USE OF, OR THE INABILITY TO USE, ANY OF OUR SITES OR MATERIALS OR FUNCTIONS ON ANY SUCH SITE, EVEN IF WE HAVE BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES. THE FOREGOING LIMITATIONS SHALL APPLY NOTWITHSTANDING ANY FAILURE OF ESSENTIAL PURPOSE OF ANY LIMITED REMEDY.  6. EXCLUSIONS AND LIMITATIONS  SOME JURISDICTIONS DO NOT ALLOW THE EXCLUSION OF CERTAIN WARRANTIES OR THE LIMITATION OR EXCLUSION OF LIABILITY FOR INCIDENTAL OR CONSEQUENTIAL DAMAGES. ACCORDINGLY, OUR LIABILITY IN SUCH JURISDICTION SHALL BE LIMITED TO THE MAXIMUM EXTENT PERMITTED BY LAW.  7. OUR PROPRIETARY RIGHTS  This Site and all its Contents are intended solely for personal, non-commercial use. Except as expressly provided, nothing within the Site shall be construed as conferring any license under our or any third party\'s intellectual property rights, whether by estoppel, implication, waiver, or otherwise. Without limiting the generality of the foregoing, you acknowledge and agree that all content available through and used to operate the Site and its services is protected by copyright, trademark, patent, or other proprietary rights. You agree not to: (a) modify, alter, or deface any of the trademarks, service marks, trade dress (collectively \"Trademarks\") or other intellectual property made available by us in connection with the Site; (b) hold yourself out as in any way sponsored by, affiliated with, or endorsed by us, or any of our affiliates or service providers; (c) use any of the Trademarks or other content accessible through the Site for any purpose other than the purpose for which we have made it available to you; (d) defame or disparage us, our Trademarks, or any aspect of the Site; and (e) adapt, translate, modify, decompile, disassemble, or reverse engineer the Site or any software or programs used in connection with it or its products and services.  The framing, mirroring, scraping or data mining of the Site or any of its content in any form and by any method is expressly prohibited.";
		public static String title = "Sign up";
		public static String termsTitle = "Terms & Conditions";
		public static String mmHeader = "Okay. Would you like to receive digital mail from Mina Meddelan in e-Boks?";
		public static String mmDetail = "You will be able to receive mail from public authorities directly in your e-Boks.";
		public static String cprHint = "Your Social Security Number";
		public static String continueWithoutMMButton = "Continue without Mina meddelanden";
		public static String signupWithMMButton = "Sign up with Mina Meddelanden";
		public static String invalidName = "Invalid name";
		public static String invalidPassword = "Invalid password";
		public static String invalidPasswordMatch = "Passwords don\'t match";
		public static String termsClickAbleText = "By doing this you accept the [Terms & Conditions]";
		public static String invalidEmail = "Invalid email";
		public static String mmInvalidCprNumber = "mm Invalid cpr number";
		public static String login = "Log on";
		public static String completedHeader = "Yay! We\'re all set!\r\nWe hope you\'ll enjoy your new all digital inbox!";
		public static String typeHere = "Type here";
		public static String termsAcceptButton = "I accept";
		public static String termsAcceptHeader = "Welcome! To use e-Boks you need to accept the Terms & Conditions.";
		public static String termsAcceptSubHeader = "Please read & accept to continue";
		public static String cancelTermsBtn = "Discard my information";
		public static String signOnBankIDtitle = "Sign up with Mobile BankID";
		public static String signOnBankIDMessage = "You will need to sign in with BankID: Don\'t worry, it\'s a one time thing. You will be sent to the BankID app to log in.";
		public static String signOnBankIDButton = "Sign up with Mobile BankID";
		public static String cancelDialogHeader = "Are you sure you want to discard your information?";
		public static String cancelDialogText = "This will delete everything you have done up until this step.";
		public static String cancelDialogDiscardBtn = "Discard";
		public static String cancelDialogCancelBtn = "Cancel";
		public static String termsWhyAcceptButton = "Why do I need to accept this?";
		public static String termsInfoTitle = "A safe, fair and legal handling of your information is important";
		public static String termsInfoMessage = "The Terms & Conditions are here to ensure that your information is handled in a safe and fair way, and to have a clear agreement about what is allowed and what isn\'t.";
		public static String signOnNemIdTitle = "Sign up with NemID";
		public static String signOnNemIdMessage = "__signOnNemIdMessage";
		public static String signOnNemIdButton = "Sign up with NemID";
		public static String signOnBankIDMessageNOR = "NOR MSG You will need to sign in with BankID: Don\'t worry, it\'s a one time thing. You will be sent to the BankID app to log in.";
		public static String signOnBankIDtitleNOR = "Sign up with BankID";
		public static String signOnBankIDButtonNOR = "Sign up with BankID";
		public static String signupTermsAcceptHeader = "One last thing. To use e-Boks you need to accept the Terms & Conditions.";
		public static String dialogEmailExistsTitle = "This email address is already connected to an e-Boks account";
		public static String dialogEmailExistsMsg = "Do you want to sign in instead?";
		public static String dialogEmailExistsPositiveBtn = "Sign up";
	}
	public final static class activationcode {
		public static String title = "Activation Code";
		public static String subtitle = "__subtitle";
		public static String textFieldHeader = "Activation code";
		public static String infoButtonText = "Where do i find the code?";
		public static String placeholder = "Type here";
		public static String continueButton = "Continue";
		public static String invalidActivationCode = "Invalid activation code";
	}
	public final static class senderdetails {
		public static String register = "Register";
		public static String whatYouCanReceiveTitle = "What you can receive";
		public static String informationTitle = "Information";
		public static String registeredTypeNo = "Not registered";
		public static String registeredTypeYes = "Registered";
		public static String registeredTypePartial = "Partially registered";
		public static String readMore = "Read more";
		public static String publicAuthoritiesHeader = "Public authorities";
		public static String publicAuthoritiesUnregisteredDescription = "Get mail from all public authorities with a single tap. See the senders below";
		public static String numberOfSenders = "senders";
		public static String publicAuthoritiesRegisteredDescription = "You are registered to receive mail from all public authorities.";
	}
	public final static class findactivationcode {
		public static String title = "Finding your activation code";
		public static String message = "__message";
	}
	public final static class message {
		public static String information = "Information";
		public static String title = "Message";
		public static String notes = "Notes";
		public static String folder = "Folder";
		public static String attachments = "Attachments";
		public static String protectedTitle = "This message is protected";
		public static String protectedMessage = "Donec euismod a nulla a cursus. Nullam varius magna augue, in tincidunt arcu aliquet lobortis. Quisque ac ipsum tellus. Morbi nibh risus, fermentum a nunc id, cursus vehicula nibh.";
		public static String recalledTitle = "Recalled by sender";
		public static String recalledMessage = "Donec euismod a nulla a cursus. Nullam varius magna augue, in tincidunt arcu aliquet lobortis. Quisque ac ipsum tellus. Morbi nibh risus, fermentum a nunc id, cursus vehicula nibh.";
		public static String privateSenderTitle = "Private sender";
		public static String privateSenderMessage = "Donec euismod a nulla a cursus. Nullam varius magna augue, in tincidunt arcu aliquet lobortis. Quisque ac ipsum tellus. Morbi nibh risus, fermentum a nunc id, cursus vehicula nibh.";
		public static String openMessageButton = "Open message";
		public static String deleteMessageButton = "Delete message";
		public static String receiptTitle = "Opening receipt";
		public static String receiptMessage = "Donec euismod a nulla a cursus. Nullam varius magna augue, in tincidunt arcu aliquet lobortis. Quisque ac ipsum tellus. Morbi nibh risus, fermentum a nunc id, cursus vehicula nibh.";
		public static String recipientPrefixTo = "To:";
		public static String uploadedByYou = "Uploaded by you";
		public static String notePlaceholder = "Write note";
		public static String draft = "Draft";
		public static String shareMessageText = "Share";
		public static String lockedTitle = "Locked Content";
		public static String lockedMessage = "This message content could not be opened due to a virus found";
		public static String quarantinedTitle = "Quarantined";
		public static String quarantinedMessage = "This message is quarantined due to a problem found";
		public static String promulgationTopBarTitle = "Promulgation";
		public static String promulgationOkButton = "OK";
		public static String numberOfAttachmentsPluralSuffix = "attachments \u2022";
		public static String numberOfAttachmentsSingularSuffix = "attachment \u2022";
		public static String promulgationMessage = "You have received a court message that you have opened or otherwise processed. The message and exhibits, if any, have been properly serviced to you.    It is important that you thoroughly read the court message and exhibits, if any.    The message may for example include a summons, a call for a court hearing, or a judicial decision that all may have certain ramifications for you.   Any time limits apply as of today.    The court has received a return receipt in evidence of the message being serviced to you. You will find the return receipt under the folder Sent Items.";
		public static String voluntaryReceiptTitle = "Opening receipt";
		public static String voluntaryReceiptMessage = "__voluntaryReceiptMessage";
		public static String openMessageWithReceiptButton = "Open message with receipt";
		public static String openMessageWithoutReceiptButton = "Open message without receipt";
		public static String note = "Note";
		public static String reply = "Reply";
	}
	public final static class iosbiometrics {
		public static String errorAuthentication = "There was a problem verifying your identity";
		public static String errorUserCancel = "You cancelled";
		public static String errorUserFallback = "You pressed password";
		public static String faceID = "Face ID";
		public static String touchID = "Touch ID";
		public static String errorBiometryNotAvailable = "[idtype] is not available";
		public static String errorBiometryNotSetUp = "[idtype] is not set up";
		public static String errorBiometryLocked = "[idtype] is locked";
		public static String errorUnknownBiometryEvent = "Unknown problem with [idtype]";
		public static String logonWithBiometryReason = "Use [idtype] to log on";
		public static String nextTimeTitle = "Don\'t you want to use [idtype] to log on next time?";
		public static String nextTimeTouchIdMessage = "Just use your finger blablablala blabablablabla";
		public static String nextTimeFaceIdMessage = "Sign in just by looking by enabling FaceID in the e-Boks app";
		public static String nextTimeEnableButton = "Enable [idtype]";
		public static String nextTimeDontUseButton = "I don\'t want to use [idtype]";
		public static String logonWithReplaceableType = "Log on with [idtype]";
	}
	public final static class logonmethods {
		public static String mobileAccess = "Mobile access";
		public static String ssn = "Social Security number";
		public static String nemId = "NemID";
		public static String bankId = "BankID";
		public static String idPorten = "ID-porten";
	}
	public final static class settings {
		public static String header = "Settings";
		public static String creditCardHeader = "Credit Cards";
		public static String removeChannelBtn = "Remove Channel";
		public static String pinHeader = "Pin to home screen";
		public static String pinSubHeader = "Show this channel on the e-boks home screen";
	}
	public final static class uploads {
		public static String uploadsHeader = "Upload and store your important files and pictures in one secure place";
		public static String title = "Uploads";
		public static String takePhoto = "Take Photo";
		public static String chooseFile = "Choose a file";
		public static String rowHeader = "Latest uploads";
		public static String storageRowHeader = "Get 1 GB free storage";
		public static String storageRowSubHeader = "and much more";
		public static String storageRowButton = "Verify profile";
		public static String topbarEdit = "Edit";
		public static String nonVerifiedMsg = "Verify your profile and get 1 GB free space for your pictures and documents.";
		public static String nonVerifiedHeader = "Get 1 GB free storage";
		public static String nonVerifiedButton = "Verify my profile";
		public static String verifiedEmptyMsg = "You haven\u2019t uploaded anything yet";
		public static String uploadingFile = "Uploading file";
		public static String chosen = "Chosen";
		public static String uploadFile = "Upload file";
		public static String fileName = "File name";
		public static String incorrectFileName = "Incorrect file name";
		public static String verifiedInitialAvailableSpace = "1 GB free storage available";
		public static String notVerifiedInitialAvailableSpace = "100 MB free storage available";
		public static String remainingText = "Left";
		public static String filesHeaderShowAll = "Show all";
		public static String outOfStorageAlertVerify = "Verify";
		public static String outOfStorageAlertTitle = "You are out of storage space";
		public static String outOfStorageAlertMessage = "You have used all the free space that you have for your own documents and pictures. Verify your profile and get 1 GB of free space.";
		public static String photos = "Photos";
		public static String comingSoonHeader = "Coming soon!";
		public static String comingSoonMessage = "Soon you will be able to upload and store your important files securely in e-Boks";
	}
	public final static class loginproviders {
		public static String nemidTitle = "NemID";
		public static String idPortenTitle = "ID-porten";
		public static String bankSeTitle = "Mobile BankID";
		public static String bankNoTitle = "BankID";
		public static String nemidDescription = "Log on with NemID";
		public static String idPortenDescription = "Log on with ID-porten";
		public static String bankSeDescription = "Log on with Mobile BankID";
		public static String bankNoDescription = "Log on with BankID";
	}
	public final static class profile {
		public static String signOut = "Log off";
		public static String help = "Help";
		public static String privacyStatement = "Privacy Statement";
		public static String termsAndConditions = "Terms And Conditions";
		public static String keepMeSignedIn = "Keep me logged on";
		public static String fingerprint = "Fingerprint";
		public static String myInformation = "Name, email, mobile number";
		public static String keepMeSignedInDescription = "Mail from senders and services may require that you log on again before they can be accessed.";
		public static String verifyButton = "Verify my profile";
		public static String touchID = "Touch ID";
		public static String faceID = "Face ID";
		public static String version = "Version";
		public static String title = "Profile";
		public static String takePhoto = "Take photo";
		public static String chooseFromLibrary = "Choose from library";
		public static String verified = "Verified";
		public static String verifyingAccountTitle = "Verify my profile";
		public static String verifyingAccountBody = "Verify your profile by logging on with NemID.";
		public static String logOnWithNemID = "Log on with NemID";
		public static String verifyEmailHeader = "Verify your email address";
		public static String verifyEmailText = "Confirm your email address by following the link included in the message sent to [email]";
		public static String verifyEmailBtn = "Resend confirmation email";
		public static String verifyEmailCancel = "Cancel";
		public static String fingerHintTitle = "Enable mobile access to use fingerprint";
		public static String fingerHintText = "__fingerHintText";
		public static String fingerDrawerHeader = "Enable fingerprint";
		public static String fingerDrawerText = "__fingerDrawerText";
		public static String enable = "Enable";
		public static String mergeAccountHeader = "You already have a verified profile";
		public static String mergeAccountText = "Do you want to merge your profiles so you only have one single account ?";
		public static String mergeAccountBtn = "Merge my profiles";
		public static String mergeAccountCancel = "Keep profiles seperated";
		public static String verifyMobilHeader = "Verify your mobile number";
		public static String verifyMobilText = "Confirm your mobile number by entering the code texted to you on [mobileNumber]";
		public static String verifyMobilResend = "Resend code";
		public static String verifyMobileContinueBtn = "Continue";
		public static String verifyMobilCancel = "Cancel";
		public static String verifyMobilHintText = "Verification code";
		public static String enableMobileAccessTitle = "[idtype] requires mobile access enabled";
		public static String enableMobileAccessMessage = "The use of [idtype] requires that you log on to e-Boks in a browser and activate mobile access";
		public static String enableBiometricsTitle = "Enable [idtype]";
		public static String enableBiometricsMessage = "Type your password to enable [idtype]";
		public static String ssn = "Social Security Number";
		public static String password = "Password";
		public static String typeHere = "Type here";
		public static String myProfile = "Profile";
		public static String yourInfoWasSaved = "Your information was saved";
		public static String failedToSaveProfile = "Failed to save profile";
		public static String errorBiometryTakenTitle = "[idtype] already set up";
		public static String errorbiometryTakenMessage = "by: [username]";
		public static String errorBiometryVerificationRequiredTitle = "Verification required";
		public static String errorbiometryVerificationRequiredMessage = "You need to verify your profile to enable [idtype]";
		public static String feedbackCellTitle = "Provide feedback";
	}
	public final static class myInformation {
		public static String title = "My Information";
		public static String name = "Name";
		public static String primaryEmail = "Email 1";
		public static String secondaryEmail = "Email 2";
		public static String verifyEmail = "Verify email";
		public static String typeHere = "Type here";
		public static String notifyByEmailText = "You will be notified by email when you receive new mail";
		public static String mobileNumber = "Mobile number";
		public static String verifyNumber = "Verify number";
		public static String notifyBySMSText = "You will be notified by text message when you receive new mail";
		public static String newsletter = "Newsletter";
	}
	public final static class home {
		public static String messagesEmptyButton = "See my inbox";
		public static String bottomChannelHeaderNoChannels = "Self service with with e-Boks Plus";
		public static String messagesEmptyTitle = "No new mail";
		public static String messagesEmptyMessage = "You have no new mail or messages that require your action";
		public static String bottomChannelTextNoChannels = "With e-Boks Plus you get access to a range of new services that make your everyday life easier.";
		public static String bottomChannelHeaderChannels = "There is more to see!";
		public static String bottomChannelTextChannels = "We continuously add new services.";
		public static String messagesSectionHeaderButtonNewMessagesSuffix = "[value] new messages";
		public static String bottomChannelBtn = "View services";
		public static String messagesHeader = "Mail";
		public static String messagesSectionHeaderShowAllButton = "Show all";
		public static String channelsHeader = "Services";
		public static String channelsHeaderButton = "Edit";
		public static String messagesUnverifiedTitle = "Receive your mail digitally";
		public static String messagesUnverifiedMessage = "Receive your mail digitally from private companies and public authorities.\r\nGet started by verifying your profile.";
		public static String messagesUnverifiedButton = "Verify my profile";
		public static String windowHeader = "Home";
	}
	public final static class systemfolders {
		public static String inbox = "Inbox";
		public static String highlights = "Highlights";
	}
	public final static class channelsettings {
		public static String pinTitle = "Pin to Home screen";
		public static String pinSubtitle = "Show this channel on the Home screen";
		public static String pushTitle = "Push notifications";
		public static String pushSubtitle = "Get notified whenever something new happens";
		public static String title = "Settings";
		public static String removeButton = "Remove service";
		public static String confirmDeleteTitle = "Are you sure you want to remove this channel?";
		public static String confirmDeleteMessageReplaceChannelName = "Removing this channel will delete it from your e-Boks app, but will not delete your user at [channelname].";
		public static String confirmRemoveButton = "Remove";
		public static String removeChannelButtonRemovedState = "Removed!";
		public static String receiptsTitle = "Receive only receipts digitally";
		public static String receiptsSubtitle = "Accepting this then you will receive paper receipts";
		public static String addAnotherCard = "Add another payment card";
	}
	public final static class storeboxlist {
		public static String title = "Storebox";
		public static String emptyCardsHeader = "Get started";
		public static String emptyCardsMessage = "To receive your receipts in Storebox, \u2028you need to add a credit card.";
		public static String addCardButton = "Add credit card";
		public static String emptyLabel = "No receipts";
	}
	public final static class storeboxreceipt {
		public static String orderNo = "Order nr.";
		public static String receipt = "Receipt";
		public static String deliveredByStorebox = "Delivered by Storebox";
		public static String total = "Total";
		public static String vatAmount = "VAT amount";
		public static String confirmDeleteTitle = "Are you sure you want to delete this receipt?";
		public static String confirmDeleteMessage = "Are you sure you want to delete this receipt?";
	}
	public final static class channelsettingsstoreboxadditions {
		public static String digitalReceiptsTitle = "Receive only receipts digitally";
		public static String digitalReceiptsMessage = "You will only receive your receipts digitally when you shop in stores that are registeret with Storebox";
		public static String creditCardsSectionHeader = "Payment cards";
		public static String addCardTitle = "Add another payment card";
		public static String deleteCardAlertTitle = "Are you sure you want to remove this payment card?";
		public static String deleteCardAlertButton = "Remove";
		public static String deleteCardCancelButton = "Cancel";
		public static String removeChannelTitle = "Are you sure you want to remove this channel?";
		public static String removeChannelMessage = "Removing this channel will delete it from your e-boks app, but will not delete your user at storebox.";
	}
	public final static class overlaymenu {
		public static String move = "Move";
		public static String delete = "Delete";
		public static String print = "Print";
		public static String mail = "Email";
		public static String openIn = "Open with...";
		public static String forward = "Forward";
		public static String done = "Done";
		public static String chooseLocation = "Choose location";
		public static String markAsRead = "Mark as read";
		public static String markAsUnread = "Mark as unread";
		public static String archive = "Archive";
	}
	public final static class senderregistration {
		public static String title = "Registration Needed";
		public static String body = "__body";
		public static String register = "Register";
		public static String placeholder = "Type here";
	}
	public final static class reply {
		public static String title = "Reply";
		public static String required = "Required";
		public static String typeHere = "Type here";
		public static String select = "Select";
		public static String minLengthError = "Enter at least [CHARS] characters";
		public static String maxLengthError = "Enter at most [CHARS] characters";
		public static String minValueError = "Value must be [VALUE] or above";
		public static String maxValueError = "Value must be [VALUE] or below";
		public static String nanError = "Value must be number";
		public static String submitButton = "Submit";
		public static String datePickerCaption = "Choose date";
		public static String dateTimePickerCaption = "Choose date & time";
		public static String uploadingFile = "Uploading file";
	}
	public final static class androidfingerprint {
		public static String successMessage = "Successfully Authenticated";
		public static String errorMessage = "Unable to Authenticate";
		public static String errorInvalidData = "Data is not valid encrypted data";
		public static String dialogTitle = "Log on with fingerprint";
		public static String dialogSubtitle = "Use your fingerprint for faster, easier access to your account";
		public static String dialogDescription = "Confirm fingerprint to continue";
		public static String errorGeneric = "Unable to complete action";
		public static String dialogDefaultMessage = "Touch sensor";
		public static String dialogCancelButton = "Cancel";
		public static String dialogUsePasswordButton = "Use password";
	}
	public final static class inbox {
		public static String actionMove = "Move";
		public static String actionDelete = "Delete";
		public static String actionMarkAsRead = "Mark as read";
		public static String editButton = "Edit";
		public static String actionMarkAsUnread = "Mark as unread";
	}
	public final static class sharedusers {
		public static String title = "Select share";
		public static String sharedAccountsHeader = "My shared accounts";
		public static String permissionA = "Administrator";
		public static String permissionL = "Read only";
		public static String permissionF = "Editor";
		public static String permissionO = "Administration access (mail from authorities)";
		public static String myProfile = "My profile";
	}
	public final static class ekey {
		public static String topBarTitle = "e-Key";
		public static String addItemTopBarTitle = "Add item";
		public static String addItemLogin = "Credentials";
		public static String addItemCards = "Pin code";
		public static String addItemNote = "Note";
		public static String addItemSelectCategory = "Select category";
		public static String saveTopbar = "Add [item]";
		public static String inputHintName = "Name *";
		public static String inputHintNote = "Note";
		public static String requiredFields = "*Required fields";
		public static String inputHintPin = "Pin Code *";
		public static String inputHintUsername = "User name *";
		public static String inputHintPassword = "Password *";
		public static String overviewAddItemHeadline = "Headline";
		public static String overviewAddItemText = "__overviewAddItemText";
		public static String overviewAddItemBtn = "Add Item";
		public static String overviewNote = "Note";
		public static String overviewLogins = "Credentials";
		public static String overviewPins = "Pin Codes";
		public static String overviewNotes = "Notes";
		public static String deleteDialogMsg = "Are you sure you want to remove [item]?";
		public static String deleteDialogRemoveBtn = "Remove";
		public static String noContent = "You haven\'t added anything yet";
		public static String copiedToClipboard = "Copied to clipboard!";
		public static String inputHintCardholder = "Cardholder Name *";
		public static String edit = "Edit";
		public static String name = "Name";
		public static String cardholderName = "Cardholder Name";
		public static String pincode = "Pin Code";
		public static String username = "User name";
		public static String password = "Password";
		public static String addItemCardsSubtitle = "Pin Code to your credit cards";
		public static String addItemLoginSubtitle = "E.g. Mail, Facebook";
		public static String addItemNoteSubtitle = "__addItemNoteSubtitle";
		public static String addPinCode = "Add pin code";
		public static String inputHintCardtype = "Card type *";
		public static String cardType = "Card Type";
		public static String overviewEkey = "e-Key";
		public static String addLogin = "Add credentials";
		public static String addNote = "Add note";
		public static String editPin = "Edit pin code";
		public static String editLogin = "Edit credentials";
		public static String editNote = "Edit Note";
		public static String typeHere = "Type here";
	}
	public final static class storeboxlogin {
		public static String header = "Connect your Storebox profile";
		public static String subheader = "Connect your Storebox profile to e-Boks by entering the email address and optional mobile number from your Storebox Profile.";
		public static String title = "Sign in";
		public static String emailHeader = "Email";
		public static String phoneHeader = "Phone number";
		public static String required = "Required";
		public static String placeholder = "Type here";
		public static String errorNoExistingProfileMessage = "We couldn\'t find an exisiting Storebox profile with this email";
		public static String createUserButton = "Create Storebox profile";
		public static String errorAlreadyExistsMessage = "You already have a Storebox account with this information";
		public static String signInButton = "Sign in";
		public static String continueButton = "Continue";
		public static String tryAgainButton = "Try again";
	}
	public final static class storeboxverify {
		public static String header = "We sent you a confirmation code";
		public static String subheader = "Find the confirmation we just sent you, and type it in below to verify your identity.";
		public static String confirmationCodeHeader = "Confirmation code";
	}
	public final static class uploadspreview {
		public static String uploadsHeader = "Upload file";
		public static String folderTitle = "Folder";
		public static String fileTitle = "File name";
		public static String uploadPreview = "Save";
		public static String defaultFolder = "Default";
		public static String noPreviewAvailable = "No preview available";
	}
	public final static class editFolder {
		public static String folderNameTitle = "Folder Name";
		public static String editTitle = "Edit Folder";
		public static String saveButton = "Save";
		public static String defaultFolderLocation = "Default";
		public static String folderNameNonEmpty = "Folder name can\'t be empty";
		public static String deleteAlertTitle = "Delete?";
		public static String deleteAlertMessage = "Are you sure?";
		public static String createTitle = "Add Folder";
	}
}
