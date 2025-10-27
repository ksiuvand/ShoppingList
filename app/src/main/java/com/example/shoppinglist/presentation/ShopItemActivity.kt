package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {

    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var textInputLayoutCount: TextInputLayout
    private lateinit var textInputEditTextName: TextInputEditText
    private lateinit var textInputEditTextCount: TextInputEditText
    private lateinit var buttonSave: Button

    private lateinit var shopItemViewModel: ShopItemViewModel

    private var screenMode = ""
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        parseExtra()
        initViews()
        shopItemViewModel = ViewModelProvider(this).get(ShopItemViewModel::class.java)

        setUpRightMode()
        addTextListeners()
        observeViewModel()

    }

    private fun setUpRightMode(){
        when (screenMode){
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun addTextListeners(){
        textInputEditTextName.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                shopItemViewModel.resetErrorInputName()
            }

        })
        textInputEditTextCount.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                shopItemViewModel.resetErrorInputCount()
            }

        })
    }

    private fun observeViewModel(){
        shopItemViewModel.errorInputName.observe(this){
            if (it){
                textInputLayoutName.error = "Error"
            }else{
                textInputLayoutName.error = null
            }
        }

        shopItemViewModel.errorInputCount.observe(this){
            if (it){
                textInputLayoutCount.error = "Error"
            }else{
                textInputLayoutCount.error = null
            }
        }

        shopItemViewModel.shouldCloseScreen.observe(this){
            finish()
        }
    }



    private fun launchEditMode(){
        shopItemViewModel.getShopItem(shopItemId)
        shopItemViewModel.shopItem.observe(this){
            textInputEditTextName.setText(it.name)
            textInputEditTextCount.setText(it.count.toString())
        }
        buttonSave.setOnClickListener{
            shopItemViewModel.editShopItem(
                textInputEditTextName.text?.toString(),
                textInputEditTextCount.text?.toString()
            )
        }
    }

    private fun launchAddMode(){
        buttonSave.setOnClickListener{
            shopItemViewModel.addShopItem(
                textInputEditTextName.text?.toString(),
                textInputEditTextCount.text?.toString()
            )
        }
    }

    private fun parseExtra(){
        if (!intent.hasExtra(EXTRA_MODE)){
            throw RuntimeException()
        }
        val mode = intent.getStringExtra(EXTRA_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT){
            throw RuntimeException()
        }
        screenMode = mode
        if (mode == MODE_EDIT){
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)){
                throw RuntimeException()
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun initViews(){
        textInputLayoutCount = findViewById(R.id.txtLayout2)
        textInputLayoutName = findViewById(R.id.txtLayout1)
        textInputEditTextName = findViewById(R.id.textInputName)
        textInputEditTextCount = findViewById(R.id.textInputCount)
        buttonSave = findViewById(R.id.buttonSave)
    }

    companion object {

        private const val EXTRA_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"

        fun newIntentAddMode(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditMode(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }

    }
}