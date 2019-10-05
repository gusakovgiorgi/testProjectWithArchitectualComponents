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
    private val KEY_ITEMS = "key_items"

    companion object {
        fun newInstance() = UsersFragment()
    }

    private var userAdapter: UserAdapter? = null
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
        initViews(savedInstanceState)
    }

    private fun initViews(savedInstanceState: Bundle?) {
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
        savedInstanceState?.let {
            userAdapter!!.listItem = it.getParcelableArrayList<User>(KEY_ITEMS) ?: arrayListOf()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        userAdapter?.apply {
            if (listItem is ArrayList) {
                outState.putParcelableArrayList(KEY_ITEMS, listItem as ArrayList<User>)
            } else {
                outState.putSerializable(KEY_ITEMS, arrayListOf(listItem))
            }
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
        userAdapter!!.listItem = users
    }

    private fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    private fun showProgressDialog() {
        progressDialog = showDialog(ProgressDialog())
    }

}
