package com.gusakov.frogogo.ui.create_user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gusakov.frogogo.R
import com.gusakov.frogogo.extension.showDialog
import com.gusakov.frogogo.ui.dialog.ProgressDialog
import kotlinx.android.synthetic.main.create_or_modify_user_fragment.*
import org.koin.android.ext.android.inject

class CreateOrModifyUserFragment : Fragment() {

    companion object {
        fun newInstance() = CreateOrModifyUserFragment()
    }

    val args: CreateOrModifyUserFragmentArgs by navArgs()

    private var progressDialog: ProgressDialog? = null
    private val createOrModifyUserViewModel: CreateOrModifyUserViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_or_modify_user_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val user = args.user
        if (user != null) {
            (activity as AppCompatActivity?)?.supportActionBar?.setTitle(R.string.update)
            nameEdt.setText(user.firstName)
            surnameEdt.setText(user.lastName)
            emailEdt.setText(user.email)
        } else {
            (activity as AppCompatActivity?)?.supportActionBar?.setTitle(R.string.create)
        }
        createBtn.setOnClickListener {
            if (user != null) {
                createOrModifyUserViewModel.updateUser(user)
            } else {
                createOrModifyUserViewModel.createUser(
                    nameEdt.text.toString(),
                    surnameEdt.text.toString(),
                    emailEdt.text.toString()
                )
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeOnEvents()
    }

    private fun subscribeOnEvents() {
        createOrModifyUserViewModel.createUserStateLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { createUserState ->
                when (createUserState) {
                    is CreateUserState.Success -> handleSuccess()
                    is CreateUserState.Loading -> showProgressDialog()
                    is CreateUserState.Error -> handleError(createUserState)
                }
            }
        })
    }

    private fun handleError(error: CreateUserState.Error) {
        hideProgressDialog()
        when {
            error.firstNameError != null -> {
                nameEdt.error = getString(error.firstNameError)
                nameEdt.requestFocus()
            }
            error.lastNameError != null -> {
                surnameEdt.error = getString(error.lastNameError)
                surnameEdt.requestFocus()
            }
            error.emailError != null -> {
                emailEdt.error = getString(error.emailError)
                emailEdt.requestFocus()
            }
            error.serverConnectionError != null && error.serverConnectionError -> Toast.makeText(
                context,
                R.string.error_occurred,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    private fun showProgressDialog() {
        progressDialog = showDialog(ProgressDialog())
    }

    private fun handleSuccess() {
        hideProgressDialog()
        if (args.user == null) {
            Toast.makeText(context, R.string.user_created, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, R.string.user_updated, Toast.LENGTH_SHORT).show()
        }
        findNavController().navigateUp()
    }

}
