package net.d3b8g.vktestersmentoring.helper

object DateHelper {

    fun Int.getMonth(case: StringCase): String = when(case) {
        StringCase.Nominative -> {
            when(this) {
                2 -> "Февраль"
                3 -> "Март"
                4 -> "Апрель"
                5 -> "Май"
                6 -> "Июнь"
                7 -> "Июль"
                8 -> "Август"
                9 -> "Сентябрь"
                10 -> "Октябрь"
                11 -> "Ноябрь"
                12 -> "Декабрь"
                else -> "Январь"
            }
        }
        StringCase.Genitive -> {
            when(this) {
                2 -> "Февраля"
                3 -> "Марта"
                4 -> "Апреля"
                5 -> "Мая"
                6 -> "Июня"
                7 -> "Июля"
                8 -> "Августа"
                9 -> "Сентября"
                10 -> "Октября"
                11 -> "Ноября"
                12 -> "Декабря"
                else -> "Января"
            }
        }
        else -> "January"
    }

    fun String.getWeekDay(): String = when(this) {
        "Mon" -> "Понедельник"
        "Tue" -> "Вторник"
        "Wed" -> "Среда"
        "Thu" -> "Четверг"
        "Fri" -> "Пятница"
        "Sat" -> "Суббота"
        "Sun" -> "Воскресение"
        else -> "aboba"
    }
}