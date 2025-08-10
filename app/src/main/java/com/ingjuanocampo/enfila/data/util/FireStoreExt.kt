package com.ingjuanocampo.enfila.data.util

import com.google.firebase.firestore.FirebaseFirestore
import com.ingjuanocampo.enfila.domain.entity.IdentifyObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.lang.Exception

fun <T> FirebaseFirestore.uploadProcess(
    dataMapper: (T) -> Any,
    data: T,
    path: String,
    id: String
): Flow<T?> {
    val sharedFlow = MutableSharedFlow<T?>()
    val toUpload = dataMapper(data)
    this.collection(path)
        .document(id)
        .set(toUpload)
        .addOnSuccessListener { documentReference ->
            GlobalScope.launch {
                sharedFlow.emit(data)
            }
        }
        .addOnFailureListener { e ->
            GlobalScope.launch {
                sharedFlow.emit(null)
            }
        }
    return sharedFlow
}

fun <T> FirebaseFirestore.uploadProcessMultiples(
    dataMapper: (T) -> Any,
    dataList: List<IdentifyObject>,
    path: String
): Flow<List<T>?> {
    val sharedFlow = MutableSharedFlow<List<T>?>()
    dataList.forEach { data ->
        val toUpload = dataMapper(data as T)
        this.collection(path)
            .document(data.id)
            .set(toUpload)
            .addOnSuccessListener { documentReference ->
                GlobalScope.launch {
                    sharedFlow.emit(listOf(data))
                }
            }
            .addOnFailureListener { e ->
                GlobalScope.launch {
                    sharedFlow.emit(null)
                }
            }
    }
    return sharedFlow
}

fun <T> FirebaseFirestore.fetchProcessMultiples(
    dataMapper: (Map<String, Any>) -> T,
    path: String
): Flow<List<T>?> {
    val sharedFlow = MutableSharedFlow<List<T>?>()
    try {
        this.collection(path)
            .get()
            .addOnSuccessListener { result ->
                GlobalScope.launch {
                    val mapped = ArrayList<T>()
                    try {
                        result.documents.forEach {
                            mapped.add(dataMapper(it.data!!))
                        }
                        sharedFlow.emit(mapped)
                    } catch (e: Exception) {
                        sharedFlow.emit(null)
                    }
                }
            }.addOnFailureListener {
                GlobalScope.launch {
                    sharedFlow.emit(null)
                }
            }
    } catch (e: Exception) {
        GlobalScope.launch {
            sharedFlow.emit(null) // This scenario  is not working, review
        }
    }

    return sharedFlow
}

fun <T> FirebaseFirestore.fetchProcess(
    dataMapper: (Map<String, Any>) -> T,
    path: String,
    id: String
): Flow<T?> {
    val sharedFlow = MutableSharedFlow<T?>()
    try {
        this.collection(path).document(id)
            .get()
            .addOnSuccessListener { result ->
                GlobalScope.launch {
                    try {
                        val data = result.data?.let { dataMapper(it) }
                        sharedFlow.emit(
                            data
                        )
                    } catch (e: Exception) {
                        sharedFlow.emit(null)
                    }
                }
            }.addOnFailureListener {
                GlobalScope.launch {
                    sharedFlow.emit(null)
                }
            }
    } catch (e: Exception) {
        GlobalScope.launch {
            sharedFlow.emit(null)
        }
    }

    return sharedFlow
}
