package com.criticalgnome.recyclerviewwithkotlin

import java.util.*

class ObserverInit() : Observable() {
    override fun notifyObservers(arg: Any) {
        super.setChanged()
        super.notifyObservers(arg)
        super.clearChanged()
    }
}