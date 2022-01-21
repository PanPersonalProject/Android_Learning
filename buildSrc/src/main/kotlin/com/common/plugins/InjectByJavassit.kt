package com.common.plugins

import com.common.plugins.InjectByJavassit
import groovy.lang.Closure
import javassist.*
import org.codehaus.groovy.runtime.DefaultGroovyMethods
import org.codehaus.groovy.runtime.ResourceGroovyMethods
import org.gradle.api.Project
import java.io.File
import java.io.IOException
import java.lang.Exception

object InjectByJavassit {
    fun inject(path: String, project: Project) {
        try {
            val dir = File(path)
            if (dir.isDirectory) {
                dir.listFiles().forEach { file ->
                    if (file.name.endsWith("Activity.class")) {
                        doInject(project, file, path)
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(CannotCompileException::class, IOException::class, NotFoundException::class)
    private fun doInject(project: Project, clsFile: File, originPath: String) {
        println("[Inject] DoInject: " + clsFile.absolutePath)
        var cls = ResourceGroovyMethods.relativePath(File(originPath), clsFile).replace("/", ".")
        cls = cls.substring(0, cls.lastIndexOf(".class"))
        println("[Inject] Cls: $cls")
        val pool = ClassPool.getDefault()
        // 加入当前路径
        pool.appendClassPath(originPath)
        // project.android.bootClasspath 加入android.jar，不然找不到android相关的所有类
//        pool.appendClassPath(project.android.bootClasspath,);
        // 引入android.os.Bundle包，因为onCreate方法参数有Bundle
        pool.importPackage("android.os.Bundle")
        val ctClass = pool.getCtClass(cls)
        // 解冻
        if (ctClass.isFrozen) {
            ctClass.defrost()
        }

        // 获取方法
        val ctMethod = ctClass.getDeclaredMethod("onCreate")
        val toastStr =
            "android.widget.Toast.makeText(this, \"I am the injected code\", android.widget.Toast.LENGTH_SHORT).show();"

        // 方法尾插入
        ctMethod.insertAfter(toastStr)
        ctClass.writeFile(originPath)

        // 释放
        ctClass.detach()
    }
}