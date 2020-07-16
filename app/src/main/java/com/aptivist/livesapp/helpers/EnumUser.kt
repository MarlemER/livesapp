package com.aptivist.livesapp.helpers

enum class EnumUser {
    Signed,
    Unsigned
}

enum class EnumIncidenceType(type:Int){
    Stole(1),
    Homicid(2),
    IlegalSales(3),
    Misplacement(4),
    Atack(5),
    Accident(6),
    Shoplifting(7),
    Vandalism(8),
    Other(9)
}
