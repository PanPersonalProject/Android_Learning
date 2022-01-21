package com.common.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class PluginDemo : Plugin<Project> {
    override fun apply(project: Project) {
        //创建一个authorInfo对象
        val author = project.extensions.create<Author>("authorInfo")
//        //等project读取完配置回调,否则authorInfo没被修改
        project.afterEvaluate {
            println("author is ${author.authorName}");
        }
    }

}

open class Author {
    var authorName: Any? = null
}
