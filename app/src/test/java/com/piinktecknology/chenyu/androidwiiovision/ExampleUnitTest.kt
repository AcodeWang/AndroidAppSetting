package com.piinktecknology.chenyu.androidwiiovision

import android.content.Context
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock

import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.internal.matchers.Null
import org.mockito.junit.MockitoJUnitRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class ExampleUnitTest{

//    val mockedList = mock(mutableListOf<String>().javaClass)
//    val spyList = spy(mutableListOf<String>())

    @Test
    fun notiong(){
        val mockedList = Mockito.mock(mutableListOf<String>().javaClass)
        mockedList.add("twice")
        verify(mockedList).add("twice")
        mockedList.add("twice")
        verify(mockedList, times(2)).add("twice")
    }


}
