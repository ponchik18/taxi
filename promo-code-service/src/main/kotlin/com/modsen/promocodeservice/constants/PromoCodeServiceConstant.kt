package com.modsen.promocodeservice.constants

interface PromoCodeServiceConstant {
    interface Validation {
        interface Message {
            companion object {
                const val NOT_RIGHT_DATE = "Not right date!"
                const val NOT_RIGHT_RANGE = "Discount should be from 1 to 100!"
            }
        }
    }

    interface Errors {
        interface Message {
            companion object {
                const val DUPLICATE_NAME = "A promo code with the name '%s' already exists!"
                const val PROMO_CODE_NOT_FOUND = "Promo code with name '%s' not found!"
            }
        }
    }

    interface BasePath {
        companion object {
            const val PROMO_CODE_CONTROLLER_PATH = "/api/v1/promo-code"
        }
    }
}