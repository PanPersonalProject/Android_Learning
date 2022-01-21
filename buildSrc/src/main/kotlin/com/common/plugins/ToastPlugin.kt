package com.common.plugins

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class ToastPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val transform = TransformDemo(project)
        project.afterEvaluate {
            val baseExtension = project.extensions.getByType(BaseExtension::class.java)
            baseExtension.registerTransform(transform)
        }
    }

}


