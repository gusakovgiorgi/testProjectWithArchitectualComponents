package com.gusakov.frogogo.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gusakov.frogogo.R
import com.gusakov.frogogo.extension.showDialog
import com.gusakov.frogogo.model.User
import com.gusakov.frogogo.ui.dialog.ProgressDialog
import com.gusakov.frogogo.ui.users.adapter.UserAdapter
import kotlinx.android.synthetic.main.fragment_users.*
import org.koin.android.ext.android.inject

class UsersFragment : Fragment() {

    companion object {
        fun newInstance() = UsersFragment()
    }

    private lateinit var userAdapter: UserAdapter
    var progressDialog: ProgressDialog? = null
    private val viewModel: UsersViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        userAdapter = UserAdapter {
            findNavController().navigate(
                UsersFragmentDirections.actionUsersFragmentToCreateUserFragment(
                    it
                )
            )
        }
        usersRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }
        addUserFab.setOnClickListener {
            findNavController().navigate(UsersFragmentDirections.actionUsersFragmentToCreateUserFragment())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.setTitle(R.string.app_name)
        subscribeOnEvents()
        if (savedInstanceState == null) {
            viewModel.loadUsers()
        }
    }

    private fun subscribeOnEvents() {
        viewModel.usersStateLiveData.observe(viewLifecycleOwner, Observer { usersState ->
            usersState?.let {
                when (it) {
                    is UsersState.Loading -> showProgressDialog()
                    is UsersState.Success -> handleSuccess(it.users)
                    is UsersState.Error -> handleError()
                }
            }
        })
    }

    private fun handleError() {
        hideProgressDialog()
        Toast.makeText(context, "Error occured", Toast.LENGTH_LONG).show()
    }

    private fun handleSuccess(users: List<User>) {
        hideProgressDialog()
        userAdapter.listItem = users
    }

    private fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    private fun showProgressDialog() {
        progressDialog = showDialog(ProgressDialog())
    }

}
