package com.example.buildsrc

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.api.Project

class TransformDemo extends Transform {
    Project project

    TransformDemo(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "transformDemo"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        def inputs = transformInvocation.inputs
        def outputProvider = transformInvocation.outputProvider
        inputs.each {
            it.jarInputs.each {
                // 执行注入逻辑
                InjectByJavassit.inject(path, project)
                // 获取输出目录
                File dest = outputProvider.getContentLocation(it.name, it.contentTypes, it.scopes, Format.JAR)
                // 将input的目录复制到output指定目录
                FileUtils.copyFile(it.file, dest)
            }

            it.directoryInputs.each {
                File dest = outputProvider.getContentLocation(it.name, it.contentTypes, it.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(it.file, dest)
            }
        }
    }
}
