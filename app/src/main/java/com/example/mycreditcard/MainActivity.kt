package com.example.mycreditcard

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val edtCardNumber = findViewById<EditText>(R.id.edtCardNumber)
        val txtCardName = findViewById<EditText>(R.id.edtCardName)
        val edtCardVal = findViewById<EditText>(R.id.edtCardVal)

        edtCardNumber.addTextChangedListener(object : TextWatcher {
            private var isEditing = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isEditing || s == null) return

                isEditing = true

                // Remove tudo que não for número
                val cleanText = s.toString().replace(Regex("[^\\d]"), "")

                // Insere espaço a cada 4 números
                val formattedText = StringBuilder()
                for (i in cleanText.indices) {
                    if (i > 0 && i % 4 == 0) {
                        formattedText.append(" ")
                    }
                    formattedText.append(cleanText[i])
                }

                // Atualiza o EditText sem causar loop infinito
                val cursorPosition = formattedText.length
                edtCardNumber.setText(formattedText.toString())
                edtCardNumber.setSelection(cursorPosition) // Mantém o cursor no final

                isEditing = false
            }
        })

        txtCardName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val cleanText = s.toString().replace(Regex("[^a-zA-ZÀ-ÿ\\s]"), "")
                if (s.toString() != cleanText) {
                    txtCardName.setText(cleanText)
                    txtCardName.setSelection(cleanText.length)
                }
            }
        })

        // Máscara da validade (MM/AA)
        edtCardVal.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) return

                var input = s.toString().replace("/", "")
                if (input.length > 4) input = input.substring(0, 4)

                val formatted = StringBuilder()
                for (i in input.indices) {
                    if (i == 2) formatted.append("/")
                    formatted.append(input[i])
                }

                isUpdating = true
                edtCardVal.setText(formatted.toString())
                edtCardVal.setSelection(formatted.length)
                isUpdating = false
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}