package dk.eboks.app.mail.injection

import dagger.Binds
import dagger.Module
import dk.eboks.app.mail.domain.interactors.GetCategoriesInteractor
import dk.eboks.app.mail.domain.interactors.GetMailCategoriesInteractorImpl
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
import dk.eboks.app.mail.domain.interactors.messageoperations.DeleteMessagesInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.DeleteMessagesInteractorImpl
import dk.eboks.app.mail.domain.interactors.messageoperations.MoveMessagesInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.MoveMessagesInteractorImpl
import dk.eboks.app.mail.domain.interactors.messageoperations.UpdateMessageInteractor
import dk.eboks.app.mail.domain.interactors.messageoperations.UpdateMessageInteractorImpl
import dk.eboks.app.mail.domain.interactors.senders.GetSendersInteractor
import dk.eboks.app.mail.domain.interactors.senders.GetSendersInteractorImpl

@Module
abstract class MailBindingInteractorModule {
    @Binds
    abstract fun bindCreateFolder(interactor: CreateFolderInteractorImpl): CreateFolderInteractor

    @Binds
    abstract fun bindDeleteFolder(interactor: DeleteFolderInteractorImpl): DeleteFolderInteractor

    @Binds
    abstract fun bindEditFolder(interactor: EditFolderInteractorImpl): EditFolderInteractor

    @Binds
    abstract fun bindGetFoldersInteractor(interactor: GetFoldersInteractorImpl): GetFoldersInteractor

    @Binds
    abstract fun bindOpenFolderInteractor(interactor: OpenFolderInteractorImpl): OpenFolderInteractor

    @Binds
    abstract fun bindGetLatestUploadsInteractor(interactor: GetLatestUploadsInteractorImpl): GetLatestUploadsInteractor

    @Binds
    abstract fun bindGetMessagesInteractor(interactor: GetMessagesInteractorImpl): GetMessagesInteractor

    @Binds
    abstract fun bindGetReplyFormInteractor(interactor: GetReplyFormInteractorImpl): GetReplyFormInteractor

    @Binds
    abstract fun bindGetSignLinkInteractor(interactor: GetSignLinkInteractorImpl): GetSignLinkInteractor

    @Binds
    abstract fun bindGetStorageInteractor(interactor: GetStorageInteractorImpl): GetStorageInteractor

    @Binds
    abstract fun bindMoveMessagesInteractor(interactor: MoveMessagesInteractorImpl): MoveMessagesInteractor

    @Binds
    abstract fun bindOpenAttachmentInteractor(interactor: OpenAttachmentInteractorImpl): OpenAttachmentInteractor

    @Binds
    abstract fun bindOpenMessageInteractor(interactor: OpenMessageInteractorImpl): OpenMessageInteractor

    @Binds
    abstract fun bindSaveAttachmentInteractor(interactor: SaveAttachmentInteractorImpl): SaveAttachmentInteractor

    @Binds
    abstract fun bindSubmitReplyFormInteractor(interactor: SubmitReplyFormInteractorImpl): SubmitReplyFormInteractor

    @Binds
    abstract fun bindUploadFileInteractor(interactor: UploadFileInteractorImpl): UploadFileInteractor

    @Binds
    abstract fun bindDeleteMessagesInteractor(interactor: DeleteMessagesInteractorImpl): DeleteMessagesInteractor

    @Binds
    abstract fun bindUpdateMessageInteractor(interactor: UpdateMessageInteractorImpl): UpdateMessageInteractor

    @Binds
    abstract fun bindGetSendersInteractor(interactor: GetSendersInteractorImpl): GetSendersInteractor

    @Binds
    abstract fun bindGetCategoriesInteractor(interactor: GetMailCategoriesInteractorImpl): GetCategoriesInteractor
}