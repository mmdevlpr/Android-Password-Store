package dev.msfjarvis.aps.pgp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.github.ajalt.timberkt.d
import com.proton.Gopenpgp.crypto.Key
import dev.msfjarvis.aps.pgp.android.AndroidKeyManager
import kotlinx.coroutines.launch

class KeyListActivity : AppCompatActivity() {

  private val readFileTask =
    registerForActivityResult(ActivityResultContracts.GetContent()) { fileUri ->
      if (fileUri == null) return@registerForActivityResult
      val keyManager = AndroidKeyManager(filesDir)

      contentResolver.openInputStream(fileUri)?.bufferedReader().use { reader ->
        if (reader == null) return@registerForActivityResult
        val fileContent = reader.readText()
        val key = Key(fileContent)
        // val check = key.check()
        d { key.armor() }

        lifecycleScope.launch {
          keyManager.addKey(Key(fileContent))
        }
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { readFileTask.launch("application/pgp-signature") }) { Text(text = "Launch Activity") }
      }
    }
  }
}
