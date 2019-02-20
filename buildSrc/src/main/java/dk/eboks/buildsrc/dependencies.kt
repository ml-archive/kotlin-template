package dk.eboks.buildsrc

object Versions {
    val ktlint = "0.30.0"
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:3.3.1"

    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val junit = "junit:junit:4.12"
    const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0"
    const val ribbonizer = "com.github.gfx.ribbonizer:ribbonizer-plugin:2.1.0"
    const val exifinterface = "com.android.support:exifinterface:28.0.0"
    const val swipelayout = "com.daimajia.swipelayout:library:1.2.0@aar"
    const val hockeySDK = "net.hockeyapp.android:HockeySDK:5.1.1"
    const val eventbus = "org.greenrobot:eventbus:3.1.1"
    const val fastscroll = "com.l4digital.fastscroll:fastscroll:2.0.1"
    const val materialedittext = "com.rengwuxian.materialedittext:library:2.1.4"
    const val circularProgressBar = "com.mikhaellopez:circularprogressbar:2.0.0"
    const val persistentCookieJar = "com.github.franmontiel:PersistentCookieJar:1.0.1"

    object Nodes {

        const val arch = "dk.nodes.arch:base:2.3.0"
        const val filepicker = "dk.nodes.filepicker:filepicker:2.0.1"
        const val locksmith = "dk.nodes.locksmith:core:1.2.2"
        const val bitrise = "dk.nodes.ci:bitrise:1.0"

        object Nstack {
            const val translation = "dk.nodes.nstack:translation:2.3.0"
            const val kotlin = "dk.nodes.nstack:nstack-kotlin:2.2.0"
        }
    }

    object Google {
        const val material = "com.google.android.material:material:1.1.0-alpha02"
        const val zxingCore = "com.google.zxing:core:3.3.3"
    }

    object Kotlin {
        private const val version = "1.3.21"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    }

    object Coroutines {
        private const val version = "1.1.1"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.0.2"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.0.0"
        const val cardview = "androidx.cardview:cardview:1.0.0"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.0-alpha3"

    }

    object RxJava {
        const val rxJava = "io.reactivex.rxjava2:rxjava:2.2.6"
        const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:2.3.0"
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.1.1"
    }

    object Dagger {
        private const val version = "2.21"
        const val dagger = "com.google.dagger:dagger:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
    }

    object Glide {
        private const val version = "4.9.0"
        val glide = "com.github.bumptech.glide:glide:$version"
        val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object Retrofit {
        private const val version = "2.5.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val retrofitRxjavaAdapter = "com.squareup.retrofit2:adapter-rxjava2:$version"
        const val scalarsConverter = "com.squareup.retrofit2:converter-scalars:$version"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:$version"
    }

    object OkHttp {
        private const val version = "3.12.1"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Test {

        const val runner = "androidx.test:runner:1.1.1"
        object Expresso {
            private const val version = "3.1.1"
            const val core = "androidx.test.espresso:espresso-core:$version"
            const val intent = "androidx.test.espresso:espresso-intents:$version"
            const val contrib = "androidx.test.espresso:espresso-contrib:$version"
        }

        object Mockito {
            private const val version = "2.24.5"
            const val core = "org.mockito:mockito-core:$version"
            const val android = "org.mockito:mockito-android:$version"
        }
    }
}
