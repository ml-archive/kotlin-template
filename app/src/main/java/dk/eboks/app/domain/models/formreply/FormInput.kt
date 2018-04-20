package dk.eboks.app.domain.models.formreply

import java.io.Serializable

data class FormInput (
       var name : String,
       var type : FormInputType,
       var label : String? = null,
       var validate : String? = null,
       var minValue : Double? = null,
       var maxValue : Double? = null,
       var minLength : Int? = null,
       var required : Boolean = false,
       var readonly : Boolean = false,
       var error : String? = null,
       var value : String? = null,
       var options : List<FormInputOption>?
) : Serializable
