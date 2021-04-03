
/*
 * Copyright © 2014-2021 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

plugins {
  id("com.android.library")
  kotlin("android")
  `aps-plugin`
}

android {
  defaultConfig {
    versionCode = 1
    versionName = "1.0.0"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildFeatures { compose = true }

  composeOptions {
    kotlinCompilerExtensionVersion = "1.0.0-beta03"
  }
}

dependencies {
  compileOnly(Dependencies.AndroidX.annotation)
  compileOnly(Dependencies.AndroidX.activity_ktx)
  compileOnly(Dependencies.AndroidX.appcompat)
  implementation(project(":gopenpgp"))
  implementation(Dependencies.Compose.activity)
  implementation(Dependencies.Compose.material)
  implementation(Dependencies.Compose.runtime)
  implementation(Dependencies.ThirdParty.crypto_android)
  implementation(Dependencies.ThirdParty.crypto_common)
  implementation(Dependencies.ThirdParty.timberkt)

  testImplementation(Dependencies.Testing.junit)
  testImplementation(Dependencies.Testing.kotlin_test_junit)
}
