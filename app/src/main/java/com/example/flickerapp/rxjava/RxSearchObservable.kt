package com.example.flickerapp.rxjava


import androidx.appcompat.widget.SearchView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class RxSearchObservable {
    companion object {
        fun fromView(searchView: SearchView): Observable<String> {
            val subject = PublishSubject.create<String>()
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    subject.onComplete()
                    return true
                }

                override fun onQueryTextChange(text: String?): Boolean {
                    subject.onNext(text)
                    return true
                }

            })
            return subject
        }
    }


}