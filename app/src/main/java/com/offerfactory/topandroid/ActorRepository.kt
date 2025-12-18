package com.offerfactory.topandroid

object ActorRepository {
    private val default = Actor(
        id = 1,
        name = "Бредд Питт",
        birthYear = 1963,
        country = "США",
        avatarUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Brad_Pitt-69858.jpg/250px-Brad_Pitt-69858.jpg",
        bio = "Американский актёр и кинопродюсер. Обладатель премий 'Оскар' и 'Золотой глобус'. Известен по ролям в фильмах 'Бойцовский клуб', 'Семь', 'Одиннадцать друзей Оушена', 'Загадочная история Бенджамина Баттона' и др.",
        knownFor = listOf(
            "Бойцовский клуб",
            "Семь",
            "Однажды в… Голливуде",
            "Одиннадцать друзей Оушена",
            "Загадочная история Бенджамина Баттона"
        ),
        wikiUrl = "https://ru.wikipedia.org/wiki/%D0%9F%D0%B8%D1%82%D1%82,_%D0%91%D1%80%D1%8D%D0%B4"
    )

    fun getDefaultActor(): Actor = default
}
