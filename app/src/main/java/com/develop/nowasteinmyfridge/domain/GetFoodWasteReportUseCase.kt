package com.develop.nowasteinmyfridge.domain

import com.develop.nowasteinmyfridge.data.model.Report
import com.develop.nowasteinmyfridge.data.repository.FirebaseFirestoreRepository
import javax.inject.Inject

class GetFoodWasteReportUseCase @Inject constructor(
    private val firestoreRepository: FirebaseFirestoreRepository
) {
    suspend operator fun invoke():List<Report> {
        return firestoreRepository.getFoodWasteReport()
    }
}