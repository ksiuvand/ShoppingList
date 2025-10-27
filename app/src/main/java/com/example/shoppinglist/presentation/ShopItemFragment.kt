package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment(): Fragment() {

    private lateinit var onEditingFinishedInterface: OnEditingFinishedInterface

    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var textInputLayoutCount: TextInputLayout
    private lateinit var textInputEditTextName: TextInputEditText
    private lateinit var textInputEditTextCount: TextInputEditText
    private lateinit var buttonSave: Button

    private lateinit var shopItemViewModel: ShopItemViewModel

    private var screenMode : String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedInterface){
            onEditingFinishedInterface = context
        }else{
            throw RuntimeException()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseParams()
        initViews(view)
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
        textInputEditTextName.addTextChangedListener(object: TextWatcher {
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
        shopItemViewModel.errorInputName.observe(viewLifecycleOwner){
            if (it){
                textInputLayoutName.error = "Error"
            }else{
                textInputLayoutName.error = null
            }
        }

        shopItemViewModel.errorInputCount.observe(viewLifecycleOwner){
            if (it){
                textInputLayoutCount.error = "Error"
            }else{
                textInputLayoutCount.error = null
            }
        }

        shopItemViewModel.shouldCloseScreen.observe(viewLifecycleOwner){
            onEditingFinishedInterface.onEditingFinished()
        }
    }

    private fun launchEditMode(){
        shopItemViewModel.getShopItem(shopItemId)
        shopItemViewModel.shopItem.observe(viewLifecycleOwner){
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

    private fun parseParams(){
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)){
            throw RuntimeException()
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT){
            throw RuntimeException()
        }
        screenMode = mode
        if (mode == MODE_EDIT){
            if (!args.containsKey(SHOP_ITEM_ID)){
                throw RuntimeException()
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun initViews(view: View){
        textInputLayoutCount = view.findViewById(R.id.txtLayout2)
        textInputLayoutName = view.findViewById(R.id.txtLayout1)
        textInputEditTextName = view.findViewById(R.id.textInputName)
        textInputEditTextCount = view.findViewById(R.id.textInputCount)
        buttonSave = view.findViewById(R.id.buttonSave)
    }

    interface OnEditingFinishedInterface{
        fun onEditingFinished()
    }

    companion object {

        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddMode(): ShopItemFragment{
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditMode(shopItemId: Int): ShopItemFragment{
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }

}