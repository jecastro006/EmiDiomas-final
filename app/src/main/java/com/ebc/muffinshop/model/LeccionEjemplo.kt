package com.ebc.muffinshop.model

// datos de ejemplo
val tarjetasItaliano = listOf(
    Tarjeta("ciao", "Ciao, come stai?", mapOf(
        "Español" to Traduccion("Hola / adiós", "Hola, ¿cómo estás?", "Saludo informal. Se usa al llegar y al despedirse."),
        "Inglés" to Traduccion("Hello / goodbye", "Hello, how are you?", "Informal greeting, used when arriving and leaving."),
        "Francés" to Traduccion("Salut", "Salut, comment vas-tu ?", "Salutation familière, utilisée pour dire bonjour et au revoir.")
    )),
    Tarjeta("andare", "Devo andare alla stazione.", mapOf(
        "Español" to Traduccion("Ir", "Tengo que ir a la estación.", "Verbo en infinitivo: andare. Presente: vado, vai, va, andiamo, andate, vanno."),
        "Inglés" to Traduccion("To go", "I have to go to the station.", "Infinitive: andare. Present tense: vado, vai, va, andiamo, andate, vanno."),
        "Francés" to Traduccion("Aller", "Je dois aller à la gare.", "Verbe à l’infinitif : andare. Présent : vado, vai, va, andiamo, andate, vanno.")
    )),
    Tarjeta("prendere", "Voglio prendere un biglietto.", mapOf(
        "Español" to Traduccion("Tomar / coger", "Quiero tomar un boleto.", "Infinitivo: prendere. Presente: prendo, prendi, prende, prendiamo, prendete, prendono."),
        "Inglés" to Traduccion("To take / get", "I want to get a ticket.", "Infinitive: prendere. Present tense: prendo, prendi, prende, prendiamo, prendete, prendono."),
        "Francés" to Traduccion("Prendre", "Je veux prendre un billet.", "Infinitif : prendere. Présent : prendo, prendi, prende, prendiamo, prendete, prendono.")
    )),
    Tarjeta("biglietto", "Un biglietto per Roma, per favore.", mapOf(
        "Español" to Traduccion("Boleto / billete", "Un boleto para Roma, por favor.", "Sustantivo masculino: il biglietto. Plural: i biglietti."),
        "Inglés" to Traduccion("Ticket", "A ticket to Rome, please.", "Masculine noun: il biglietto. Plural: i biglietti."),
        "Francés" to Traduccion("Billet", "Un billet pour Rome, s’il vous plaît.", "Nom masculin : il biglietto. Pluriel : i biglietti.")
    )),
    Tarjeta("stazione", "La stazione è vicino al bar.", mapOf(
        "Español" to Traduccion("Estación", "La estación está cerca del bar.", "Sustantivo femenino: la stazione. Plural: le stazioni."),
        "Inglés" to Traduccion("Station", "The station is near the café.", "Feminine noun: la stazione. Plural: le stazioni."),
        "Francés" to Traduccion("Gare", "La gare est près du café.", "Nom féminin : la stazione. Pluriel : le stazioni.")
    )),
    Tarjeta("vicino", "Il bar è vicino alla stazione.", mapOf(
        "Español" to Traduccion("Cerca", "El bar está cerca de la estación.", "Aquí expresa cercanía. Se usa con a: vicino alla stazione."),
        "Inglés" to Traduccion("Near", "The café is near the station.", "Here it describes proximity. Used with a: vicino alla stazione."),
        "Francés" to Traduccion("Près", "Le café est près de la gare.", "Ici, il exprime la proximité. Avec a : vicino alla stazione.")
    ))
)

// lectura de ejemplo
const val lecturaItaliano = "Ciao! Devo andare alla stazione. Voglio prendere un biglietto per Roma. La stazione è vicino al bar."

// traducciones de la lectura
val lecturaTraducida = mapOf(
    "Español" to "¡Hola! Tengo que ir a la estación. Quiero tomar un boleto para Roma. La estación está cerca del bar.",
    "Inglés" to "Hello! I have to go to the station. I want to get a ticket to Rome. The station is near the café.",
    "Francés" to "Salut ! Je dois aller à la gare. Je veux prendre un billet pour Rome. La gare est près du café."
)
