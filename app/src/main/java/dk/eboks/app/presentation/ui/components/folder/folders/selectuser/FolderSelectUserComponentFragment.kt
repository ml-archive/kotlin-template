package dk.eboks.app.presentation.ui.components.folder.folders.selectuser

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.login.SharedUser
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_folders_selectuser.*
import kotlinx.android.synthetic.main.viewholder_select_user.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class FolderSelectUserComponentFragment : BaseFragment(), FolderSelectUserComponentContract.View {

    @Inject
    lateinit var presenter: FolderSelectUserComponentContract.Presenter
    var sharedUsers: MutableList<SharedUser> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_folders_selectuser, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)


        setup()
    }

    override fun setUser(user: User?) {
        myProfileNameTv.text = user?.name ?: Translation.myInformation.name


        profileFl.visibility = View.GONE
        profilePicIv.visibility = View.VISIBLE

        Glide.with(context)
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



    private fun createMocks() {
        sharedUsers.add(SharedUser(1, 2, "_Peter Petersen", "_Administrator", null, null))
        sharedUsers.add(SharedUser(1, 3, "_John Johnson", "_Read only", null, null))
        sharedUsers.add(SharedUser(1, 4, "_Søren Sørensen", "_Read only", null, null))
        sharedUsers.add(SharedUser(1, 5, "_Ole Olsen", "_Administrator", null, null))
    }

    private fun setup() {
        createMocks()
        setupMyProfile()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        sharedAccountsRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        sharedAccountsRv.adapter = SharedUserAdapter()
    }

    private fun setupMyProfile() {
        myProfileLl.setOnClickListener {
            //close
            activity.onBackPressed()
        }
        myProfileSubHeaderTv.text = Translation.profile.myProfile

    }

    inner class SharedUserAdapter : RecyclerView.Adapter<SharedUserAdapter.SharedUserViewHolder>() {

        inner class SharedUserViewHolder(val root: View) : RecyclerView.ViewHolder(root) {

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

        override fun onBindViewHolder(holder: SharedUserViewHolder?, position: Int) {
            var currentUser = sharedUsers[position]
            holder?.nameTv?.text = currentUser.name
            holder?.roleTv?.text = currentUser.permission
            holder?.root?.setOnClickListener {
                //open normal maillist for sharedUsers[position] and close the drawer
                println(sharedUsers[position].toString())
                activity.onBackPressed()
            }
        }
    }

}