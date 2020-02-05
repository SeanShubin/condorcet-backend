package com.seanshubin.condorcet.backend.console

class DependencyInjectionArgs(val args:Array<String>){
    val argsToConfig = ArgsToConfig()
    val runner:Runnable = RunnerConfig(args, argsToConfig)
}