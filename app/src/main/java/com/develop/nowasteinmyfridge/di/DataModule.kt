package com.develop.nowasteinmyfridge.di

import com.develop.nowasteinmyfridge.data.repository.AuthRepository
import com.develop.nowasteinmyfridge.data.repository.AuthRepositoryImpl
import com.develop.nowasteinmyfridge.data.repository.FirebaseFirestoreRepository
import com.develop.nowasteinmyfridge.data.repository.FirebaseFirestoreRepositoryImpl
import com.develop.nowasteinmyfridge.data.repository.IngredientByBarcodeRepositoryImpl
import com.develop.nowasteinmyfridge.data.repository.IngredientByBarCodeRepository
import com.develop.nowasteinmyfridge.data.repository.RecipeRepository
import com.develop.nowasteinmyfridge.data.repository.RecipeRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun providesAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun providesFirestoreRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
        firebaseStorage: FirebaseStorage,
    ): FirebaseFirestoreRepository {
        return FirebaseFirestoreRepositoryImpl(
            firestore,
            firebaseAuth,
            firebaseStorage,
        )
    }

    @Provides
    @Singleton
    fun providesRecipeRepository(): RecipeRepository {
        return RecipeRepositoryImpl()
    }

    @Provides
    @Singleton
    fun providesGetBarcodeRepository(): IngredientByBarCodeRepository {
        return IngredientByBarcodeRepositoryImpl()
    }


}