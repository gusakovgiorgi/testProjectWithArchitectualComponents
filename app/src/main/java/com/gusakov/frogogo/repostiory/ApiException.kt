package com.gusakov.frogogo.repostiory

import java.io.IOException

class ApiException(errorMessage: String) : IOException(errorMessage) {
}