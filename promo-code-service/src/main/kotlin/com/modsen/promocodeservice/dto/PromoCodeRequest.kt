package com.modsen.promocodeservice.dto
import com.modsen.promocodeservice.constants.PromoCodeServiceConstant
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import java.time.LocalDate

data class PromoCodeRequest(

    var name: String,
    var fromDate: LocalDate,
    var endDate: LocalDate,
    @Min(value = 1, message = PromoCodeServiceConstant.Validation.Message.NOT_RIGHT_RANGE)
    @Max(value = 100, message = PromoCodeServiceConstant.Validation.Message.NOT_RIGHT_RANGE)
    var discount: Int
) {
    @AssertTrue(message = PromoCodeServiceConstant.Validation.Message.NOT_RIGHT_DATE)
    fun isEndTimeAfterStartTime(): Boolean {
        return fromDate.isAfter(LocalDate.now()) && endDate.isAfter(fromDate)
    }
}