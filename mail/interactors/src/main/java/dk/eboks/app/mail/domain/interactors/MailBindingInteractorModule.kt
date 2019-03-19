package dk.eboks.app.mail.domain.interactors

import dagger.Binds
import dagger.Module
import dk.eboks.app.mail.domain.interactors.categories.GetCategoriesInteractor
import dk.eboks.app.mail.domain.interactors.categories.GetMailCategoriesInteractorImpl
import dk.eboks.app.mail.domain.interactors.folder.CreateFolderInteractor
import dk.eboks.app.mail.domain.interactors.folder.CreateFolderInteractorImpl
import dk.eboks.app.mail.domain.interactors.folder.DeleteFolderInteractor
import dk.eboks.app.mail.domain.interactors.folder.DeleteFolderInteractorImpl
import dk.eboks.app.mail.domain.interactors.folder.EditFolderInteractor
import dk.eboks.app.mail.domain.interactors.folder.EditFolderInteractorImpl
import dk.eboks.app.mail.domain.interactors.folder.GetFoldersInteractor
import dk.eboks.app.mail.domain.interactors.folder.GetFoldersInteractorImpl
import dk.eboks.app.mail.domain.interactors.folder.OpenFolderInteractor
import dk.eboks.app.mail.domain.interactors.folder.OpenFolderInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.GetLatestUploadsInteractor
import dk.eboks.app.mail.domain.interactors.message.GetLatestUploadsInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.GetMessagesInteractor
import dk.eboks.app.mail.domain.interactors.message.GetMessagesInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.GetReplyFormInteractor
import dk.eboks.app.mail.domain.interactors.message.GetReplyFormInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.GetSignLinkInteractor
import dk.eboks.app.mail.domain.interactors.message.GetSignLinkInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.GetStorageInteractor
import dk.eboks.app.mail.domain.interactors.message.GetStorageInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.OpenAttachmentInteractor
import dk.eboks.app.mail.domain.interactors.message.OpenAttachmentInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.OpenMessageInteractor
import dk.eboks.app.mail.domain.interactors.message.OpenMessageInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.SaveAttachmentInteractor
import dk.eboks.app.mail.domain.interactors.message.SaveAttachmentInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.SubmitReplyFormInteractor
import dk.eboks.app.mail.domain.interactors.message.SubmitReplyFormInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.UploadFileInteractor
import dk.eboks.app.mail.domain.interactors.message.UploadFileInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.payment.GetPaymentDetailsInteractor
import dk.eboks.app.mail.domain.interactors.message.payment.GetPaymentDetailsInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.payment.GetPaymentLinkInteractor
import dk.eboks.app.mail.domain.interactors.message.payment.GetPaymentLinkInteractorImpl
import dk.eboks.app.mail.domain.interactors.message.payment.TogglePaymentNotificationInteractor
import dk.eboks.app.mail.domain.interactors.message.payment.TogglePaymentNotificationInteractorImpl
import dk.eboks.app.mail.domain.interactors.messageoperations.DeleteMessagesInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.DeleteMessagesInteractorImpl
import dk.eboks.app.mail.domain.interactors.messageoperations.MoveMessagesInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.MoveMessagesInteractorImpl
import dk.eboks.app.mail.domain.interactors.messageoperations.UpdateMessageInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.UpdateMessageInteractorImpl
import dk.eboks.app.mail.domain.interactors.senders.GetSendersInteractor
import dk.eboks.app.mail.domain.interactors.senders.GetSendersInteractorImpl
import dk.eboks.app.mail.domain.interactors.shares.GetAllSharesInteractor
import dk.eboks.app.mail.domain.interactors.shares.GetAllSharesInteractorImpl

@Module
abstract class MailBindingInteractorModule {
    @Binds
    internal abstract fun bindCreateFolder(interactor: CreateFolderInteractorImpl): CreateFolderInteractor

    @Binds
    internal abstract fun bindDeleteFolder(interactor: DeleteFolderInteractorImpl): DeleteFolderInteractor

    @Binds
    internal abstract fun bindEditFolder(interactor: EditFolderInteractorImpl): EditFolderInteractor

    @Binds
    internal abstract fun bindGetFoldersInteractor(interactor: GetFoldersInteractorImpl): GetFoldersInteractor

    @Binds
    internal abstract fun bindOpenFolderInteractor(interactor: OpenFolderInteractorImpl): OpenFolderInteractor

    @Binds
    internal abstract fun bindGetLatestUploadsInteractor(interactor: GetLatestUploadsInteractorImpl): GetLatestUploadsInteractor

    @Binds
    internal abstract fun bindGetMessagesInteractor(interactor: GetMessagesInteractorImpl): GetMessagesInteractor

    @Binds
    internal abstract fun bindGetReplyFormInteractor(interactor: GetReplyFormInteractorImpl): GetReplyFormInteractor

    @Binds
    internal abstract fun bindGetSignLinkInteractor(interactor: GetSignLinkInteractorImpl): GetSignLinkInteractor

    @Binds
    internal abstract fun bindGetStorageInteractor(interactor: GetStorageInteractorImpl): GetStorageInteractor

    @Binds
    internal abstract fun bindMoveMessagesInteractor(interactor: MoveMessagesInteractorImpl): MoveMessagesInteractor

    @Binds
    internal abstract fun bindOpenAttachmentInteractor(interactor: OpenAttachmentInteractorImpl): OpenAttachmentInteractor

    @Binds
    internal abstract fun bindOpenMessageInteractor(interactor: OpenMessageInteractorImpl): OpenMessageInteractor

    @Binds
    internal abstract fun bindSaveAttachmentInteractor(interactor: SaveAttachmentInteractorImpl): SaveAttachmentInteractor

    @Binds
    internal abstract fun bindSubmitReplyFormInteractor(interactor: SubmitReplyFormInteractorImpl): SubmitReplyFormInteractor

    @Binds
    internal abstract fun bindUploadFileInteractor(interactor: UploadFileInteractorImpl): UploadFileInteractor

    @Binds
    internal abstract fun bindDeleteMessagesInteractor(interactor: DeleteMessagesInteractorImpl): DeleteMessagesInteractor

    @Binds
    internal abstract fun bindUpdateMessageInteractor(interactor: UpdateMessageInteractorImpl): UpdateMessageInteractor

    @Binds
    internal abstract fun bindGetSendersInteractor(interactor: GetSendersInteractorImpl): GetSendersInteractor

    @Binds
    internal abstract fun bindGetCategoriesInteractor(interactor: GetMailCategoriesInteractorImpl): GetCategoriesInteractor

    @Binds
    internal abstract fun bindGetAllSharesInteractor(interactor: GetAllSharesInteractorImpl): GetAllSharesInteractor

    @Binds
    internal abstract fun bindGetPaymentLinkInteractor(interactor: GetPaymentLinkInteractorImpl): GetPaymentLinkInteractor

    @Binds
    internal abstract fun bindGetPaymentDetailsInteractor(interactor: GetPaymentDetailsInteractorImpl): GetPaymentDetailsInteractor

    @Binds
    internal abstract fun bindTogglePaymentNotificationInteractor(interactor: TogglePaymentNotificationInteractorImpl): TogglePaymentNotificationInteractor
}