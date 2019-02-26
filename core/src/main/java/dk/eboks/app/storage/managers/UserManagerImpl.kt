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
    override var users: MutableList<User> = ArrayList()
    private val userStore = UserStore()

    init {
        val type = object : TypeToken<ArrayList<User>>() {}.type
        try {
            users = userStore.load(type)
            Timber.e("Loaded currentUser store with ${users.size} entries")

            for (entry in users) {
                Timber.e("Entry: $entry")
            }
        } catch (t: Throwable) {
        }
    }

    /**
     * try to update an existing currentUser, or add it if it doesn't exist yet
     * if updated the reference saved in the currentUser manager is returned
     * replace this in your client code
     */
    override fun put(user: User): User {
        for (u in users) {
            if (u.id == user.id) {
                FieldMapper.copyAllFields(u, user)

                userStore.save(users)
                Timber.e("User ${user.id} : ${user.name} updated")
                return u
            }
        }
        // clear CPR
        user.identity = ""
        users.add(user)
        Timber.e("User ${user.id} : ${user.name} added")
        userStore.save(users)
        return user
    }

    override fun remove(user: User) {
        Timber.e("Removing ${user.name} (${user.id})")
        users.remove(user)
        userStore.save(users)
    }

    override fun save() {
        userStore.save(users)
    }

    inner class UserStore :
        GsonFileStorageRepository<MutableList<User>>(context, gson, "users.json")
}