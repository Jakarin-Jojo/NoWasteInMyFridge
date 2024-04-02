package com.develop.nowasteinmyfridge.data.repository

import android.net.Uri
import android.util.Log
import com.develop.nowasteinmyfridge.data.model.GroceryList
import com.develop.nowasteinmyfridge.data.model.GroceryListCreate
import com.develop.nowasteinmyfridge.data.model.Ingredient
import com.develop.nowasteinmyfridge.data.model.IngredientCreate
import com.develop.nowasteinmyfridge.data.model.UserCreate
import com.develop.nowasteinmyfridge.data.model.UserProfile
import com.develop.nowasteinmyfridge.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFirestoreRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
) : FirebaseFirestoreRepository {
    private val db = firebaseFirestore
    private val userEmail = firebaseAuth.currentUser?.email.orEmpty()
    private val storageRef = firebaseStorage.reference

    override suspend fun deleteIngredient(ingredientID: String): Result<Unit> {
        return try {
            val querySnapshot = db.collection("users/$userEmail/ingredients")
                .whereEqualTo("id", ingredientID)
                .get()
                .await()
            if (!querySnapshot.isEmpty) {
                val documentReference = querySnapshot.documents[0].reference
                documentReference.delete().await()
                Result.Success(Unit)
            } else {
                Result.Error(Exception("Ingredient not found"))
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error deleting ingredient: $e")
            Result.Error(e)
        }
    }


    override suspend fun clearGroceryList() {
        try {
            val querySnapshot = db.collection("users/$userEmail/groceryList").get().await()

            for (document in querySnapshot.documents) {
                document.reference.delete().await()
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error clearing grocery list from Firestore: $e")
            throw e
        }
    }

    override suspend fun addGroceryList(groceryList: GroceryListCreate) {
        try {
            db.collection("users/$userEmail/groceryList")
                .add(
                    GroceryList(
                        name = groceryList.name,
                        quantity = groceryList.quantity,
                    )
                ).await()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error adding grocery list to Firestore: $e")
            throw e
        }
    }


    override suspend fun getGroceryList(): List<GroceryList> {
        var groceryLists = emptyList<GroceryList>()
        try {
            val querySnapshot = db.collection("users/$userEmail/groceryList").get().await()
            groceryLists = querySnapshot.toObjects(GroceryList::class.java)
        } catch (e: FirebaseFirestoreException) {
            Log.d("Error", "Unable to getGrocery: $e")
        }
        return groceryLists
    }

    override suspend fun getIngredient(): List<Ingredient> {
        var ingredients = emptyList<Ingredient>()
        try {
            val querySnapshot = db.collection("users/$userEmail/ingredients").get().await()
            ingredients = querySnapshot.toObjects(Ingredient::class.java)
        } catch (e: FirebaseFirestoreException) {
            Log.d("Error", "Unable to getIngredient: $e")
        }
        return ingredients
    }

    override suspend fun addIngredient(ingredient: IngredientCreate) {
        try {
            val ingredientId = db.collection("users/$userEmail/ingredients").document().id
            val imageUri = ingredient.image as? Uri

            if (imageUri != null) {
                val ref = storageRef.child("users/$userEmail/ingredients/${imageUri.lastPathSegment}")
                val uploadTask = ref.putFile(imageUri)
                try {
                    val taskSnapshot = uploadTask.await()
                    val imageUrl = taskSnapshot.metadata!!.reference!!.downloadUrl.await().toString()
                    val newIngredient = Ingredient(
                        id = ingredientId,
                        name = ingredient.name,
                        quantity = ingredient.quantity,
                        image = imageUrl,
                        mfg = ingredient.mfg,
                        efd = ingredient.efd,
                        isInFreezer = ingredient.inFreeze,
                    )
                    db.collection("users/$userEmail/ingredients").document(ingredientId)
                        .set(newIngredient)
                        .await()
                } catch (uploadException: Exception) {
                    Log.e("Error", "Error uploading image: $uploadException")
                }
            } else if (ingredient.image is String) { // Handle the case where image is a String
                // In this case, the image is already a URL, so use it directly
                val newIngredient = Ingredient(
                    id = ingredientId,
                    name = ingredient.name,
                    quantity = ingredient.quantity,
                    image = ingredient.image,
                    mfg = ingredient.mfg,
                    efd = ingredient.efd,
                    isInFreezer = ingredient.inFreeze,
                )
                db.collection("users/$userEmail/ingredients").document(ingredientId)
                    .set(newIngredient)
                    .await()
            }
        } catch (e: FirebaseFirestoreException) {
            Log.d("FirestoreError", "Error adding ingredient to Firestore: $e")
        }
    }

    override suspend fun getUserInfo(): Flow<Result<UserProfile>> {
        return flow {
            emit(Result.Loading)
            try {
                val userDocument = db.collection("users").document(userEmail)

                val documentSnapshot = userDocument.get().await()

                if (documentSnapshot.exists()) {
                    val userProfile = documentSnapshot.toObject<UserProfile>()
                    emit(
                        Result.Success(
                            userProfile ?: throw FirebaseFirestoreException(
                                "User data is null",
                                FirebaseFirestoreException.Code.ABORTED,
                            )
                        )
                    )
                } else {
                    emit(
                        Result.Error(
                            FirebaseFirestoreException(
                                "User document does not exist",
                                FirebaseFirestoreException.Code.NOT_FOUND,
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("FirestoreRepository", "Error fetching user data: ${e.message}")
                emit(Result.Error(e))
            }
        }.onStart { emit(Result.Loading) }.catch { e ->
            Log.e("FirestoreRepository", "Error during flow: ${e.message}")
            emit(Result.Error(e))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun createUser(userCreate: UserCreate): Flow<Result<Unit>> {
        return flow {
            emit(Result.Loading)
            try {
                firebaseAuth.createUserWithEmailAndPassword(userCreate.email, userCreate.password)
                val usersCollection = db.collection("users")
                usersCollection.document(userCreate.email).set(
                    UserProfile(
                        firstName = userCreate.firstName,
                        lastName = userCreate.lastName,
                        gender = userCreate.gender,
                        email = userCreate.email,
                        birthday = userCreate.birthday,
                        profileImageUrl = userCreate.profileImageUrl,
                    )
                ).await()
                emit(Result.Success(Unit))
            } catch (e: Exception) {
                emit(
                    Result.Error(
                        FirebaseFirestoreException(
                            e.message ?: "UNKNOWN",
                            FirebaseFirestoreException.Code.UNAVAILABLE
                        )
                    )
                )
            }
        }.catch { e ->
            emit(Result.Error(e))
        }.flowOn(Dispatchers.IO)
    }
}