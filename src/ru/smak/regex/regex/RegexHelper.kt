package ru.smak.regex.regex

import java.util.regex.Pattern

class RegexHelper {
    var regex: String? = null

    fun findIn(text: String): MutableList<Pair<Int, Int>>{
        val res = mutableListOf<Pair<Int, Int>>()
        if (regex==null) return res
        val ptrn = Pattern.compile(
            regex?:"",
            Pattern.UNICODE_CHARACTER_CLASS or
            Pattern.UNICODE_CASE or
            Pattern.MULTILINE
        )
        val matcher = ptrn.matcher(text)
        while (matcher.find()){
            res.add(Pair(matcher.start(), matcher.end()))
        }
        return res
    }


}