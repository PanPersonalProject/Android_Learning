package com.common.plugins

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.gradle.api.Project

class TransformDemo(var project: Project) : Transform() {
    override fun getName(): String {
        return "toastPlugin"
    }

    override fun getInputTypes(): Set<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope>? {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun transform(transformInvocation: TransformInvocation) {
        val inputs = transformInvocation.inputs
        val outputProvider = transformInvocation.outputProvider
        inputs.forEach { it ->
            it.jarInputs.forEach { jar ->
                // 执行注入逻辑
//                InjectByJavassit.inject(path, project)
                // 获取输出目录
                val dest = outputProvider.getContentLocation(
                    jar.name,
                    jar.contentTypes,
                    jar.scopes,
                    Format.JAR
                )
                // 将input的目录复制到output指定目录
                FileUtils.copyFile(jar.file, dest)
            }

            it.directoryInputs.forEach { directory ->
                val dest = outputProvider.getContentLocation(
                    directory.name,
                    directory.contentTypes,
                    directory.scopes,
                    Format.DIRECTORY
                )
                FileUtils.copyDirectory(directory.file, dest)
            }
        }
    }

}