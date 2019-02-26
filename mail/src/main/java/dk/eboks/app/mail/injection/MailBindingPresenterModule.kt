package dk.eboks.app.mail.injection

import dagger.Binds
import dagger.Module
import dk.eboks.app.mail.presentation.ui.components.foldershortcuts.FolderShortcutsComponentContract
import dk.eboks.app.mail.presentation.ui.components.foldershortcuts.FolderShortcutsComponentPresenter
import dk.eboks.app.mail.presentation.ui.components.maillist.MailListComponentContract
import dk.eboks.app.mail.presentation.ui.components.maillist.MailListComponentPresenter
import dk.eboks.app.mail.presentation.ui.components.sendercarousel.SenderCarouselComponentContract
import dk.eboks.app.mail.presentation.ui.components.sendercarousel.SenderCarouselComponentPresenter
import dk.eboks.app.mail.presentation.ui.folder.components.FolderSelectUserComponentContract
import dk.eboks.app.mail.presentation.ui.folder.components.FolderSelectUserComponentPresenter
import dk.eboks.app.mail.presentation.ui.folder.components.FoldersComponentContract
import dk.eboks.app.mail.presentation.ui.folder.components.FoldersComponentPresenter
import dk.eboks.app.mail.presentation.ui.folder.components.NewFolderComponentContract
import dk.eboks.app.mail.presentation.ui.folder.components.NewFolderComponentPresenter
import dk.eboks.app.mail.presentation.ui.folder.screens.FolderContract
import dk.eboks.app.mail.presentation.ui.folder.screens.FolderPresenter
import dk.eboks.app.mail.presentation.ui.message.components.detail.attachments.AttachmentsComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.detail.attachments.AttachmentsComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.detail.documents.DocumentComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.detail.documents.DocumentComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.detail.folderinfo.FolderInfoComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.detail.folderinfo.FolderInfoComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.detail.header.HeaderComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.detail.header.HeaderComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.detail.notes.NotesComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.detail.notes.NotesComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.detail.reply.ReplyButtonComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.detail.reply.ReplyButtonComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.detail.share.ShareComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.detail.share.ShareComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.detail.sign.SignButtonComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.detail.sign.SignButtonComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.opening.privatesender.PrivateSenderWarningComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.opening.privatesender.PrivateSenderWarningComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.opening.promulgation.PromulgationComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.opening.promulgation.PromulgationComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.opening.protectedmessage.ProtectedMessageComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.opening.protectedmessage.ProtectedMessageComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.opening.quarantine.QuarantineComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.opening.quarantine.QuarantineComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.opening.recalled.RecalledComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.opening.recalled.RecalledComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.opening.receipt.OpeningReceiptComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.opening.receipt.OpeningReceiptComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.viewers.html.HtmlViewComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.viewers.html.HtmlViewComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.viewers.image.ImageViewComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.viewers.image.ImageViewComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.viewers.pdf.PdfViewComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.viewers.pdf.PdfViewComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.components.viewers.text.TextViewComponentContract
import dk.eboks.app.mail.presentation.ui.message.components.viewers.text.TextViewComponentPresenter
import dk.eboks.app.mail.presentation.ui.message.screens.MessageContract
import dk.eboks.app.mail.presentation.ui.message.screens.MessagePresenter
import dk.eboks.app.mail.presentation.ui.message.screens.embedded.MessageEmbeddedContract
import dk.eboks.app.mail.presentation.ui.message.screens.embedded.MessageEmbeddedPresenter
import dk.eboks.app.mail.presentation.ui.message.screens.reply.ReplyFormContract
import dk.eboks.app.mail.presentation.ui.message.screens.reply.ReplyFormPresenter
import dk.eboks.app.mail.presentation.ui.message.screens.sign.SignContract
import dk.eboks.app.mail.presentation.ui.message.screens.sign.SignPresenter
import dk.eboks.app.mail.presentation.ui.screens.list.MailListContract
import dk.eboks.app.mail.presentation.ui.screens.list.MailListPresenter
import dk.eboks.app.mail.presentation.ui.screens.overview.MailOverviewContract
import dk.eboks.app.mail.presentation.ui.screens.overview.MailOverviewPresenter
import dk.nodes.arch.domain.injection.scopes.ActivityScope

@Module
abstract class MailBindingPresenterModule {
    @ActivityScope
    @Binds
    abstract fun bindFolderShortcutsComponentPresenter(presenter: FolderShortcutsComponentPresenter): FolderShortcutsComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindMailListComponentPresenter(presenter: MailListComponentPresenter): MailListComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindSenderCarouselComponentPresenter(presenter: SenderCarouselComponentPresenter): SenderCarouselComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindFoldersComponentPresenter(presenter: FoldersComponentPresenter): FoldersComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindFolderSelectUserComponentPresenter(presenter: FolderSelectUserComponentPresenter): FolderSelectUserComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindNewFolderComponentPresenter(presenter: NewFolderComponentPresenter): NewFolderComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindFolderPresenter(presenter: FolderPresenter): FolderContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindAttachmentsComponentPresenter(presenter: AttachmentsComponentPresenter): AttachmentsComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindDocumentComponentPresenter(presenter: DocumentComponentPresenter): DocumentComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindFolderInfoComponentPresenter(presenter: FolderInfoComponentPresenter): FolderInfoComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindHeaderComponentPresenter(presenter: HeaderComponentPresenter): HeaderComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindNotesComponentPresenter(presenter: NotesComponentPresenter): NotesComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindReplyButtonComponentPresenter(presenter: ReplyButtonComponentPresenter): ReplyButtonComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindShareComponentPresenter(presenter: ShareComponentPresenter): ShareComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindSignButtonComponentPresenter(presenter: SignButtonComponentPresenter): SignButtonComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindPrivateSenderWarningComponentPresenter(presenter: PrivateSenderWarningComponentPresenter): PrivateSenderWarningComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindPromulgationComponentPresenter(presenter: PromulgationComponentPresenter): PromulgationComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindProtectedMessageComponentPresenter(presenter: ProtectedMessageComponentPresenter): ProtectedMessageComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindQuarantineComponentPresenter(presenter: QuarantineComponentPresenter): QuarantineComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindRecalledMessageComponentPresenter(presenter: RecalledComponentPresenter): RecalledComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindOpeningReceiptComponentPresenter(presenter: OpeningReceiptComponentPresenter): OpeningReceiptComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindHtmlViewComponentPresenter(presenter: HtmlViewComponentPresenter): HtmlViewComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindImageViewComponentPresenter(presenter: ImageViewComponentPresenter): ImageViewComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindPdfPreviewComponentPresenter(presenter: PdfViewComponentPresenter): PdfViewComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindTextViewComponentPresenter(presenter: TextViewComponentPresenter): TextViewComponentContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindMessageSheetPresenter(presenter: MessageEmbeddedPresenter): MessageEmbeddedContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindReplyFormPresenter(presenter: ReplyFormPresenter): ReplyFormContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindSignPresenter(presenter: SignPresenter): SignContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindMessagePresenter(presenter: MessagePresenter): MessageContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindMailListPresenter(presenter: MailListPresenter): MailListContract.Presenter

    @ActivityScope
    @Binds
    abstract fun bindMailOverviewPresenter(presenter: MailOverviewPresenter): MailOverviewContract.Presenter
}