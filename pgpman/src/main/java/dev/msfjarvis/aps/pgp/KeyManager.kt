package dev.msfjarvis.aps.pgp

import com.proton.Gopenpgp.crypto.Key

interface KeyManager {

  suspend fun addKey(key: Key): Boolean

  suspend fun removeKey(key: Key): Boolean

  suspend fun findKeyById(id: String): Key

  suspend fun listKeys(): List<Key>
}
