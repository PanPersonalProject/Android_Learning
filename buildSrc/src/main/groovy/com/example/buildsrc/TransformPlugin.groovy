package com.example.buildsrc

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.BaseExtension

class TransformPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def transform = new TransformDemo(project)
        project.afterEvaluate {
            def baseExtension = project.extensions.getByType(BaseExtension)
            baseExtension.registerTransform(transform)
        }

    }
}