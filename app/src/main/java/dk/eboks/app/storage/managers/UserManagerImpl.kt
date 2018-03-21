package dk.eboks.app.storage.managers

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.storage.base.GsonFileStorageRepository
import dk.eboks.app.util.FieldMapper
import timber.log.Timber

/**
 * Created by bison on 17-02-2018.
 */
class UserManagerImpl(val context: Context, val gson: Gson) : UserManager {
    override var users : MutableList<User> = ArrayList()
    private val userStore = UserStore()
    private var lastId : Long = 1

    init {
        val type = object : TypeToken<ArrayList<User>>(){}.type
        try {
            users = userStore.load(type)
            Timber.e("Loaded user store with ${users.size} entries")
            lastId = users.size.toLong()
            for(entry in users)
            {
                Timber.e("Entry: ${entry}")
            }

        }
        catch (t : Throwable)
        {
        }
    }

    override fun add(user : User)
    {
        user.id = lastId++
        users.add(user)
        userStore.save(users)
    }

    override fun remove(user : User)
    {
        users.remove(user)
        userStore.save(users)
    }

    override fun save(user: User) {
        for(u in users)
        {
            if(u.id == user.id)
            {
                FieldMapper.copyAllFields(user, u)
                break
            }
        }
        userStore.save(users)
    }

    inner class UserStore : GsonFileStorageRepository<MutableList<User>>(context, gson, "users.json")
}