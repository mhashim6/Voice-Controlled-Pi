package mhashim6.school.android.voiceControlledPi

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mhashim6.school.android.voiceControlledPi.Response.*

private const val VOICE_REQUEST_CODE = 118

private val PROMPTS = arrayOf(
    "Say a number from 1 to 10",
    "Say red, green, or yellow",
    "Say light, or dark",
    "Say dance"
)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        voiceButton.setOnClickListener { launchRecognizer("en") }
        voiceButton.setOnLongClickListener { launchRecognizer("ar"); true } //why not.

        titleText.setOnLongClickListener { launchRecognizer("ja"); true } // I mean, I've already gone so far.
    }

    private fun launchRecognizer(language: String) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            language
        )
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            PROMPTS.random()
        )
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        startActivityForResult(intent, VOICE_REQUEST_CODE)
    }

    private val main = CoroutineScope(Dispatchers.Main)
    private val background = CoroutineScope(Dispatchers.IO)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == VOICE_REQUEST_CODE && resultCode == RESULT_OK) {
            val matches = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            background.launch {
                val status = processCommand(matches[0])
                main.launch {
                    when (status) {
                        ERR_WRONG_COMMAND ->
                            Snackbar.make(root, "This has nothing to do with this project.", LENGTH_LONG).show()
                        ERR_CONNECTION ->
                            Snackbar.make(root, "Connection error.", LENGTH_LONG).show()
                        SUCCESSFUL -> Unit
                    }
                }
            }
        }
    }
}
