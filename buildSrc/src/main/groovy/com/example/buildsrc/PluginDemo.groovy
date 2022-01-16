package com.example.buildsrc

import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginDemo implements Plugin<Project> {
    @Override
    void apply(Project project) {
        //创建一个authorInfo对象
        def author = project.extensions.create("authorInfo", Author)
        //等project读取完配置回调,否则authorInfo没被修改
        project.afterEvaluate {
            println "author is ${author.authorName}"
        }

    }
    static class Author {
        def authorName
    }
}