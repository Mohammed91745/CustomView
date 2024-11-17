package com.app.customview

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var inputField: EditText
    private lateinit var addNameButton: Button
    private lateinit var messageContainer: LinearLayout
    private var clickCount = 0 // Keeps track of button clicks to manage dynamic delay

    // List to store messages for restoring after rotation
    private val messagesList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get references to the UI elements
        inputField = findViewById(R.id.inputField)
        addNameButton = findViewById(R.id.addNameButton)
        messageContainer = findViewById(R.id.messageContainer) // Add LinearLayout in XML as container for TextViews

        // Restore messages if the activity is recreated
        if (savedInstanceState != null) {
            val savedMessages = savedInstanceState.getStringArrayList("messages")
            savedMessages?.let {
                messagesList.addAll(it)
                restoreMessages()
            }
        }

        // Set click listener on the button
        addNameButton.setOnClickListener {
            // Get the text from the input field
            val name = inputField.text.toString().trim()

            if (name.isNotEmpty()) {
                clickCount++
                val delayTime = 1000L * clickCount // Dynamic delay based on click count

                // Launch a coroutine in the Main scope
                CoroutineScope(Dispatchers.Main).launch {
                    delay(delayTime) // Delay increases with each new name
                    addMessage("The name is $name and delay was $delayTime milliseconds")
                }
            } else {
                // If input is empty, show a hint or error message immediately
                addMessage("Please enter a name.")
            }
        }
    }

    private fun addMessage(message: String) {
        // Add message to the list
        messagesList.add(message)

        // Create a new TextView for each message
        val newTextView = TextView(this).apply {
            text = message
            textSize = 16f
            setTextColor(resources.getColor(android.R.color.black, null))
            background = resources.getDrawable(R.drawable.rounded_background, null) // Set the background drawable

            // Add padding for text inside the TextView
            setPadding(20, 35, 20, 35)  // Padding: left, top, right, bottom

            // Set margin to add space between TextViews
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(16, 16, 16, 16)  // Margin: left, top, right, bottom
            this.layoutParams = layoutParams

            // Set gravity to center the text
            gravity = android.view.Gravity.CENTER
        }

        // Add the new TextView to the container
        messageContainer.addView(newTextView)
    }

    private fun restoreMessages() {
        // Clear the container before restoring to avoid duplicate views
        messageContainer.removeAllViews()

        // Add each saved message as a new TextView
        for (message in messagesList) {
            val newTextView = TextView(this).apply {
                text = message
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.black, null))
                background = resources.getDrawable(R.drawable.rounded_background, null) // Set the background drawable

                // Add padding for text inside the TextView
                setPadding(20, 35, 20, 35)  // Padding: left, top, right, bottom

                // Set margin to add space between TextViews
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(16, 16, 16, 16)  // Margin: left, top, right, bottom
                this.layoutParams = layoutParams

                // Set gravity to center the text
                gravity = android.view.Gravity.CENTER
            }

            // Add the new TextView to the container
            messageContainer.addView(newTextView)
        }
    }


    // Save the state when the activity is about to be destroyed (e.g., on rotation)
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the list of messages
        outState.putStringArrayList("messages", ArrayList(messagesList))
    }
}
