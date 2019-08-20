# SampleCleanArchitecture project

Sample Android application following clean architecutre principles using Kotlin.
The app searches local (simple json file asset) and remote (iTunesSearchApi) repositories for songs, based on provided artist's name.

This app is a WORK IN PROGRESS

### Technologies used
* Kotlin
* RxJava/RxKotlin
* MVVM
* Koin
* Data Binding
* OKHTTP
* Retrofit
* Moshi

### TODO
* remove unnecessary gradle references; add test implementations
* implement and update tests
* implement room database
* think about including additional interfaces for providers in data layer
* change data layer return type
* introduce proper API requests error handling

### License

    Copyright 2019 Wojciech Latala

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
