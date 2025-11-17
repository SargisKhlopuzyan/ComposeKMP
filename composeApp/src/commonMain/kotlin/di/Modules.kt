package di

import dependencies.MyRepository
import dependencies.MyRepositoryImpl
import dependencies.MyViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single<MyRepository> {
        MyRepositoryImpl(get())
    }

//    singleOf(::MyRepositoryImpl).bind<MyRepository>()

//    single {
//        MyRepositoryImpl(get())
//    }.bind<MyRepository>()

    viewModelOf(::MyViewModel)
}