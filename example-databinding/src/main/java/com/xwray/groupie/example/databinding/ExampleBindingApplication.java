package com.xwray.groupie.example.databinding;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;


public class ExampleBindingApplication extends Application {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}