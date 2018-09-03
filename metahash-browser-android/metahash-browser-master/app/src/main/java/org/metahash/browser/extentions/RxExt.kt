package org.metahash.browser.extentions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RxExt {

    companion object {
        fun getTextChangeObservable(editText: EditText): Observable<String> {
            val subject = PublishSubject.create<String>()
            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    subject.onNext(p0?.toString() ?: "")
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            }
            editText.addTextChangedListener(textWatcher)
            return subject
        }

        fun getFocusChangeObservable(editText: EditText): Observable<Boolean> {
            val subject = PublishSubject.create<Boolean>()
            editText.setOnFocusChangeListener { _, focus ->
                subject.onNext(focus)
            }
            return subject
        }
    }
}