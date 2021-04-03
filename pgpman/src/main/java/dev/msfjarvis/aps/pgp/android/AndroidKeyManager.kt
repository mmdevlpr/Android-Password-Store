package dev.msfjarvis.aps.pgp.android

import com.proton.Gopenpgp.crypto.Key
import dev.msfjarvis.aps.pgp.KeyManager
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidKeyManager(filesDir: File) : KeyManager {

  private val keyDir = File(filesDir, KeyDir)

  override suspend fun addKey(key: Key): Boolean = withContext(Dispatchers.IO) {
    if (!keyDirExists()) return@withContext false

    val result = runCatching {
      val keyFile = File(keyDir, "${key.hexKeyID}.key")
      keyFile.writeText(key.armor())
    }

    return@withContext result.isSuccess
  }

  override suspend fun removeKey(key: Key): Boolean = withContext(Dispatchers.IO) {
    if (!keyDirExists()) return@withContext false

    val result = runCatching {
      val keyFile = File(keyDir, "${key.hexKeyID}.key")
      if (!keyFile.exists()) {
        return@runCatching false
      }
      keyFile.delete()
    }

    return@withContext result.getOrDefault(false)
  }

  override suspend fun findKeyById(id: String): Key = withContext(Dispatchers.IO) {
    if (!keyDirExists()) error("Key directory does not exist")

    keyDir.listFiles()?.forEach { file ->
      if (file.isFile && file.nameWithoutExtension == id) {
        val fileContent = file.readText()
        return@withContext Key(fileContent)
      }
    }

    error("Key with id: $id not found in directory")
  }

  override suspend fun listKeys(): List<Key> {
    if (!keyDirExists()) return emptyList()

    val keyList = arrayListOf<Key>()

    keyDir.listFiles()?.forEach { file ->
      if (file.isFile && file.extension == "key") {
        val fileContent = file.readText()
        keyList.add(Key(fileContent))
      }
    }

    return keyList
  }

  private fun keyDirExists(): Boolean {
    if (!keyDir.exists()) {
      if (!keyDir.mkdir()) {
        return false
      }
    }

    return true
  }

  companion object {
    private const val KeyDir = "keyDir"
  }
}
