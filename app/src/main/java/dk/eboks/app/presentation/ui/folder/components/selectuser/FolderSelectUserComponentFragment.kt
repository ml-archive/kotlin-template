package dk.eboks.app.presentation.ui.folder.components.selectuser

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.SharedUser
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_folders_selectuser.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class FolderSelectUserComponentFragment : BaseFragment(), FolderSelectUserComponentContract.View {

    @Inject
    lateinit var presenter: FolderSelectUserComponentContract.Presenter
    var sharedUsers: MutableList<SharedUser> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_folders_selectuser, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        profileContentLl.visibility = View.GONE
        profileProgress.visibility = View.VISIBLE

        setup()

        presenter.getShared()
    }

    override fun setUser(user: User?) {
        myProfileNameTv.text = user?.name ?: Translation.myInformation.name

        profileFl.visibility = View.GONE
        profilePicIv.visibility = View.VISIBLE

        Glide.with(context ?: return)
                .applyDefaultRequestOptions(RequestOptions().circleCrop())
                .load(user?.avatarUri)
                .listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        profilePicIv.visibility = View.GONE
                        profileFl.visibility = View.VISIBLE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }
                })
                .into(profilePicIv)
    }

    private fun setup() {
        setupMyProfile()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        sharedAccountsRv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            androidx.recyclerview.widget.RecyclerView.VERTICAL,
            false
        )
        sharedAccountsRv.adapter = SharedUserAdapter()
    }

    private fun setupMyProfile() {
        myProfileLl.setOnClickListener {
            //close
            presenter.setSharedUser(null)
            activity?.onBackPressed()
        }
        myProfileSubHeaderTv.text = Translation.profile.myProfile

    }

    override fun showShares(shares: List<SharedUser>) {
        sharedUsers.addAll(shares)
        sharedAccountsRv.adapter?.notifyDataSetChanged()
    }

    override fun showProgress(visible: Boolean) {
        if(visible) {
            profileProgress.visibility = View.VISIBLE
            profileContentLl.visibility = View.GONE
        } else {
            profileProgress.visibility = View.GONE
            profileContentLl.visibility = View.VISIBLE
        }
    }


    inner class SharedUserAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<SharedUserAdapter.SharedUserViewHolder>() {

        inner class SharedUserViewHolder(val root: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(root) {

            val nameTv = root.findViewById<TextView>(R.id.nameTv)
            val roleTv = root.findViewById<TextView>(R.id.roleTv)
            val checkbox = root.findViewById<ImageButton>(R.id.checkboxIb)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedUserViewHolder {
            val v = LayoutInflater.from(context).inflate(R.layout.viewholder_select_user, parent, false)
            val vh = SharedUserViewHolder(v)
            return vh
        }

        override fun getItemCount(): Int {
            return sharedUsers.size
        }

        override fun onBindViewHolder(holder: SharedUserViewHolder, position: Int) {
            val currentUser = sharedUsers[position]
            holder.nameTv?.text = currentUser.name
            holder.roleTv?.text = currentUser.permission
            holder.root.setOnClickListener {
                //open normal maillist for sharedUsers[position] and close the drawer
                Timber.d(sharedUsers[position].toString())
                presenter.setSharedUser(sharedUsers[position])
                activity?.onBackPressed()
            }
        }
    }

}