package com.aptivist.livesapp.helpers

enum class EnumUser {
    Signed,
    Unsigned
}

enum class EnumIncidenceType(type:Int){
    Stole(1){ override fun incidenceName()="Stolen" },
    Homicid(2){ override fun incidenceName()="Homicid" },
    IlegalSales(3){ override fun incidenceName()="IlegalSales" },
    Misplacement(4){ override fun incidenceName()="Misplacement" },
    Atack(5){ override fun incidenceName()="Atack" },
    Accident(6){ override fun incidenceName()="Accident" },
    Shoplifting(7){ override fun incidenceName()="Shoplifting" },
    Vandalism(8){ override fun incidenceName()="Vandalism" },
    Other(9){ override fun incidenceName()="Other"};
    abstract fun incidenceName(): String
}
