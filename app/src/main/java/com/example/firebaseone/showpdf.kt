package com.example.firebaseone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.firebaseone.databinding.FragmentShowpdfBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class showpdf : Fragment(), pdfAdapter.onClickListen {
    private lateinit var bind: FragmentShowpdfBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var adapter: pdfAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bind = FragmentShowpdfBinding.inflate(layoutInflater, container, false)



        databaseReference = FirebaseDatabase.getInstance().reference.child("pdf")

        initRecyclerView()
        getPdf()






        return bind.root
    }

    private fun initRecyclerView() {

        bind.recyclerPdf.setHasFixedSize(true)
        bind.recyclerPdf.layoutManager = GridLayoutManager(context, 2)
        adapter = pdfAdapter(this)
        bind.recyclerPdf.adapter = adapter


    }

    private fun getPdf() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val tempList = mutableListOf<PdfFile>()
                snapshot.children.forEach {
                    val pdfFilee = it.getValue(PdfFile::class.java)
                    if (pdfFilee != null) {
                        tempList.add(pdfFilee)
                    }
                }

                if (tempList.isEmpty()) {
                    Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()

                }

                adapter.submitList(tempList)
                bind.progressBar.visibility = View.GONE


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message.toString(), Toast.LENGTH_SHORT).show()
                bind.progressBar.visibility = View.GONE

            }


        })


    }

    override fun pdfClicked(pdfFile: PdfFile) {



    }


}